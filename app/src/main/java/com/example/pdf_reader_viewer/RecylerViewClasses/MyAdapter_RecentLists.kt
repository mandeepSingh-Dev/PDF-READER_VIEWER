package com.example.pdf_reader_viewer.RecylerViewClasses

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.example.pdf_reader_viewer.R
import com.example.pdf_reader_viewer.Roomclasses.Items_RecentPdfs

class MyAdapter_RecentLists(context: Context,arrayList:ArrayList<Items_RecentPdfs>):RecyclerView.Adapter<MyAdapter_RecentLists.MyViewholder>()
{
    var context=context
    var recentlist=arrayList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
            var view = LayoutInflater.from(context).inflate(R.layout.recent_list_item,parent,false)
            var viewHolder=MyViewholder(view)
        return viewHolder
    }

    @SuppressLint("Range")
    override fun onBindViewHolder(holder: MyViewholder, position: Int) {

        var uri=recentlist.get(position).pdfUri
      var cursor = context.contentResolver?.query(Uri.parse(uri),null,null,null,null)
               if(cursor?.moveToFirst()!!)
               {
                  var name = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                   holder.pdfNameTextView.text=name
               }

    }

    override fun getItemCount(): Int {
     return recentlist.size
    }

    class MyViewholder(itemview: View):RecyclerView.ViewHolder(itemview)
    {
        var pdfNameTextView=itemview.findViewById<TextView>(R.id.recentPdfname)

    }

}