package com.example.pdf_reader_viewer.fragments

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.pdf_reader_viewer.R
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getExternalFilesDirs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.BuildConfig

import com.example.pdf_reader_viewer.RecylerViewClasses.Items_pdfs
import com.example.pdf_reader_viewer.RecylerViewClasses.MyAdapter
import com.example.pdf_reader_viewer.databinding.FragmentPdfListBinding
import java.io.File
import java.util.jar.Manifest


class Fragment_pdf_list : Fragment() {

    var pdflist:ArrayList<Items_pdfs>?=null
    var binding:FragmentPdfListBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pdflist=ArrayList()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=FragmentPdfListBinding.inflate(inflater)
        return binding?.root
    }

    @SuppressLint("Range")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//       Log.d("djsjdsk",context?.filesDir?.absolutePath!!)
//        Log.d("djsjdsk",Environment.getExternalStorageDirectory().listFiles().size.toString())

       // val uri=MediaStore.Files.getContentUri("external")

     /*  if(Environment.isExternalStorageManager())
       {
           Toast.makeText(context,"Manage_Permission granted",Toast.LENGTH_SHORT).show()
       }
        else {
           val uri = Uri.parse(context?.packageName)
           startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri))
       }*/


          /* val uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
           var stringProjectioon = arrayOf(MediaStore.Files.FileColumns.DISPLAY_NAME,
                                           MediaStore.Files.FileColumns.MIME_TYPE,
                                           MediaStore.Files.FileColumns.DATE_MODIFIED,
                                           MediaStore.Files.FileColumns.SIZE,
                                           MediaStore.Files.FileColumns.DOCUMENT_ID)

           val selection = MediaStore.Files.FileColumns.MIME_TYPE + " = ?"

           val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")
           val selectionArgs = arrayOf(mimeType)
           var contentResolver = context?.contentResolver
           var cursor = contentResolver?.query(uri, stringProjectioon, selection, selectionArgs, null)

           Log.d("gjfkgf", cursor.toString())
           while (cursor?.moveToNext()!!) {

               var displeynamee = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME))
               var id=cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DOCUMENT_ID))
               var date_modified=cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED))
               var size=cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE))
               Log.d("PPPDFDF", displeynamee + " ")

              var data_uri= Uri.withAppendedPath(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL),id)
               pdflist?.add(Items_pdfs(displeynamee,size,data_uri,date_modified))
              // Toast.makeText(context,string,Toast.LENGTH_SHORT).show()
           }
*/
            var pdflist=get_PdfList(requireContext())
           /* var recyclerView:RecyclerView=view.findViewById(R.id.pdfListRecylerView)
            var myAdapter=MyAdapter(requireContext(),pdflist!!)
            recyclerView.layoutManager=LinearLayoutManager(requireContext())
            recyclerView.adapter=myAdapter*/

    }


    @SuppressLint("Range")
    fun get_PdfList(context: Context)
    {
          /**REQUESTING MANAGE_EXTERNAL_FILES PERMISSION FOR ACCESS ALL FILES/PDFs*/
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                Toast.makeText(context, "Manage_Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                val uri = Uri.parse(context?.packageName)
                startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri))
            }
        }//END OF IF BLOCK WHERE WE USE CONDITION FOR ANDROID R.

           /**GETTING PDF FOR ANDROID Q AND ABOVE*/
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q) {
             /**getting External_Uri to get query files..*/
            val uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
                   /**media-database-columns-to-retrieve*/
            var stringProjectioon = arrayOf(
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DOCUMENT_ID
            )
            /**sql-where-clause-with-placeholder-variables  Here we select MimeType*/
            val selection = MediaStore.Files.FileColumns.MIME_TYPE + " = ?"
           /**getting MIME type for pdf*/
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")
            /**values-of-placeholder-variables  giving mimetype to selection_args */
            val selectionArgs = arrayOf(mimeType)
            /**content Resolver to get cursor for filess..*/
            var contentResolver = context?.contentResolver
            var cursor = contentResolver?.query(uri, stringProjectioon, selection, selectionArgs, null)

            Log.d("gjfkgf", cursor.toString())
            /**getting files from cursor by using Files Column index*/
            while (cursor?.moveToNext()!!) {

                var displeynamee =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME))
                var id =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DOCUMENT_ID))
                var date_modified =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED))
                var size =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE))
                Log.d("PPPDFDF", displeynamee + " ")

                /** As Data column id deprecated so we appended id with VOLUME_EXTERNAL*/
                var data_uri = Uri.withAppendedPath(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), id)
                pdflist?.add(Items_pdfs(displeynamee, size, data_uri, date_modified))
                // Toast.makeText(context,string,Toast.LENGTH_SHORT).show()

            }

          }//END OF IF BLOCK
        /**GETTING PDFs FOR BELOW ANDROID Q*/
        else{


          // ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),100)
           var arry= getExternalFilesDirs(context,null)

            arry.forEach {
                Log.d("fdhfdj",it.absolutePath)
            }

//            Environment.getExternalStoragePublicDirectory(null)
            val file1 = Environment.getExternalStorageDirectory()
            file1.listFiles().forEach {
                Log.d("fkhdj",it.name)
                if(it.name.equals(Environment.DIRECTORY_DOWNLOADS))
                {
                    it.listFiles().forEach {
                        if(it.name.endsWith(".pdf"))
                        Log.d("jjdkdjfd",it.name)
                    }
                }
            }
          Log.d("kjksdjksd",file1?.absolutePath!!)
              file1.listFiles().forEach {
                  Log.d("ALLFILES",it.name)
              }
            Log.d("kjksdjksd","${/*file?.listFiles()?.*/file1?.listFiles()?.size}pp")
        }

    }
}