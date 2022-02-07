package com.example.pdf_reader_viewer.fragments

import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pdf_reader_viewer.R
import com.example.pdf_reader_viewer.RecylerViewClasses.MyAdapter_ImagesToPDF
import com.example.pdf_reader_viewer.UtilClasses.ConversionandUtilsClass
import com.example.pdf_reader_viewer.UtilClasses.PdfOperations
import com.example.pdf_reader_viewer.databinding.ImageToPdfFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList


class ImageTo_Pdf_Fragment : Fragment() {

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

/*binding?.selectedImagesTextview?.setOnClickListener {
  //  Log.d("3t3tg3t3",posList?.get(0).toString())
    bitmapLIST?.forEach { Log.d("44ffyuyy7",it.toString())}
    var count=0
    fromposList?.forEach {
        Collections.swap(bitmapLIST!!,it,toposlist?.get(count)!!)
        count++
    }
    bitmapLIST?.forEach { Log.d("44ffyuyy7",it.toString())}

}*/



        recyclerView = view.findViewById(R.id.imagesRecylerView)
       // itemTouchHelper.attachToRecyclerView(recyclerView)

        bottomdialogue = BottomSheetDialog(requireContext(), R.style.Theme_Design_BottomSheetDialog)
        bottomdialogue?.setContentView(viewbottomsheet!!)
        bottomsheetButtonClickAction(bottomdialogue!!)


        binding?.selectImagesbutton?.setOnClickListener {
            bottomdialogue?.show()
        }

        var view22 = LayoutInflater.from(requireContext()).inflate(R.layout.customprogressdialogue, activity?.findViewById<ViewGroup>(R.id.content), false)
        alertDialogprogress=createAlertdialogue(view22)



        alertDialog = createAndClickDialog(bitmapLIST!!)
        binding?.createPDFbutton?.setOnClickListener {
            alertDialog?.show()
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

        var alertdialogBuilder = MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)

        val viewGroup = activity?.findViewById<ViewGroup>(R.id.content)
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.custom_quality_dialogue, viewGroup)

        alertdialogBuilder.setView(view)
            .setPositiveButton("create", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
               // bitmapLIST?.forEach { Log.d("44fff44ff",it.toString()) }


                if (!bitmapList?.isEmpty()!!) {
                  //  Log.d("3r3fg3",from.toString()+" "+to.toString())
                   // Collections.swap(bitmapLIST,from!!,to!!)

                    //getting radiogroup abnd buitton to get quality text
                    radioGroup = alertDialog?.findViewById<RadioGroup>(R.id.radioGroupDialogue)
                    var radbtnID = radioGroup?.checkedRadioButtonId
                    var radioButton = radioGroup?.findViewById<RadioButton>(radbtnID!!)

                    Toast.makeText(requireContext(), radioButton?.text.toString(), Toast.LENGTH_SHORT).show()
                    //split quality text of radiobutton
                    var imgQuality=splitImgQaulity(radioButton?.text.toString())
                    //creating pdf acc to quality
                    CoroutineScope(Dispatchers.IO).launch {
                        withContext(Dispatchers.Main) {
                            importingDailogTextview = alertDialogprogress?.findViewById<TextView>(R.id.importingtextview)
                            importingnumberDailogText = alertDialogprogress?.findViewById<TextView>(R.id.importedNumberTextview)
                         //these dailog textview had importing and imprting number texts
                            importingDailogTextview?.text="please wait..."
                            importingnumberDailogText?.visibility=View.GONE
                            alertDialogprogress?.show()}

                        PdfOperations(requireActivity())?.createPdf(view, requireActivity(), bitmapList, imgQuality)

                        withContext(Dispatchers.Main) {alertDialogprogress?.hide()
                        }

                    }
                  } else {
                      Toast.makeText(requireContext(), "No images selected", Toast.LENGTH_SHORT)
                          .show()
                      }
            }
        })
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
            return splstringarr[0].toString().toInt()
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

    }
