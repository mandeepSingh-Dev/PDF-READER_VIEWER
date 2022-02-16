package com.example.pdf_reader_viewer.fragments

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.FileUtils
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pdf_reader_viewer.R
import com.example.pdf_reader_viewer.RecylerViewClasses.Items_pdfs
import com.example.pdf_reader_viewer.RecylerViewClasses.MyAdapter_ImagesToPDF
import com.example.pdf_reader_viewer.UtilClasses.PDFProp
import com.example.pdf_reader_viewer.UtilClasses.PdfOperations
import com.example.pdf_reader_viewer.databinding.ImageToPdfFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.collections.ArrayList


class ImageTo_Pdf_Fragment : DialogFragment() {

  //  var from:Int?=null
   // var to:Int?=null
    var binding: ImageToPdfFragmentBinding? = null
    var viewbottomsheet: View? = null
    var bottomdialogue: BottomSheetDialog? = null
    var bitmapLIST: ArrayList<Bitmap>? = null
    var radioGroup: RadioGroup? = null
    var radioButton: RadioButton? = null
    var alertDialog: androidx.appcompat.app.AlertDialog? = null
    var recyclerView: RecyclerView? = null
    var adapter: MyAdapter_ImagesToPDF? = null
    var alertDialogprogress:androidx.appcompat.app.AlertDialog?=null
    var importingDailogTextview:TextView?=null
    var importingnumberDailogText:TextView?=null
    var importingnumberTextview:TextView?=null
    var swapedbitmaplISTT:ArrayList<Bitmap>?=null
    var getnameTextinputlayout:TextInputLayout?=null
    var createButton:AppCompatButton?=null

     var outputStream:OutputStream?=null

    var  imgQuality:Int?=null

    var Comingfrom_MergeFrag:String?=null
    var mergeSelectedPdfList:ArrayList<Items_pdfs>?=null
    var length_mergeselected_list:Int?=null

