package com.example.tubes_pbp.camera

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tubes_pbp.R
import android.hardware.Camera
import android.util.Log
import android.view.View
import android.widget.FrameLayout

class CameraActivity : AppCompatActivity() {
    private var mCamera: Camera? = null
    private var mCameraView: CameraView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        supportActionBar?.hide()

        try {
            mCamera = Camera.open()
        }catch (e: Exception) {
            Log.d("Error", "Failed to get Camera" + e.message)
        }
        if(mCamera != null) {
            mCameraView = CameraView(this, mCamera!!)
            val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
            camera_view.addView(mCameraView)
        }
    }
}