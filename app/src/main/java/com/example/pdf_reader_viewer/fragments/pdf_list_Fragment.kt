package com.example.pdf_reader_viewer.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pdf_reader_viewer.PdfView_Activity
import com.example.pdf_reader_viewer.PdfsTools_Activity
import com.example.pdf_reader_viewer.R
import com.example.pdf_reader_viewer.RecylerViewClasses.Items_pdfs
import com.example.pdf_reader_viewer.RecylerViewClasses.MyAdapter
import com.example.pdf_reader_viewer.UtilClasses.*
import com.example.pdf_reader_viewer.databinding.PdfListFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPage
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.font.PDFont
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font
import com.tom_roush.pdfbox.pdmodel.graphics.image.JPEGFactory
import com.tom_roush.pdfbox.pdmodel.graphics.image.LosslessFactory
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader
import kotlinx.coroutines.*
import java.io.*
import java.lang.Exception


class pdf_list_Fragment : Fragment() {

    var pdflist: ArrayList<Items_pdfs>? = null
    var binding: PdfListFragmentBinding? = null
    var progressBar: ProgressBar? = null
    var recyclerView: RecyclerView? = null
    var externalUri: Uri? = null
    var emptyView: ImageView? = null
 /*   var fab1:FloatingActionButton?=null
    var fab2:FloatingActionButton?=null
    var fab3:FloatingActionButton?=null
    var fab:FloatingActionButton?=null*/

    var bottomSheetDialog:BottomSheetDialog?=null
    var bottomSheetView:View?=null
    var pdfName1_bottomsheet:TextView?=null

    lateinit var openLinearLayout:LinearLayout
    lateinit var mergeLinearLayout:LinearLayout
    lateinit  var splitLinearLayout:LinearLayout
    lateinit  var deleteLinearLayout:LinearLayout
    lateinit  var renameeLinearLayout:LinearLayout
    lateinit  var detailsLinearLayout:LinearLayout
    var myAdapter:MyAdapter?=null

//    var OPEN_MERGE_FRAGMENT="OPEN_MERGE_FRAGMENT"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pdflist = ArrayList()
        PDFBoxResourceLoader.init(activity?.applicationContext);
//        throw RuntimeException()

    }

    @SuppressLint("ResourceType")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = PdfListFragmentBinding.inflate(inflater)
        //getting bottomsheetView layout
        bottomSheetView= LayoutInflater.from(requireContext()).inflate(R.layout.bottomsheet_dialogue,container)
        //creating BottomSheetDialogue instance
        bottomSheetDialog=BottomSheetDialog(requireContext(),R.style.Theme_Design_BottomSheetDialog)
        //then set bottomsheetView to bottomsheetDialogue instance
        bottomSheetDialog?.setContentView(bottomSheetView!!)
        //init pdfNamebottomsheet textview of bottomsheetView
        pdfName1_bottomsheet=bottomSheetView?.findViewById<TextView>(R.id.pdfName1_bottomsheet)


        return binding?.root
    }

    @SuppressLint("Range", "RestrictedApi")
    override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // PdfOperations(requireActivity()).createNativedoc(requireActivity())
       binding?.floatingButton?.setOnClickListener {

           PdfOperations(requireActivity()).createNativedoc(requireActivity())
         //  lkdlks.launch(null)
       /*    var intent=Intent()
           intent.action=Intent.ACTION_CREATE_DOCUMENT
           intent.type="application/pdf"
           lkdlks.launch("pppdfdf.pdf")*/
       }
