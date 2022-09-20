package com.example.tubes_pbp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.tubes_pbp.entity.room.Users
import com.example.tubes_pbp.entity.room.UsersDB
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var usersDb :UsersDB
    private lateinit var inputNama: TextInputLayout
    private lateinit var inputEmail: TextInputLayout
    private lateinit var inputNoHP : TextInputLayout
    private lateinit var inputTglLahir : TextInputLayout
    private lateinit var inputUsername : TextInputLayout
    private lateinit var inputPassword : TextInputLayout
    private lateinit var inputKonfirmasi : TextInputLayout
    private lateinit var textTglLahir : TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()


        usersDb = UsersDB.getDatabase(this)

        inputNama = findViewById(R.id.til_namaLengkap)
        inputEmail = findViewById(R.id.til_email)
        inputNoHP = findViewById(R.id.til_noHP)
        inputTglLahir = findViewById(R.id.til_tglLahir)
        inputUsername = findViewById(R.id.til_username)
        inputPassword = findViewById(R.id.til_password)
        textTglLahir = findViewById(R.id.tiet_tglLahir)

        textTglLahir.setFocusable(false)

        btnBackLoginListener()
        btnRegisterListener()

        val myCalendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(myCalendar)
        }

        textTglLahir.setOnClickListener {
            DatePickerDialog(this, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun btnRegisterListener(){
        reg_btnRegister.setOnClickListener(View.OnClickListener {
            var checkRegister = false
            resetAlert()

            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()
            val nama: String = inputNama.getEditText()?.getText().toString()
            val noHp: String = inputNoHP.getEditText()?.getText().toString()
            val email: String = inputEmail.getEditText()?.getText().toString()
            val tglLahir: String = inputTglLahir.getEditText()?.getText().toString()

            val mBundle = Bundle()

            mBundle.putString("username", username)
            mBundle.putString("password", password)

            if (username.isEmpty()){
                inputUsername.setError("Username must be filled ")
                checkRegister = false
            }

            if (password.isEmpty()){
                inputPassword.setError("Password must be filled ")
                checkRegister = false
            }

            if (nama.isEmpty()){
                inputNama.setError("Nama must be filled ")
                checkRegister = false
            }

            if (noHp.isEmpty()){
                inputNoHP.setError("Nomor HP must be filled ")
                checkRegister = false
            }

            if (email.isEmpty()){
                inputEmail.setError("Email must be filled ")
                checkRegister = false
            }

            if (tglLahir.isEmpty()){
                inputTglLahir.setError("Tanggal Lahir must be filled ")
                checkRegister = false
            }




            if(!nama.isEmpty() && !tglLahir.isEmpty() && !noHp.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty() ){
                checkRegister = true
            }

            if(!checkRegister){
                return@OnClickListener

            }else{
//                val moveLogin = Intent(this, LoginActivity::class.java)

                val user = Users(0,username,password,nama,email,noHp,tglLahir)
                GlobalScope.launch(Dispatchers.IO){
                    usersDb.usersDao().addUsers(user)

                }
                Toast.makeText(this,"Masukkan berhasil!",Toast.LENGTH_SHORT).show()



//                moveLogin.putExtra("register",mBundle)


//                startActivity(moveLogin)
            }

        })
    }

    private fun btnBackLoginListener(){
        reg_imgBack.setOnClickListener {
            val moveHome = Intent(this, MainActivity::class.java)
            startActivity(moveHome)
        }
    }

    private fun updateLable(myCalendar: Calendar){
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK )
        textTglLahir.setText(sdf.format(myCalendar.time))
    }

    private fun resetAlert(){
        inputUsername.setError(null)

        inputPassword.setError(null)

        inputNama.setError(null)

        inputNoHP.setError(null)

        inputEmail.setError(null)

        inputTglLahir.setError(null)
    }
}