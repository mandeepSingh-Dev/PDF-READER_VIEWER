package com.example.pdf_reader_viewer.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.pdf_reader_viewer.*
import com.example.pdf_reader_viewer.RecylerViewClasses.Items_pdfs
import com.example.pdf_reader_viewer.RecylerViewClasses.MyAdapter
import com.example.pdf_reader_viewer.Roomclasses.Room_For_BOOKMARKS.Items_Bookmarks
import com.example.pdf_reader_viewer.Roomclasses.Room_For_BOOKMARKS.MyRoomDatabase2
import com.example.pdf_reader_viewer.UtilClasses.*
import com.example.pdf_reader_viewer.databinding.PdfListFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*
import java.util.*
import java.util.jar.Manifest


class pdf_list_Fragment : Fragment() {

    var pdflist: ArrayList<Items_pdfs>? = null
    var binding: PdfListFragmentBinding? = null
    var recyclerView: RecyclerView? = null
    var emptyView: ImageView? = null

    var bottomSheetDialog:BottomSheetDialog?=null
    var bottomSheetView:View?=null

    lateinit var pdfName1_bottomsheet:TextView
    lateinit var openLinearLayout:LinearLayout
    lateinit var mergeLinearLayout:LinearLayout
    lateinit var splitLinearLayout:LinearLayout
    lateinit var deleteLinearLayout:LinearLayout
    lateinit var renameeLinearLayout:LinearLayout
    lateinit var detailsLinearLayout:LinearLayout
    lateinit var bookmarkIcon_bottomsheet:ImageView
    lateinit var removebookmark_bottomsheet:ImageView
    lateinit var shareIcon_bottomsheet:ImageView

    var myAdapter:MyAdapter?=null
    var intent:Intent?=null
    var receiver:BroadcastReceiver?=null

    private var isReadPermissionGranted:Boolean?=false
    private var isWritePermissionGranted:Boolean?=false

    val localBroadcastManager:LocalBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(requireContext())
    }
    var uri :Uri?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pdflist = ArrayList()
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = PdfListFragmentBinding.inflate(inflater)
        return binding?.root
    }

  //  @SuppressLint("Range", "RestrictedApi")
    override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        intent=Intent(Intent.ACTION_SEND)
        intent?.type="application/pdf"

       /**____________________*/
              //getNewPdfs()
/*
      binding?.textviewALLL?.setOnClickListener {
         // launcher.launch(arrayOf("application/pdf"))
          var op= PDFOperationNATIVE(requireActivity())
                  op.setStartPage(12)
        //  op.showpage()
      }
*/
   /*   binding?.settingsIcONBottom?.setOnClickListener {
          startActivity(Intent(requireContext(),SettingsActviity::class.java))
      }*/
      //performing floating button
