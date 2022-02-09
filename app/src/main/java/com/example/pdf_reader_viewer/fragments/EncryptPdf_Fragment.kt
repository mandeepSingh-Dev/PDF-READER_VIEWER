package com.example.pdf_reader_viewer.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pdf_reader_viewer.R
import com.example.pdf_reader_viewer.UtilClasses.PdfOperations
import com.example.pdf_reader_viewer.databinding.EncryptPdfFragmentBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.tom_roush.pdfbox.pdmodel.PDDocument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.async
import java.lang.Exception


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
        if(it!=null) {

           var pdfName = getMetadata(it)
            binding?.PDFNameEncrypt?.text = pdfName
            var document=PDDocument.load(activity?.contentResolver?.openInputStream(it))
            var totalpages=document.numberOfPages
            binding?.pdfTotalPage?.text="Total pages : " + totalpages.toString()

            Log.d("383hgf", it.toString())
            var uri = it
            var isEncrypted = false
            binding?.encryptButton?.setOnClickListener {
                var text = binding?.edittextlayout11?.editText?.text.toString()
                try {
                    if (!text.isEmpty()) {
                 /**here we check whether pdf is encrypted if yes then delete selected pdf and create new one with password]*/
                      var job= CoroutineScope(Dispatchers.IO).async {
                           isEncrypted = PdfOperations(requireActivity()).createEncryptedPdf( uri, pdfName,text, it)
                     if (isEncrypted) {
                         Snackbar.make(it, "PDF Encrypted", 3500).show()
                         deleteContent(uri)
                     }
                      }

                    } else {
                        binding?.edittextlayout11?.error = "Invalid"
                    }
                } catch (e: Exception) {
                    if (e.message != null) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    })

    fun deleteContent(uri: Uri):Boolean
    {

        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        // Check for the freshest data.
        activity?.contentResolver?.takePersistableUriPermission(uri, takeFlags)
        var bool=DocumentsContract.deleteDocument(activity?.applicationContext?.contentResolver!!, uri)
        Log.d("867r38fh",bool.toString())

        return bool
    }

    @SuppressLint("Range")
    fun getMetadata(uri:Uri):String
    {
        var name:String? = null
        var cursor=activity?.contentResolver?.query(uri,null,null,null,null)

        cursor?.let {
            if(it.moveToFirst())
            {
               name= it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
        return name!!
    }

}