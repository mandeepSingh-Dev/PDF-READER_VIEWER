package com.example.pdf_reader_viewer.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pdf_reader_viewer.UtilClasses.PdfOperations
import com.example.pdf_reader_viewer.databinding.EncryptPdfFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.tom_roush.pdfbox.pdmodel.PDDocument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.OutputStream
import java.lang.Exception


class EncryptPdf_Fragment : Fragment() {

    var uri:Uri?=null

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

             try{
            var pdfName = getMetadata(it)
            binding?.PDFNameEncrypt?.text = pdfName
            var document = PDDocument.load(activity?.contentResolver?.openInputStream(it))
            var totalpages = document.numberOfPages
            binding?.pdfTotalPage?.text = "Total pages : " + totalpages.toString()

            Log.d("383hgf", it.toString())
            uri = it

            var intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
            intent.type = "application/pdf"
            intent.putExtra(Intent.EXTRA_TITLE, pdfName)

                 var passwordText = binding?.edittextlayout11?.editText?.text.toString()
                       binding?.encryptButton?.setOnClickListener {
                           //only create pdf file if password is not empty
                           if (!passwordText.isEmpty()) {
                           launcher4.launch(intent)
                           } else {
                               binding?.edittextlayout11?.error = "Invalid"
                           }//else block closed
                              }//encrypt button closed

        }catch (e:Exception){
                 binding?.pdfTotalPage?.text = "Cannot Encrypt PDF "
                 binding?.pdfTotalPage?.setTextColor(Color.RED)
                 Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_LONG).show()
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
        cursor?.close()
        return name!!
    }

    //custom Contracts for encrypting pdf document
    var contract = object : ActivityResultContract<Intent, Uri>() {
        override fun createIntent(context: Context, input: Intent?): Intent {
            return input!!
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri {
            var uri = intent?.data
            if (uri != null) {
                return uri
            } else {
                return Uri.EMPTY
            }
        }
    }
    var launcher4 = registerForActivityResult(contract, ActivityResultCallback {

        try {
            var outputStream: OutputStream? = null
            if (it != null) {
                outputStream = activity?.contentResolver?.openOutputStream(it)!!

                Log.d("of66jd", it.path.toString())
                Log.d("34g93jg", outputStream.toString())

                if (outputStream != null) {

                    var isEncrypted = false
                    var passwordText = binding?.edittextlayout11?.editText?.text.toString()
                    try {
                        if (!passwordText.isEmpty()) {
                            /**here we check whether pdf is encrypted if yes then delete selected pdf and create new one with password]*/
                            var job = CoroutineScope(Dispatchers.IO).async {
                                isEncrypted = PdfOperations(requireActivity()).createEncryptedPdf(
                                    uri!!,
                                    passwordText,
                                    outputStream
                                )
                                if (isEncrypted) {
                                    Snackbar.make(
                                        binding?.holderContstraintlayout!!,
                                        "PDF Encrypted",
                                        3500
                                    ).show()
                                    deleteContent(uri!!)
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

                }//if block for output stream null or not
            }
        }catch (e:Exception){}
    })

}