package com.example.pdf_reader_viewer

import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import android.widget.Toast
import com.github.barteksc.pdfviewer.PDFView
import java.io.File
import java.io.FileOutputStream

class PdfView_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_view)


        var pageNumber: TextView =findViewById(R.id.pageNumber)

        var uri_String=intent.extras?.getString("PDF_URI")
        var uri= Uri.parse(uri_String)
        Toast.makeText(this,uri_String,Toast.LENGTH_LONG).show()


        val pdfView = findViewById<PDFView>(R.id.pdfView)

        var intentUri=intent.data
        pdfView.fromUri(uri).onTap { e ->
            e.action = MotionEvent.ACTION_SCROLL
            true
        }.spacing(0).onPageChange { page, pageCount ->
            pageNumber.setText(page.toString())

        }.enableSwipe(true).swipeHorizontal(true).enableAnnotationRendering(false)
            .enableDoubletap(true).enableAntialiasing(true).defaultPage(0).spacing(10).load()


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

        Log.d("54tger","dcsd")
/*try {
    bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

    val canvas: Canvas = page.canvas

    val paint = Paint()
    paint.setColor(Color.parseColor("#ffffff"))
    canvas.drawPaint(paint)

    paint.setColor(Color.BLUE);
    canvas.drawBitmap(bitmap, 0f, 0f, null);
    document.finishPage(page);

    document.writeTo(FileOutputStream(filesDir));
    document.close()
}catch (e:Exception){}*/


    }
}