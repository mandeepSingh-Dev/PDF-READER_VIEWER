package com.example.pdf_reader_viewer

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.pdf_reader_viewer.fragments.SettingsFragment


class SettingsActviity : AppCompatActivity() {

  //  var binding:ActivitySettingsActviityBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // binding= ActivitySettingsActviityBinding.inflate(LayoutInflater.from(this))
        setContentView(com.example.pdf_reader_viewer.R.layout.setting_activity)

        supportActionBar?.title = "settings"
        if (findViewById<FrameLayout>(com.example.pdf_reader_viewer.R.id.framelayout) != null) {
            if (savedInstanceState != null) {
                return;
            }
            supportFragmentManager?.beginTransaction()
                ?.add(com.example.pdf_reader_viewer.R.id.framelayout, SettingsFragment()).commit()
        }
    }
}
