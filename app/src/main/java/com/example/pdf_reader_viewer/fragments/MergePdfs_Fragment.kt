package com.example.pdf_reader_viewer.fragments
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.MediaMetadata
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.net.toFile
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pdf_reader_viewer.RecylerViewClasses.Items_pdfs
import com.example.pdf_reader_viewer.RecylerViewClasses.MyAdapter_ForMerge
import com.example.pdf_reader_viewer.UtilClasses.PDFProp
import com.example.pdf_reader_viewer.UtilClasses.PdfOperations
import com.example.pdf_reader_viewer.UtilClasses.ViewAnimation
import com.example.pdf_reader_viewer.databinding.MergePdfsFragmentBinding
import org.bouncycastle.asn1.DERTaggedObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList

class MergePdfs_Fragment : Fragment() {

    var binding: MergePdfsFragmentBinding? = null
    var isRotate = true
    var pdfUrifromFileManager:Uri?=null
    var selectedPdf_list:ArrayList<Items_pdfs>?=null
    var adapter:MyAdapter_ForMerge?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedPdf_list= ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = MergePdfsFragmentBinding.inflate(inflater)
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        var intent=Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
//        launcher.launch(intent)

        //whenever we click menu button of specific pdf then here uri will come
        if(arguments!=null)
        {
            var pdfTitle=arguments?.getString(PDFProp.PDF_TITLE)
            var pdfSize=arguments?.getString(PDFProp.PDF_SIZE)
            var pdfUri=arguments?.getParcelable<Uri>(PDFProp.PDF_APPENDED_URI)
            //var pdfdate=arguments?.getString(PDFProp.PDF_DATEMODIFIED)

           // Log.d("efhhegfehd",pdfUri.toString())

            if( pdfTitle!=null && pdfSize!=null && pdfUri!=null )
          //  Toast.makeText(requireContext(),arguments?.getString(PDFProp.PDF_TITLE)+"dgd",Toast.LENGTH_LONG).show()
            {
                selectedPdf_list?.add(Items_pdfs(pdfTitle!!, pdfSize!!, pdfUri!!))
            }
            createrecyclerView()
        }
        binding?.mergeButton?.setOnClickListener {
           // Log.d("34ge",selectedPdf_list?.get(0)?.appendeduri.toString())
            mergeSelectedPdfs(selectedPdf_list!!)
        }


        Log.d("vdvdvegvd","dfdsfdfvd")
        ViewAnimation.init(binding?.fab2Linearlayout!!)
        ViewAnimation.init(binding?.fab3Linearlayout!!)
        ViewAnimation.init(binding?.fab4Linearlayout!!)

         binding?.topFab?.setOnClickListener {
            animate_fab_buttons()
        }

        binding?.fab4?.setOnClickListener {
            //getting pdf document from device \
            var intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("application/pdf")
            launcher.launch(intent)
           // Toast.makeText(requireContext(),pdfUrifromFileManager?.toString(),Toast.LENGTH_LONG).show()

        }



    }
    fun animate_fab_buttons(){
        binding?.topFab?.animate()?.rotationBy(180f); //to rotate main fab 180 degree
        // to show upper fab buttons
        if(isRotate) {
            ViewAnimation.showIn(binding?.fab2Linearlayout!!)
            ViewAnimation.showIn(binding?.fab3Linearlayout!!)
            ViewAnimation.showIn(binding?.fab4Linearlayout!!)
            isRotate=false
        }//to hide upper fab button
        else{
            Log.d("38thg",isRotate.toString())
            ViewAnimation.showOut(binding?.fab2Linearlayout!!)
            ViewAnimation.showOut(binding?.fab3Linearlayout!!)
            ViewAnimation.showOut(binding?.fab4Linearlayout!!)
            isRotate=true
        }
    }

    //getting uri from another intent Activity means from flemanager
    var activityResultContracts=object:ActivityResultContract<Intent, Uri>(){
        override fun createIntent(context: Context, input: Intent?): Intent {
            return input!!
        }
        override fun parseResult(resultCode: Int, intent: Intent?): Uri {
          var uri=intent?.data
            if(uri!=null)
            {
                pdfUrifromFileManager=uri
            }
            else{
                pdfUrifromFileManager=Uri.parse("null uri")
            }
            return pdfUrifromFileManager!!
        }
    }
    var launcher=registerForActivityResult(activityResultContracts,object:ActivityResultCallback<Uri>{
        override fun onActivityResult(result: Uri?)
        {
            Log.d("3tfr3wgfv","Ss")
            if(result.toString().equals("null uri")){
                //do nothing bcoz uri is null
            }
            else {
                Log.d("3ty378hfg", result.toString()+"yhuigjr")



                Log.d("585gydjk",activity?.contentResolver?.openInputStream(result!!).toString()+"sfsdf")
                var list=getMetadata_fromUri(pdfUrifromFileManager!!)

                selectedPdf_list?.add(Items_pdfs(list.get(0), list.get(1),pdfUrifromFileManager))
                //   Log.d("3f3f3w",selectedPdf_list?.size.toString())
                createrecyclerView()
               // Log.d("3scsdf56565",getMetadata_fromUri(pdfUrifromFileManager!!)+"fsdfdfd")
              //  PdfOperations(activity!!).splittingPdf(requireActivity(),pdfUrifromFileManager!!)

            }

           // Toast.makeText(requireContext(),pdfUrifromFileManager?.toString(),Toast.LENGTH_LONG).show()

        }
    })

    //creating adapter and set to recyclerView
    fun createrecyclerView(){
        adapter= MyAdapter_ForMerge(requireContext(),selectedPdf_list!!)
        binding?.mergePdfListRecyclerView?.layoutManager=LinearLayoutManager(requireContext())
        binding?.mergePdfListRecyclerView?.adapter=adapter

    }

    fun mergeSelectedPdfs(pdflist:ArrayList<Items_pdfs>){
       // PdfOperations().mergePdfs(requireActivity(),pdflist,"mymergesFinal")


    }

    @SuppressLint("Range")
    fun getMetadata_fromUri(uri:Uri):ArrayList<String>{

         var arraylist:ArrayList<String> = ArrayList()
         var resolver=requireContext().contentResolver

         var cursor=resolver.query(uri,null,null,null,null)
         if(cursor?.moveToFirst()!!) {
            var displayName=cursor?.getString(cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))!!
            var size=cursor?.getString(cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE))!!
              arraylist.add(displayName)
              arraylist.add(size)
         }
      //  var displayName=metadata.extractMetadata(MediaMetadata.METADATA_KEY_DISPLAY_TITLE.toInt())

      //  metadata.release()
    return arraylist!!
    }
}