package com.example.pdf_reader_viewer.RecylerViewClasses

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pdf_reader_viewer.PdfView_Activity
import com.example.pdf_reader_viewer.R
import com.example.pdf_reader_viewer.Roomclasses.Room_For_RecentPDFs.Items_RecentPdfs
import com.example.pdf_reader_viewer.UtilClasses.ConversionandUtilsClass
import com.example.pdf_reader_viewer.UtilClasses.PDFProp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyAdapter_RecentLists(context: Context,arrayList:ArrayList<Items_RecentPdfs>):RecyclerView.Adapter<MyAdapter_RecentLists.MyViewholder>()
{
    var context=context
    var recentlist=arrayList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
            var view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
            var viewHolder=MyViewholder(view)
        return viewHolder
    }

    @SuppressLint("Range")
    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
         var itempdf=recentlist.get(position)
        var uri=itempdf.pdfUri
        var name:String?=null
        CoroutineScope(Dispatchers.IO).launch {
            var cursor = context.contentResolver?.query(Uri.parse(uri), null, null, null, null)
            if (cursor != null) {
                if (cursor?.moveToFirst()!!) {
                    name =
                        cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                    if (name == null) {
                        name = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE))
                    }
                    var size = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE))

                    withContext(Dispatchers.Main) {
                    holder.pdfNameTextView.text = name
                    holder.recentPDFsize.text = ConversionandUtilsClass.bytesToMB(size)
                    // holder.recentpdfDATE.text=ConversionandUtilsClass.convertToDate(System.currentTimeMillis()/1000L).get(1)
                    holder.recentpdfDATE.text =
                        ConversionandUtilsClass.convertToDate(itempdf.date / 1000L).get(0)
                    // Log.d("85thrng",ConversionandUtilsClass.convertToDate(System.currentTimeMillis()/1000L).get(1))
                }
                }
            }
        }//corotuinescope closes


            holder.itemView.setOnClickListener {

                context?.startActivity(Intent(context, PdfView_Activity::class.java)
                    .putExtra(PDFProp.PDF_APPENDED_URI,uri)
                    .putExtra(PDFProp.PDF_TITLE,name))
            }



    }

    override fun getItemCount(): Int {
     return recentlist.size
    }

    class MyViewholder(itemview: View):RecyclerView.ViewHolder(itemview)
    {
       /* var pdfNameTextView=itemview.findViewById<TextView>(R.id.recentPdfname)
        var recentPDFsize=itemview.findViewById<TextView>(R.id.recentPDFsize)
        var recentpdfDATE=itemview.findViewById<TextView>(R.id.recentpdfDATE)*/

        var pdfNameTextView=itemview.findViewById<TextView>(R.id.pdfName1)
        var recentPDFsize=itemview.findViewById<TextView>(R.id.sizePDF)
        var recentpdfDATE=itemview.findViewById<TextView>(R.id.dateModifiedText)



    }


}