/*
      binding?.floatingButton?.setOnClickListener {
            var intent=Intent(requireContext(),PdfsTools_Activity::class.java)

            intent.putExtra(FragmentNames.OPEN_IMGTOPDF_FRAGMENT,FragmentNames.OPEN_IMGTOPDF_FRAGMENT)
            startActivity(intent)
          Log.d("3igwn3bg","mskmsk")
      }
*/
       /**____________________*/

        recyclerView = view.findViewById(R.id.pdfListRecylerView)
      //  progressBar = activity?.findViewById<ProgressBar>(R.id.progress_bar)
        emptyView = view.findViewById<ImageView>(R.id.emptyView)


      Log.d("349tu4jg4",viewLifecycleOwner.lifecycle.currentState.name)
     /* //here in ViewModelProvider(this...)
      var myViewModel=ViewModelProvider(requireActivity(),ViewModelProvider.AndroidViewModelFactory.getInstance(activity?.application!!)).get(MyViewModel_For_pdflist::class.java)
      myViewModel.getpdflistttt().observe(viewLifecycleOwner, object : Observer<ArrayList<Items_pdfs>> {
          override fun onChanged(pdflist: ArrayList<Items_pdfs>?) {
              if (pdflist?.isEmpty()!!) {
                  Log.d("3tubuenfe", "3ufufbkscsdc")
                  binding?.emptyView?.visibility = View.VISIBLE
                  binding?.emptyText?.visibility = View.VISIBLE
                  if(Build.VERSION.SDK_INT==Build.VERSION_CODES.Q) {
                      binding?.emptyText?.text = "Only created or modified files will be shown here"
                  }

                  binding?.pdfListProgress?.visibility = View.GONE
              } else {
                  Log.d("3u8hfjsncsjcisj8", "cnsncjnj2")

                  myAdapter = MyAdapter(requireContext(), pdflist!!)
                  recyclerView?.layoutManager = LinearLayoutManager(requireContext())
                  recyclerView?.adapter = myAdapter

                  binding?.pdfListProgress?.visibility = View.GONE
                  binding?.emptyView?.visibility = View.GONE
                  binding?.emptyText?.visibility = View.GONE

                  var textView: TextView = view.findViewById(R.id.textviewALLL)
                  textView.text = "All (" + pdflist?.size.toString() + ")"

                  activity?.runOnUiThread {
                      myAdapter?.notifyDataSetChanged()
                  }
                  searchPdfs(pdflist)
                  //this method for  setCustomClickListner method that is defined in MyAdapter class
                  myAdapterClickListner(myAdapter!!, pdflist)
              }

          }
      })
*/
      //getting permission and pdfs(as result) on click allow permission for below android 11
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.R){
           requestPermssions(requireContext())
        }//-------------------------->
      // **IMP** we get permission for above android R in onResume method because of MANAGE_EXTERNAL_STORAGE
      // permission which takes to settings activity and pdflist fragmenst gets stopped
     //------------------------------->

      // getting pdfs if READ_EXTERNAL_STORAGE is GRANTED only
     if( ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
     {
         setUpviewmodelWithRECYLERView()
         binding?.swipeRefreshLayout?.setOnRefreshListener {
             binding?.swipeRefreshLayout?.setRefreshing(false);
             setUpviewmodelWithRECYLERView()
         }
     }

         /* setUpviewmodelWithRECYLERView(view)
       binding?.swipeRefreshLayout?.setOnRefreshListener {
           binding?.swipeRefreshLayout?.setRefreshing(false);
           setUpviewmodelWithRECYLERView(view) }*/

    }// END OF onViewCreated block



    /**Method for Removing duplicate bucket/folder names from folderList*/
    fun filtering_FolderArraylist(folderList: ArrayList<String>) {
        Log.d("3ifhgjntggngvngc38", folderList.size.toString())
        var list = ArrayList<String>()
        folderList.forEach {
            if (!list.contains(it)) {
                list.add(it)
            }
        }

        Log.d("34fe56hdf", list.size.toString())
        list.forEach {
            Log.d("itemsidfhdc8948", it)
        }
        /**to find numbers of folders or bucket in folderListexm*/
        var count = 0;
        list.forEach {
            for (i in 0..folderList.size - 1) {
                if (folderList.get(i).equals(it)) {
                    Log.d("iryhtugfvn8ygh", count++.toString() + it)
                }
            }
            count = 0;

        }
    }


    /* folderList.forEach {
            Log.d("filteringARRAYLIST",it)
        }*/


    fun displayRenderedImage(view: View, activity: Activity, pageImage: Bitmap) {
        Thread(Runnable {
            activity?.runOnUiThread(Runnable() {
                Log.d("38hgi3h", "sksjdfksj")
                var imageView = view.findViewById<ImageView>(R.id.emptyView);
                imageView.setImageBitmap(pageImage);
            })
        })
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

        var appendedurii = pdflist.get(position).appendeduri!!
        Log.d("38gh8",appendedurii.toString())
        var sizee = pdflist.get(position).size
        var date_modifiedd = pdflist.get(position).date_modified!!
        var titlee = pdflist.get(position).title

        mergeLinearLayout?.setOnClickListener {

            var intent=Intent(context,PdfsTools_Activity::class.java)

            intent.putExtra(FragmentNames.OPEN_MERGE_FRAGMENT,FragmentNames.OPEN_MERGE_FRAGMENT)
                  .putExtra(PDFProp.PDF_TITLE,pdflist?.get(position)?.title)
                  .putExtra(PDFProp.PDF_APPENDED_URI,pdflist?.get(position)?.appendeduri)
                  .putExtra(PDFProp.PDF_SIZE,pdflist?.get(position)?.size)
            startActivity(intent)

            Log.d("3igwn3bg","mskmsk")
            bottomSheetDialog?.hide()
        }
        //this will send user to PdfTools_Activity----> Split Fragment with pdfuri and other data acc to position
        splitLinearLayout?.setOnClickListener {

            var intent=Intent(context,PdfsTools_Activity::class.java)

            intent.putExtra(FragmentNames.OPEN_SPLIT_FRAGMENT,FragmentNames.OPEN_SPLIT_FRAGMENT)
                .putExtra(PDFProp.PDF_TITLE,pdflist?.get(position)?.title)
                .putExtra(PDFProp.PDF_APPENDED_URI,pdflist?.get(position)?.appendeduri)
                .putExtra(PDFProp.PDF_SIZE,pdflist?.get(position)?.size)

            startActivity(intent)
            Log.d("3igwn3bg","mskmsk")
            bottomSheetDialog?.hide()
        }
        //this will send user to PdfViewActivity with pdfuri AND pdftitle acc to position
        openLinearLayout?.setOnClickListener {

            var intent=Intent(context,PdfView_Activity::class.java)
                intent.setAction(PDFProp.MY_OPEN_ACTION)
            intent.putExtra(PDFProp.PDF_APPENDED_URI,pdflist?.get(position)?.appendeduri.toString())
                  .putExtra(PDFProp.PDF_TITLE,pdflist?.get(position)?.title)
                  .putExtra(PDFProp.PDF_SIZE,pdflist?.get(position)?.size)

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
                myAdapter.notifyItemRangeChanged(position, pdflist?.size)
            }catch (e:Exception){Toast.makeText(requireContext(),e.message,Toast.LENGTH_SHORT).show()}
        }

        //to bookmark the pdf into database
        bookmarkIcon_bottomsheet?.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    Log.d("3g8h3g",date_modifiedd.toString())

                    addBookmarks(appendedurii,titlee,sizee,date_modifiedd.toLong())
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
            startActivity(Intent.createChooser(intent,pdflist.get(position).title))

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

    fun searchPdfs(pdfList:ArrayList<Items_pdfs>)
    {
        var arrayListt=ArrayList<Items_pdfs>()

       // binding?.pdflistSearchView?.set

        binding?.searchEdittext?.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                          }

            override fun afterTextChanged(s: Editable?) {
                var newText = s.toString()
                arrayListt.removeAll(arrayListt)

                pdfList.forEach {
                    if (it.title.lowercase().contains(newText?.lowercase())) {
                        binding?.emptyText?.visibility = View.GONE
                        binding?.emptyView?.visibility = View.GONE
                        arrayListt.add(it)

                    }
                }
                if (arrayListt.isEmpty()) {
                    binding?.emptyText?.visibility = View.VISIBLE
                    binding?.emptyView?.visibility = View.VISIBLE
                } else {
                    myAdapter = MyAdapter(requireContext(), arrayListt)
                    recyclerView?.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView?.adapter = myAdapter
                    //this method for  setCustomClickListner method that is defined in MyAdapter class
                    myAdapterClickListner(myAdapter!!, arrayListt)
                }
            }
        })

          /*  binding?.pdflistSearchView?.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    arrayListt.removeAll(arrayListt)

                    pdfList.forEach {
                        if (it.title.lowercase().contains(newText?.lowercase()!!)) {
                            arrayListt.add(it)

                        }
                    }
                    if (arrayListt.isEmpty()) {
                        binding?.emptyText?.visibility = View.VISIBLE
                        binding?.emptyView?.visibility = View.VISIBLE
                    } else {
                        myAdapter = MyAdapter(requireContext(), arrayListt)
                        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
                        recyclerView?.adapter = myAdapter
                        //this method for  setCustomClickListner method that is defined in MyAdapter class
                        myAdapterClickListner(myAdapter!!, arrayListt)

                    }
                    return true
                }

            })*/

    }
    fun myAdapterClickListner(myAdapter:MyAdapter,pdflist:ArrayList<Items_pdfs>)
    {
/*
        myAdapter?.setCustomOnClickListenerr(object:MyAdapter.CustomOnClickListener{
            override fun customOnClick(position: Int) {
                Log.d("3iegnv3me,wv",position.toString())
                pdfName1_bottomsheet?.setText(position.toString())
                bottomSheetDialog?.show()
                clickOnbottomSheetViews(pdflist,position,myAdapter)

            }
        })
*/
        myAdapter?.setMCustomClickListenr(object:MCustomOnClickListener {
            override fun onClick(position: Int) {
                Log.d("3iegnv3me,wv",position.toString())
                pdfName1_bottomsheet?.setText(pdflist.get(position).title)
                bottomSheetDialog?.show()
                Log.d("hfe","NEW INTERFACE IS NOW READY")
                clickOnbottomSheetViews(pdflist,position,myAdapter)
            }
        })


    }
    fun showDialoguewithDetails(pdflist:ArrayList<Items_pdfs>,position:Int){

        var materialBuilder=MaterialAlertDialogBuilder(requireContext(),R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
        var viewgroup=activity?.findViewById<ViewGroup>(R.id.content)
        var view = LayoutInflater.from(requireContext()).inflate(R.layout.custom_details_dialgue,viewgroup)
        materialBuilder.setView(view)
        val namedialogue=view.findViewById<TextView>(R.id.nameTextviewDialogue)
        val datedialogue=view.findViewById<TextView>(R.id.datetextviewDailgoue)
        val sizedialogue=view.findViewById<TextView>(R.id.sizeTextviewDialogue)
        // val pathdialogue=view.findViewById<TextView>(R.id.pathtextviewDailgoue)

        namedialogue.text=pdflist?.get(position)?.title
        datedialogue.text=ConversionandUtilsClass.convertToDate(pdflist?.get(position)?.date_modified?.toLong()!!).get(1)  //get date with time also
        sizedialogue.text=ConversionandUtilsClass.bytesToMB(pdflist?.get(position)?.size)+" mb"
        /*if(pdflist.get(position).relativePath!=null) {
            pathdialogue.text = pdflist?.get(position)?.relativePath
        }*/

        var dialoguee=materialBuilder.create()
        dialoguee.show()
    }

   suspend fun addBookmarks(uri:Uri,pdfname:String,pdfsize:String,pdfDate:Long) = withContext(Dispatchers.IO)
    {
        MyRoomDatabase2.getInstance(requireContext()).daoMethods().insert(Items_Bookmarks(uri.toString(),pdfname,pdfsize,pdfDate))
    }

    suspend fun removeBookmarks(uri:Uri) = withContext(Dispatchers.IO)
    {
        var itemsBookmarks:Items_Bookmarks?=null
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

    fun setUpviewmodelWithRECYLERView(view:View){
        //here in ViewModelProvider(this...)
       var myViewModel=ViewModelProvider(requireActivity(),ViewModelProvider.AndroidViewModelFactory.getInstance(activity?.application!!)).get(MyViewModel_For_pdflist::class.java)

        myViewModel.getpdflistttt().observe(viewLifecycleOwner, object : Observer<ArrayList<Items_pdfs>> {
            override fun onChanged(pdflist: ArrayList<Items_pdfs>?) {
                if (pdflist?.isEmpty()!!) {
                    Log.d("3tubuenfe", "3ufufbkscsdc")
                    binding?.emptyView?.visibility = View.VISIBLE
                    binding?.emptyText?.visibility = View.VISIBLE
                    if(Build.VERSION.SDK_INT==Build.VERSION_CODES.Q) {
                        binding?.emptyText?.text = "Only created or modified files will be shown here"
                    }

                    binding?.pdfListProgress?.visibility = View.GONE
                } else {
                    Log.d("3u8hfjsncsjcisj8", "cnsncjnj2")

                    myAdapter = MyAdapter(requireContext(), pdflist)
                    recyclerView?.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView?.adapter = myAdapter

                    binding?.pdfListProgress?.visibility = View.GONE
                    binding?.emptyView?.visibility = View.GONE
                    binding?.emptyText?.visibility = View.GONE

                   // var textView: TextView = view.findViewById(R.id.textviewALLL)
                    binding?.textviewALLL?.text = "All (" + pdflist?.size.toString() + ")"

                    activity?.runOnUiThread {
                        myAdapter?.notifyDataSetChanged()

                    }
                    searchPdfs(pdflist)
                    //this method for  setCustomClickListner method that is defined in MyAdapter class
                    myAdapterClickListner(myAdapter!!, pdflist)
                }

            }
        })


       // myViewModel.viewModelScope.cancel(null)

    }
    fun setUpviewmodelWithRECYLERView(){
        //here in ViewModelProvider(this...)
        var myViewModel=ViewModelProvider(requireActivity(),ViewModelProvider.AndroidViewModelFactory.getInstance(activity?.application!!)).get(MyViewModel_For_pdflist::class.java)

        myViewModel.getpdflistttt().observe(viewLifecycleOwner, object : Observer<ArrayList<Items_pdfs>> {
            override fun onChanged(pdflist: ArrayList<Items_pdfs>?) {
                if (pdflist?.isEmpty()!!) {
                    Log.d("3tubuenfe", "3ufufbkscsdc")
                    binding?.emptyView?.visibility = View.VISIBLE
                    binding?.emptyText?.visibility = View.VISIBLE
                    if(Build.VERSION.SDK_INT==Build.VERSION_CODES.Q) {
                        binding?.emptyText?.text = "Only created or modified files will be shown here"
                    }

                    binding?.pdfListProgress?.visibility = View.GONE
                } else {
                    Log.d("3u8hfjsncsjcisj8", "cnsncjnj2")

                    myAdapter = MyAdapter(requireContext(), pdflist)
                    recyclerView?.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView?.adapter = myAdapter

                    binding?.pdfListProgress?.visibility = View.GONE
                    binding?.emptyView?.visibility = View.GONE
                    binding?.emptyText?.visibility = View.GONE

                    // var textView: TextView = view.findViewById(R.id.textviewALLL)
                    binding?.textviewALLL?.text = "All (" + pdflist?.size.toString() + ")"

                    activity?.runOnUiThread {
                        myAdapter?.notifyDataSetChanged()

                    }
                    searchPdfs(pdflist)
                    //this method for  setCustomClickListner method that is defined in MyAdapter class
                    myAdapterClickListner(myAdapter!!, pdflist)
                }

            }
        })


        // myViewModel.viewModelScope.cancel(null)

    }

   // @SuppressLint("Range")
  // @SuppressLint("Range")
   /*fun getNewPdfs()
    {
        var collection:Uri
       *//* var filelist = arrayListOf<Items_pdfs>()
       // val selection = "_data LIKE'%.pdf'"
        *//**//**sql-where-clause-with-placeholder-variables  Here we select MimeType*//**//*
        val selection = MediaStore.Files.FileColumns.MIME_TYPE + " = ?"
        *//**//**getting MIME type for pdf*//**//*
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")
        *//**/
    override fun onResume() {
        super.onResume()
        requesting_MANAGE_ALL_DOCUMENT_Permission()
    }
      //permission for above android R for getting All PDFs
    fun requesting_MANAGE_ALL_DOCUMENT_Permission()
    {
        /**REQUESTING MANAGE_EXTERNAL_FILES PERMISSION FOR ACCESS ALL FILES/PDFs*/
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                Toast.makeText(requireContext(), "Manage_Permission granted", Toast.LENGTH_SHORT).show()

                Log.d("sdljsds","${myAdapter?.itemCount}")
                  if(myAdapter?.itemCount==null) {
                      setUpviewmodelWithRECYLERView()
                      binding?.swipeRefreshLayout?.setOnRefreshListener {
                          binding?.swipeRefreshLayout?.setRefreshing(false);
                          setUpviewmodelWithRECYLERView()
                      }
                  }
                var sharedprefrence = activity?.getSharedPreferences("managed", Context.MODE_PRIVATE)
                sharedprefrence?.edit()?.putBoolean("MANGEEEGED",true)?.apply()

            } else {
                Toast.makeText(requireContext(), "Manage_Permission NOT granted", Toast.LENGTH_SHORT).show()
                val uri = Uri.parse("package:" + activity?.getPackageName())
                startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,uri))
            }
        }//END OF IF BLOCK WHERE WE USE CONDITION FOR ANDROID R.
    }
