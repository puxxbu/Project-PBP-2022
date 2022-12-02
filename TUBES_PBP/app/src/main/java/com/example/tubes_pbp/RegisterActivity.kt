package com.example.tubes_pbp

import android.app.*
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isEmpty
import com.example.tubes_pbp.databinding.ActivityFormEditBookmarkBinding
import com.example.tubes_pbp.databinding.ActivityMainBinding
import com.example.tubes_pbp.databinding.ActivityRegisterBinding
import com.example.tubes_pbp.entity.room.Users
import com.example.tubes_pbp.entity.room.UsersDB
import com.example.tubes_pbp.notifications.NotificationReceiver
import com.example.tubes_pbp.webapi.RClient
import com.example.tubes_pbp.webapi.ResponseCreate
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import www.sanju.motiontoast.MotionToast
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var usersDb: UsersDB
    private lateinit var prefManager: PrefManager
    private lateinit var binding : ActivityRegisterBinding
    private val CHANNEL_ID = "register_notification"
    private val notificationId = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.tietTglLahir.setFocusable(false)
        usersDb = UsersDB.getDatabase(this)

        createNotificationChannel()

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
             val user = Users(0,username,password,nama,email,noHp,tglLahir)
             saveData(user)


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

    fun saveData(user: Users){

            val username =  user.username
            val password: String =   user.password
            val nama: String = user.nama
            val noHP: String = user.noHP
            val email: String = user.email
            val tglLahir: String =user.tglLahir

            val moveLogin = Intent(this, LoginActivity::class.java)
            val mBundle = Bundle()

            mBundle.putString("username", username)
            mBundle.putString("password", password)


            prefManager = PrefManager(this)
            prefManager.setUsername(username)

            Log.d(TAG,username)


            RClient.instances.createDataUser(username,password,nama,email,noHP,tglLahir).enqueue(object :
                Callback<ResponseCreate> {
                override fun onResponse(
                    call: Call<ResponseCreate>,
                    response: retrofit2.Response<ResponseCreate>
                ) {
                    if(response.isSuccessful){
                        Log.d(TAG, "WOI AMAN")



                        moveLogin.putExtra("register",mBundle)
                        startActivity(moveLogin)
                        sendNotification(nama)
                        finish()
//                        Toast.makeText(this@RegisterActivity,"Register is Success\",\n" +
//                                "                            \"Selamat Sekarang Anda Hanya Perlu Login", Toast.LENGTH_LONG).show()
                        MotionToast.Companion.createToast( this@RegisterActivity, "Register is Success",
                            "Selamat Sekarang Anda Hanya Perlu Login",
                            MotionToast.TOAST_SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            null
                        )

                    }else {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())

                        val error = getError(jsonObj,"nama")
                        Log.d(TAG, "WOI ERROR" + error )

                        with(binding){

                            if (getError(jsonObj,"username").isNotEmpty()){

                                tilUsername.setError(getError(jsonObj,"username"))
                            }

                            if (getError(jsonObj,"password").isNotEmpty()){
                                tilPassword.setError(getError(jsonObj,"password"))
                            }

                            if (getError(jsonObj,"nama").isNotEmpty()){
                                tilNamaLengkap.setError(getError(jsonObj,"nama"))
                            }

                            if (getError(jsonObj,"noHP").isNotEmpty()){
                                tilNoHP.setError(getError(jsonObj,"noHP"))
                            }

                            if (getError(jsonObj,"email").isNotEmpty()){
                                tilEmail.setError(getError(jsonObj,"email"))
                            }

                            if (getError(jsonObj,"tglLahir").isNotEmpty()){
                                tilTglLahir.setError(getError(jsonObj,"tglLahir"))
                            }
                        }

//                        Toast.makeText(applicationContext,"Maaf sudah ada datanya", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {
                }
            })

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

