package com.example.tubes_pbp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var loginLayout: ConstraintLayout



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setTitle("User Login")
        inputUsername = findViewById(R.id.inputLayoutUsername)!!
        inputPassword = findViewById(R.id.inputLayoutPassword)!!
        loginLayout = findViewById(R.id.loginLayout)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        inputUsername.getEditText()?.setText("")
        inputPassword.getEditText()?.setText("")

        btnBackLoginListener()





        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin = false
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            if (username.isEmpty()){
                inputUsername.setError("Username must be filled with text")
                checkLogin = false
            }

            if (password.isEmpty()){
                inputPassword.setError("Password must be filled with text")
                checkLogin = false
            }

            if (username == "admin" && password == "0606") checkLogin = true
            if (!checkLogin) return@OnClickListener
//            val moveHome = Intent(this, HomeActivity::class.java)
//            startActivity(moveHome)

        })
    }

    private fun btnBackLoginListener(){
        lgn_imgBack.setOnClickListener {
            val moveHome = Intent(this, MainActivity::class.java)
            startActivity(moveHome)
        }
    }
}