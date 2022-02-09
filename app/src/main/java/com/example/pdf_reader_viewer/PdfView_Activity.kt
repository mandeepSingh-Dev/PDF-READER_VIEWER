package com.example.pdf_reader_viewer

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.TextView
import android.widget.Toast
import com.example.pdf_reader_viewer.UtilClasses.PDFProp
import com.example.pdf_reader_viewer.databinding.ActivityPdfViewBinding

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


}