  //  var fromposList:ArrayList<Int>? = ArrayList()
   // var toposlist:ArrayList<Int>? = ArrayList()
   /* val itemTouchHelper by lazy {
       var simpleItemTouchHelper = object:ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,0){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean
            {
                val adapter = recyclerView.adapter as MyAdapter_ImagesToPDF
                 from = viewHolder.adapterPosition
                 to = target.adapterPosition
                fromposList?.add(from!!)
                toposlist?.add(to!!)
                Log.d("3gf3gvwefw",from.toString()+" "+to.toString())
                // 2. Update the backing model. Custom implementation in
                //    MainRecyclerViewAdapter. You need to implement
                //    reordering of the backing model inside the method.

                adapter.movetoItem(from!!,to!!)
                //Collections.swap(bitmapLIST,from!!,to!!)
                adapter.notifyItemMoved(from!!,to!!)
                return  true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }

           override fun onMoved(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, fromPos: Int, target: RecyclerView.ViewHolder, toPos: Int, x: Int, y: Int) {
               super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
               Log.d("3489gh3wbfg",x.toString())
            //   fromposList?.add(fromPos)

           }

           override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
               super.onSelectedChanged(viewHolder, actionState)
               Log.d("4g4eg4eg4eg",actionState.toString())
           }

        }
        ItemTouchHelper(simpleItemTouchHelper)
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bitmapLIST = ArrayList()
        swapedbitmaplISTT = ArrayList()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = ImageToPdfFragmentBinding.inflate(inflater, container, false)
        viewbottomsheet = inflater.inflate(R.layout.bottomsheet_for_selectimages, container, false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //here we'r getting selectedPdfs_list from MergeFragment to add new pdfs list in this list
        if(arguments!=null)
        {
           Comingfrom_MergeFrag =  arguments?.getString(PDFProp.COMING_FROM_MERGEFRAG)
            mergeSelectedPdfList = arguments?.getParcelableArrayList<Items_pdfs>(PDFProp.MERGE_SELECTED_LIST)
            length_mergeselected_list = mergeSelectedPdfList?.size
        }

        recyclerView = view.findViewById(R.id.imagesRecylerView)
       // itemTouchHelper.attachToRecyclerView(recyclerView)

        bottomdialogue = BottomSheetDialog(requireContext(), R.style.Theme_Design_BottomSheetDialog)
        bottomdialogue?.setContentView(viewbottomsheet!!)
        bottomsheetButtonClickAction(bottomdialogue!!)


        binding?.selectImagesbutton?.setOnClickListener {
            bottomdialogue?.show()
        }

        var view22 = LayoutInflater.from(requireContext()).inflate(R.layout.custom_progress_dialogue, activity?.findViewById<ViewGroup>(R.id.content), false)
        alertDialogprogress=createAlertdialogue(view22)



        alertDialog = createAndClickDialog(bitmapLIST!!)
        binding?.createPDFbutton?.setOnClickListener {
            alertDialog?.show()
        }


    }


    //create document
    var launcher3 = registerForActivityResult(ActivityResultContracts.CreateDocument(),
        ActivityResultCallback {
            if(it!=null) {
                outputStream = activity?.contentResolver?.openOutputStream(it)!!
                Log.d("of66jd",it.lastPathSegment.toString())
                Log.d("34g93jg",outputStream.toString())

                if(outputStream!=null) {
                    //creating pdf acc to quality in this coroutine scope
                    CoroutineScope(Dispatchers.IO).launch {
                        //showing pleasewait dialogue
                        withContext(Dispatchers.Main) {
                            importingDailogTextview =
                                alertDialogprogress?.findViewById<TextView>(R.id.importingtextview)
                            importingnumberDailogText =
                                alertDialogprogress?.findViewById<TextView>(R.id.importedNumberTextview)
                            //these dailog textview had importing and imprting number texts
                            importingDailogTextview?.text = "please wait..."
                            importingnumberDailogText?.visibility = View.GONE
                            alertDialogprogress?.show()
                        }

                        PdfOperations(requireActivity())?.createPdf(bitmapLIST!!, "pdfName", 0,outputStream!!)

                        //hide please wait dialogue
                        withContext(Dispatchers.Main) {
                            alertDialogprogress?.hide()
                        }

                    }
                }//if block for output stream null or not
            }
        })

    fun bottomsheetButtonClickAction(bottomSheetDialog: BottomSheetDialog) {
        var linearSCanlayout = bottomSheetDialog.findViewById<LinearLayout>(R.id.linearlayScan)
        var linearSelectlayout = bottomSheetDialog.findViewById<LinearLayout>(R.id.linearlaySelect)

        linearSCanlayout?.setOnClickListener {
            launcher.launch(null)
            bottomdialogue?.hide()
        }
        linearSelectlayout?.setOnClickListener {
            launcher1.launch(arrayOf("image/*"))
            bottomdialogue?.hide()
        }

    }

    fun createAndClickDialog(bitmapList: ArrayList<Bitmap>): androidx.appcompat.app.AlertDialog {

        var alertdialogBuilder = AlertDialog.Builder(requireContext(), R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)

        val viewGroup = activity?.findViewById<ViewGroup>(R.id.content)
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.custom_imagequality_dialogue, viewGroup)
        createButton=view?.findViewById(R.id.createButtondialogue)

        createButton?.setOnClickListener {
            if (!bitmapList?.isEmpty()!!) {

                //getting radiogroup abnd buitton to get quality text
                radioGroup = alertDialog?.findViewById<RadioGroup>(R.id.radioGroupDialogue)
                getnameTextinputlayout = alertDialog?.findViewById<TextInputLayout>(R.id.getnamecustomDialogueInputLayout)

                var radbtnID = radioGroup?.checkedRadioButtonId
                var radioButton = radioGroup?.findViewById<RadioButton>(radbtnID!!)

                //split quality text of radiobutton
                 imgQuality=splitImgQaulity(radioButton?.text.toString())
                var pdfName=getnameTextinputlayout?.editText?.text.toString()

                if(!pdfName.equals("null") && !pdfName.isEmpty()) {

                    alertDialog?.hide()
                    Log.d("ifnmwe",pdfName)

                  //  launcher3.launch(pdfName+".pdf")

                    var intentt= Intent(Intent.ACTION_CREATE_DOCUMENT)
                    intentt.type = "application/pdf"

                    //launcher4.launch(intentt)

                    var intent= Intent(Intent.ACTION_CREATE_DOCUMENT)
                       intent.type="application/pdf"
                           intent.putExtra(Intent.EXTRA_TITLE, pdfName)
                      launcher4.launch(intent)


                }//if block for pdfname editext is not empty
                else{
                    Log.d("3ivnmw3ev","pdfname isempty")
                    getnameTextinputlayout?.isErrorEnabled=true
                    getnameTextinputlayout?.error="Invalid"
                }
            }//if block for !bitmap.isEmpty()
            else {
                Toast.makeText(requireContext(), "No images selected", Toast.LENGTH_SHORT)
                    .show()
            }
              Toast.makeText(requireContext(),"sidhfsfds",Toast.LENGTH_LONG).show()
          }
        alertdialogBuilder.setView(view)
            .setCancelable(true)

        var alertDialogue = alertdialogBuilder.create()


        return alertDialogue
    }
    //recyclerview with bitmap
    fun setup_updateRecyclerView(bitmapLIST:ArrayList<Bitmap>) {
        adapter = MyAdapter_ImagesToPDF(requireContext(), bitmapLIST!!)
        recyclerView?.layoutManager =
            StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recyclerView?.adapter = adapter
    }
    //decoding uri -->bitmap-->bitmap_Thumbnail
   suspend fun decodeUritoBitmap(it: ArrayList<Uri>): ArrayList<Bitmap> = withContext(Dispatchers.IO) {
        var arraylist: ArrayList<Bitmap> = ArrayList()
        var listSize=it.size

           var count=1
           var thumbnailbitmap:Bitmap?=null

        if(!it.isEmpty()) {
            withContext(Dispatchers.Main){
                alertDialogprogress?.show()
                importingnumberTextview = alertDialogprogress?.findViewById<TextView>(R.id.importedNumberTextview)
            }
            it.forEach {

                //  var  bitmapp=activity?.contentResolver?.loadThumbnail(it,Size(300,300),null)
                var bitmmmap = BitmapFactory.decodeStream(activity?.contentResolver?.openInputStream(it))
              thumbnailbitmap=  Bitmap.createScaledBitmap(bitmmmap,1000,1000,false)
               // thumbnailbitmap=ThumbnailUtils.extractThumbnail(bitmmmap,300,300)

                     arraylist.add(thumbnailbitmap!!)
                     bitmapLIST?.add(thumbnailbitmap!!)

                withContext(Dispatchers.Main){
                    setup_updateRecyclerView(bitmapLIST!!)
                    importingnumberTextview?.text = count++.toString()+"/"+listSize
                }

             }
            Log.d("3refef",arraylist.size.toString())

            withContext(Dispatchers.Main) {
                alertDialogprogress?.hide()
                if (!bitmapLIST?.isEmpty()!!) {
                    binding?.selectedImagesTextview?.visibility = View.VISIBLE
                    binding?.selectedImagesTextview?.text = bitmapLIST?.size.toString() + " image(s) selected."
                }
                }
        }// closing if block
        return@withContext bitmapLIST!!

  }

    fun createAlertdialogue(view:View):androidx.appcompat.app.AlertDialog{
        var alertbuilder2 = MaterialAlertDialogBuilder(requireContext(),R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
        alertbuilder2.setView(view)
        alertbuilder2.setCancelable(false)

        return alertbuilder2.create()
    }

    fun splitImgQaulity(quality:String):Int{
        if(!quality.equals("null")) {
            Log.d("sdsds",quality)
            var splstringarr = quality.replace(Regex("[ %]"), "")
            Log.d("48hfcv","${splstringarr.toInt()}klklk")
            return splstringarr.toInt()
        }else{
            Log.d("e4eg34g",100.toString())
            return 100
        }
    }

    fun drag_dropRecyclerVieww(){

       var itemTouchHelper = object:ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,0){
          override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean
          {
              val adapter = recyclerView.adapter as MyAdapter_ImagesToPDF
              val from = viewHolder.adapterPosition
              val to = target.adapterPosition
              // 2. Update the backing model. Custom implementation in
              //    MainRecyclerViewAdapter. You need to implement
              //    reordering of the backing model inside the method.
              adapter.movetoItem(from,to)
              adapter.notifyItemMoved(from,to)
              return  true
          }

          override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int)
          {
          }

      }

    }


    // getting result of camera picture bitmap
    var launcher = registerForActivityResult(ActivityResultContracts.TakePicturePreview(),
        ActivityResultCallback {

            var thumbnailbitmap:Bitmap?=null
            if (it != null) {

                thumbnailbitmap=  Bitmap.createScaledBitmap(it,1000,1000,false)
//                thumbnailbitmap= ThumbnailUtils.extractThumbnail(it,300,300)
                bitmapLIST?.add(thumbnailbitmap)
            }
            if (!bitmapLIST?.isEmpty()!!) {
                binding?.selectedImagesTextview?.visibility = View.VISIBLE
                binding?.selectedImagesTextview?.text = bitmapLIST?.size.toString() + " image(s) selected."

                setup_updateRecyclerView(bitmapLIST!!)
            }
            //   PdfOperations(requireActivity())?.createPdf(view,requireActivity(),bitmapLIST!!)
        })

    // getting result from filemanager
    var launcher1 = registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments(),
        ActivityResultCallback {
            var job= CoroutineScope(Dispatchers.Main).async {
                decodeUritoBitmap(it as ArrayList<Uri>)

            }
        })


    //custom Contracts for creating pdf document
    var contract = object:ActivityResultContract<Intent,Uri>(){
        override fun createIntent(context: Context, input: Intent?): Intent {
            return input!!
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri {
        var uri = intent?.data
            if(uri!=null)
            {
                return uri
            }
            else{
                return Uri.EMPTY
            }
        }
    }
    @SuppressLint("Range")
    var launcher4 = registerForActivityResult(contract, ActivityResultCallback {

        if(it!=null) {
            outputStream = activity?.contentResolver?.openOutputStream(it)!!

            Log.d("of66jd", it.path.toString())
            Log.d("34g93jg", outputStream.toString())

            if (outputStream != null) {
                //creating pdf acc to quality in this coroutine scope
                CoroutineScope(Dispatchers.IO).launch {
                    //showing pleasewait dialogue
                    withContext(Dispatchers.Main) {
                        importingDailogTextview = alertDialogprogress?.findViewById<TextView>(R.id.importingtextview)
                        importingnumberDailogText = alertDialogprogress?.findViewById<TextView>(R.id.importedNumberTextview)
                        //these dailog textview had importing and imprting number texts
                        importingDailogTextview?.text = "please wait..."
                        importingnumberDailogText?.visibility = View.GONE
                        alertDialogprogress?.show()
                    }

                    PdfOperations(requireActivity())?.createPdf(bitmapLIST!!, "pdfName", imgQuality!!, outputStream!!)

                    //hide please wait dialogue
                    withContext(Dispatchers.Main) {
                        alertDialogprogress?.hide()

                        //if Comingfrom_MergeFrag is not null then return uri to merge frag with new created pdf
                       if(Comingfrom_MergeFrag.equals(PDFProp.COMING_FROM_MERGEFRAG)) {

                           var title = getpdfNAME(it)
                           mergeSelectedPdfList?.add(Items_pdfs(title,"0",it))
                           var bundle = Bundle()
//                                 bundle.putParcelable(PDFProp.PDF_APPENDED_URI,it)
//                                 bundle.putString(PDFProp.PDF_TITLE,"pdfTitle")
//                                 bundle.putString(PDFProp.PDF_SIZE,"0")
                                   bundle.putParcelableArrayList(PDFProp.MERGE_SELECTED_LIST,mergeSelectedPdfList)
                                 var mergepdfsFragment = MergePdfs_Fragment()
                                 mergepdfsFragment.arguments = bundle
                                 activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView,mergepdfsFragment)?.commit()

                         }
                    }

                }
            }//if block for output stream null or not
        }
    })

    @SuppressLint("Range")
    fun getpdfNAME(uri:Uri):String{
       var name = "No name"
        val cursor = activity?.contentResolver?.query(uri,null,null,null,null)
     if(cursor!=null)
     {
         cursor.let {
             if(it.moveToFirst()) {
                 name = it.getString(it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
             }
         }
     }
        return name
    }



    }