//permission for below android 11 for getting pdfs
    fun requestPermssions(context:Context)
    {
        /** Below two lines are checking the permissions are granted or not*/
        var readPermission=ContextCompat.checkSelfPermission(context,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        var writePersmission=ContextCompat.checkSelfPermission(context,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        var manage_documentsPermission=ContextCompat.checkSelfPermission(context,android.Manifest.permission.MANAGE_DOCUMENTS) == PackageManager.PERMISSION_GRANTED

        var sdkVersion = Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q
        /**here we created a mutablelist to add permissions in list if permissions are not granted*/
        var permissionList= mutableListOf<String>()

        isReadPermissionGranted = readPermission
        isWritePermissionGranted = writePersmission || sdkVersion

        if(!readPermission)
        {
            permissionList.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            Log.d("dkhnd","read")
        }
        if(!writePersmission)
        {
            permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            Log.d("dkhnd","writePersmission")

        }
        if(!manage_documentsPermission)
        {
            permissionList.add(android.Manifest.permission.MANAGE_DOCUMENTS)
            Log.d("dkhnd","manage_documentsPermission")

        }

        /**now here we request permissions from resultLauncher... only if permissions are not granted*/
        if(permissionList.isNotEmpty())
        {
            launcher.launch(permissionList.toTypedArray())
        }
    }

    /**requesting permissions request*/
    var launcher=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(),
        ActivityResultCallback {
            setUpviewmodelWithRECYLERView()
            binding?.swipeRefreshLayout?.setOnRefreshListener {
                binding?.swipeRefreshLayout?.setRefreshing(false);
                setUpviewmodelWithRECYLERView()
            }
        })



}