package com.example.pdf_reader_viewer

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.util.Log
import android.view.View
import com.google.android.material.internal.ViewUtils.getContentView
import java.io.OutputStream


class PDFOperationNATIVE(activity:Activity)
{
    var activity = activity
    var pagebitmapList:ArrayList<Bitmap>?=null
    var startpage = 0
    var endpage = 1
    var splitAtPage = 0

    fun splitPdf(uri: Uri, outputStream: OutputStream)
    {
        pagebitmapList =ArrayList()
        var parcelFileDescriptor =  activity?.contentResolver?.openFileDescriptor(uri,"r")
       var pdfRender = PdfRenderer(parcelFileDescriptor!!)

        var pdfLength = pdfRender.pageCount
        var document = PdfDocument()
                                    //Exampless   -----------------------------------
        startpage = startpage-1   //in reallife it is 5 in index viewof point it is 4
        endpage = endpage-1    //in reallife it is 9 in index viewof point it is 10
        splitAtPage = splitAtPage-1   //in reallife it is 9 in index viewof point it is 10
        Log.d("pageNUmebr",startpage.toString()+endpage.toString()+splitAtPage.toString())
        // create a page description
        if((startpage>0) && endpage>0) {
            Log.d("pageNUmebr",startpage.toString()+endpage.toString()+splitAtPage.toString())
            Log.d("1st_condition","1st_condition")
            for (i in startpage..endpage) {
                var renderedpage = pdfRender.openPage(i)
                val pageInfo = PageInfo.Builder(renderedpage.width, renderedpage.height, 1).create()
                Log.d(
                    "efh8hfsizepage",
                    renderedpage.width.toString() + "fdfd" + renderedpage.height
                )
                //getting bitmap from pdf
                var bitmap = Bitmap.createBitmap(
                    renderedpage.width - 20,
                    renderedpage.height - 20,
                    Bitmap.Config.ARGB_8888
                )
                renderedpage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                val page = document.startPage(pageInfo)
                var canvas = page.canvas

                var divwidth = renderedpage.width / 10
                var divheight = renderedpage.height / 10

                // canvas.drawBitmap(bitmap,(renderedpage.width/60).toFloat(),(renderedpage.height/78).toFloat(), Paint())
                canvas.drawBitmap(
                    bitmap,
                    (renderedpage.width / divwidth).toFloat(),
                    (renderedpage.height / divheight).toFloat(),
                    Paint()
                )

                /* val content: View? = activity.findViewById(android.R.id.content);
             content?.draw(canvas)*/
                document.finishPage(page)
                renderedpage.close()
            }
        }
        else if(startpage<=0 && endpage<=0 &&splitAtPage>0) {
            Log.d("pageNUmebr",startpage.toString()+endpage.toString()+splitAtPage.toString())
            Log.d("2st_condition","2st_condition")
            for (i in splitAtPage..pdfLength-1) {
                var renderedpage = pdfRender.openPage(i)
                val pageInfo = PageInfo.Builder(renderedpage.width, renderedpage.height, 1).create()
                Log.d(
                    "efh8hfsizepage",
                    renderedpage.width.toString() + "fdfd" + renderedpage.height
                )
                //getting bitmap from pdf
                var bitmap = Bitmap.createBitmap(
                    renderedpage.width - 20,
                    renderedpage.height - 20,
                    Bitmap.Config.ARGB_8888
                )
                renderedpage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                val page = document.startPage(pageInfo)
                var canvas = page.canvas

                var divwidth = renderedpage.width / 10
                var divheight = renderedpage.height / 10

                // canvas.drawBitmap(bitmap,(renderedpage.width/60).toFloat(),(renderedpage.height/78).toFloat(), Paint())
                canvas.drawBitmap(
                    bitmap,
                    (renderedpage.width / divwidth).toFloat(),
                    (renderedpage.height / divheight).toFloat(),
                    Paint()
                )

                /* val content: View? = activity.findViewById(android.R.id.content);
             content?.draw(canvas)*/
                document.finishPage(page)
                renderedpage.close()
            }
        }
        else{
            Log.d("pageNUmebr",startpage.toString()+endpage.toString()+splitAtPage.toString())
            Log.d("3st_condition","3st_condition")
            for (i in 0..pdfLength-1) {
                var renderedpage = pdfRender.openPage(i)
                val pageInfo = PageInfo.Builder(renderedpage.width, renderedpage.height, 1).create()
                Log.d(
                    "efh8hfsizepage",
                    renderedpage.width.toString() + "fdfd" + renderedpage.height
                )
                //getting bitmap from pdf
                var bitmap = Bitmap.createBitmap(
                    renderedpage.width - 20,
                    renderedpage.height - 20,
                    Bitmap.Config.ARGB_8888
                )
                renderedpage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                val page = document.startPage(pageInfo)
                var canvas = page.canvas

                var divwidth = renderedpage.width / 10
                var divheight = renderedpage.height / 10

                // canvas.drawBitmap(bitmap,(renderedpage.width/60).toFloat(),(renderedpage.height/78).toFloat(), Paint())
                canvas.drawBitmap(
                    bitmap,
                    (renderedpage.width / divwidth).toFloat(),
                    (renderedpage.height / divheight).toFloat(),
                    Paint()
                )

                /* val content: View? = activity.findViewById(android.R.id.content);
             content?.draw(canvas)*/
                document.finishPage(page)
                renderedpage.close()
            }
        }
        document.writeTo(outputStream)
        document.close()

    }

    fun setStartPage(startpage:Int)
    {
        this.startpage=startpage
    }
    fun setEndPage(endpage:Int)
    {
        this.endpage=endpage
    }

    fun setSplitAtPage1(splitAtPage:Int)
    {
        this.splitAtPage=splitAtPage
    }

}