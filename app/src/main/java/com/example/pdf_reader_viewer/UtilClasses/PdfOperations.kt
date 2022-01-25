package com.example.pdf_reader_viewer.UtilClasses

import android.app.Activity
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import com.tom_roush.pdfbox.multipdf.PDFMergerUtility
import com.tom_roush.pdfbox.multipdf.Splitter
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPage
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.font.PDFont
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font
import com.tom_roush.pdfbox.pdmodel.graphics.image.JPEGFactory
import com.tom_roush.pdfbox.pdmodel.graphics.image.LosslessFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import com.tom_roush.pdfbox.text.PDFTextStripper
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.ImageFormat
import android.graphics.pdf.PdfDocument
import android.media.Image
import android.media.ImageWriter
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi

import com.tom_roush.pdfbox.rendering.PDFRenderer
import org.bouncycastle.jce.provider.BouncyCastleProvider

import com.tom_roush.pdfbox.pdmodel.encryption.StandardProtectionPolicy

import com.tom_roush.pdfbox.pdmodel.encryption.AccessPermission
import java.security.Security


class PdfOperations
{
    var pageImage:Bitmap?=null

    fun createPdf(v: View?,activity: Activity) {
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
            var file= File("sdcard/manddeettttp")
            if(!file.exists())
            {
                file.mkdir()
            }
            var stream= FileOutputStream(file.absolutePath+"/jkjkj.pdf")

            //  val path: String = /*root.getAbsolutePath().toString() + "/Created.pdf"*/ activity?.applicationContext?.filesDir?.absolutePath.toString()+"/created.pdf"
            val path: String=file.absolutePath.toString()
            // Log.d("37fgewf",file.absolutePath.toString())

            document.save(stream)
            document.close()
            // tv.setText("Successfully wrote PDF to $path")
        } catch (e: IOException) {
            Log.e("PdfBox-Android-Sample", "Exception thrown while creating PDF", e)
        }
    }

    fun stripText(v: View?,activity: Activity) {
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
            Log.d("48tyfhigvng",parsedText!!)
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

    fun splittingPdf(activity: Activity) {
        //Loading an existing PDF document
     //   var  file = File(activity?.assets.open("Hello.pdf"));
        var document = PDDocument.load((activity?.assets.open("Hello.pdf")))

        //Instantiating Splitter class
        var splitter =  Splitter();

        //splitting the pages of a PDF document
       // splitter.setSplitAtPage(2)
        var Pages = splitter.split(document);

        //Creating an iterator
        var iterator = Pages.listIterator();

        //Saving each page as an individual document
        var i = 1;
        while (iterator.hasNext()) {
            var pd = iterator.next();
            pd.save("sdcard/sample" + i++ + ".pdf");
        }
        System.out.println("Multiple PDF’s created");
        Log.d("388fh3ev","Multiple PDF’s created")
        document.close();

    }


    fun mergePdfs(activity: Activity) {
        //Instantiating PDFMergerUtility class
        var PDFmerger =  PDFMergerUtility();

        //Setting the destination file
        PDFmerger.setDestinationFileName("sdcard/Merged.pdf");

        //adding the source files
        PDFmerger.addSource(activity?.assets.open("Hello.pdf"));
        PDFmerger.addSource(activity?.assets.open("Hello.pdf"));

        //Merging the two documents
        PDFmerger.mergeDocuments(true);
        System.out.println("Documents merged");
    }


    fun renderFile(v: View?, activity:Activity):Bitmap {
        // Render the page and save it to an image file
        try {
            // Load in an already created PDF
            val document: PDDocument = PDDocument.load(activity.assets.open("Hello.pdf"))
            // Create a renderer for the document
            val renderer = PDFRenderer(document)
            // Render the image to an RGB Bitmap
            pageImage = renderer.renderImage(0, 1f,Bitmap.Config.RGB_565)

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

    fun createEncryptedPdf(v: View?,activity: Activity) {
        val path: String ="sdcard/crypt.pdf"
        val keyLength = 128 // 128 bit is the highest currently supported

        // Limit permissions of those without the password
        val ap = AccessPermission()
        ap.setCanPrint(false)

        // Sets the owner password and user password
        val spp = StandardProtectionPolicy("12345", "hi", ap)

        // Setups up the encryption parameters
        spp.encryptionKeyLength = keyLength
        spp.permissions = ap
        val provider = BouncyCastleProvider()
        Security.addProvider(provider)

        val font: PDFont = PDType1Font.HELVETICA
        val document = PDDocument()
        val page = PDPage()
        document.addPage(page)
     //to encrypt existing pdf
      /*  var pdddovcument=PDDocument.load(activity?.assets?.open("Hello.pdf"))
        pdddovcument.protect()*/
        try {
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
        }
    }


    }

