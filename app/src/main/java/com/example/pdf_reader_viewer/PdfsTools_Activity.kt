package com.example.pdf_reader_viewer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.NavHostFragment
import com.example.pdf_reader_viewer.R
import com.example.pdf_reader_viewer.UtilClasses.FragmentNames
import com.example.pdf_reader_viewer.UtilClasses.PDFProp
import com.example.pdf_reader_viewer.fragments.EncryptPdf_Fragment
import com.example.pdf_reader_viewer.fragments.MergePdfs_Fragment
import com.example.pdf_reader_viewer.fragments.SplitPdf_Fragment
import java.nio.BufferUnderflowException

class PdfsTools_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfs_tools)

        supportActionBar?.hide()

        if(intent.getStringExtra(FragmentNames.OPEN_MERGE_FRAGMENT).equals(FragmentNames.OPEN_MERGE_FRAGMENT))
            {
                Log.d("4gu989gbj","merge")

                /*  var navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                       navHostFragment.navController.navigate(R.id.fragment_MergePdfs)*/
              //  navHostFragment.navController.popBackStack()
               //initialize and setting arguments to MergeFragment
                var bundle=createBundleForArguments(intent)
                var mergeFragment=MergePdfs_Fragment()
                mergeFragment.arguments=bundle

                var fragmentTransaction=supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragmentContainerView,mergeFragment)/*.addToBackStack(null).setReorderingAllowed(true)*/.commit()


            }
        else if(intent.getStringExtra(FragmentNames.OPEN_SPLIT_FRAGMENT).equals(FragmentNames.OPEN_SPLIT_FRAGMENT))
        {
            Log.d("4gu989gbj","split")
            /*  var navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                   navHostFragment.navController.navigate(R.id.fragment_MergePdfs)*/
            //  navHostFragment.navController.popBackStack()
            var bundle=createBundleForArguments(intent)
            var splitFragment=SplitPdf_Fragment()
            splitFragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,splitFragment)/*.addToBackStack(null).setReorderingAllowed(true)*/.commit()
        }
        else if(intent.getStringExtra(FragmentNames.OPEN_ENCRYPT_FRAGMENT).equals(FragmentNames.OPEN_ENCRYPT_FRAGMENT))
        {
            Log.d("4gu989gbj","ENCRYPT")
            /*  var navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                   navHostFragment.navController.navigate(R.id.fragment_MergePdfs)*/
            //  navHostFragment.navController.popBackStack()
            var bundle=createBundleForArguments(intent)
            var encryptFragment=EncryptPdf_Fragment()
            encryptFragment.arguments=bundle
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,encryptFragment)/*.addToBackStack(null).setReorderingAllowed(true)*/.commit()
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }
    fun createBundleForArguments(intent: Intent):Bundle {
       var bundle=Bundle()
        bundle.putString(PDFProp.PDF_TITLE,intent?.getStringExtra(PDFProp.PDF_TITLE))
        bundle.putParcelable(PDFProp.PDF_APPENDED_URI,intent?.getParcelableExtra(PDFProp.PDF_APPENDED_URI))
        bundle.putString(PDFProp.PDF_SIZE,intent?.getStringExtra(PDFProp.PDF_SIZE))

        return bundle
    }
}