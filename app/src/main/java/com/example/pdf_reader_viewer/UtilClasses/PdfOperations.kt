package com.example.pdf_reader_viewer.UtilClasses

import android.app.Activity
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.graphics.BitmapCompat
import com.example.pdf_reader_viewer.PDFOperationNATIVE
import com.example.pdf_reader_viewer.R
import com.example.pdf_reader_viewer.RecylerViewClasses.Items_pdfs
import com.google.android.material.snackbar.Snackbar
import com.tom_roush.pdfbox.multipdf.PDFMergerUtility
import com.tom_roush.pdfbox.multipdf.Splitter
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPage
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.encryption.AccessPermission
import com.tom_roush.pdfbox.pdmodel.encryption.StandardProtectionPolicy
import com.tom_roush.pdfbox.pdmodel.font.PDFont
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font
import com.tom_roush.pdfbox.pdmodel.graphics.image.JPEGFactory
import com.tom_roush.pdfbox.pdmodel.graphics.image.LosslessFactory
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject
import com.tom_roush.pdfbox.rendering.PDFRenderer
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.io.*
import java.security.Security
import java.util.*
import kotlin.collections.ArrayList


class PdfOperations(activity:Activity) {
    var pageImage: Bitmap? = null
    var parcelFileDescriptor: ParcelFileDescriptor? = null
    var startPage: String? = null
    var endPage: String? = null
    var splitAtpage:String?=null
    var activity = activity

    fun createPdf(v: View?, activity: Activity, bitmap: Bitmap) {


        val document = PDDocument()
        for (i in 0..3) {
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
                /*  contentStream.addRect(5f, 500f, 100f, 100f)
            contentStream.setNonStrokingColor(0, 255, 125)*/
                contentStream.fill()

                // Draw the falcon base image
                val ximage = JPEGFactory.createFromStream(document, inn)
                contentStream.drawImage(ximage, 20f, 20f)

                // Draw the red overlay image
                val alphaImage = BitmapFactory.decodeStream(alpha)
                //TODO we can tell user that which quality he wants
                alphaImage.compress(Bitmap.CompressFormat.JPEG, 100, null)
                val alphaXimage: PDImageXObject
                if (i == 1) {
                    alphaXimage = LosslessFactory.createFromImage(document, alphaImage)
                } else {
                    alphaXimage = LosslessFactory.createFromImage(document, bitmap)
                }
                /*  alphaXimage.width=ViewGroup.LayoutParams.MATCH_PARENT
            alphaXimage.height=ViewGroup.LayoutParams.MATCH_PARENT
*/
                contentStream.drawImage(alphaXimage, 20f, 20f, 500f, 700f)


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

                /* var jfsd=PDStream(document,COSName.FLATE_DECODE)
            jfsd.addCompression()
*/
                document.save(stream)

                // tv.setText("Successfully wrote PDF to $path")
            } catch (e: IOException) {
                Log.e("PdfBox-Android-Sample", "Exception thrown while creating PDF", e)
            }
        }
        document.close()
    }

    //this method is for multiple images/bitmaps
    fun createPdf( imgList: ArrayList<Bitmap>,pdfName:String, quality: Int,outStream: OutputStream) {

        val document = PDDocument()
        val appendeddocument=PDDocument()
//        var stream = createPDFFolder( PDFProp.CREATEDPDF_FOLDER, pdfName, System.currentTimeMillis())
          //var fos=stream as FileOutputStream

        /*  activity?.runOnUiThread {
          AlertDialog.Builder(activity?.applicationContext,  R.style.Theme_AppCompat_Dialog_Alert).setView(v).create().show()
            v?.visibility=View.VISIBLE }*/

        imgList.forEach {
            val page = PDPage()
            document.addPage(page)
           // fos.flush()

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
               /* contentStream.beginText()
                contentStream.setNonStrokingColor(15, 38, 192)
                contentStream.setFont(font, 12f)
                contentStream.newLineAtOffset(100f, 700f)
                contentStream.showText("Hello World")
                contentStream.endText()*/

                // Load in the images
              /*  val inn: InputStream = activity?.assets?.open("falcon.jpg")!!
                val alpha: InputStream = activity?.assets?.open("trans.png")!!*/

                // Draw a green rectangle
                /*  contentStream.addRect(5f, 500f, 100f, 100f)
            contentStream.setNonStrokingColor(0, 255, 125)*/
                contentStream.fill()

                // Draw the falcon base image
               // val ximage = JPEGFactory.createFromStream(document, inn)
              //  contentStream.drawImage(ximage, 20f, 20f)

                // Draw the red overlay image
              //  val alphaImage = BitmapFactory.decodeStream(alpha)

                //TODO we can tell user that which quality he wants
                // alphaImage.compress(Bitmap.CompressFormat.JPEG,30,null)
                //compressbitmap
                var compressesBitmap = compressBitmap(it, quality)
                val alphaXimage: PDImageXObject

                //alphaXimage = LosslessFactory.createFromImage(document, alphaImage)
                alphaXimage = LosslessFactory.createFromImage(document, compressesBitmap)

                /*  alphaXimage.width=ViewGroup.LayoutParams.MATCH_PARENT
        alphaXimage.height=ViewGroup.LayoutParams.MATCH_PARENT
*/
              //  contentStream.drawImage(alphaXimage, 20f, 20f, 500f, 700f)
                contentStream.drawImage(alphaXimage, 50f, 85f, 500f, 600f)


                // Make sure that the content stream is closed:
                contentStream.close()

//                // Save the final pdf document to a file
//                var foldername = PDFProp.CREATEDPDF_FOLDER
//                var file = File("sdcard/" + foldername + "/" + foldername)
//                if (!file.exists()) {
//                    file.mkdir()
//                }
//                var stream = FileOutputStream(file.absolutePath + "/jkjkj.pdf")
//
//                //  val path: String = /*root.getAbsolutePath().toString() + "/Created.pdf"*/ activity?.applicationContext?.filesDir?.absolutePath.toString()+"/created.pdf"
//                val path: String = file.absolutePath.toString()
//                // Log.d("37fgewf",file.absolutePath.toString())
//
//               // document.save(stream)

                // tv.setText("Successfully wrote PDF to $path")
            } catch (e: IOException) {
                Log.e("PdfBox-Android-Sample", "Exception thrown while creating PDF", e)
            }
        }
