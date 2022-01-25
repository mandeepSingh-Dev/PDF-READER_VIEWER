package com.example.pdf_reader_viewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.pdf_reader_viewer.R

class PdfsTools_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfs_tools)

        if(intent.getStringExtra("OPEN_MERGE_FRAGMENT").equals("OPEN_MERGE_FRAGMENT"))
            {
            var navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                 navHostFragment.navController.navigate(R.id.fragment_MergePdfs)
            }


    }
}