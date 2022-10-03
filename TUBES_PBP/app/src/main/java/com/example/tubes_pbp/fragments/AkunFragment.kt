package com.example.tubes_pbp.fragments

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import com.example.tubes_pbp.*
import com.example.tubes_pbp.databinding.FragmentAkunBinding
import com.example.tubes_pbp.databinding.FragmentEditAkunBinding
import com.example.tubes_pbp.entity.room.UsersDB
import com.example.tubes_pbp.notifications.NotificationReceiver
import kotlinx.android.synthetic.main.fragment_akun.view.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class AkunFragment : Fragment(R.layout.fragment_akun) {
    private var _binding : FragmentAkunBinding? = null
    private val binding get() = _binding!!

    private lateinit var usersDb: UsersDB
    private lateinit var prefManager: PrefManager

    private val CHANNEL_ID = "progress_load"
    private val notificationId = 3
    private val CHANNEL_ID2 = "progress_done"
    private val notificationId2 = 4

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_akun,container, false)

        var dialog = LogoutAlert()
        val rootView: View = inflater.inflate(R.layout.fragment_akun, container, false)

        prefManager = PrefManager(requireContext())
        usersDb = UsersDB.getDatabase(requireContext())

        createNotificationChannel()

        binding.user = prefManager.getUser()



//        val nama = prefManager.getUser()?.nama
//        val tglLahir = prefManager.getUser()?.tglLahir
//        val noHP = prefManager.getUser()?.noHP
//        val email = prefManager.getUser()?.email
//
//        binding.textNamaUser.setText(nama)
//        binding.tietNamaLengkap.setText(nama)
//        binding.tietTglLahir.setText(tglLahir)
//        binding.tietNoHP.setText(noHP)
//        binding.tietEmail.setText(email)

        val myCalendar = Calendar.getInstance()





        binding.tietTglLahir.setFocusable(false)

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(myCalendar, binding)
        }

        binding.tietTglLahir.setOnClickListener {
            Log.d("CALENDAR", "TES MASUK KALENDER HARUSE")
            DatePickerDialog(
                requireContext(),
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.btnEdit.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val id = prefManager.getUser()?.id
                val nama = binding.tilNamaLengkap.getEditText()?.getText().toString()
                val tglLahir = binding.tilTglLahir.getEditText()?.getText().toString()
                val noHP = binding.tilNoHP.getEditText()?.getText().toString()
                val email = binding.tilEmail.getEditText()?.getText().toString()
                usersDb.usersDao().updateUser(id,nama,tglLahir,noHP,email)


                withContext(Dispatchers.Main){
                    val user = usersDb.usersDao().getUserbyID(id)
                    prefManager.setUser(user)

//                    binding.textNamaUser.setText(nama)
//                    binding.tietNamaLengkap.setText(nama)
//                    binding.tietTglLahir.setText(tglLahir)
//                    binding.tietNoHP.setText(noHP)
//                    binding.tietEmail.setText(email)
                    sendNotification()
                }


            }
        }



        binding.btnLogout.setOnClickListener {
            dialog.show(parentFragmentManager, "alertLogout" )
        }

        return binding.root
    }

    private fun updateLable(myCalendar: Calendar, binding: FragmentAkunBinding) {

        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.tietTglLahir.setText(sdf.format(myCalendar.time))
    }

    private fun createNotificationChannel() {
        val notificationManager : NotificationManager =
            getActivity()?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID,"Main Channel", NotificationManager.IMPORTANCE_LOW).apply {
                description = descriptionText
            }
            val channel2 = NotificationChannel(CHANNEL_ID2,"Second Channel", NotificationManager.IMPORTANCE_LOW).apply {
                description = descriptionText
            }

           notificationManager.createNotificationChannel(channel1)
           notificationManager.createNotificationChannel(channel2)
        }
    }

    private fun sendNotification() {
        val intent : Intent = Intent(getActivity(), HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val notificationManager : NotificationManagerCompat

        val max = 10
        var progress = 0

        val pendingIntent : PendingIntent = PendingIntent.getActivity(getActivity(), 0,intent,0)


        val broadcastIntent : Intent = Intent(getActivity(), NotificationReceiver::class.java)
        val actionIntent = PendingIntent.getBroadcast(getActivity(), 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)



        val builder2 = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_beranda_24)
            .setContentTitle("Completed!")
            .setContentText("")
            .setContentIntent(null)
            .clearActions()
            .setProgress(0, 0,false)



        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main){
                while (progress != max){
                    delay(1000)
                    progress += 1
                    var builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_beranda_24)
                        .setContentTitle("Uploading!")
                        .setContentText("${progress}/${max}")
                        .setProgress(max, progress,false)
                    with(NotificationManagerCompat.from(requireContext())){
                        notify(notificationId,builder.build())
                    }
                }
                with(NotificationManagerCompat.from(requireContext())){
                    notify(notificationId,builder2.build())
                }
                binding.user = prefManager.getUser()
            }


        }




    }

}