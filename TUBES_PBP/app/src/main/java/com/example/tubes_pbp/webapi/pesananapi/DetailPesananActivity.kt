package com.example.tubes_pbp.webapi.pesananapi

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tubes_pbp.databinding.ActivityDetailPesananBinding
import com.example.tubes_pbp.webapi.*
import com.google.gson.Gson
import com.itextpdf.barcodes.BarcodeQRCode
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class DetailPesananActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailPesananBinding
    private var b:Bundle? = null
    private val listPesanan = ArrayList<PesananData>()
    val pesanan = ArrayList<PesananData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        b = intent.extras
        val id = b?.getInt("id")
        if (id != null) {
            Log.d(TAG, id.toString() + "TESSSS")
            getDataDetail(id)
        }

        id?.let { getDataDetail(it) }
        binding.btnHapus.setOnClickListener {
            id?.let { it1 -> deleteData(it1) }
        }
        binding.btnEdit.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    FormEditPesananActivity::class.java
                ).apply {
                    putExtra("id", id)
                })
        }
        binding.btnPrintPdf.setOnClickListener {
            with(binding) {
                val nama = tvNama.text.toString()
                val wilayah = tvWilayah.text.toString()
                val alamat = tvAlamat.text.toString()

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        createPdf(nama, wilayah, alamat)
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }
    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Throws(
        FileNotFoundException::class
    )
    fun getDataDetail(id:Int){ RClient.instances.getData(id.toString()).enqueue(object :
        Callback<ResponseDataPesanan>{
        override fun onResponse(
            call: Call<ResponseDataPesanan>,
            response: Response<ResponseDataPesanan>
        ) {
            if(response.isSuccessful){
                response.body()?.let {
                    listPesanan.addAll(it.data)
                    pesanan.add(listPesanan.find { it.id == id }!!)
                }
                Log.d(TAG,  "getDataDetail")
                with(binding) {
                    tvNama.text = pesanan[0].nama
                    tvWilayah.text = pesanan[0].wilayah
                    tvAlamat.text = pesanan[0].alamat
                }
            }
        }
        override fun onFailure(call: Call<ResponseDataPesanan>, t: Throwable) {
            t.message?.let { Log.d("failure", it) }
        }
    })
    }
    override fun onRestart() {
        super.onRestart()
        this.recreate()
    }
    fun deleteData(idData:Int){
        val builder = AlertDialog.Builder(this@DetailPesananActivity)
        builder.setMessage("Anda yakin ingin menghapus pesanan ini ?.")
            .setCancelable(false)
            .setPositiveButton("Ya"){dialog, id->doDeleteData(idData)
                MotionToast.Companion.createToast( this, "Delete Data is Success",
                    "Data Pesanan Berhasil Dihapus",
                    MotionToast.TOAST_SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null
                )
            }
            .setNegativeButton("Batal"){dialog,id -> dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }
    private fun doDeleteData(id:Int) {
        RClient.instances.deleteData(id).enqueue(object : Callback<ResponseCreate>{
            override fun onResponse(call: Call<ResponseCreate>,
                                    response: Response<ResponseCreate>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "Data berhasil dihapus", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
            override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {
            }
        })
    }
    private fun createPdf(nama: String, wilayah: String, alamat: String) {
        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val randString = getRandomString(5)
        val file = File(pdfPath, "Pesanan Hotel Tubes_PBP ${randString}.pdf")

        FileOutputStream(file)
        //Initiate
        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)
        pdfDocument.defaultPageSize = PageSize.A4
        document.setMargins(5f,5f,5f,5f)

        //Isi data
        val dataPesanan = Paragraph("Data Pesanan").setBold().setFontSize(24f).setTextAlignment(TextAlignment.CENTER)
        val width = floatArrayOf(100f, 100f)
        val table = Table(width)
        table.setHorizontalAlignment(HorizontalAlignment.CENTER)
        table.addCell(Cell().add(Paragraph("Nama Hotel")))
        table.addCell(Cell().add(Paragraph(nama)))
        table.addCell(Cell().add(Paragraph("Wilayah Hotel")))
        table.addCell(Cell().add(Paragraph(wilayah)))
        table.addCell(Cell().add(Paragraph("Alamat Hotel")))
        table.addCell(Cell().add(Paragraph(alamat)))
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        table.addCell(Cell().add(Paragraph("Tanggal Buat PDF")))
        table.addCell(Cell().add(Paragraph(LocalDate.now().format(dateTimeFormatter))))
        val gson = Gson()

        val data = PesananData(1,nama,wilayah,alamat)
        val dataJSON = gson.toJson(data)
        val barcodeQRCode = BarcodeQRCode(dataJSON)
        val qrCodeObject = barcodeQRCode.createFormXObject(ColorConstants.BLACK, pdfDocument)
        val qrCodeImage = Image(qrCodeObject).setWidth(80f).setHorizontalAlignment(HorizontalAlignment.CENTER)


        document.add(dataPesanan)
        document.add(table)
        document.add(qrCodeImage)
        document.close()
        Toast.makeText(this, "Pdf berhasil dibuat", Toast.LENGTH_LONG).show()
    }

    fun getRandomString(length: Int) : String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }
}

