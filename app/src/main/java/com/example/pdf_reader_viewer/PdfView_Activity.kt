package com.example.pdf_reader_viewer

import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.TextView
import android.widget.Toast
import com.example.pdf_reader_viewer.UtilClasses.PDFProp
import com.example.pdf_reader_viewer.databinding.ActivityPdfViewBinding
import com.github.barteksc.pdfviewer.PDFView
import java.io.File
import java.io.FileOutputStream

class PdfView_Activity : AppCompatActivity()
{
    var binding:ActivityPdfViewBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPdfViewBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)


        var pageNumber: TextView =findViewById(R.id.pageNumber)

        var pdfuri_String=intent.extras?.getString(PDFProp.PDF_APPENDED_URI)
        var pdftitle=intent.extras?.getString(PDFProp.PDF_TITLE)
        var uri= Uri.parse(pdfuri_String)

        Toast.makeText(this,pdfuri_String,Toast.LENGTH_LONG).show()

       // val pdfView = findViewById<PDFView>(R.id.pdfView)
        binding?.pdfTitle?.setText(pdftitle)

       // var intentUri=intent.data
        binding?.pdfView?.fromUri(uri)?.onTap { e ->
            e.action = MotionEvent.ACTION_SCROLL
            true
        }?.spacing(0)?.onPageChange { page, pageCount ->
            //pageNumber.setText(page.toString())
            binding?.pageNumber?.setText((page+1).toString())

        }?.enableSwipe(true)?.swipeHorizontal(false)?.enableAnnotationRendering(false)
            ?.enableDoubletap(true)?.enableAntialiasing(true)?.defaultPage(0)?.spacing(10)?.load()

    }
    fun bitmapTopdf_usingnativePdfDocument(){
        var bitmap= BitmapFactory.decodeResource(resources,R.drawable.planewallpaper)
        var file= File(Environment.DIRECTORY_DOCUMENTS)
        //  var byteArrayOutputStream=ByteArrayOutputStream()
        //   bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
        //  byteArrayOutputStream.toByteArray()

        /*  pdfView.fromBytes(byteArrayOutputStream.toByteArray()).onTap {
              it.action=MotionEvent.ACTION_SCROLL
              true
          }.enableSwipe(true).swipeHorizontal(true).enableAnnotationRendering(false)
              .enableDoubletap(true).enableAntialiasing(true).defaultPage(0).spacing(10).load()
*/
        val pdfdocument = PdfDocument()
        Log.d("efjsdcv",bitmap.width.toString())
        val pageInfo: PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page: PdfDocument.Page = pdfdocument.startPage(pageInfo)
        page.getCanvas().drawBitmap(bitmap,0f,0f, null);
        pdfdocument.finishPage(page);

        try {
            var stringfile=filesDir.toString()+"/jdkcvd.pdf"
            /**to save in app specific folder*/
            pdfdocument.writeTo(FileOutputStream(filesDir.toString()+"/FirstPdfYOYOYOYOYO.pdf"))
            /**to save in phone storage*/
            pdfdocument.writeTo(FileOutputStream("sdcard/FirstPdfYOYOYOYOYO.pdf"))


            Log.d("fef3er3",filesDir.toString())
        }catch (e:Exception){
            Log.d("4trgdfvgdfsd",e.cause.toString()+e.message)
        }
        pdfdocument.close()

    }
}