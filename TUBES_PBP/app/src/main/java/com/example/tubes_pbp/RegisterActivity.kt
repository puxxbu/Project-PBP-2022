package com.example.tubes_pbp

import android.annotation.SuppressLint
import android.app.*
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isEmpty
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubes_pbp.api.RClient
import com.example.tubes_pbp.api.UserApi
import com.example.tubes_pbp.api.UserData
import com.example.tubes_pbp.api.response.ResponseCreate
import com.example.tubes_pbp.api.response.ResponseDataUser
import com.example.tubes_pbp.databinding.ActivityMainBinding
import com.example.tubes_pbp.databinding.ActivityRegisterBinding
import com.example.tubes_pbp.entity.User
import com.example.tubes_pbp.entity.room.Users
import com.example.tubes_pbp.entity.room.UsersDB
import com.example.tubes_pbp.notifications.NotificationReceiver
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var usersDb: UsersDB
    private lateinit var prefManager: PrefManager
    private var binding: ActivityRegisterBinding? = null
    private val CHANNEL_ID = "register_notification"
    private val notificationId = 101
    private var queue : RequestQueue? = null
    private val listUser = ArrayList<UserData>()
    @SuppressLint("RestrictedApi")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        queue = Volley.newRequestQueue(this)


        var binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.tietTglLahir.setFocusable(false)
        usersDb = UsersDB.getDatabase(this)

        createNotificationChannel()
        getDetailData()

        val myCalendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(myCalendar, binding)
        }

        binding.tietTglLahir.setOnClickListener {
            DatePickerDialog(
                this,
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        binding.regImgBack.setOnClickListener {
            val moveHome = Intent(this, MainActivity::class.java)
            startActivity(moveHome)
        }

        btnRegisterListener(binding)


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

    fun btnRegisterListener(binding: ActivityRegisterBinding) {


         binding.regBtnRegister.setOnClickListener(View.OnClickListener {
            var checkRegister = false
            resetAlert(binding)
             val username =  binding.tilUsername.getEditText()?.getText().toString()
             val password: String =   binding.tilPassword.getEditText()?.getText().toString()
             val nama: String = binding.tilNamaLengkap.getEditText()?.getText().toString()
             val noHp: String = binding.tilNoHP.getEditText()?.getText().toString()
             val email: String = binding.tilEmail.getEditText()?.getText().toString()
             val tglLahir: String = binding.tilTglLahir.getEditText()?.getText().toString()

             val user = User(nama,username,password,email,noHp,tglLahir)


             val mBundle = Bundle()

             mBundle.putString("username", username)
             mBundle.putString("password", password)

             if (username.isEmpty()){
                 binding.tilUsername.setError("Username must be filled ")
                 checkRegister = false
             }

             if (password.isEmpty()){
                 binding.tilPassword.setError("Password must be filled ")
                 checkRegister = false
             }

             if (nama.isEmpty()){
                 binding.tilNamaLengkap.setError("Nama must be filled ")
                 checkRegister = false
             }

             if (noHp.isEmpty()){
                 binding.tilNoHP.setError("Nomor HP must be filled ")
                 checkRegister = false
             }

             if (email.isEmpty()){
                 binding.tilEmail.setError("Email must be filled ")
                 checkRegister = false
             }

             if (tglLahir.isEmpty()){
                 binding.tilTglLahir.setError("Tanggal Lahir must be filled ")
                 checkRegister = false
             }

             if(!nama.isEmpty() && !tglLahir.isEmpty() && !noHp.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty() ){
                 checkRegister = true
//                 createUser(user)
                 saveData(user)
                 getDetailData()
             }

             if(!checkRegister){
                 return@OnClickListener

             }else {
                val moveLogin = Intent(this, LoginActivity::class.java)

                moveLogin.putExtra("register",mBundle)
                startActivity(moveLogin)
                sendNotification(nama)

                prefManager = PrefManager(this)
                prefManager.setUsername(username)

                val user = Users(0,username,password,nama,email,noHp,tglLahir)
                 // ROOM DELETE
//                CoroutineScope(Dispatchers.IO).launch{
//                     usersDb.usersDao().addUsers(user)
//                     finish()
//                }
                 Toast.makeText(this, "Masukkan berhasil!", Toast.LENGTH_SHORT).show()

             }

             })
    }

//    private fun createUser(user: User){
//        Log.d(TAG, user.nama)
//        val stringRequest : StringRequest =
//            object : StringRequest(Method.POST, UserApi.ADD_URL, Response.Listener { response ->
//                val gson = Gson()
//                var user = gson.fromJson(response, User::class.java)
//
//                if (user != null)
//                    Toast.makeText(this@RegisterActivity, "Data Berhasil Ditambahkan",Toast.LENGTH_LONG).show()
//
//
//            }, Response.ErrorListener { error ->
//                try {
//                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
//                    val errors = JSONObject(responseBody)
//                    Log.d(TAG, errors.getString("message"))
//                    Toast.makeText(
//                        this@RegisterActivity,
//                        errors.getString("message"),
//                        Toast.LENGTH_LONG
//
//                    ).show()
//                }catch (e: Exception) {
//                    e.message?.let { Log.d(TAG, it) }
//                    Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_LONG).show()
//                }
//            }){
//                @Throws(AuthFailureError::class)
//                override fun getHeaders(): Map<String, String> {
//                    val headers = HashMap<String, String>()
//                    headers["Accept"] = "application/json"
//                    print("headers")
//                    return headers
//                }
//
//                @Throws(AuthFailureError::class)
//                override fun getBody(): ByteArray {
//                    val gson = Gson()
//                    val requestBody = gson.toJson(user)
//                    print("body")
//                    return requestBody.toByteArray(StandardCharsets.UTF_8)
//                }
//
//                override fun getBodyContentType(): String {
//                    return "application/json"
//                }
//            }
//
//        queue!!.add(stringRequest)
//
//    }

    fun saveData(user: User){
        with(binding) {
            val username =  user.username
            val password: String =   user.password
            val nama: String = user.nama
            val noHP: String = user.noHP
            val email: String = user.email
            val tglLahir: String =user.tglLahir

            Log.d(TAG,username)


            RClient.instances.createData(nama,username,password,email,noHP,tglLahir).enqueue(object :
                Callback<ResponseCreate> {
                override fun onResponse(
                    call: Call<ResponseCreate>,
                    response: retrofit2.Response<ResponseCreate>
                ) {
                    if(response.isSuccessful){

                        Toast.makeText(applicationContext,"${response.body()?.pesan}",
                            Toast.LENGTH_LONG).show()
                        finish()
                    }else {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())

                        Toast.makeText(applicationContext,"Maaf sudah ada datanya", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {
                }
            })
        }
    }

    fun getDetailData() {
        RClient.instances.getData().enqueue(object : Callback<ResponseDataUser> {
            override fun onResponse(
                call: Call<ResponseDataUser>,
                response: retrofit2.Response<ResponseDataUser>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        listUser.addAll(it.data) }
                    Log.d(TAG, "BERHASIL")
                    Log.d(TAG, listUser.toString())
                }
            }
            override fun onFailure(call: Call<ResponseDataUser>, t: Throwable) {
                Log.d(TAG, "GAGAL")
            }
        })
    }

    private fun sendNotification(nama :String) {
        val intent : Intent = Intent(this, RegisterActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent : PendingIntent = PendingIntent.getActivity(this, 0,intent,0)

        val registerBigPicBitmap = ContextCompat.getDrawable(this, R.drawable.checklist)?.toBitmap()

        val broadcastIntent : Intent = Intent(this, NotificationReceiver::class.java)
        // input data nama user pada binding text pada register layout
//        broadcastIntent.putExtra("toastMessage", binding?.etMessage?.text.toString())
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_beranda_24)
            .setContentTitle("Terima kasih telah mendaftar!")
            .setContentText("Selamat datang di YourTravel, ${nama}")
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

    private fun resetAlert(binding: ActivityRegisterBinding) {
        binding.tilEmail.setError(null)
        binding.tilUsername.setError(null)
        binding.tilNoHP.setError(null)
        binding.tilTglLahir.setError(null)
        binding.tilNamaLengkap.setError(null)
        binding.tilPassword.setError(null)
    }


    private fun updateLable(myCalendar: Calendar, binding: ActivityRegisterBinding) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.tietTglLahir.setText(sdf.format(myCalendar.time))
    }

}

