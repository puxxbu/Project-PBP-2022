package com.example.tubes_pbp

import android.R.attr.button
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
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.get
import com.example.tubes_pbp.entity.room.UsersDB
import com.example.tubes_pbp.maps.MapActivity
import com.example.tubes_pbp.notifications.NotificationReceiver
import com.example.tubes_pbp.webapi.RClient
import com.example.tubes_pbp.webapi.ResponseCreate
import com.example.tubes_pbp.webapi.userApi.ResponseDataUser
import com.example.tubes_pbp.webapi.userApi.UserData
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import kotlin.reflect.full.memberProperties


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
        val imgLogin: ImageView = findViewById(R.id.loginimageView)
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

        imgLogin.setOnClickListener{
            val balloon = Balloon.Builder(this@LoginActivity)
                .setWidthRatio(1.0f)
                .setHeight(BalloonSizeSpec.WRAP)
                .setText("Kamu telah menekan gambar login kami !!!")
                .setTextColorResource(R.color.white)
                .setTextSize(15f)
                .setIconDrawableResource(R.drawable.ic_edit)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowSize(10)
                .setArrowPosition(0.5f)
                .setPadding(12)
                .setCornerRadius(8f)
                .setBackgroundColorResource(R.color.primaryColor)
                .setBalloonAnimation(BalloonAnimation.ELASTIC)
                .build()
            balloon.showAlignTop(imgLogin)
            Handler().postDelayed({ balloon.dismiss() }, 2000)
        }

        btnLogin.setOnClickListener(View.OnClickListener {

            prefManager.setLoggin(false)

            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()



            login(username,password)

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

    fun login(username :String,password :String) {
        RClient.instances.loginUser(username,password).enqueue(object :
            Callback<ResponseCreate> {
            override fun onResponse(
                call: Call<ResponseCreate>,
                response: Response<ResponseCreate>
            ){
                if (response.isSuccessful){
                    response.body()?.let {
                    prefManager.setLoggin(true)
                    listUser.add(it.data)
                    Log.d(TAG,  " LIST USER WONTEN " + listUser.toString())

                    prefManager.setUserID(listUser[0].id)

                    showToast("Login Berhasil")
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    }
                }else{
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())

                    if (getError(jsonObj,"username").isNotEmpty() && getError(jsonObj,"password").isNotEmpty() ){
                        Toast.makeText(
                            this@LoginActivity,
                            getError(jsonObj,"username") + " dan " + getError(jsonObj,"password"),
                            Toast.LENGTH_SHORT
                        ).show()
                        prefManager.setLoggin(false)
                    }else{
                        if (getError(jsonObj,"username").isNotEmpty()){
                            Toast.makeText(
                                this@LoginActivity,
                                getError(jsonObj,"username"),
                                Toast.LENGTH_SHORT
                            ).show()
                            prefManager.setLoggin(false)
                        }

                        if (getError(jsonObj,"password").isNotEmpty()){
                            Toast.makeText(
                                this@LoginActivity,
                                getError(jsonObj,"password"),
                                Toast.LENGTH_SHORT
                            ).show()
                            prefManager.setLoggin(false)
                        }

                    }

                    if (getError(jsonObj,"message").isNotEmpty()){
                        Toast.makeText(
                            this@LoginActivity,
                            getError(jsonObj,"message"),
                            Toast.LENGTH_SHORT
                        ).show()
                        prefManager.setLoggin(false)
                    }








                    Log.d(TAG, "WOI ERROR" + jsonObj )
                }
            }
            override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {
            }
        })

    }

    fun showToast(text : String){
        MotionToast.Companion.createToast( this, text,
            "Selamat Datang Di YourTravel",
            MotionToast.TOAST_SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular)
        )
    }

    fun getError(jsonObject: JSONObject, target: String): String {
        if (jsonObject.has(target)){
            return jsonObject.get(target).toString()
                .replace("[","")
                .replace("]","")
                .replace("\"","")
        }else{
            return ""
        }

    }
}