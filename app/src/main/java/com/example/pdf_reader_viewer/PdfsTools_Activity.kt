package com.example.pdf_reader_viewer

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.pdf_reader_viewer.UtilClasses.FragmentNames
import com.example.pdf_reader_viewer.UtilClasses.PDFProp
import com.example.pdf_reader_viewer.databinding.ActivityPdfsToolsBinding
import com.example.pdf_reader_viewer.fragments.*

class PdfsTools_Activity : AppCompatActivity() {

    var binding:ActivityPdfsToolsBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfsToolsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)

        supportActionBar?.hide()

        var anim = AnimationUtils.loadAnimation(this,R.anim.open_animation)
        binding?.parentPDFTOOLactivity?.startAnimation(anim)

        //opens pdftools_fragment without pdftitle and other details
        if(intent.getStringExtra(FragmentNames.OPEN_MERGE_FRAGMENT).equals(FragmentNames.OPEN_MERGE_FRAGMENT))
        {
                Log.d("4gu989gbj","merge")
               //initialize and setting arguments to MergeFragment
                var bundle=createBundleForToolsArguments(intent)
                var mergeFragment=MergePdfs_Fragment()
                mergeFragment.arguments=bundle

                var fragmentTransaction=supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragmentContainerView,mergeFragment)/*.addToBackStack(null).setReorderingAllowed(true)*/.commit()


            }
        //opens pdftools_fragment without pdftitle and other details
        else if(intent.getStringExtra(FragmentNames.OPEN_SPLIT_FRAGMENT).equals(FragmentNames.OPEN_SPLIT_FRAGMENT))
        {
            Log.d("4gu989gbj","split")
            /*  var navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                   navHostFragment.navController.navigate(R.id.fragment_MergePdfs)*/
            //  navHostFragment.navController.popBackStack()
            var bundle=createBundleForToolsArguments(intent)
            var splitFragment=SplitPdf_Fragment()
            splitFragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,splitFragment)/*.addToBackStack(null).setReorderingAllowed(true)*/.commit()
        }
        //opens pdftools_fragment without pdftitle and other details
        else if(intent.getStringExtra(FragmentNames.OPEN_ENCRYPT_FRAGMENT).equals(FragmentNames.OPEN_ENCRYPT_FRAGMENT))
        {
            Log.d("4gu989gbj","ENCRYPT")
            /*  var navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                   navHostFragment.navController.navigate(R.id.fragment_MergePdfs)*/
            //  navHostFragment.navController.popBackStack()
            var bundle=createBundleForToolsArguments(intent)
            var encryptFragment=EncryptPdf_Fragment()
            encryptFragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,encryptFragment)/*.addToBackStack(null).setReorderingAllowed(true)*/.commit()
        }
        //opens pdftools_fragment without pdftitle and other details
        else if(intent.getStringExtra(FragmentNames.OPEN_IMGTOPDF_FRAGMENT).equals(FragmentNames.OPEN_IMGTOPDF_FRAGMENT))
        {
            var imagtopdf_fragment=ImageTo_Pdf_Fragment()
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,imagtopdf_fragment).commit()
        }

        //opens folder_pdflist_fragment without pdftitle and other details
        else if(intent.getStringExtra(PDFProp.MERGEPDF_FOLDER).equals(PDFProp.MERGEPDF_FOLDER))
        {
            Toast.makeText(this,"merge folder",Toast.LENGTH_SHORT).show()
            var folderPdflistFragment=Folder_PdfList_Fragment()
            var bundlee = Bundle()
            bundlee.putString(PDFProp.FOLDER_KEY,PDFProp.MERGEPDF_FOLDER)

            folderPdflistFragment.arguments = bundlee
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,folderPdflistFragment).commit()
        }
        //opens folder_pdflist_fragment without pdftitle and other details
        else if(intent.getStringExtra(PDFProp.SPLITPDF_FOLDER).equals(PDFProp.SPLITPDF_FOLDER))
        {
            Toast.makeText(this,"split folder",Toast.LENGTH_SHORT).show()
            var folderPdflistFragment=Folder_PdfList_Fragment()
            var bundle = Bundle()
            bundle.putString(PDFProp.FOLDER_KEY,PDFProp.SPLITPDF_FOLDER)
            folderPdflistFragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,folderPdflistFragment).commit()
        }
        //opens folder_pdflist_fragment without pdftitle and other details
        else if(intent.getStringExtra(PDFProp.CREATEDPDF_FOLDER).equals(PDFProp.CREATEDPDF_FOLDER))
        {
            Toast.makeText(this,"split folder",Toast.LENGTH_SHORT).show()
            var folderPdflistFragment=Folder_PdfList_Fragment()
            var bundle = Bundle()
            bundle.putString(PDFProp.FOLDER_KEY,PDFProp.CREATEDPDF_FOLDER)
            folderPdflistFragment.arguments = bundle

            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,folderPdflistFragment).commit()
        }
        //opens folder_pdflist_fragment without pdftitle and other details
        else if(intent.getStringExtra(PDFProp.ENCRYPTEDPDF_FOLDER).equals(PDFProp.ENCRYPTEDPDF_FOLDER))
        {
            Toast.makeText(this,"split folder",Toast.LENGTH_SHORT).show()
            var folderPdflistFragment=Folder_PdfList_Fragment()
            var bundle = Bundle()
            bundle.putString(PDFProp.FOLDER_KEY,PDFProp.ENCRYPTEDPDF_FOLDER)
            folderPdflistFragment.arguments = bundle

            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,folderPdflistFragment).commit()
        }



    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }
    fun createBundleForToolsArguments(intent: Intent):Bundle {
       var bundle=Bundle()
        bundle.putString(PDFProp.PDF_TITLE,intent?.getStringExtra(PDFProp.PDF_TITLE))
        bundle.putParcelable(PDFProp.PDF_APPENDED_URI,intent?.getParcelableExtra(PDFProp.PDF_APPENDED_URI))
        bundle.putString(PDFProp.PDF_SIZE,intent?.getStringExtra(PDFProp.PDF_SIZE))

        return bundle
    }



}