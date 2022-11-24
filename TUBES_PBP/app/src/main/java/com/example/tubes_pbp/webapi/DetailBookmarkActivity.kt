package com.example.tubes_pbp.webapi

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
import com.example.tubes_pbp.databinding.ActivityDetailBookmarkBinding
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
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
import java.time.format.DateTimeFormatter

class DetailBookmarkActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBookmarkBinding
    private var b:Bundle? = null
    private val listBookmark = ArrayList<BookmarkData>()
    val bookmark = ArrayList<BookmarkData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBookmarkBinding.inflate(layoutInflater)
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
                    FormEditBookmarkActivity::class.java
                ).apply {
                    putExtra("id", id)
                })
        }
        binding.btnPrintPdf.setOnClickListener {
            with(binding) {
                val nama = tvNama.text.toString()
                val alamat = tvAlamat.text.toString()

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        createPdf(nama, alamat)
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
        Callback<ResponseDataBookmark> {
        override fun onResponse(
            call: Call<ResponseDataBookmark>,
            response: Response<ResponseDataBookmark>
        ) {
            if(response.isSuccessful){
                response.body()?.let {
                    listBookmark.addAll(it.data)
                     bookmark.add(listBookmark.find { it.id == id }!!)
                }
                Log.d(TAG,  "getDataDetail")
                with(binding) {
                    tvNama.text = bookmark[0].nama
                    tvAlamat.text = bookmark[0].alamat
                }
            }
        }
        override fun onFailure(call: Call<ResponseDataBookmark>, t: Throwable) {
            t.message?.let { Log.d("failure", it) }
        }
    })
    }
    override fun onRestart() {
        super.onRestart()
        this.recreate()
    }
    fun deleteData(idData:Int){
        val builder = AlertDialog.Builder(this@DetailBookmarkActivity)
        builder.setMessage("Anda yakin ingin menghapus hotel ini ?.")
            .setCancelable(false)
            .setPositiveButton("Ya"){dialog, id->doDeleteData(idData)
                MotionToast.Companion.createToast( this, "Delete Data is Success",
                    "Data Hotel Berhasil Dihapus",
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
    private fun createPdf(nama: String, alamat: String) {
        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val file = File(pdfPath, "Bookmark Hotel Tubes_PBP.pdf")
        FileOutputStream(file)
        //Initiate
        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)
        pdfDocument.defaultPageSize = PageSize.A4
        document.setMargins(5f,5f,5f,5f)

        //Isi data
        val dataBookmark = Paragraph("Data Bookmark").setBold().setFontSize(24f).setTextAlignment(TextAlignment.CENTER)
        val width = floatArrayOf(100f, 100f)
        val table = Table(width)
        table.setHorizontalAlignment(HorizontalAlignment.CENTER)
        table.addCell(Cell().add(Paragraph("Nama Hotel")))
        table.addCell(Cell().add(Paragraph(nama)))
        table.addCell(Cell().add(Paragraph("Alamat Hotel")))
        table.addCell(Cell().add(Paragraph(alamat)))
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        table.addCell(Cell().add(Paragraph("Tanggal Buat PDF")))
        table.addCell(Cell().add(Paragraph(LocalDate.now().format(dateTimeFormatter))))

        document.add(dataBookmark)
        document.add(table)
        document.close()
        Toast.makeText(this, "Pdf berhasil dibuat", Toast.LENGTH_LONG).show()
    }
}