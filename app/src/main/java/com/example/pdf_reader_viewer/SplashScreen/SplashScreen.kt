package com.example.pdf_reader_viewer.SplashScreen

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import com.example.pdf_reader_viewer.MainActivity_ViewPagerHolder
import com.example.pdf_reader_viewer.R
import kotlinx.coroutines.*
import java.io.File

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen_activity)




        CoroutineScope(Dispatchers.IO).launch {
           // delay(1000)

            startActivity(Intent(applicationContext,MainActivity_ViewPagerHolder::class.java))
            finish()
        }
    }
}