package com.example.tubes_pbp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tubes_pbp.entity.room.UsersDB
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var loginLayout: ConstraintLayout

    lateinit var  mBundle:Bundle

    private lateinit var usersDb: UsersDB
    private lateinit var prefManager: PrefManager

    lateinit var vUser: String
    lateinit var vPassword: String
    private var checkLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        prefManager = PrefManager(this)

        usersDb = UsersDB.getDatabase(this)

        val moveHome = Intent(this, HomeActivity::class.java)

        inputUsername = findViewById(R.id.inputLayoutUsername)!!
        inputPassword = findViewById(R.id.inputLayoutPassword)!!
        loginLayout = findViewById(R.id.loginLayout)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        inputUsername.getEditText()?.setText("")
        inputPassword.getEditText()?.setText("")
        vUser = ""
        vPassword = ""

        val mySnackbar = Snackbar.make(loginLayout,"Registrasi Terlebih Dahulu !",Snackbar.LENGTH_SHORT)

        btnBackLoginListener()

        if (intent.getBundleExtra("register") != null){
            getBundle()
            setText()
        }

        btnLogin.setOnClickListener(View.OnClickListener {
            prefManager.setLoggin(false)

            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            if (username.isEmpty()){
                inputUsername.setError("Username must be filled with text")
                prefManager.setLoggin(false)
            }

            if (password.isEmpty()){
                inputPassword.setError("Password must be filled with text")
                prefManager.setLoggin(false)
            }

            CoroutineScope(Dispatchers.IO).launch {
                val user = usersDb.usersDao().getUser(username,password)

                if (user == null){
                    Log.d("LoginActivity","USER IS NULL")
                    withContext(Dispatchers.Main){
                        inputUsername.setError("Username tidak sesuai !")
                        inputPassword.setError("Password tidak sesuai !")
                        prefManager.setLoggin(false)

                    }
                }else{
                    Log.d("LoginActivity","USER FOUND")
                    withContext(Dispatchers.Main){
                        startActivity(moveHome)
                        prefManager.setLoggin(true)
                        prefManager.setUser(user)
                    }

                }

            }

//
//            if(vUser.isNotEmpty() && vPassword.isNotEmpty()){
//                val username: String = inputUsername.getEditText()?.getText().toString()
//                val password: String = inputPassword.getEditText()?.getText().toString()
//
//                if (username.isEmpty()){
//                    inputUsername.setError("Username must be filled with text")
//                    checkLogin = false
//                }
//
//                if (password.isEmpty()){
//                    inputPassword.setError("Password must be filled with text")
//                    checkLogin = false
//                }
//
//                if (username == vUser && password == vPassword) {
//                    checkLogin = true
//                } else if(username != vUser && password == vPassword){
//                    inputUsername.setError("Username tidak sesuai !")
//                    checkLogin = false
//                } else if(username == vUser && password != vPassword){
//                    inputPassword.setError("Password tidak sesuai !")
//                    checkLogin = false
//                } else{
//                    inputUsername.setError("Username tidak sesuai !")
//                    inputPassword.setError("Password tidak sesuai !")
//                    checkLogin = false
//                }
//                if (!checkLogin) return@OnClickListener
//                val moveHome = Intent(this, HomeActivity::class.java)
//                startActivity(moveHome)
////                setContentView(R.layout.activity_home)
//            }else{
//                mySnackbar.show()
//            }

        })
    }

    private fun btnBackLoginListener(){
        lgn_imgBack.setOnClickListener {
            val moveHome = Intent(this, MainActivity::class.java)
            startActivity(moveHome)
        }
    }

    fun getBundle(){
        mBundle = intent.getBundleExtra("register")!!
        vUser = mBundle.getString("username")!!
        vPassword = mBundle.getString("password")!!
    }

    fun setText(){
        inputUsername.getEditText()?.setText(vUser)
        inputPassword.getEditText()?.setText(vPassword)
    }
}