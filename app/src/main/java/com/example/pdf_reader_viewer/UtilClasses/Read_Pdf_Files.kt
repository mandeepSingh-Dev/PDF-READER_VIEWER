package com.example.pdf_reader_viewer.UtilClasses

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import com.example.pdf_reader_viewer.RecylerViewClasses.Items_pdfs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// pdfs Repositry (Means that this class is for getting pdflist from local device)
class Read_Pdf_Files(context:Context)
{
    var context=context
    var pdflist:ArrayList<Items_pdfs>?=null


    suspend fun getPdfList_2():ArrayList<Items_pdfs> = withContext(Dispatchers.Default) {

        pdflist=ArrayList()
        // progressBar?.visibility=View.VISIBLE


        /**THIS URI IS WORKING FOR ALL ANDROID VERSIONS*/
       var externalUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)

        /**sql-where-clause-with-placeholder-variables  Here we select MimeType*/
        /**sql-where-clause-with-placeholder-variables  Here we select MimeType*/
        val selection = MediaStore.Files.FileColumns.MIME_TYPE + " = ?"

        /**getting MIME type for pdf*/
        /**getting MIME type for pdf*/
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")

        /**values-of-placeholder-variables  giving mimetype to selection_args */
        /**values-of-placeholder-variables  giving mimetype to selection_args */
        val selectionArgs = arrayOf(mimeType)

        /**content Resolver to get cursor for filess..*/
        /**content Resolver to get cursor for filess..*/
        var contentResolver = context?.contentResolver
        var cursor: Cursor? = null

        /**media-database-columns-to-retrieve*/
        /**media-database-columns-to-retrieve*/
        var stringProjectioon:Array<String>? =null

        /**list for buckets or folders*/
        /**list for buckets or folders*/
        var bucketsList = ArrayList<String>()

        /**getting list according to android build versions */

        /**getting list according to android build versions */
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q) {
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

            /**below line is to get files from particular bucket or folder*/
            //   var cursor = contentResolver?.query(externalUri!!, stringProjectioon,  MediaStore.Images.Media._ID + " like ? ", arrayOf("%Download%"), null, null)

            /**getting coloumns name*/
            /**getting coloumns name*/
            var titleColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.TITLE)!!
            val idColoumn = cursor?.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)!!
            val displayColoumn = cursor?.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)!!
            val bucketColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)!!
            val dateModColoumn = cursor?.getColumnIndex(MediaStore.MediaColumns.DATE_MODIFIED)!!
            val sizeColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.SIZE)!!
            val relativePathColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.RELATIVE_PATH)!!

            /**Getting all cursors in loop*/

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
                Log.d("fooehuifhned",title+id+bucket+dateModified+size+relativepath)


                //practise
                Log.d("3ggh3gh3vb",dateModified.toString()+"gfgege")

                //Handling Nullability for varables
                if (bucket != null) { bucketsList.add(bucket!!) }
                if(title==null){title="N.A"}
                if(dateModified==null){dateModified="N.A"}
                if(size==null){size="N.A"}
                if(relativepath==null){relativepath="N.A"}
                if(bucket==null){bucket="N.A"}



                // Log.d("PPPDFDF", displeynamee + " " + title + "____"/*+RELATIVEPATH*/ + id +"_____"+bucket)
                // activity?.runOnUiThread {
                //   Toast.makeText(context,/*RELATIVEPATH+*/"  " + title + volumename, Toast.LENGTH_LONG).show()
                //  }
                /** As Data column id deprecated so we appended id with VOLUME_EXTERNAL*/
                /** As Data column id deprecated so we appended id with VOLUME_EXTERNAL*/
                var data_uri = Uri.withAppendedPath(
                    MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL),
                    id.toString()
                )
                //  pdflist?.add(Items_pdfs(title!!, size!!, data_uri, dateModified, relativepath!!, bucket))
                /**adding cursoritems to pdflist in loop */
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

            /**Query cursors that include files or data*/
            cursor = contentResolver?.query(externalUri!!, stringProjectioon, selection, selectionArgs, null)
            /**below line is to get files from particular bucket or folder*/
            /**below line is to get files from particular bucket or folder*/
            //   var cursor = contentResolver?.query(externalUri!!, stringProjectioon,  MediaStore.Images.Media._ID + " like ? ", arrayOf("%Download%"), null, null)

            /**getting coloumns name*/
            /**getting coloumns name*/
            var titleColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.TITLE)!!
            val idColoumn = cursor?.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)!!
            val displayColoumn = cursor?.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)!!
            val bucketColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)!!
            val dateModColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED)!!
            val sizeColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.SIZE)!!
            val relativePathColoumn = cursor?.getColumnIndex(MediaStore.Files.FileColumns.RELATIVE_PATH)!!

            /**Getting all cursors in loop*/

            /**Getting all cursors in loop*/
            while (cursor?.moveToNext()!!) {

                // Log.d("dfdfd",cursor.toString())
                var title = cursor?.getString(titleColoumn)
                var id = cursor?.getLong(idColoumn)
                var bucket = cursor?.getString(bucketColoumn)
                var dateModified = cursor?.getString(dateModColoumn)
                var size = cursor?.getString(sizeColoumn)



                //Handling Nullability for varables
                if (bucket != null) { bucketsList.add(bucket!!) }
                if (bucket != null) { bucketsList.add(bucket!!) }
                if(title==null){title="N.A"}
                if(dateModified==null){dateModified="N.A"}
                if(size==null){size="N.A"}
                if(bucket==null){bucket="N.A"}
                Log.d("PPPDFDF",  " " + title + "____"/*+RELATIVEPATH*/ + id +"_____"+bucket)
                // activity?.runOnUiThread {
                //   Toast.makeText(context,/*RELATIVEPATH+*/"  " + title + volumename, Toast.LENGTH_LONG).show()
                //  }
                /** As Data column id deprecated so we appended id with VOLUME_EXTERNAL*/
                /** As Data column id deprecated so we appended id with VOLUME_EXTERNAL*/
                var data_uri = Uri.withAppendedPath(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), id.toString())
                //  pdflist?.add(Items_pdfs(title!!, size!!, data_uri, dateModified, relativepath!!, bucket))
                /**adding cursoritems to pdflist in loop */
                /**adding cursoritems to pdflist in loop */

                pdflist?.add(Items_pdfs(title!!, size!!, data_uri, dateModified, bucket))
            }

            return@withContext pdflist!!
        } //END OF ELSE BLOCK

        //    Log.d("3oujr74ghvn",pdflist?.size.toString())
        /**Method for Removing duplicate bucket/folder names from folderList*/
        /**Method for Removing duplicate bucket/folder names from folderList*/
        // filtering_FolderArraylist(folderlistExm)


        //  LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(Intent("SENDINGFOLDERLIST").putStringArrayListExtra("FOLDERLIST",folderlistExm))

    }




}