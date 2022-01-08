package com.example.pdf_reader_viewer.fragments

import android.annotation.SuppressLint
import android.content.*
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Settings
import android.text.method.Touch
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.pdf_reader_viewer.R
import android.webkit.MimeTypeMap
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getExternalFilesDirs
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.BuildConfig

import com.example.pdf_reader_viewer.RecylerViewClasses.Items_pdfs
import com.example.pdf_reader_viewer.RecylerViewClasses.MyAdapter
import com.example.pdf_reader_viewer.databinding.FragmentPdfListBinding
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnTapListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.io.File
import java.util.jar.Manifest
import kotlin.coroutines.CoroutineContext


class Fragment_pdf_list : Fragment() {

    var pdflist:ArrayList<Items_pdfs>?=null
    var binding:FragmentPdfListBinding?=null

    var externalUri:Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pdflist=ArrayList()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=FragmentPdfListBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var recyclerView:RecyclerView=view.findViewById(R.id.pdfListRecylerView)

           // var pdflist=get_PdfList(requireContext())
        var job=CoroutineScope(Dispatchers.Main).async {

            /**getting pdf list from Mediastore Api*/
            var finalPDFList=getPdfList_2(view)


           var myAdapter=MyAdapter(requireContext(),finalPDFList!!)
           recyclerView.layoutManager=LinearLayoutManager(requireContext())
           recyclerView.adapter=myAdapter

            var textView:TextView=view.findViewById(R.id.textviewALLL)
            textView.text="All ("+finalPDFList?.size.toString()+")"

        }
    }// END OF onViewCreated block

   suspend fun getPdfList_2(view:View):ArrayList<Items_pdfs> = withContext(Dispatchers.Default) {

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
        var stringProjectioon:Array<String>? =null

            /**list for buckets or folders*/
            var folderlistExm = ArrayList<String>()

            /**getting list according to android build versions */
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q) {
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

            cursor = contentResolver?.query(externalUri!!, stringProjectioon, selection, selectionArgs, null)

            /**below line is to get files from particular bucket or folder*/
            //   var cursor = contentResolver?.query(externalUri!!, stringProjectioon,  MediaStore.Images.Media._ID + " like ? ", arrayOf("%Download%"), null, null)

            /**getting coloumns name*/
            var titleColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.TITLE)!!
            val idColoumn = cursor?.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)!!
            val displayColoumn = cursor?.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)!!
            val bucketColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)!!
            val dateModColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED)!!
            val sizeColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.SIZE)!!
            val relativePathColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.RELATIVE_PATH)!!

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
                    folderlistExm.add(bucket!!)
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
                pdflist?.add(Items_pdfs(title!!, size!!, data_uri, dateModified, relativepath!!, bucket))
            }
                return@withContext pdflist!!
            } //END OF IF BLOCK
            else{
            stringProjectioon= arrayOf(
                MediaStore.Files.FileColumns.DISPLAY_NAME,       //this column shows null below android 10
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME, //For folder/bucket name of item/pdf
                MediaStore.Files.FileColumns.MIME_TYPE,           //for selecting only MIME type
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.SIZE )

                /**Query cursors that include files or data*/
                cursor = contentResolver?.query(externalUri!!, stringProjectioon, selection, selectionArgs, null)
                /**below line is to get files from particular bucket or folder*/
                //   var cursor = contentResolver?.query(externalUri!!, stringProjectioon,  MediaStore.Images.Media._ID + " like ? ", arrayOf("%Download%"), null, null)

                /**getting coloumns name*/
                var titleColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.TITLE)!!
                val idColoumn = cursor?.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)!!
                val displayColoumn = cursor?.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)!!
                val bucketColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)!!
                val dateModColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED)!!
                val sizeColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.SIZE)!!
                val relativePathColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.RELATIVE_PATH)!!

                /**Getting all cursors in loop*/
                while (cursor?.moveToNext()!!) {

                    // Log.d("dfdfd",cursor.toString())
                    var title = cursor?.getString(titleColoumn)
                    var id = cursor?.getLong(idColoumn)
                    var bucket = cursor?.getString(bucketColoumn)
                    var dateModified = cursor?.getString(dateModColoumn)
                    var size = cursor?.getString(sizeColoumn)

                    if (bucket != null) {
                        folderlistExm.add(bucket!!)
                    }
                     Log.d("PPPDFDF",  " " + title + "____"/*+RELATIVEPATH*/ + id +"_____"+bucket)
                    // activity?.runOnUiThread {
                    //   Toast.makeText(context,/*RELATIVEPATH+*/"  " + title + volumename, Toast.LENGTH_LONG).show()
                    //  }
                    /** As Data column id deprecated so we appended id with VOLUME_EXTERNAL*/
                    var data_uri = Uri.withAppendedPath(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), id.toString())
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
    fun filtering_FolderArraylist(folderList:ArrayList<String>)
    {
        Log.d("3ifhgjntggngvngc38",folderList.size.toString())
        var list=ArrayList<String>()
        folderList.forEach {
            if(!list.contains(it))
            {
                list.add(it)
            }
        }

        Log.d("34fe56hdf",list.size.toString())
        list.forEach {
            Log.d("itemsidfhdc8948",it)
        }
        /**to find numbers of folders or bucket in folderListexm*/
 var count=0;
        list.forEach {
            for(i in 0..folderList.size-1)
            {
                if(folderList.get(i).equals(it))
                {
                    Log.d("iryhtugfvn8ygh",count++.toString()+it)
                }
            }
            count=0;

            }
        }


       /* folderList.forEach {
            Log.d("filteringARRAYLIST",it)
        }*/
    }
