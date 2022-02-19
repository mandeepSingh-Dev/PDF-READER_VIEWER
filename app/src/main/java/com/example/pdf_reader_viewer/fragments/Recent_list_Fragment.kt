package com.example.pdf_reader_viewer.fragments

import android.app.AlertDialog
import android.content.ClipData
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pdf_reader_viewer.*
import com.example.pdf_reader_viewer.RecylerViewClasses.Items_pdfs
import com.example.pdf_reader_viewer.RecylerViewClasses.MyAdapter
import com.example.pdf_reader_viewer.RecylerViewClasses.MyAdapter_RecentLists
import com.example.pdf_reader_viewer.Roomclasses.Room_For_BOOKMARKS.Items_Bookmarks
import com.example.pdf_reader_viewer.Roomclasses.Room_For_BOOKMARKS.MyRoomDatabase2
import com.example.pdf_reader_viewer.Roomclasses.Room_For_RecentPDFs.Items_RecentPdfs
import com.example.pdf_reader_viewer.Roomclasses.Room_For_RecentPDFs.MyRoomDatabase
import com.example.pdf_reader_viewer.UtilClasses.ConversionandUtilsClass
import com.example.pdf_reader_viewer.UtilClasses.FragmentNames
import com.example.pdf_reader_viewer.UtilClasses.PDFProp
import com.example.pdf_reader_viewer.databinding.RecentListpdfFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.lang.Exception


class Recent_list_Fragment : Fragment() {

    var binding:RecentListpdfFragmentBinding?=null
    var recentdbList:LiveData<List<Items_RecentPdfs>>?=null
    var myAdapter:MyAdapter_RecentLists?=null

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
    lateinit var renameDialog :AlertDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         recentdbList= MutableLiveData()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= RecentListpdfFragmentBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        intent=Intent(Intent.ACTION_SEND)
        intent?.type="application/pdf"

