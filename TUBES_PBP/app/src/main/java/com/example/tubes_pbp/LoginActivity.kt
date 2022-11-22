package com.example.tubes_pbp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.tubes_pbp.entity.room.UsersDB
import com.example.tubes_pbp.notifications.NotificationReceiver
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_login.*
import com.example.tubes_pbp.maps.*
import com.example.tubes_pbp.webapi.RClient
import com.example.tubes_pbp.webapi.userApi.ResponseDataUser
import com.example.tubes_pbp.webapi.userApi.UserData
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import kotlin.math.log


class LoginActivity : AppCompatActivity() {

    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var loginLayout: ConstraintLayout
    private val CHANNEL_ID = "login_notification"
    private val notificationId = 101

    lateinit var  mBundle:Bundle

    private lateinit var usersDb: UsersDB
    private lateinit var prefManager: PrefManager

    lateinit var vUser: String
    lateinit var vPassword: String
    private var checkLogin = false

    private var listUser = ArrayList<UserData>()
    private val filterUser = ArrayList<UserData>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        prefManager = PrefManager(this)

        usersDb = UsersDB.getDatabase(this)

        val moveHome = Intent(this, HomeActivity::class.java)
        val moveMap = Intent(this, MapActivity::class.java)

        inputUsername = findViewById(R.id.inputLayoutUsername)!!
        inputPassword = findViewById(R.id.inputLayoutPassword)!!
        loginLayout = findViewById(R.id.loginLayout)
        val btnLogin: Button = findViewById(R.id.btnLogin)
//        val btnMap: Button = findViewById(R.id.btnMap)
        inputUsername.getEditText()?.setText("")
        inputPassword.getEditText()?.setText("")
        vUser = ""
        vPassword = ""

        val mySnackbar = Snackbar.make(loginLayout,"Login Invalid !",Snackbar.LENGTH_SHORT)


        btnBackLoginListener()
        createNotificationChannel()

        if (intent.getBundleExtra("register") != null){
            getBundle()
            setText()
        }

//        btnMap.setOnClickListener(View.OnClickListener {
//            startActivity(moveMap)
//        })

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

            getAllDataUser()

            MotionToast.Companion.createToast( this, "Login Success!",
            "Selamat Datang Di YourTravel",
                MotionToast.TOAST_SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular)
            )











//            CoroutineScope(Dispatchers.IO).launch {
//                val user = usersDb.usersDao().getUser(username,password)
//
//                if (user == null){
//                    Log.d("LoginActivity","USER IS NULL")
//                    withContext(Dispatchers.Main){
//                        inputUsername.setError("Username tidak sesuai !")
//                        inputPassword.setError("Password tidak sesuai !")
//                        prefManager.setLoggin(false)
//                        mySnackbar.show()
//                    }
//                }else{
//                    Log.d("LoginActivity","USER FOUND")
//                    withContext(Dispatchers.Main){
//                        startActivity(moveHome)
//                        prefManager.setLoggin(true)
//                        prefManager.setUser(user)
//                        sendNotification()
//                    }
//
//                }
//
//            }


        })
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID,name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager : NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)

        }
    }

    private fun sendNotification() {
        val intent : Intent = Intent(this, RegisterActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent : PendingIntent = PendingIntent.getActivity(this, 0,intent,0)

        val registerBigPicBitmap = ContextCompat.getDrawable(this, R.drawable.travelshot)?.toBitmap()

        val broadcastIntent : Intent = Intent(this, NotificationReceiver::class.java)
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_beranda_24)
            .setContentTitle("Terima kasih telah login!")
            .setContentText("Selamat menikmati fitur kami untuk Anda")
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(registerBigPicBitmap))
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.CYAN)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId,builder.build())
        }

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

    fun getAllDataUser(): ArrayList<UserData> {
        RClient.instances.getAllDataUser().enqueue(object :
            Callback<ResponseDataUser> {
            override fun onResponse(
                call: Call<ResponseDataUser>,
                response: Response<ResponseDataUser>
            ){
                if (response.isSuccessful){
                    listUser.clear()
                    response.body()?.let {
                        listUser.addAll(it.data)
                        listUser.find { it.username == inputUsername.getEditText()?.getText().toString() && it.password == inputPassword.getEditText()?.getText().toString() }
                            ?.let { it1 -> filterUser.add(it1) }

                        Log.d(TAG, listUser.toString() + " LIST USER")

                        if (filterUser.isEmpty()){
                            Log.d(TAG,filterUser.toString() + " LIST USER EMPTY")
                            prefManager.setLoggin(false)
                            Snackbar.make(loginLayout,"Login Invalid !",Snackbar.LENGTH_SHORT).show()
                        }else{
                            Log.d(TAG,filterUser.toString() + " LIST USER WONTEN")
                            prefManager.setLoggin(true)
                            prefManager.setUserID(filterUser[0].id)
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        }


                    }
                }
            }
            override fun onFailure(call: Call<ResponseDataUser>, t: Throwable) {
            }
        })

        return listUser
    }
}