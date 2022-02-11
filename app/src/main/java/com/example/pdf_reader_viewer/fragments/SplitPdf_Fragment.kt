package com.example.pdf_reader_viewer.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pdf_reader_viewer.R
import com.example.pdf_reader_viewer.UtilClasses.ConversionandUtilsClass
import com.example.pdf_reader_viewer.UtilClasses.PDFProp
import com.example.pdf_reader_viewer.UtilClasses.PdfOperations
import com.example.pdf_reader_viewer.databinding.SplitpdfFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tom_roush.pdfbox.pdmodel.PDDocument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class SplitPdf_Fragment : Fragment() {

    var binding: SplitpdfFragmentBinding? = null
    var startPage: String? = null
    var endPage: String? = null
    var alertDialogprogress:androidx.appcompat.app.AlertDialog?=null
    var importingDailogTextview:TextView?=null
    var importingnumberDailogText:TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = SplitpdfFragmentBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // activity?.actionBar?.hide()
        Log.d("sugfduysfb", "started Splitfragment")

        // launcher.launch("application/pdf")

        if (arguments != null) {
            var pdfTitle = arguments?.getString(PDFProp.PDF_TITLE)
            var pdfSize = arguments?.getString(PDFProp.PDF_SIZE)
            var pdfUri = arguments?.getParcelable<Uri>(PDFProp.PDF_APPENDED_URI)
            //var pdfdate=arguments?.getString(PDFProp.PDF_DATEMODIFIED)

            if (pdfTitle != null && pdfSize != null && pdfUri != null)
            //  Toast.makeText(requireContext(),arguments?.getString(PDFProp.PDF_TITLE)+"dgd",Toast.LENGTH_LONG).show()
            {

                binding?.PDFNameSplit?.text = pdfTitle
                CoroutineScope(Dispatchers.Main).launch {
                    splitPDF(pdfUri)

                }
                //selectedPdf_list?.add(Items_pdfs(pdfTitle!!, pdfSize!!, pdfUri!!))
            }
            // createrecyclerView()


        }
        binding?.addpDFImageView?.setOnClickListener {
            launcher.launch("application/pdf")
        }
        var viewgroup = activity?.findViewById<ViewGroup>(R.id.content)
        var view22 = LayoutInflater.from(requireContext()).inflate(R.layout.custom_progress_dialogue, viewgroup , false)
        alertDialogprogress=createAlertdialogue(view22)

    }

    fun splitPDF(appendedUri: Uri) {
        var totalPages = 0
        CoroutineScope(Dispatchers.Default).launch {
            var inputStream = ConversionandUtilsClass.convertContentUri_toInputStream(
                requireActivity(),
                appendedUri
            )
            Log.d("fhdjfdn", inputStream.toString())
            //practise only
            //   var inptstrm = activity?.contentResolver?.openInputStream(appendedUri)
            try {
                var document = PDDocument.load(inputStream)

                totalPages = document?.numberOfPages!!
                Log.d("8y7ehfgbne", totalPages.toString())
                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), totalPages.toString(), Toast.LENGTH_LONG)
                        .show()
                    Log.d("377t738ghvn", totalPages.toString())

                }
            } catch (e: Exception) {
                Log.d("377t738ghvn", e.cause.toString())
            }

            withContext(Dispatchers.Main) {
                binding?.pdfTotalPage?.setText("Total pages : " + totalPages.toString())
                binding?.splitButton?.setOnClickListener {
                    Log.d("8y7ehfgbne", "dfsdfsf")
                    var atPage = binding?.edittextlayout1?.editText?.text.toString()
                    var pdfName = binding?.edittextlayout2?.editText?.text?.toString()
                    // var listnumber = PdfOperations(requireActivity()).formattingof_Pagenumber(atPage)
                    var listnumber = ConversionandUtilsClass.formattingof_Pagenumber(atPage)

                    Log.d("SIZEWHAT", listnumber.size.toString())

                    if (listnumber.size == 0) {
                        Log.d("hfdf", "dkfjdk")
                        binding?.edittextlayout1?.error = "Invalid"
                    } else {
                        if (!(pdfName?.isEmpty()!!)) {
                            Log.d("hfdf", "not dkfjdk")
                            binding?.edittextlayout2?.error = ""
                            binding?.edittextlayout2?.isErrorEnabled = false
                            binding?.edittextlayout1?.isErrorEnabled = false

                            CoroutineScope(Dispatchers.Main).launch {
                                    importingDailogTextview = alertDialogprogress?.findViewById<TextView>(R.id.importingtextview)
                                    importingnumberDailogText = alertDialogprogress?.findViewById<TextView>(R.id.importedNumberTextview)
                                    //these dailog textview had importing and imprting number texts
                                    importingDailogTextview?.text = "please wait..."
                                    importingnumberDailogText?.visibility = View.GONE
                                    alertDialogprogress?.show()


                                PdfOperations(requireActivity()).splittingPdf(appendedUri, listnumber, pdfName)

                                  alertDialogprogress?.hide()
                            }
                        } else {
                            binding?.edittextlayout2?.error = "Invalid"
                        }
                    }


                }
            }//withContext closed
        } //coroutine scope closed
    } //split fnction closed


    var launcher = registerForActivityResult(object : ActivityResultContracts.GetContent() {

    }, ActivityResultCallback {
//       Log.d("34y3478fg4",it.toString())
        // CoroutineScope(Dispatchers.Main).launch {
        if (it != null) {

            splitPDF(it)

        }
        // }

        /* var cursor= activity?.contentResolver?.query(it,null,null,null,null)
         while(cursor?.moveToNext()!!)
         {
             var displayName=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))
            // Log.d("4fj3i3knv",displayName+"fekf")
             binding?.PDFNameSplit?.text=displayName
         }*/
    })
    fun createAlertdialogue(view:View):androidx.appcompat.app.AlertDialog{
        var alertbuilder2 = MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
        alertbuilder2.setView(view)
        alertbuilder2.setCancelable(false)

        return alertbuilder2.create()
    }
}


