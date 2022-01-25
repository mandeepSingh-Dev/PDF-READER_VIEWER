package com.example.pdf_reader_viewer.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pdf_reader_viewer.R
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pdf_reader_viewer.UtilClasses.MyViewModel

import com.example.pdf_reader_viewer.RecylerViewClasses.Items_pdfs
import com.example.pdf_reader_viewer.RecylerViewClasses.MyAdapter
import kotlinx.coroutines.*
import com.tom_roush.pdfbox.pdmodel.graphics.image.LosslessFactory

import android.graphics.BitmapFactory

import android.graphics.Bitmap
import com.example.pdf_reader_viewer.databinding.PdfListFragmentBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.tom_roush.pdfbox.pdmodel.graphics.image.JPEGFactory

import com.tom_roush.pdfbox.pdmodel.PDPageContentStream

import com.tom_roush.pdfbox.pdmodel.font.PDType1Font

import com.tom_roush.pdfbox.pdmodel.font.PDFont

import com.tom_roush.pdfbox.pdmodel.PDPage

import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader
import java.io.*


class pdf_list_Fragment : Fragment() {

    var pdflist: ArrayList<Items_pdfs>? = null
    var binding: PdfListFragmentBinding? = null
    var progressBar: ProgressBar? = null
    var recyclerView: RecyclerView? = null
    var externalUri: Uri? = null
    var emptyView: ImageView? = null
    var fab1:FloatingActionButton?=null
    var fab2:FloatingActionButton?=null
    var fab3:FloatingActionButton?=null
    var fab:FloatingActionButton?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pdflist = ArrayList()
        PDFBoxResourceLoader.init(activity?.applicationContext);


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = PdfListFragmentBinding.inflate(inflater)
        return binding?.root
    }

    @SuppressLint("Range", "RestrictedApi")
    override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* //create pdf
        createPdf(view)
        //function to strip text from pdf
        PdfOperations().stripText(view,requireActivity())
      //function to spiltting pdf
        PdfOperations().splittingPdf(requireActivity()!!)
        //merging two pdfs
        PdfOperations().mergePdfs(requireActivity()!!)
        //read and return pdf as image
        var bitmap=PdfOperations().renderFile(view,requireActivity()!!)
        displayRenderedImage(view,requireActivity(),bitmap)
        //encrypting pdf file
        PdfOperations().createEncryptedPdf(view,requireActivity()!!)*/


        recyclerView = view.findViewById(R.id.pdfListRecylerView)
        progressBar = activity?.findViewById<ProgressBar>(R.id.progress_bar)
        emptyView = view.findViewById<ImageView>(R.id.emptyView)

        var myViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(activity?.application!!)
        ).get(MyViewModel::class.java)

        myViewModel.getpdflistttt().observe(viewLifecycleOwner, object : Observer<ArrayList<Items_pdfs>> {
                override fun onChanged(pdflist: ArrayList<Items_pdfs>?) {

                    if (pdflist?.isEmpty()!!) {
                        Log.d("3tubuenfe", "3ufufbkscsdc")
                        binding?.emptyView?.visibility = View.VISIBLE
                        binding?.emptyText?.visibility = View.VISIBLE
                    } else {
                        Log.d("3u8hfjsncsjcisj8", "cnsncjnj2")
                    }
                    var myAdapter = MyAdapter(requireContext(), pdflist!!)
                    recyclerView?.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView?.adapter = myAdapter
                    var textView: TextView = view.findViewById(R.id.textviewALLL)
                    textView.text = "All (" + pdflist?.size.toString() + ")"
                }
            })
    }// END OF onViewCreated block

    suspend fun getPdfList_2(): ArrayList<Items_pdfs> = withContext(Dispatchers.Default) {

        // progressBar?.visibility=View.VISIBLE


        /**THIS URI IS WORKING FOR ALL ANDROID VERSIONS*/
        externalUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)

        /**sql-where-clause-with-placeholder-variables  Here we select MimeType*/
        val selection = MediaStore.Files.FileColumns.MIME_TYPE + " = ?"

        /**getting MIME type for pdf*/
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")

        /**values-of-placeholder-variables  giving mimetype to selection_args */
        val selectionArgs = arrayOf(mimeType)

        /**content Resolver to get cursor for filess..*/
        var contentResolver = context?.contentResolver
        var cursor: Cursor? = null

        /**media-database-columns-to-retrieve*/
        var stringProjectioon: Array<String>? = null

        /**list for buckets or folders*/
        var bucketsList = ArrayList<String>()

        /**getting list according to android build versions */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            stringProjectioon = arrayOf(
                MediaStore.Files.FileColumns.DISPLAY_NAME,       //this column shows null below android 10
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME, //For folder/bucket name of item/pdf
                MediaStore.Files.FileColumns.MIME_TYPE,           //for selecting only MIME type
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.RELATIVE_PATH
            )

            cursor = contentResolver?.query(
                externalUri!!,
                stringProjectioon,
                selection,
                selectionArgs,
                null
            )

            /**below line is to get files from particular bucket or folder*/
            //   var cursor = contentResolver?.query(externalUri!!, stringProjectioon,  MediaStore.Images.Media._ID + " like ? ", arrayOf("%Download%"), null, null)

            /**getting coloumns name*/
            var titleColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.TITLE)!!
            val idColoumn = cursor?.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)!!
            val displayColoumn =
                cursor?.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)!!
            val bucketColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)!!
            val dateModColoumn =
                cursor?.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED)!!
            val sizeColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.SIZE)!!
            val relativePathColoumn =
                cursor?.getColumnIndex(MediaStore.Files.FileColumns.RELATIVE_PATH)!!

            /**Getting all cursors in loop*/
            while (cursor?.moveToNext()!!) {

                // Log.d("dfdfd",cursor.toString())
                var displeynamee = cursor?.getString(displayColoumn)
                var title = cursor?.getString(titleColoumn)
                var id = cursor?.getLong(idColoumn)
                var bucket = cursor?.getString(bucketColoumn)
                var dateModified = cursor?.getString(dateModColoumn)
                var size = cursor?.getString(sizeColoumn)
                var relativepath = cursor?.getString(relativePathColoumn)

                if (bucket != null) {
                    bucketsList.add(bucket!!)
                }

                // Log.d("PPPDFDF", displeynamee + " " + title + "____"/*+RELATIVEPATH*/ + id +"_____"+bucket)
                // activity?.runOnUiThread {
                //   Toast.makeText(context,/*RELATIVEPATH+*/"  " + title + volumename, Toast.LENGTH_LONG).show()
                //  }
                /** As Data column id deprecated so we appended id with VOLUME_EXTERNAL*/
                var data_uri = Uri.withAppendedPath(
                    MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL),
                    id.toString()
                )
                //  pdflist?.add(Items_pdfs(title!!, size!!, data_uri, dateModified, relativepath!!, bucket))
                /**adding cursoritems to pdflist in loop */
                pdflist?.add(
                    Items_pdfs(
                        title!!,
                        size!!,
                        data_uri,
                        dateModified,
                        relativepath!!,
                        bucket
                    )
                )
            }
            return@withContext pdflist!!
        } //END OF IF BLOCK
        else {
            stringProjectioon = arrayOf(
                MediaStore.Files.FileColumns.DISPLAY_NAME,       //this column shows null below android 10
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME, //For folder/bucket name of item/pdf
                MediaStore.Files.FileColumns.MIME_TYPE,           //for selecting only MIME type
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.SIZE
            )

            /**Query cursors that include files or data*/
            cursor = contentResolver?.query(
                externalUri!!,
                stringProjectioon,
                selection,
                selectionArgs,
                null
            )
            /**below line is to get files from particular bucket or folder*/
            //   var cursor = contentResolver?.query(externalUri!!, stringProjectioon,  MediaStore.Images.Media._ID + " like ? ", arrayOf("%Download%"), null, null)

            /**getting coloumns name*/
            var titleColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.TITLE)!!
            val idColoumn = cursor?.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)!!
            val displayColoumn =
                cursor?.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)!!
            val bucketColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)!!
            val dateModColoumn =
                cursor?.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED)!!
            val sizeColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.SIZE)!!
            val relativePathColoumn =
                cursor?.getColumnIndex(MediaStore.Files.FileColumns.RELATIVE_PATH)!!

            /**Getting all cursors in loop*/
            while (cursor?.moveToNext()!!) {

                // Log.d("dfdfd",cursor.toString())
                var title = cursor?.getString(titleColoumn)
                var id = cursor?.getLong(idColoumn)
                var bucket = cursor?.getString(bucketColoumn)
                var dateModified = cursor?.getString(dateModColoumn)
                var size = cursor?.getString(sizeColoumn)

                if (bucket != null) {
                    bucketsList.add(bucket!!)
                }
                Log.d("PPPDFDF", " " + title + "____"/*+RELATIVEPATH*/ + id + "_____" + bucket)
                // activity?.runOnUiThread {
                //   Toast.makeText(context,/*RELATIVEPATH+*/"  " + title + volumename, Toast.LENGTH_LONG).show()
                //  }
                /** As Data column id deprecated so we appended id with VOLUME_EXTERNAL*/
                var data_uri = Uri.withAppendedPath(
                    MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL),
                    id.toString()
                )
                //  pdflist?.add(Items_pdfs(title!!, size!!, data_uri, dateModified, relativepath!!, bucket))
                /**adding cursoritems to pdflist in loop */

                pdflist?.add(Items_pdfs(title!!, size!!, data_uri, dateModified, bucket))
            }

            return@withContext pdflist!!
        } //END OF ELSE BLOCK

        //    Log.d("3oujr74ghvn",pdflist?.size.toString())
        /**Method for Removing duplicate bucket/folder names from folderList*/
        // filtering_FolderArraylist(folderlistExm)


        //  LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(Intent("SENDINGFOLDERLIST").putStringArrayListExtra("FOLDERLIST",folderlistExm))

    }


    /**Method for Removing duplicate bucket/folder names from folderList*/
    fun filtering_FolderArraylist(folderList: ArrayList<String>) {
        Log.d("3ifhgjntggngvngc38", folderList.size.toString())
        var list = ArrayList<String>()
        folderList.forEach {
            if (!list.contains(it)) {
                list.add(it)
            }
        }

        Log.d("34fe56hdf", list.size.toString())
        list.forEach {
            Log.d("itemsidfhdc8948", it)
        }
        /**to find numbers of folders or bucket in folderListexm*/
        var count = 0;
        list.forEach {
            for (i in 0..folderList.size - 1) {
                if (folderList.get(i).equals(it)) {
                    Log.d("iryhtugfvn8ygh", count++.toString() + it)
                }
            }
            count = 0;

        }
    }


    /* folderList.forEach {
            Log.d("filteringARRAYLIST",it)
        }*/

    fun createPdf(v: View?) {
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
            var file = File("sdcard/manddeettttp")
            if (!file.exists()) {
                file.mkdir()
            }
            var stream = FileOutputStream(file.absolutePath + "/jkjkj.pdf")

            //  val path: String = /*root.getAbsolutePath().toString() + "/Created.pdf"*/ activity?.applicationContext?.filesDir?.absolutePath.toString()+"/created.pdf"
            val path: String = file.absolutePath.toString()
            // Log.d("37fgewf",file.absolutePath.toString())

            document.save(stream)
            document.close()
            // tv.setText("Successfully wrote PDF to $path")
        } catch (e: IOException) {
            Log.e("PdfBox-Android-Sample", "Exception thrown while creating PDF", e)
        }
    }

    fun displayRenderedImage(view: View, activity: Activity, pageImage: Bitmap) {
        Thread(Runnable {
            activity?.runOnUiThread(Runnable() {
                Log.d("38hgi3h", "sksjdfksj")
                var imageView = view.findViewById<ImageView>(R.id.emptyView);
                imageView.setImageBitmap(pageImage);
            })
        })
    }
}