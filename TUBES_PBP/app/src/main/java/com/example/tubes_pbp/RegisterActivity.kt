package com.example.tubes_pbp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var inputNama: TextInputLayout
    private lateinit var inputEmail: TextInputLayout
    private lateinit var inputNoHP : TextInputLayout
    private lateinit var inputTglLahir : TextInputLayout
    private lateinit var inputUsername : TextInputLayout
    private lateinit var inputPassword : TextInputLayout
    private lateinit var inputKonfirmasi : TextInputLayout


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
        inputKonfirmasi = findViewById(R.id.til_verPassword)

        btnBackLoginListener()
        btnRegisterListener()
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
            val confirmPassword: String = inputKonfirmasi.getEditText()?.getText().toString()

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

            if (confirmPassword.isEmpty()){
                inputKonfirmasi.setError("Password must be filled with text")
                checkRegister = false
            }


            if(!nama.isEmpty() && !tglLahir.isEmpty() && !noHp.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()){
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
}