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
import com.example.pdf_reader_viewer.UtilClasses.FragmentNames
import com.example.pdf_reader_viewer.UtilClasses.PDFProp
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
        binding?.MErgerlinearlay?.setOnClickListener {
            var intent= Intent(activity,PdfsTools_Activity::class.java)
            intent.putExtra(FragmentNames.OPEN_MERGE_FRAGMENT, FragmentNames.OPEN_MERGE_FRAGMENT)
            startActivity(intent)
        }
        binding?.splitlinearlay?.setOnClickListener {
            var intent= Intent(activity,PdfsTools_Activity::class.java)
            intent.putExtra(FragmentNames.OPEN_SPLIT_FRAGMENT, FragmentNames.OPEN_SPLIT_FRAGMENT)
            startActivity(intent)
        }
        binding?.encryptlinearlay?.setOnClickListener {
            var intent= Intent(activity,PdfsTools_Activity::class.java)
            intent.putExtra(FragmentNames.OPEN_ENCRYPT_FRAGMENT, FragmentNames.OPEN_ENCRYPT_FRAGMENT)
            startActivity(intent)
        }
        binding?.imagetoPdflinearlay?.setOnClickListener {
            var intent= Intent(activity,PdfsTools_Activity::class.java)
            intent.putExtra(FragmentNames.OPEN_IMGTOPDF_FRAGMENT, FragmentNames.OPEN_IMGTOPDF_FRAGMENT)
            startActivity(intent)
        }
      /*  //click on merge folder to pass merge folder name to folder fragment
       binding?.mergefolderLayout?.setOnClickListener {
          startActivity(Intent(activity,PdfsTools_Activity::class.java).putExtra(PDFProp.MERGEPDF_FOLDER,PDFProp.MERGEPDF_FOLDER))
            }
        //click on split folder to pass split folder name to folder fragment
        binding?.splitFolderlayout?.setOnClickListener {
            startActivity(Intent(activity,PdfsTools_Activity::class.java).putExtra(PDFProp.SPLITPDF_FOLDER,PDFProp.SPLITPDF_FOLDER))

        }
        //click on encrypt folder to pass encrypt folder name to folder fragment
        binding?.encryptFolderlayout?.setOnClickListener {
            startActivity(Intent(activity,PdfsTools_Activity::class.java).putExtra(PDFProp.ENCRYPTEDPDF_FOLDER,PDFProp.ENCRYPTEDPDF_FOLDER))
        }
        //click on imageToPDF folder to pass created folder name to folder fragment
        binding?.imgtoPDFFolderlayout?.setOnClickListener {
            startActivity(Intent(activity,PdfsTools_Activity::class.java).putExtra(PDFProp.CREATEDPDF_FOLDER,PDFProp.CREATEDPDF_FOLDER))
        }
*/
    }
}