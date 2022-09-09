package com.example.tubes_pbp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
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
                inputUsername.setError("Username must be filled with text")
                checkRegister = false
            }

            if (password.isEmpty()){
                inputPassword.setError("Password must be filled with text")
                checkRegister = false
            }

            if (nama.isEmpty()){
                inputNama.setError("Password must be filled with text")
                checkRegister = false
            }

            if (noHp.isEmpty()){
                inputNoHP.setError("Password must be filled with text")
                checkRegister = false
            }

            if (email.isEmpty()){
                inputEmail.setError("Password must be filled with text")
                checkRegister = false
            }

            if (tglLahir.isEmpty()){
                inputTglLahir.setError("Username must be filled with text")
                checkRegister = false
            }




            if(!nama.isEmpty() && !tglLahir.isEmpty() && !noHp.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty() ){
                checkRegister = true
            }

            if(!checkRegister){
                return@OnClickListener

            }else{
                val moveLogin = Intent(this, LoginActivity::class.java)

                moveLogin.putExtra("register",mBundle)


                startActivity(moveLogin)
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
}