/*
        activity?.runOnUiThread { v?.visibility=View.GONE}
*/     // appendeddocument.save(fos)
        Log.d("38f3hfg4",document.pages.count.toString() )
      //  document.save(stream)
        document.save(outStream)

        document.close()
        //stream.close()
        outStream.close()

    }

    /**we create our own this custommergePdf Method
     * Because thirdParty pdf library doesnt fetch text from pdf only fetch images,
     * so we use combine third party and android native Pdfdocument library which fetch
     * text from pdf as well */
   suspend fun myCustomNativeMergePdf(pdflist: ArrayList<Items_pdfs>,pdfNAME: String,password:String,outputStream: OutputStream)= withContext(Dispatchers.IO){

        var pdDocument= PDDocument()
        var parcelFileDescriptor:ParcelFileDescriptor
       // var stream2 = createPDFFolder( PDFProp.MERGEPDF_FOLDER, pdfNAME, System.currentTimeMillis())


        pdflist.forEach {

            parcelFileDescriptor = activity?.contentResolver?.openFileDescriptor(it.appendeduri!!, "r")!!
            var renderer= PdfRenderer(parcelFileDescriptor!!)
            Log.d("48y5gh",renderer.pageCount.toString())

            var pdfLength=renderer.pageCount
            for(i in 0..pdfLength-1){

                var page= renderer.openPage(i)

                var page2=PDPage()

                pdDocument.addPage(page2)
                var contentStream=PDPageContentStream(pdDocument,page2)
                contentStream.fill()

                var bitmap= Bitmap.createBitmap(400,400, Bitmap.Config.ARGB_8888)
                page.render(bitmap,null,null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

               var imageXObject = LosslessFactory.createFromImage(pdDocument,bitmap)
                contentStream.drawImage(imageXObject, 50f, 85f, 500f, 600f)
               // contentStream.drawImage(imageXObject, 50f, 85f, page.width.toFloat(), page.height.toFloat())

                contentStream.close()
                page.close()


                //  binding?.lllllll?.setImageBitmap(bitmap)


                /* val pageInfo = PageInfo.Builder(350, 600, 1).create()
                 var page = document.startPage(pageInfo)*/
            }
              renderer.close()



            /*   var appendUri=activity.contentResolver.openInputStream(it.appendeduri!!)
               var pdf=PDDocument.load(appendUri)*/
            //  PdfRenderer()

        }
        Log.d("38f3hfg4",pdDocument.pages.count.toString() )

        //if password null then do nothing else ENCRYPT this pdDocument Merged pdf
        if(password.equals("NULL")){
            pdDocument.save(outputStream)
            pdDocument.close()
            outputStream.close()}
        else{
            var pdDocumentENCRPTD = enryptPdf(pdDocument,password,outputStream)
            pdDocumentENCRPTD.save(outputStream)
            pdDocumentENCRPTD.close()
            outputStream.close()
        }



    }

    fun stripText(v: View?, activity: Activity) {
        var parsedText: String? = null
        var document: PDDocument? = null
        try {
            document = PDDocument.load(activity?.assets.open("Hello.pdf"))
        } catch (e: IOException) {
            Log.e("PdfBox-Android-Sample", "Exception thrown while loading document to strip", e)
        }
        try {
            val pdfStripper = PDFTextStripper()
            pdfStripper.startPage = 0
            pdfStripper.endPage = 1
            parsedText = "Parsed text: " + pdfStripper.getText(document)
            Log.d("48tyfhigvng", parsedText!!)
        } catch (e: IOException) {
            Log.e("PdfBox-Android-Sample", "Exception thrown while stripping text", e)
        } finally {
            try {
                document?.close()
            } catch (e: IOException) {
                Log.e("PdfBox-Android-Sample", "Exception thrown while closing document", e)
            }
        }

    }

   suspend fun splittingPdf( uri: Uri, numberList: List<String>,outputStream: OutputStream)= withContext(Dispatchers.IO) {

       // var outstream2 = createPDFFolder( PDFProp.SPLITPDF_FOLDER, pdfNAME, System.currentTimeMillis())

        parcelFileDescriptor = activity?.contentResolver?.openFileDescriptor(uri, "r")!!
        val fileDescriptor: FileDescriptor = parcelFileDescriptor?.fileDescriptor!!
        var inputStraem = FileInputStream(fileDescriptor)

       // var inputStraem=activity.contentResolver.openInputStream(uri)
        var document = PDDocument.load(inputStraem)

        //Instantiating Splitter class
        var splitter = Splitter();
        //  var listString = formattingof_Pagenumber(atPage)
try {
    if (numberList.size > 1) {
        startPage = numberList.get(0)
        endPage = numberList.get(1)
        Log.d("ifg7egjdf", startPage + endPage + "sdhsj")
    } else if (numberList.size == 1) {
        startPage = numberList.get(0)
        Log.d("ifg7egjdf", startPage!!)
    } else {
        //  return bool
    }

    //splitting the pages of a PDF document
    if (startPage != null) {
        Log.d("fy7fgsjfstartPage", startPage!!)
        splitter.setStartPage(startPage!!.toInt())
    }
    if (endPage != null) {
        Log.d("fy7fgsjfendpage", endPage!!)
        splitter.setEndPage(endPage!!.toInt())
    }

    if (startPage != null && endPage == null) {
        splitter.setSplitAtPage(startPage!!.toInt().minus(1)) //here we give .minus(1) because when se split at given pagenumber then splitter split pagenumber single page then further pages gets split.
        Log.d("fy7fgsjfsplitAtPage", startPage!!)
    }
    //splitter.setSplitAtPage(20)
    var Pages = splitter.split(document);

    Log.d("eifjkfds", Pages.size.toString())

    //Creating an iterator

    var iterator = Pages.listIterator();
    //creating file path i.e sdcard/...
    var file = createFilePath()

    var pd = PDDocument()
    var pdd = PDDocument()
    var fileStr = file.toString()
    //Saving each page as an individual document
    var i = 1;
    while (iterator.hasNext()) {

        pd = iterator.next();
        PDFMergerUtility().appendDocument(pdd, pd)  //here we append every splited page to one pdd PDDdocument
        // pd.save("sdcard/"+pdfNAME + i++ + ".pdf");

    }

    //  pdd.save(fileStr + "/" + pdfNAME + i++ + ".pdf")
    pdd.save(outputStream)

    Log.d("388fh3ev", "Multiple PDF’s created")
   // pdd.close()
    document.close();
    outputStream.close()
}catch (e:Exception) {
    withContext(Dispatchers.Main) {
        Toast.makeText(activity.applicationContext, e.message, Toast.LENGTH_LONG).show()
              }
}
    }


    fun mergePdfs(activity: Activity) {
        //Instantiating PDFMergerUtility class
        var PDFmerger = PDFMergerUtility();

        //Setting the destination file
        PDFmerger.setDestinationFileName("sdcard/Merged.pdf");

        //adding the source files
        PDFmerger.addSource(activity?.assets.open("Hello.pdf"));
        PDFmerger.addSource(activity?.assets.open("Hello.pdf"));

        //Merging the two documents
        PDFmerger.mergeDocuments(true);
        System.out.println("Documents merged");
    }
    fun mergePdfs( pdflist: ArrayList<Items_pdfs>, mergedPdfName: String) {
        //Instantiating PDFMergerUtility class
        try {
            var pdfmerger = PDFMergerUtility();

            //Setting the destination file
            var file = File("sdcard/mergedPdfNameee")
            if (!file.exists()) {
                file.mkdir()
            }
            var outputStream = FileOutputStream(file.absolutePath + "/mergedPdfNee.pdf")
            pdfmerger.destinationStream = outputStream/*"sdcard/mergedPdfName.pdf"*/;
            Toast.makeText(activity?.applicationContext, pdflist.size.toString(), Toast.LENGTH_SHORT).show()

            //getting and adding the source files
            pdflist.forEach {
                //getting inputStream from uri using ParcelFileDFescripter because simple uri not able use with PDFMerger.addSource
                parcelFileDescriptor = activity?.contentResolver?.openFileDescriptor(it.appendeduri!!, "r")!!
                val fileDescriptor: FileDescriptor = parcelFileDescriptor?.fileDescriptor!!
                var inputStraem = FileInputStream(fileDescriptor)

                Log.d("4t4g4e", inputStraem.toString())

              var newINPSTRM= activity.contentResolver.openInputStream(it.appendeduri!!)
                pdfmerger.addSource(newINPSTRM)
                Log.d("3g3efgwe", "fdfd")
            }
            /* PDFmerger.addSource(activity?.assets.open("Hello.pdf"));
        PDFmerger.addSource(activity?.assets.open("Hello.pdf"));*/

            //Merging the two documents
            pdfmerger.mergeDocuments(true);
           // System.out.println("Documents merged");
            parcelFileDescriptor?.close()
            //  pdflist.removeAll(pdflist)
        } catch (e: Exception) {
            Toast.makeText(
                activity?.applicationContext,
                e.cause.toString() + e.message + e.stackTrace,
                Toast.LENGTH_LONG
            ).show()
        }

    }

    fun renderFile(v: View?, activity: Activity): Bitmap {
        // Render the page and save it to an image file
        try {
            // Load in an already created PDF
            val document: PDDocument = PDDocument.load(activity.assets.open("Hello.pdf"))
            // Create a renderer for the document
            val renderer = PDFRenderer(document)
            // Render the image to an RGB Bitmap
            pageImage = renderer.renderImage(0, 1f, Bitmap.Config.RGB_565)

            // Save the render result to an image
            val path: String = "sdcard/readed.pdf"
            val renderFile = File(path)
            val fileOut = FileOutputStream(renderFile)
            pageImage?.compress(Bitmap.CompressFormat.JPEG, 100, fileOut)
            fileOut.close()
            Log.d("347fyh3g3vbn", "Successfully rendered image to $path")
            // Optional: display the render result on screen
            //   displayRenderedImage()
        } catch (e: IOException) {
            Log.e("PdfBox-Android-Sample", "Exception thrown while rendering file", e)
        }
        return pageImage!!
    }

    fun enryptPdf(pdDocument: PDDocument,password: String,outStream:OutputStream):PDDocument
    {
        var spp=getStandardProtectionPolicy_forEncrypPDF(password)
      //pdf is protect
        pdDocument.protect(spp)

        return pdDocument
    }
    fun getStandardProtectionPolicy_forEncrypPDF(password: String):StandardProtectionPolicy{
        val keyLength = 128 // 128 bit is the highest currently supported
        // Limit permissions of those without the password
        val ap = AccessPermission()
        ap.setCanPrint(false)

        // Sets the owner password and user password
        val spp = StandardProtectionPolicy(password, password, ap)

        // Setups up the encryption parameters
        spp.encryptionKeyLength = keyLength
        spp.permissions = ap
        val provider = BouncyCastleProvider()
        Security.addProvider(provider)

        return spp
    }
    fun createEncryptedPdf( uri: Uri, owner_user_password: String,outputStream: OutputStream): Boolean {

        //TODO CHANGE THE ENCRYPTED FILE NAME AS A PDF FILE NAME
         //  var outstream = createPDFFolder(PDFProp.ENCRYPTEDPDF_FOLDER,pdfNAME,System.currentTimeMillis())
       // var inputStream = ConversionandUtilsClass().convertContentUri_toInputStream(actvity, uri)
        var inputstreamm = activity.contentResolver.openInputStream(uri)
        val path: String = "sdcard/crypt.pdf"

       /* val keyLength = 128 // 128 bit is the highest currently supported

        // Limit permissions of those without the password
        val ap = AccessPermission()
        ap.setCanPrint(false)

        // Sets the owner password and user password
        val spp = StandardProtectionPolicy(owner_user_password, owner_user_password, ap)

        // Setups up the encryption parameters
        spp.encryptionKeyLength = keyLength
        spp.permissions = ap
        val provider = BouncyCastleProvider()
        Security.addProvider(provider)*/

        //getting spp for protect/encrypt the pdf
        val spp=getStandardProtectionPolicy_forEncrypPDF(owner_user_password)

        val document = PDDocument()
        val page = PDPage()
        document.addPage(page)
        //to encrypt existing pdf
        var pdddovcument = PDDocument.load(inputstreamm)
        if (!pdddovcument.isEncrypted) {
            pdddovcument.protect(spp)
           // pdddovcument.save(createFilePath().toString() + "/" + owner_user_password + ".pdf")
            pdddovcument.save(outputStream)

            pdddovcument.close()
        } else {
           // Snackbar.make(view, "Already Encrypted", 2000).show()
        }

        var isEncrypted = pdddovcument.isEncrypted

        /*   try {
            val contentStream = PDPageContentStream(document, page)

            // Write Hello World in blue text
            contentStream.beginText()
            contentStream.setNonStrokingColor(15, 38, 192)
            contentStream.setFont(font, 12f)
            contentStream.newLineAtOffset(100f, 700f)
            contentStream.showText("Hello World")
            contentStream.endText()
            contentStream.close()

            // Save the final pdf document to a file
            document.protect(spp) // Apply the protections to the PDF
            document.save(path)
            document.close()
           // tv.setText("Successfully wrote PDF to $path")
        } catch (e: IOException) {
            Log.e("PdfBox-Android-Sample", "Exception thrown while creating PDF for encryption", e)
        }*/
        return isEncrypted
    }

    fun convertContentUri_toInputStream(activity: Activity, appendedUri: Uri): InputStream {
        parcelFileDescriptor = activity?.contentResolver?.openFileDescriptor(appendedUri, "r")!!
        val fileDescriptor: FileDescriptor = parcelFileDescriptor?.fileDescriptor!!
        var inputStraem = FileInputStream(fileDescriptor)

        return inputStraem
    }

    fun formattingof_Pagenumber(atPage: String): List<String> {

        var list: MutableList<String> = mutableListOf()
        //return populated list only if atPage is not empty
        if (!atPage.isEmpty()) {
            var numberList: MutableList<String> = mutableListOf()
            // var pageee=atPage.trim()
            // Log.d("eifnefe",pageee)
            //val pagenumberstr = atPage.replace("\\s", "")
            var pagenumberstr = atPage.replace(" ", "")
            var regex = Regex("[a-zA-Z.+ ]*")
            pagenumberstr = atPage.replace(regex, "")


            Log.d("gfe8ghe", pagenumberstr)
            if (atPage.contains("-")) {
                val finalpagenumberlist = pagenumberstr.split("-")
                finalpagenumberlist.forEach {
                    Log.d("893u3ng", it)
                }
                return finalpagenumberlist
            } else {
                numberList.add(pagenumberstr)
                numberList.forEach {
                    Log.d("ijff8e", it + "\n" + numberList?.size.toString())
                }
                return numberList
            }
            list.addAll(numberList)
        }
        //return populated list only if atPage is not empty
        else {
            return list
        }
    }

    fun createFilePath(): File {
        var file = File("sdcard/My PDF App")
        if (!file.exists()) {
            file.mkdir()
        }
        return file
    }

    //COMPRESSING BITMAP
    fun compressBitmap(bitmap: Bitmap, quality: Int): Bitmap {
        Log.d("3rf", quality.toString())
        var out = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
        var bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()))

        return bitmap
    }


    fun createNativedoc(activity: Activity/*,outp:OutputStream*/) {


      //  var bitmap = BitmapFactory.decodeResource(activity.resources, R.drawable.skywall)
       // var bitmapResult = Bitmap.createScaledBitmap(bitmap, 300, 400, false)


        var document = PdfDocument()

        val pageInfo = PageInfo.Builder(350, 600, 1).create()
        var page = document.startPage(pageInfo)


        var canvas = page.canvas

       // canvas.drawBitmap(bitmapResult, 80f, 80f, null)
        document.finishPage(page)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            var knfd = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL)
            // activity.contentResolver.openOutputStream(knfd)

            //  var khdfs=activity.contentResolver.openInputStream(knfd)
            //  khdfs.toString()+"/dsdws.pdf"

            var uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
            Log.d("dwfds", uri.encodedPath.toString())
            // Log.d("dwfds", outp.toString())

            var values = ContentValues()
            values.put(
                MediaStore.DownloadColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS + File.separator + "MyDownloadsS/" + PDFProp.CREATEDPDF_FOLDER
            )
            values.put(MediaStore.DownloadColumns.DISPLAY_NAME, "NEWpdfimagetoppppdf" + ".pdf")
            values.put(MediaStore.DownloadColumns.MIME_TYPE, "application/pdf")

            var uuu = activity.contentResolver.insert(uri, values)
            //  Log.d("3r3rf3wf",uuu.toString()+"ewefefef")
            // var uriii=activity.contentResolver.insert(Uri.parse(ssstr),null)
            var fos = activity.contentResolver.openOutputStream(Objects.requireNonNull(uuu!!))
            //  Log.d("33f3wf3w", fos.toString())

            //  File(uri.encodedPath)
            document.writeTo(fos)
            document.close()
        }


    }

    fun createPDFFolder( folderName: String, pdfNAME: String, dateModified: Long): OutputStream {


       val externalUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
       // val externalUri = MediaStore.Downloads.getContentUri(MediaStore.Downloads.EXTERNAL_CONTENT_URI)

        /**to create new  blank file with new folder */
        var values = ContentValues()

        values.put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + File.separator + "MyPDFs/" + folderName)
        values.put(MediaStore.DownloadColumns.DISPLAY_NAME, pdfNAME + ".pdf")
        values.put(MediaStore.DownloadColumns.MIME_TYPE, "application/pdf")
        values.put(MediaStore.DownloadColumns.DATE_MODIFIED, dateModified)

        /**to insert new blank file with new folder in MediaStore or Gallery*/
        var insertedURI = activity.contentResolver.insert(externalUri, values)

        /**now to write pdfdocument in this uri we need to open this*/
        var fos = activity.contentResolver.openOutputStream(insertedURI!!)
        /**now writing to this  outputstream*/
       //...

        return fos!!
    }

   suspend fun splitPdfNative(uri:Uri, numberList: List<String>,outputStream: OutputStream)=
       withContext(Dispatchers.IO)
    {
        var pnative = PDFOperationNATIVE(activity)

        if (numberList.size > 1) {
            startPage = numberList.get(0)
            endPage = numberList.get(1)
            Log.d("ifg7egjdf", startPage + endPage + "sdhsj")
        } else if (numberList.size == 1) {
            splitAtpage = numberList.get(0)
            Log.d("ifg7egjdf", splitAtpage!!)
        } else {
            //  return bool
        }

        if (startPage != null) {
            Log.d("fy7fgsjfstartPage", startPage!!)
            pnative.setStartPage(startPage!!.toInt())
        }
        if (endPage != null) {
            Log.d("fy7fgsjfendpage", endPage!!)
            pnative.setEndPage(endPage!!.toInt())
        }

       // if (startPage != null && endPage == null)
        if(splitAtpage!=null)
        {
            pnative.setSplitAtPage1(splitAtpage!!.toInt()) //here we give .minus(1) because when se split at given pagenumber then splitter split pagenumber single page then further pages gets split.
           // Log.d("fy7fgsjfsplitAtPage", startPage!!)
        }

        pnative.splitPdf(uri,outputStream)
    }
}