         CoroutineScope(Dispatchers.IO).async {
             recentdbList = MyRoomDatabase.getInstance(requireContext())?.daoMethod()?.query()

           withContext(Dispatchers.Main) {
               recentdbList?.observe(viewLifecycleOwner, object : Observer<List<Items_RecentPdfs>>
               {
                       override fun onChanged(pdflist: List<Items_RecentPdfs>?) {
                           if (pdflist?.isEmpty()!!) {
                               Log.d("3tubuenfe", "3ufufbkscsdc")
                               binding?.emptyView?.visibility = View.VISIBLE
                               binding?.emptyText?.visibility = View.VISIBLE

                               binding?.recentProgress?.visibility = View.GONE
                           }
                           else {

                           Log.d("eifhefg", pdflist?.size.toString())
                        var pdflisst = pdflist as ArrayList<Items_RecentPdfs>
                               //sorting list according recent opened pdf date
                               pdflisst.sortByDescending {
                                   it.date?.toInt()
                               }
                           myAdapter = MyAdapter_RecentLists(requireContext(), pdflisst)
                           binding?.recentRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
                           binding?.recentRecyclerView?.adapter = myAdapter

                           binding?.recentProgress?.visibility = View.GONE
                               binding?.emptyView?.visibility = View.GONE
                               binding?.emptyText?.visibility = View.GONE

                           myAdapter?.setMCustomClickListenr(object : MCustomOnClickListener {
                               override fun onClick(position: Int) {
                                   Log.d("3iegnv3me,wv", position.toString())
                                   pdfName1_bottomsheet?.setText(pdflist.get(position).pdfName)
                                   bottomSheetDialog?.show()
                                   Log.d("hfe", "NEW INTERFACE IS NOW READY")
                                   clickOnbottomSheetViews(pdflist, position, myAdapter!!)
                               }
                           })// adapter click listener closed
                       }//else block
                       }
                   })
           }
       }
    }//onViewCreated
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

    fun clickOnbottomSheetViews(pdflist:ArrayList<Items_RecentPdfs>, position:Int, myAdapter: MyAdapter_RecentLists){
        //this will send user to PdfTools_Activity----> Merge Fragment with pdfuri and other data acc to position

        var pdfUri = pdflist.get(position).pdfUri!!
        Log.d("38gh8",pdfUri.toString())

        var pdfSize = pdflist.get(position).pdfSize!!
        var date = pdflist.get(position).date!!
        var pdfName = pdflist.get(position).pdfName!!

        mergeLinearLayout?.setOnClickListener {

            var intent= Intent(context, PdfsTools_Activity::class.java)

            intent.putExtra(FragmentNames.OPEN_MERGE_FRAGMENT, FragmentNames.OPEN_MERGE_FRAGMENT)
                .putExtra(PDFProp.PDF_TITLE,pdflist?.get(position)?.pdfName)
                .putExtra(PDFProp.PDF_APPENDED_URI,Uri.parse(pdfUri))
                .putExtra(PDFProp.PDF_SIZE,pdflist?.get(position)?.pdfSize)
            startActivity(intent)

            Log.d("3igwn3bg","mskmsk")
            bottomSheetDialog?.hide()
        }
        //this will send user to PdfTools_Activity----> Split Fragment with pdfuri and other data acc to position
        splitLinearLayout?.setOnClickListener {

            var intent= Intent(context, PdfsTools_Activity::class.java)

            intent.putExtra(FragmentNames.OPEN_SPLIT_FRAGMENT, FragmentNames.OPEN_SPLIT_FRAGMENT)
                .putExtra(PDFProp.PDF_TITLE,pdflist?.get(position)?.pdfName)
                .putExtra(PDFProp.PDF_APPENDED_URI,Uri.parse(pdfUri))
                .putExtra(PDFProp.PDF_SIZE,pdflist?.get(position)?.pdfSize)

            startActivity(intent)
            Log.d("3igwn3bg","mskmsk")
            bottomSheetDialog?.hide()
        }
        //this will send user to PdfViewActivity with pdfuri AND pdftitle acc to position
        openLinearLayout?.setOnClickListener {

            var intent= Intent(context, PdfView_Activity::class.java)
            intent.setAction(PDFProp.MY_OPEN_ACTION)
            intent.putExtra(PDFProp.PDF_APPENDED_URI,pdfUri)
                .putExtra(PDFProp.PDF_TITLE,pdfName)

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
                var intt = context?.contentResolver?.delete(Uri.parse(pdflist?.get(position)?.pdfUri!!), null, null)
                bottomSheetDialog?.hide()
                pdflist?.remove(pdflist?.get(position))
                myAdapter.notifyItemRemoved(position)
                myAdapter.notifyItemRangeChanged(position, pdflist?.size!!)
            }catch (e: Exception){Toast.makeText(requireContext(),e.message,Toast.LENGTH_SHORT).show()}
        }

        //to bookmark the pdf into database
        bookmarkIcon_bottomsheet?.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                Log.d("3g8h3g",date.toString())
                addBookmarks(Uri.parse(pdfUri),pdfName,pdfSize,date)
                Log.d("38f3gh7fg3h","bookmark")
                bookmarkIcon_bottomsheet.visibility=View.GONE

                removebookmark_bottomsheet.visibility=View.VISIBLE
            }
        }
        //to undo or remove the bookmarked pdf from database
        removebookmark_bottomsheet?.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                removeBookmarks(Uri.parse(pdflist.get(position).pdfUri!!))
                Log.d("38f3gh7fg3h","Removed bookmark")
                //  removebookmark_bottomsheet.isEnabled=false
                removebookmark_bottomsheet.visibility=View.GONE

                bookmarkIcon_bottomsheet.visibility=View.VISIBLE

            }
        }
        //to share pdf to another apps
        shareIcon_bottomsheet?.setOnClickListener {
            intent?.putExtra(Intent.EXTRA_STREAM,pdflist.get(position).pdfUri)

            intent?.putExtra(Intent.EXTRA_SUBJECT, "Sharing File from My Pdf App.");
            // intent?.putExtra(Intent.EXTRA_TEXT, "Sharing File from Webkul to purchase items...");
            startActivity(Intent.createChooser(intent,"my PDF FILE"))

        }
        //to rename pdf name
        renameeLinearLayout?.setOnClickListener {
           /* var builder = AlertDialog.Builder(requireContext(),R.style.Theme_AppCompat_Dialog_Alert)
            var viewgroup=activity?.findViewById<ViewGroup>(R.id.content)
            var view =  LayoutInflater.from(requireContext()).inflate(R.layout.rename_dialog,viewgroup , false)
            builder.setView(view)
            builder.setCancelable(true)

            var renameButton = view.findViewById<MaterialButton>(R.id.renameButton)
            renameButton.setOnClickListener {

                var renamedittext = view.findViewById<EditText>(R.id.renameEdittext)
                var renamed = renamedittext.text.toString()

                if(!renamed.isEmpty()) {
                    var contentValues = ContentValues()
                    contentValues.put(MediaStore.MediaColumns.TITLE, renamed)
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, renamed+".pdf")

                    //to update item in mediastore shared storage
                    var intt = activity?.contentResolver?.update(Uri.parse(pdfUri), contentValues, null, null)

                    if (intt == 1) { //and this is for update item in Room Database
                        CoroutineScope(Dispatchers.IO).launch {
                            var bookmarkedItem = MyRoomDatabase?.getInstance(requireContext()).daoMethod().query(pdflist.get(position).pdfUri)
                            var newitem = Items_RecentPdfs(bookmarkedItem.pdf_ID, renamed, bookmarkedItem.pdfSize, bookmarkedItem.date, bookmarkedItem.pdfUri)

                            Log.d("2f89efhef", newitem.pdfName + "k")
                            //now update with new item replace with previous one
                            MyRoomDatabase.getInstance(requireContext()).daoMethod().update(newitem)

                            withContext(Dispatchers.Main){
                                Snackbar.make(binding?.recentRecyclerView!!,"Name Changed",2000).show()
                            }
                        }//coroutinescope


                    }
                    Log.d("4gf4h3g", intt.toString())
                    renameDialog.hide()
                    bottomSheetDialog?.hide()
                }
                else{
                    Toast.makeText(requireContext(),"Please enter name",Toast.LENGTH_SHORT).show()
                    //  renameDialog.hide()
                    // bottomSheetDialog?.hide()

                }
            } //positive button of dialog

            renameDialog = builder.create()
            renameDialog.show()*/
        ConversionandUtilsClass.renameFrom_everywhere(pdfUri,requireContext(),requireActivity(),bottomSheetDialog!!)
        }

        //to check if selected pdf is in bookmark database or not for bookmark buttons
        CoroutineScope(Dispatchers.IO).launch {
            var itemsPdf = MyRoomDatabase2.getInstance(requireContext()).daoMethods().query(pdflist.get(position).pdfUri.toString())

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
    fun showDialoguewithDetails(pdflist:ArrayList<Items_RecentPdfs>,position:Int){

        var materialBuilder= MaterialAlertDialogBuilder(requireContext(),R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
        var viewgroup=activity?.findViewById<ViewGroup>(R.id.content)
        var view = LayoutInflater.from(requireContext()).inflate(R.layout.custom_details_dialgue,viewgroup)
        materialBuilder.setView(view)
        val namedialogue=view.findViewById<TextView>(R.id.nameTextviewDialogue)
        val datedialogue=view.findViewById<TextView>(R.id.datetextviewDailgoue)
        val sizedialogue=view.findViewById<TextView>(R.id.sizeTextviewDialogue)
        // val pathdialogue=view.findViewById<TextView>(R.id.pathtextviewDailgoue)

        namedialogue.text=pdflist?.get(position)?.pdfName
        datedialogue.text= ConversionandUtilsClass.convertToDate(pdflist?.get(position)?.date?.toLong()!!).get(1)  //get date with time also
        sizedialogue.text= ConversionandUtilsClass.bytesToMB(pdflist?.get(position)?.pdfSize)+" mb"
        /*if(pdflist.get(position).relativePath!=null) {
            pathdialogue.text = pdflist?.get(position)?.relativePath
        }*/

        var dialoguee = materialBuilder.create()
        dialoguee.show()
    }

    suspend fun addBookmarks(uri:Uri,pdfname:String,pdfsize:String,pdfDate:Long) = withContext(Dispatchers.IO)
    {
        MyRoomDatabase2.getInstance(requireContext()).daoMethods().insert(Items_Bookmarks(uri.toString(),pdfname,pdfsize,pdfDate/1000L))
    }
    suspend fun removeBookmarks(uri:Uri) = withContext(Dispatchers.IO)
    {
        var liveUri = MyRoomDatabase2.getInstance(requireContext()).daoMethods().query(uri.toString())

        if(liveUri!=null) {
            deleteITEM(liveUri)
        }
    }

    fun deleteITEM(itemsBookmarks: Items_Bookmarks)
    {
        CoroutineScope(Dispatchers.Default).launch {
            MyRoomDatabase2.getInstance(requireContext()).daoMethods().delete(itemsBookmarks)
        }
    }

    override fun onStart() {
        super.onStart()
        //getting bottomsheetView layout
        var viewGroup = activity?.findViewById<ViewGroup>(R.id.content)
        bottomSheetView= LayoutInflater.from(requireContext()).inflate(R.layout.bottomsheet_dialogue,viewGroup)
        //creating BottomSheetDialogue instance
        bottomSheetDialog=BottomSheetDialog(requireContext(),R.style.ThemeOverlay_MaterialComponents_BottomSheetDialog)

        //then set bottomsheetView to bottomsheetDialogue instance
        bottomSheetDialog?.setContentView(bottomSheetView!!)

        // function for initializing bottomsheetViews
        initializeBottomsheetView()
    }

}