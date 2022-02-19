package com.example.pdf_reader_viewer.fragments

import android.R.attr.textColor
import android.R.attr.textSize
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import com.example.pdf_reader_viewer.UtilClasses.PdfOperations
import com.example.pdf_reader_viewer.databinding.TextToPdfFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TextTo_pdf_Fragment : Fragment() {

    var binding:TextToPdfFragmentBinding?=null
    var bitmapList:ArrayList<Bitmap>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bitmapList = ArrayList()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = TextToPdfFragmentBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*var text = "difhuedfdhfuwgfvhugvvgudrdifhuedfdhfuwgfvhugvvguddifhuedfdhfuwgfvhugvvgud"
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.setTextSize(20f)
        paint.setColor(Color.BLACK)
        paint.setTextAlign(Paint.Align.LEFT)

        val baseline: Float = -paint.ascent() // ascent() is negative

        val image = Bitmap.createBitmap((paint.measureText(text) + 0.5).toInt(),(baseline + paint.descent() + 0.5).toInt(),Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        canvas.drawText(text, 0f, baseline, paint)*/




        binding?.createpdfffTextTOpdf?.setOnClickListener {

            var intent= Intent(Intent.ACTION_CREATE_DOCUMENT)
            intent.type="application/pdf"
            intent.putExtra(Intent.EXTRA_TITLE, "Text_To_Pdf")
            launcher4.launch(intent)

        }

    }
    //practise for get bitmap from view
    fun getBitmapFromView(view: View): Bitmap? {
        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null) //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas)
        else  //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        return returnedBitmap
    }
    //custom Contracts for creating pdf document
    var contract = object: ActivityResultContract<Intent, Uri>(){
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
    var launcher4 = registerForActivityResult(contract, ActivityResultCallback {

        if(it!=null) {
          var outputStream = activity?.contentResolver?.openOutputStream(it)!!

            Log.d("of66jd", it.path.toString())
            Log.d("34g93jg", outputStream.toString())

            if (outputStream != null) {
                //creating pdf acc to quality in this coroutine scope
                CoroutineScope(Dispatchers.IO).launch {
                    //showing pleasewait dialogue
                    //creating bitmap from view
                    withContext(Dispatchers.Main) {
                        var bitmap = getBitmapFromView(binding?.textToPdfTextview!!)
                        bitmap?.let { bitmapList?.add(it) }
                        binding?.textpdfProgressbar?.visibility = View.GONE
                        binding?.pleasewaitTextview?.visibility = View.GONE
                    }
                    //creating bitmap from text

                    PdfOperations(requireActivity())?.createPdf(bitmapList!!, "pdfName", 0, outputStream!!)

                    //hide please wait dialogue
                    withContext(Dispatchers.Main) {
                        //alertDialogprogress?.hide()
                        binding?.textpdfProgressbar?.visibility = View.GONE
                        binding?.pleasewaitTextview?.visibility = View.GONE

                    }

                }
            }//if block for output stream null or not
        }
    })

    fun getBitmapfromtext(text:String):Bitmap{

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.setTextSize(20f)
        paint.setColor(Color.WHITE)
        paint.setTextAlign(Paint.Align.LEFT)

        val baseline: Float = -paint.ascent() // ascent() is negative

        val image = Bitmap.createBitmap((paint.measureText(text) + 0.5).toInt(),(baseline + paint.descent() + 0.5).toInt(),Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        canvas.drawText(text, 0f, baseline, paint)


        activity?.runOnUiThread {
            binding?.showbitmapImageview?.setImageBitmap(image)
        }
        return  image
    }


}