/*        var arrlist=ArrayList<Int>()
        arrlist.add(0)
        arrlist.add(1)
        arrlist.add(2)
        arrlist.add(3)
        arrlist.add(4)
        arrlist.add(5)
        arrlist.add(6)

        arrlist.forEach { Log.d("3t3f3ew",it.toString()) }
        var list=ConversionandUtilsClass().swap(arrlist,5,0)
        list.forEach { Log.d("3t3f3ew",it.toString()) }*/



        // function for initializing bottomsheetViews
        initializeBottomsheetView()

      //  launcher.launch(null)
       // launcher1.launch(arrayOf("image/*"))

        /* //create pdf
        createPdf(view)
        //function to strip text from pdf
        PdfOperations().stripText(view,requireActivity())
      //function to spiltting pdf
        PdfOperations().splittingPdf(requireActivity()!!)
        //merging two pdfs
        PdfOperations().mergePdfs(requireActivity()!!)
        //read and return pdf as image
        var bitmap=PdfOperations().renderFile(view,requireActivity()!!)
        displayRenderedImage(view,requireActivity(),bitmap)
        //encrypting pdf file
        PdfOperations().createEncryptedPdf(view,requireActivity()!!)*/

        recyclerView = view.findViewById(R.id.pdfListRecylerView)
        progressBar = activity?.findViewById<ProgressBar>(R.id.progress_bar)
        emptyView = view.findViewById<ImageView>(R.id.emptyView)

        var myViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(activity?.application!!)).get(MyViewModel::class.java)

        myViewModel.getpdflistttt().observe(viewLifecycleOwner, object : Observer<ArrayList<Items_pdfs>> {
                override fun onChanged(pdflist: ArrayList<Items_pdfs>?) {

                    if (pdflist?.isEmpty()!!) {
                        Log.d("3tubuenfe", "3ufufbkscsdc")
                        binding?.emptyView?.visibility = View.VISIBLE
                        binding?.emptyText?.visibility = View.VISIBLE
                    } else {
                        Log.d("3u8hfjsncsjcisj8", "cnsncjnj2")
                    }
                     myAdapter = MyAdapter(requireContext(), pdflist!!)
                    recyclerView?.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView?.adapter = myAdapter
                    var textView: TextView = view.findViewById(R.id.textviewALLL)
                    textView.text = "All (" + pdflist?.size.toString() + ")"

             activity?.runOnUiThread {
                   myAdapter?.notifyDataSetChanged()
             }
                    searchPdfs(pdflist)
                    //this method for  setCustomClickListner method that is defined in MyAdapter class
                    myAdapterClickListner(myAdapter!!,pdflist)

                }
            })
    }// END OF onViewCreated block

    suspend fun getPdfList_2(): ArrayList<Items_pdfs> = withContext(Dispatchers.Default) {

        // progressBar?.visibility=View.VISIBLE


        /**THIS URI IS WORKING FOR ALL ANDROID VERSIONS*/
        externalUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)

        /**sql-where-clause-with-placeholder-variables  Here we select MimeType*/
        val selection = MediaStore.Files.FileColumns.MIME_TYPE + " = ?"

        /**getting MIME type for pdf*/
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")

        /**values-of-placeholder-variables  giving mimetype to selection_args */
        val selectionArgs = arrayOf(mimeType)

        /**content Resolver to get cursor for filess..*/
        var contentResolver = context?.contentResolver
        var cursor: Cursor? = null

        /**media-database-columns-to-retrieve*/
        var stringProjectioon: Array<String>? = null

        /**list for buckets or folders*/
        var bucketsList = ArrayList<String>()

        /**getting list according to android build versions */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            stringProjectioon = arrayOf(
                MediaStore.Files.FileColumns.DISPLAY_NAME,       //this column shows null below android 10
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME, //For folder/bucket name of item/pdf
                MediaStore.Files.FileColumns.MIME_TYPE,           //for selecting only MIME type
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.RELATIVE_PATH
            )

            cursor = contentResolver?.query(
                externalUri!!,
                stringProjectioon,
                selection,
                selectionArgs,
                null
            )

            /**below line is to get files from particular bucket or folder*/
            //   var cursor = contentResolver?.query(externalUri!!, stringProjectioon,  MediaStore.Images.Media._ID + " like ? ", arrayOf("%Download%"), null, null)

            /**getting coloumns name*/
            var titleColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.TITLE)!!
            val idColoumn = cursor?.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)!!
            val displayColoumn =
                cursor?.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)!!
            val bucketColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)!!
            val dateModColoumn =
                cursor?.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED)!!
            val sizeColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.SIZE)!!
            val relativePathColoumn =
                cursor?.getColumnIndex(MediaStore.Files.FileColumns.RELATIVE_PATH)!!

            /**Getting all cursors in loop*/
            while (cursor?.moveToNext()!!) {

                // Log.d("dfdfd",cursor.toString())
                var displeynamee = cursor?.getString(displayColoumn)
                var title = cursor?.getString(titleColoumn)
                var id = cursor?.getLong(idColoumn)
                var bucket = cursor?.getString(bucketColoumn)
                var dateModified = cursor?.getString(dateModColoumn)
                var size = cursor?.getString(sizeColoumn)
                var relativepath = cursor?.getString(relativePathColoumn)

                if (bucket != null) {
                    bucketsList.add(bucket!!)
                }

                // Log.d("PPPDFDF", displeynamee + " " + title + "____"/*+RELATIVEPATH*/ + id +"_____"+bucket)
                // activity?.runOnUiThread {
                //   Toast.makeText(context,/*RELATIVEPATH+*/"  " + title + volumename, Toast.LENGTH_LONG).show()
                //  }
                /** As Data column id deprecated so we appended id with VOLUME_EXTERNAL*/
                var data_uri = Uri.withAppendedPath(
                    MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL),
                    id.toString()
                )
                //  pdflist?.add(Items_pdfs(title!!, size!!, data_uri, dateModified, relativepath!!, bucket))
                /**adding cursoritems to pdflist in loop */
                pdflist?.add(
                    Items_pdfs(
                        title!!,
                        size!!,
                        data_uri,
                        dateModified,
                        relativepath!!,
                        bucket
                    )
                )
            }
            return@withContext pdflist!!
        } //END OF IF BLOCK
        else {
            stringProjectioon = arrayOf(
                MediaStore.Files.FileColumns.DISPLAY_NAME,       //this column shows null below android 10
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME, //For folder/bucket name of item/pdf
                MediaStore.Files.FileColumns.MIME_TYPE,           //for selecting only MIME type
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.SIZE
            )

            /**Query cursors that include files or data*/
            cursor = contentResolver?.query(
                externalUri!!,
                stringProjectioon,
                selection,
                selectionArgs,
                null
            )
            /**below line is to get files from particular bucket or folder*/
            //   var cursor = contentResolver?.query(externalUri!!, stringProjectioon,  MediaStore.Images.Media._ID + " like ? ", arrayOf("%Download%"), null, null)

            /**getting coloumns name*/
            var titleColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.TITLE)!!
            val idColoumn = cursor?.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)!!
            val displayColoumn =
                cursor?.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)!!
            val bucketColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)!!
            val dateModColoumn =
                cursor?.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED)!!
            val sizeColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.SIZE)!!
            val relativePathColoumn =
                cursor?.getColumnIndex(MediaStore.Files.FileColumns.RELATIVE_PATH)!!

            /**Getting all cursors in loop*/
            while (cursor?.moveToNext()!!) {

                // Log.d("dfdfd",cursor.toString())
                var title = cursor?.getString(titleColoumn)
                var id = cursor?.getLong(idColoumn)
                var bucket = cursor?.getString(bucketColoumn)
                var dateModified = cursor?.getString(dateModColoumn)
                var size = cursor?.getString(sizeColoumn)

                if (bucket != null) {
                    bucketsList.add(bucket!!)
                }
                Log.d("PPPDFDF", " " + title + "____"/*+RELATIVEPATH*/ + id + "_____" + bucket)
                // activity?.runOnUiThread {
                //   Toast.makeText(context,/*RELATIVEPATH+*/"  " + title + volumename, Toast.LENGTH_LONG).show()
                //  }
                /** As Data column id deprecated so we appended id with VOLUME_EXTERNAL*/
                var data_uri = Uri.withAppendedPath(
                    MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL),
                    id.toString()
                )
                //  pdflist?.add(Items_pdfs(title!!, size!!, data_uri, dateModified, relativepath!!, bucket))
                /**adding cursoritems to pdflist in loop */

                pdflist?.add(Items_pdfs(title!!, size!!, data_uri, dateModified, bucket))
            }

            return@withContext pdflist!!
        } //END OF ELSE BLOCK

        //    Log.d("3oujr74ghvn",pdflist?.size.toString())
        /**Method for Removing duplicate bucket/folder names from folderList*/
        // filtering_FolderArraylist(folderlistExm)


        //  LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(Intent("SENDINGFOLDERLIST").putStringArrayListExtra("FOLDERLIST",folderlistExm))

    }


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

    fun createPdf(v: View?) {
        val document = PDDocument()
        val page = PDPage()
        document.addPage(page)

        // Create a new font object selecting one of the PDF base fonts
        val font: PDFont = PDType1Font.HELVETICA
        // Or a custom font
//        try
//        {
//            // Replace MyFontFile with the path to the asset font you'd like to use.
//            // Or use LiberationSans "com/tom_roush/pdfbox/resources/ttf/LiberationSans-Regular.ttf"
//            font = PDType0Font.load(document, assetManager.open("MyFontFile.TTF"));
//        }
//        catch (IOException e)
//        {
//            Log.e("PdfBox-Android-Sample", "Could not load font", e);
//        }
        val contentStream: PDPageContentStream
        try {
            // Define a content stream for adding to the PDF
            contentStream = PDPageContentStream(document, page)

            // Write Hello World in blue text
            contentStream.beginText()
            contentStream.setNonStrokingColor(15, 38, 192)
            contentStream.setFont(font, 12f)
            contentStream.newLineAtOffset(100f, 700f)
            contentStream.showText("Hello World")
            contentStream.endText()

            // Load in the images
            val inn: InputStream = activity?.assets?.open("falcon.jpg")!!
            val alpha: InputStream = activity?.assets?.open("trans.png")!!

            // Draw a green rectangle
            contentStream.addRect(5f, 500f, 100f, 100f)
            contentStream.setNonStrokingColor(0, 255, 125)
            contentStream.fill()

            // Draw the falcon base image
            val ximage = JPEGFactory.createFromStream(document, inn)
            contentStream.drawImage(ximage, 20f, 20f)

            // Draw the red overlay image
            val alphaImage = BitmapFactory.decodeStream(alpha)
            val alphaXimage = LosslessFactory.createFromImage(document, alphaImage)
            contentStream.drawImage(alphaXimage, 20f, 20f)

            // Make sure that the content stream is closed:
            contentStream.close()

            // Save the final pdf document to a file
            var file = File("sdcard/manddeettttp")
            if (!file.exists()) {
                file.mkdir()
            }
            var stream = FileOutputStream(file.absolutePath + "/jkjkj.pdf")

            //  val path: String = /*root.getAbsolutePath().toString() + "/Created.pdf"*/ activity?.applicationContext?.filesDir?.absolutePath.toString()+"/created.pdf"
            val path: String = file.absolutePath.toString()
            // Log.d("37fgewf",file.absolutePath.toString())

            document.save(stream)
            document.close()
            // tv.setText("Successfully wrote PDF to $path")
        } catch (e: IOException) {
            Log.e("PdfBox-Android-Sample", "Exception thrown while creating PDF", e)
        }
    }

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
        openLinearLayout=bottomSheetDialog?.findViewById<LinearLayout>(R.id.openLinearLayout)!!
        mergeLinearLayout=bottomSheetDialog?.findViewById<LinearLayout>(R.id.mergeLinearLayout)!!
        splitLinearLayout=bottomSheetDialog?.findViewById<LinearLayout>(R.id.splitLinearLayout)!!
        deleteLinearLayout=bottomSheetDialog?.findViewById<LinearLayout>(R.id.deleteLinearLayout)!!
        renameeLinearLayout=bottomSheetDialog?.findViewById<LinearLayout>(R.id.renameeLinearLayout)!!
        detailsLinearLayout=bottomSheetDialog?.findViewById<LinearLayout>(R.id.detailsLinearLayout)!!
    }

    fun clickOnbottomSheetViews(pdflist:ArrayList<Items_pdfs>,position:Int,myAdapter: MyAdapter){
        //this will send user to PdfTools_Activity----> Merge Fragment with pdfuri and other data acc to position
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
        deleteLinearLayout?.setOnClickListener {
            try {
                Log.d("in3g3", position.toString())
                var intt = context?.contentResolver?.delete(pdflist?.get(position)?.appendeduri!!, null, null)
                bottomSheetDialog?.hide()
                pdflist?.remove(pdflist?.get(position))
                myAdapter.notifyItemRemoved(position)
                myAdapter.notifyItemRangeChanged(position, pdflist?.size!!)
            }catch (e:Exception){Toast.makeText(requireContext(),e.message,Toast.LENGTH_SHORT).show()}
        }

        binding?.floatingButton?.setOnClickListener {
            var intent=Intent(context,PdfsTools_Activity::class.java)

            intent.putExtra(FragmentNames.OPEN_IMGTOPDF_FRAGMENT,FragmentNames.OPEN_IMGTOPDF_FRAGMENT)
              /*  .putExtra(PDFProp.PDF_TITLE,pdflist?.get(position)?.title)
                .putExtra(PDFProp.PDF_APPENDED_URI,pdflist?.get(position)?.appendeduri)
                .putExtra(PDFProp.PDF_SIZE,pdflist?.get(position)?.size)*/

            startActivity(intent)
            Log.d("3igwn3bg","mskmsk")
        }

    }

    fun searchPdfs(pdfList:ArrayList<Items_pdfs>)
    {
        var arrayListt=ArrayList<Items_pdfs>()

        binding?.pdflistSearchView?.setOnQueryTextListener(object:SearchView.OnQueryTextListener, androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                arrayListt.removeAll(arrayListt)

                pdfList.forEach {
                    if(it.title.lowercase().contains(newText?.lowercase()!!)) {
                        arrayListt.add(it)

                    }
                }
                if(arrayListt.isEmpty())
                {
                        binding?.emptyText?.visibility = View.VISIBLE
                        binding?.emptyView?.visibility = View.VISIBLE
                }else {
                    myAdapter = MyAdapter(requireContext(), arrayListt)
                    recyclerView?.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView?.adapter = myAdapter
                   //this method for  setCustomClickListner method that is defined in MyAdapter class
                    myAdapterClickListner(myAdapter!!,arrayListt)

                }
                return  true
            }

        })
    }
    fun myAdapterClickListner(myAdapter:MyAdapter,pdflist:ArrayList<Items_pdfs>)
    {
        myAdapter?.setCustomOnClickListenerr(object:MyAdapter.CustomOnClickListener{
            override fun customOnClick(position: Int) {
                Log.d("3iegnv3me,wv",position.toString())
                pdfName1_bottomsheet?.setText(position.toString())
                bottomSheetDialog?.show()
                clickOnbottomSheetViews(pdflist,position,myAdapter)

            }
        })

    }
    fun showDialoguewithDetails(pdflist:ArrayList<Items_pdfs>,position:Int){

        var material=MaterialAlertDialogBuilder(requireContext(),R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
        var viewgroup=activity?.findViewById<ViewGroup>(R.id.content)
        var view = LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialgue,viewgroup)
        material.setView(view)
        val namedialogue=view.findViewById<TextView>(R.id.nameTextviewDialogue)
        val datedialogue=view.findViewById<TextView>(R.id.datetextviewDailgoue)
        val sizedialogue=view.findViewById<TextView>(R.id.sizeTextviewDialogue)
        // val pathdialogue=view.findViewById<TextView>(R.id.pathtextviewDailgoue)

        namedialogue.text=pdflist?.get(position)?.title
        datedialogue.text=ConversionandUtilsClass().convertToDate(pdflist?.get(position)?.date_modified?.toLong()!!).get(1)  //get date with time also
        sizedialogue.text=ConversionandUtilsClass().bytesToMB(pdflist?.get(position)?.size)+" mb"
        /*if(pdflist.get(position).relativePath!=null) {
            pathdialogue.text = pdflist?.get(position)?.relativePath
        }*/



        var dialoguee=material.create()
        dialoguee.show()
    }



  /*  var launcher=registerForActivityResult(ActivityResultContracts.TakePicturePreview(),
        ActivityResultCallback {
         PdfOperations(requireActivity())?.createPdf(view,requireActivity(),it)
        })
    var launcher1=registerForActivityResult(ActivityResultContracts.OpenDocument(),
        ActivityResultCallback {
            var inputStream=activity?.contentResolver?.openInputStream(it)
          var bitmap=  BitmapFactory.decodeStream(inputStream)
            PdfOperations(requireActivity())?.createPdf(view,requireActivity(),bitmap)

        })*/



}