package com.example.pdf_reader_viewer.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pdf_reader_viewer.R
import com.example.pdf_reader_viewer.UtilClasses.PdfOperations
import com.example.pdf_reader_viewer.databinding.EncryptPdfFragmentBinding


class EncryptPdf_Fragment : Fragment() {

    var binding:EncryptPdfFragmentBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=EncryptPdfFragmentBinding.inflate(inflater)
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.addpDFImageView2?.setOnClickListener {
            launcher.launch(arrayOf("application/pdf"))
        }

    }


    var launcher=registerForActivityResult(object:ActivityResultContracts.OpenDocument(){

    }, ActivityResultCallback {
        Log.d("383hgf",it.toString())
        var uri=it
        var isEncrypted=false
        binding?.encryptButton?.setOnClickListener {
            var text=binding?.edittextlayout11?.editText?.text.toString()
            if(!text.isEmpty()) {
                isEncrypted=PdfOperations(requireActivity()).createEncryptedPdf(requireActivity(), uri, text)

                if(isEncrypted)
                {
                    Log.d("e;fje",isEncrypted.toString())
                    deleteContent(uri)
                }
            }
            else{
                binding?.edittextlayout11?.error = "Invalid"
            }
        }

    })

    fun deleteContent(uri: Uri)
    {
        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        // Check for the freshest data.
        activity?.contentResolver?.takePersistableUriPermission(uri, takeFlags)
        var bool=DocumentsContract.deleteDocument(activity?.applicationContext?.contentResolver!!, uri)
        Log.d("867r38fh",bool.toString())

    }

}