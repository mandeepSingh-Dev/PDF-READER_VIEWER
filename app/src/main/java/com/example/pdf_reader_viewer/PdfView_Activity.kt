package com.example.pdf_reader_viewer

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.example.pdf_reader_viewer.UtilClasses.NotificationUtill
import com.example.pdf_reader_viewer.UtilClasses.PDFProp
import com.example.pdf_reader_viewer.UtilClasses.SettingsProp
import com.example.pdf_reader_viewer.databinding.ActivityPdfViewBinding
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class PdfView_Activity : AppCompatActivity() {
    var binding: ActivityPdfViewBinding? = null
    var uri: Uri? = null
    var pdftitle: String? = null
    var pdfsize: String? = null
    var size: String? = null
    lateinit var intenntShare: Intent
    //lateinit var folderUri:Uri
    lateinit var  byteArray:ByteArray

    val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(SettingsProp.SETTINGS_SHAREDPREFERNCE,Context.MODE_PRIVATE)
    }

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)
     //Log.d("efjhfe",intent?.action!!)

        if(intent?.action?.equals(PDFProp.MY_OPEN_ACTION)!!) {
            var pdfuri_String = intent.extras?.getString(PDFProp.PDF_APPENDED_URI)
            pdftitle = intent.extras?.getString(PDFProp.PDF_TITLE)
            size = intent.extras?.getString(PDFProp.PDF_SIZE)

          //  Log.d("3pfgj",pdfuri_String!!+"difhduif")

            uri = Uri.parse(pdfuri_String)
           // if (pdfuri_String != null && pdftitle != null) {

           // }
        }
        else {
            Log.d("hfduife", intent?.action!!)

            uri = intent.data
            Log.d("hfduife", uri.toString())

            var cursor = contentResolver?.query(uri!!, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    pdftitle = it.getString(it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                    size = it.getString(it.getColumnIndex(MediaStore.MediaColumns.SIZE))
                }
            }
        }

        Toast.makeText(this, uri.toString(), Toast.LENGTH_LONG).show()

        // val pdfView = findViewById<PDFView>(R.id.pdfView)
        binding?.pdfTitle?.setText(pdftitle)

        var showPage = sharedPreferences.getString(SettingsProp.IS_SHOWPAGENUMBERS,"true")
            Log.d("39fh3wo", showPage!!)


            // var intentUri=intent.data
        if(uri!=null) {
            binding?.pdfView?.fromUri(uri)?.onTap { e ->
                e.action = MotionEvent.ACTION_SCROLL
                true
            }?.spacing(0)?.onPageChange { page, pageCount ->
                //pageNumber.setText(page.toString())
                if (showPage.equals("true")) {
                    binding?.pageNumber?.visibility = View.VISIBLE
                    binding?.pageNumber?.setText((page + 1).toString())
                }

            }?.enableSwipe(true)?.swipeHorizontal(false)?.enableAnnotationRendering(false)
                ?.enableDoubletap(true)?.enableAntialiasing(true)?.defaultPage(0)?.spacing(10)
                ?.load()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.pdfview_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.senditem -> {

                intenntShare = Intent(Intent.ACTION_SEND)
                intenntShare?.type = "application/pdf"
                intenntShare.putExtra(Intent.EXTRA_STREAM, uri)
                startActivity(Intent.createChooser(intenntShare, title))

                return true
            }
            R.id.DownlodItem -> {
                downloadPdf(uri!!, pdftitle!!,size!!)
                return true
            }
            R.id.PrintItem -> {
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }

    }

    fun downloadPdf(urii: Uri, title: String,size: String) {

       var isnotificationEnabled =  sharedPreferences.getString(SettingsProp.IS_NOTIFICATION_ENABLED,"true")
        Log.d("sdifhuebfvd",isnotificationEnabled!!)

        if (urii?.scheme.equals("content")) {



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                var values = ContentValues()
                values.put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                values.put(MediaStore.Audio.AudioColumns.DISPLAY_NAME, title + ".pdf")
                values.put(MediaStore.Audio.AudioColumns.MIME_TYPE, "application/pdf")
                values.put(MediaStore.Audio.AudioColumns.SIZE,size)

                var urie = contentResolver.insert(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), values)
                //open outputstream  of  insertes values or folder
                var outputStream = contentResolver.openOutputStream(urie!!)
                        //open inputstream of  our pdf file
                var inputStream = contentResolver?.openInputStream(urii)
                byteArray = BufferedInputStream(inputStream).readBytes()

                outputStream?.write(byteArray)
                outputStream?.close()

                  if(isnotificationEnabled.equals("true")) {
                      NotificationUtill(this).notiofiact(urii, title)
                  }
                else{
                    Toast.makeText(this, title+" Downloaded",Toast.LENGTH_LONG).show()
                }
            }
            else{
           launcher.launch(title+".pdf")

                if(isnotificationEnabled.equals("true")) {
                    NotificationUtill(this).notiofiact(urii, title)
                }
                else{
                    Toast.makeText(this, title+" Downloaded",Toast.LENGTH_LONG).show()
                }

            }
//        var downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//        var request = DownloadManager.Request(Uri.parse("https://media/external/file/267"))
//        request.setTitle("Pdf Download");
//        request.setDescription("Android Data download using DownloadManager.");
//
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//
////set the local destination for download file to a path within the application's external files directory
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"downloadPdfName");
            //  request.setMimeType("application/pdf");
//        downloadManager.enqueue(request);
        }



    }
    var launcher = registerForActivityResult(ActivityResultContracts.CreateDocument(),
        ActivityResultCallback {
            if(it!=null) {
                Log.d("3oejgfe4", uri.toString())
                var outputStream = contentResolver?.openOutputStream(it!!)
                var inputStream = contentResolver?.openInputStream(uri!!)
                byteArray = BufferedInputStream(inputStream).readBytes()
                //  if(byteArray!=null)
                // {
                outputStream?.write(byteArray)
                outputStream?.close()

                //  }
            }

        })
}
