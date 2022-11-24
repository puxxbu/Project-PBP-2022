package com.example.tubes_pbp.qrcode

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tubes_pbp.HomeActivity
import com.example.tubes_pbp.R
import com.example.tubes_pbp.databinding.ActivityMainBinding
import com.example.tubes_pbp.databinding.ActivityQrcodeBookmarkBinding
import com.example.tubes_pbp.webapi.BookmarkData
import com.example.tubes_pbp.webapi.FormAddBookmarkActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import org.json.JSONObject
import org.json.JSONTokener

class QRCodeBookmark : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityQrcodeBookmarkBinding

    companion object{
        private const val CAMERA_REQUEST_CODE = 100
        private const val STORAGE_REQUEST_CODE = 101

        private const val TAG = "MAIN_TAG"
    }

    private lateinit var cameraPermissions: Array<String>
    private lateinit var storagePermissions: Array<String>

    private var imageUri: Uri? = null

    private var barcodeScannerOption: BarcodeScannerOptions? = null
    private var barcodeScanner: BarcodeScanner? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrcodeBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cameraBtn.setOnClickListener(this)
        binding.galleryBtn.setOnClickListener(this)
        binding.scanBtn.setOnClickListener(this)

        cameraPermissions = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        barcodeScannerOption = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS).build()
        barcodeScanner = BarcodeScanning.getClient(barcodeScannerOption!!)


    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.cameraBtn -> {
                if (checkCameraPermissions()){
                    pickImageCamera()
                }
                else{
                    requestCameraPermission()
                }
            }
            R.id.galleryBtn -> {
                if (checkStoragePermision()) {
                    pickImageGallery()
                }else {
                    requestStoragePermission()
                }
            }
            R.id.scanBtn -> {
                if (imageUri == null){
                    showToast("Pick image first")
                } else {
                    detectResultFromImage()
                }
            }
        }
    }


    private fun checkStoragePermision(): Boolean {
        val result = (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
        return result
    }


    private fun checkCameraPermissions(): Boolean {
        val resultcamera = (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED)
        val resultstorage = (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)

        return resultcamera && resultstorage
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE)
    }

    private fun requestStoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermissions, STORAGE_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()){
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if (cameraAccepted && storageAccepted){
                        pickImageCamera()
                    }else {
                        showToast("Camera dan storage permission are required")
                    }
                }
            }

            STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()){
                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED

                    if (storageAccepted){
                        pickImageGallery()
                    }else
                    {
                        showToast("Storage are required...")
                    }
                }
            }

        }
    }

    private fun detectResultFromImage() {
        try {
            val inputImage = InputImage.fromFilePath(this, imageUri!!)

            val barcodeResult = barcodeScanner?.process(inputImage)
                ?.addOnSuccessListener { barcodes ->
                    extractbarcodeQrCodeInfo(barcodes)
                }
                ?.addOnFailureListener{ e ->
                    Log.d(TAG,"detectResultFromImage: ", e)
                    showToast("failed Scanning due to ${e.message}")
                }
        }catch (e: Exception){
            Log.d(TAG,"detectResultFromImage: ", e)
            showToast("Failed Due to ${e.message}")
        }
    }

    private fun extractbarcodeQrCodeInfo(barcodes: List<Barcode>) {
        for (barcode in barcodes) {
            val bound = barcode.boundingBox
            val corners = barcode.cornerPoints

            val rawValue = barcode.rawValue
            Log.d(TAG,"extractbarcodeQrCodeInfo: rawValue: $rawValue")
            supportActionBar?.hide()


            val valueType = barcode.valueType
            binding.resultView.text =
                "rawValue: $rawValue"

            val jsonObj = JSONTokener(rawValue).nextValue()

            if (jsonObj is JSONObject){
                var gson = Gson()
                var testModel = gson.fromJson(rawValue, BookmarkData::class.java)
                var i = Intent(this@QRCodeBookmark, FormAddBookmarkActivity::class.java).apply {
                    putExtra("rawValue",rawValue)
                }
                finish()
                startActivity(i)
                Log.d(TAG,testModel.nama)
            }else{
                Log.d(TAG,"NOT JSON OBJECT")
                showToast("Format QR Tidak Cocok!")
            }


        }

    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)

        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data

            imageUri = data!!.data
            Log.d(TAG,"galleryActivityResultLauncher: imageUri: $imageUri")

            binding.imageTv.setImageURI(imageUri)

        }else {
            showToast("Dibatalkan!")
        }
    }

    private fun pickImageCamera(){
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, "Foto Sample")
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Deskripsi Foto Sample")

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        cameraActivityResultLauncher.launch(intent)
    }

    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data
            Log.d(TAG, "cameraActivityResultLauncher: imageUri: $imageUri")

            binding.imageTv.setImageURI(imageUri)
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }
}