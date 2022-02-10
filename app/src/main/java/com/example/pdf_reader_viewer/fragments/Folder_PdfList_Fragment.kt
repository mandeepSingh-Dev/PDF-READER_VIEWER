package com.example.pdf_reader_viewer.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pdf_reader_viewer.MCustomOnClickListener
import com.example.pdf_reader_viewer.PdfView_Activity
import com.example.pdf_reader_viewer.PdfsTools_Activity
import com.example.pdf_reader_viewer.R
import com.example.pdf_reader_viewer.RecylerViewClasses.Items_pdfs
import com.example.pdf_reader_viewer.RecylerViewClasses.MyAdapter
import com.example.pdf_reader_viewer.Roomclasses.Room_For_BOOKMARKS.Items_Bookmarks
import com.example.pdf_reader_viewer.Roomclasses.Room_For_BOOKMARKS.MyRoomDatabase2
import com.example.pdf_reader_viewer.UtilClasses.*
import com.example.pdf_reader_viewer.databinding.FolderPdfListFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class Folder_PdfList_Fragment : Fragment() {

    var binding:FolderPdfListFragmentBinding?=null
    var myAdpater:MyAdapter?=null

    var list:ArrayList<Items_pdfs>?=null
    var mfactory:MyViewmodel_InstanceFactory_folderlist?=null
    var myviewmodel:MyViewModel_for_foldderpdflist?=null
    var pdflist:ArrayList<Items_pdfs>?=null

    var bottomSheetDialog: BottomSheetDialog?=null
    var bottomSheetView:View?=null

    lateinit var pdfName1_bottomsheet: TextView
    lateinit var openLinearLayout: LinearLayout
    lateinit var mergeLinearLayout: LinearLayout
    lateinit var splitLinearLayout: LinearLayout
    lateinit var deleteLinearLayout: LinearLayout
    lateinit var renameeLinearLayout: LinearLayout
    lateinit var detailsLinearLayout: LinearLayout
    lateinit var bookmarkIcon_bottomsheet: ImageView
    lateinit var removebookmark_bottomsheet: ImageView
    lateinit var shareIcon_bottomsheet: ImageView

    var intent:Intent?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pdflist= ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
         binding = FolderPdfListFragmentBinding.inflate(inflater)


        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        intent=Intent(Intent.ACTION_SEND)
        intent?.type="application/pdf"

//        if(!!arguments?.isEmpty!!)
//        {
           //var folderName = arguments?.getString(PDFProp.FOLDER_KEY)

        mfactory = MyViewmodel_InstanceFactory_folderlist(PDFProp.ENCRYPTEDPDF_FOLDER!!,requireContext())
        myviewmodel = ViewModelProvider(requireActivity().viewModelStore,mfactory!!).get(MyViewModel_for_foldderpdflist::class.java)

        myviewmodel?.getPdflIst()?.observe(viewLifecycleOwner,object:Observer<ArrayList<Items_pdfs>>{
            override fun onChanged(it: ArrayList<Items_pdfs>?) {
                Log.d("38hgf3",it?.size.toString())
                 myAdpater= MyAdapter(requireContext(),it!!)
                binding?.folderPdfRecyclerView?.layoutManager=LinearLayoutManager(requireContext())
                binding?.folderPdfRecyclerView?.adapter=myAdpater

                myAdpater?.setMCustomClickListenr(object:MCustomOnClickListener{
                    override fun onClick(position: Int) {
                        Log.d("3iegnv3me,wv",position.toString())
                        pdfName1_bottomsheet?.setText(it?.get(position).title)
                        bottomSheetDialog?.show()
                        Log.d("hfe","NEW INTERFACE IS NOW READY")
                        clickOnbottomSheetViews(it!!,position,myAdpater!!)

                    }
                })

            }
        })
       //}
    }

    override fun onStart() {
        super.onStart()
        var viewGroup = activity?.findViewById<ViewGroup>(R.id.content)
        bottomSheetView= LayoutInflater.from(requireContext()).inflate(R.layout.bottomsheet_dialogue,viewGroup)
        //creating BottomSheetDialogue instance
        bottomSheetDialog=BottomSheetDialog(requireContext(), R.style.ThemeOverlay_MaterialComponents_BottomSheetDialog)

        //then set bottomsheetView to bottomsheetDialogue instance
        bottomSheetDialog?.setContentView(bottomSheetView!!)

        // function for initializing bottomsheetViews
        initializeBottomsheetView()
    }

    fun initializeBottomsheetView(){
        pdfName1_bottomsheet = bottomSheetDialog?.findViewById<TextView>(R.id.pdfName1_bottomsheet)!!
        openLinearLayout = bottomSheetDialog?.findViewById<LinearLayout>(R.id.openLinearLayout)!!
        mergeLinearLayout = bottomSheetDialog?.findViewById<LinearLayout>(R.id.mergeLinearLayout)!!
        splitLinearLayout= bottomSheetDialog?.findViewById<LinearLayout>(R.id.splitLinearLayout)!!
        deleteLinearLayout = bottomSheetDialog?.findViewById<LinearLayout>(R.id.deleteLinearLayout)!!
        renameeLinearLayout = bottomSheetDialog?.findViewById<LinearLayout>(R.id.renameeLinearLayout)!!
        detailsLinearLayout = bottomSheetDialog?.findViewById<LinearLayout>(R.id.detailsLinearLayout)!!
        bookmarkIcon_bottomsheet = bottomSheetDialog?.findViewById(R.id.bookmarkIcon_bottomsheet)!!
        removebookmark_bottomsheet = bottomSheetDialog?.findViewById(R.id.removebookmarkIcon_bottomsheet)!!
        shareIcon_bottomsheet = bottomSheetDialog?.findViewById(R.id.share_bottomsheet)!!

    }
    fun clickOnbottomSheetViews(pdflist:ArrayList<Items_pdfs>,position:Int,myAdapter: MyAdapter){
        //this will send user to PdfTools_Activity----> Merge Fragment with pdfuri and other data acc to position
        mergeLinearLayout?.setOnClickListener {

            var intent= Intent(context, PdfsTools_Activity::class.java)

            intent.putExtra(FragmentNames.OPEN_MERGE_FRAGMENT, FragmentNames.OPEN_MERGE_FRAGMENT)
                .putExtra(PDFProp.PDF_TITLE,pdflist?.get(position)?.title)
                .putExtra(PDFProp.PDF_APPENDED_URI,pdflist?.get(position)?.appendeduri)
                .putExtra(PDFProp.PDF_SIZE,pdflist?.get(position)?.size)
            startActivity(intent)

            Log.d("3igwn3bg","mskmsk")
            bottomSheetDialog?.hide()
        }
        //this will send user to PdfTools_Activity----> Split Fragment with pdfuri and other data acc to position
        splitLinearLayout?.setOnClickListener {

            var intent= Intent(context, PdfsTools_Activity::class.java)

            intent.putExtra(FragmentNames.OPEN_SPLIT_FRAGMENT, FragmentNames.OPEN_SPLIT_FRAGMENT)
                .putExtra(PDFProp.PDF_TITLE,pdflist?.get(position)?.title)
                .putExtra(PDFProp.PDF_APPENDED_URI,pdflist?.get(position)?.appendeduri)
                .putExtra(PDFProp.PDF_SIZE,pdflist?.get(position)?.size)

            startActivity(intent)
            Log.d("3igwn3bg","mskmsk")
            bottomSheetDialog?.hide()
        }
        //this will send user to PdfViewActivity with pdfuri AND pdftitle acc to position
        openLinearLayout?.setOnClickListener {

            var intent= Intent(context, PdfView_Activity::class.java)

            intent.putExtra(PDFProp.PDF_APPENDED_URI,pdflist?.get(position)?.appendeduri.toString())
                .putExtra(PDFProp.PDF_TITLE,pdflist?.get(position)?.title)

            startActivity(intent)
            Log.d("3igwn3bg","mskmsk")
            bottomSheetDialog?.hide()
        }
        //this will show details on dialogue
        detailsLinearLayout?.setOnClickListener {
            showDialoguewithDetails(pdflist,position)
        }
        //this will delete pdf from list
        deleteLinearLayout?.setOnClickListener {
            try {
                Log.d("in3g3", position.toString())
                var intt = context?.contentResolver?.delete(pdflist?.get(position)?.appendeduri!!, null, null)
                bottomSheetDialog?.hide()
                pdflist?.remove(pdflist?.get(position))
                myAdapter.notifyItemRemoved(position)
                myAdapter.notifyItemRangeChanged(position, pdflist?.size!!)
            }catch (e: Exception){
                Toast.makeText(requireContext(),e.message, Toast.LENGTH_SHORT).show()}
        }


        //to bookmark the pdf into database
        bookmarkIcon_bottomsheet?.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                addBookmarks(pdflist.get(position).appendeduri!!)
                Log.d("38f3gh7fg3h","bookmark")
                bookmarkIcon_bottomsheet.visibility=View.GONE

                removebookmark_bottomsheet.visibility=View.VISIBLE
            }
        }
        //to undo or remove the bookmarked pdf from database
        removebookmark_bottomsheet?.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                removeBookmarks(pdflist.get(position).appendeduri!!)
                Log.d("38f3gh7fg3h","Removed bookmark")
                //  removebookmark_bottomsheet.isEnabled=false
                removebookmark_bottomsheet.visibility=View.GONE

                bookmarkIcon_bottomsheet.visibility=View.VISIBLE

            }
        }
        //to share pdf to another apps
        shareIcon_bottomsheet?.setOnClickListener {
            intent?.putExtra(Intent.EXTRA_STREAM,pdflist.get(position).appendeduri)

            intent?.putExtra(Intent.EXTRA_SUBJECT, "Sharing File from My Pdf App.");
            // intent?.putExtra(Intent.EXTRA_TEXT, "Sharing File from Webkul to purchase items...");
            startActivity(Intent.createChooser(intent,"my PDF FILE"))

        }

        //to check if selected pdf is in bookmark database or not for bookmark buttons
        CoroutineScope(Dispatchers.IO).launch {
            var itemsPdf = MyRoomDatabase2.getInstance(requireContext()).daoMethods().query(pdflist.get(position).appendeduri.toString())

            withContext(Dispatchers.Main) {
                if (itemsPdf != null) {
                    removebookmark_bottomsheet.visibility = View.VISIBLE
                    bookmarkIcon_bottomsheet.visibility = View.GONE
                } else {
                    bookmarkIcon_bottomsheet.visibility = View.VISIBLE
                    removebookmark_bottomsheet.visibility = View.GONE
                }
            }
        }
    }
    suspend fun addBookmarks(uri: Uri) = withContext(Dispatchers.IO) {
        MyRoomDatabase2.getInstance(requireContext()).daoMethods().insert(Items_Bookmarks(uri.toString()))
    }
    suspend fun removeBookmarks(uri:Uri) = withContext(Dispatchers.IO)
    {
        var itemsBookmarks:Items_Bookmarks?=null
        var liveUri = MyRoomDatabase2.getInstance(requireContext()).daoMethods().query(uri.toString())

        if(liveUri!=null) {
            deleteITEM(liveUri)
        }
    }
    fun showDialoguewithDetails(pdflist:ArrayList<Items_pdfs>,position:Int){

        var materialBuilder= MaterialAlertDialogBuilder(requireContext(),R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
        var viewgroup=activity?.findViewById<ViewGroup>(R.id.content)
        var view = LayoutInflater.from(requireContext()).inflate(R.layout.custom_details_dialgue,viewgroup)
        materialBuilder.setView(view)
        val namedialogue=view.findViewById<TextView>(R.id.nameTextviewDialogue)
        val datedialogue=view.findViewById<TextView>(R.id.datetextviewDailgoue)
        val sizedialogue=view.findViewById<TextView>(R.id.sizeTextviewDialogue)
        // val pathdialogue=view.findViewById<TextView>(R.id.pathtextviewDailgoue)

        namedialogue.text=pdflist?.get(position)?.title
        datedialogue.text= ConversionandUtilsClass.convertToDate(pdflist?.get(position)?.date_modified?.toLong()!!).get(1)  //get date with time also
        sizedialogue.text= ConversionandUtilsClass.bytesToMB(pdflist?.get(position)?.size)+" mb"
        /*if(pdflist.get(position).relativePath!=null) {
            pathdialogue.text = pdflist?.get(position)?.relativePath
        }*/

        var dialoguee=materialBuilder.create()
        dialoguee.show()
    }
    fun deleteITEM(itemsBookmarks: Items_Bookmarks) {
        CoroutineScope(Dispatchers.Default).launch {
            MyRoomDatabase2.getInstance(requireContext()).daoMethods().delete(itemsBookmarks)
        }
    }
}