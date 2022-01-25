package com.example.pdf_reader_viewer.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pdf_reader_viewer.PdfView_Activity
import com.example.pdf_reader_viewer.PdfsTools_Activity
import com.example.pdf_reader_viewer.R
import com.example.pdf_reader_viewer.databinding.PdfToolsFragmentBinding

class PdfTools_Fragment : Fragment() {

    var binding:PdfToolsFragmentBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= PdfToolsFragmentBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.mergeImage?.setOnClickListener {
            var intent= Intent(activity,PdfsTools_Activity::class.java)
            startActivity(intent)

        }
    }
}