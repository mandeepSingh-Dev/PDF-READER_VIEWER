package com.example.pdf_reader_viewer.RecylerViewClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pdf_reader_viewer.R

class MyAdapter_ForMerge(context:Context,arrayList:ArrayList<Items_pdfs>):RecyclerView.Adapter<MyAdapter_ForMerge.MyViewholderMerge>()
{
    var contextt=context
    var pdflist:ArrayList<Items_pdfs> = arrayList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholderMerge {
        var view=LayoutInflater.from(contextt).inflate(R.layout.list_merge_item,parent,false)

         var myViewholderMerge=MyViewholderMerge(view)
        return myViewholderMerge
    }

    override fun onBindViewHolder(holder: MyViewholderMerge, position: Int) {

        var itemPdf=pdflist.get(position)
        holder.pdfName?.setText(itemPdf?.title)
        holder.pdfSize?.setText(itemPdf?.size)
        holder.pdfDate?.setText(itemPdf?.date_modified)



    }

    override fun getItemCount(): Int {
        return  pdflist?.size
    }

    class MyViewholderMerge(itemView: View):RecyclerView.ViewHolder(itemView) {

       var pdfName=itemView?.findViewById<TextView>(R.id.pdfNameTextView)
       var pdfImageView=itemView?.findViewById<ImageView>(R.id.pdfImageView)
        var pdfSize=itemView?.findViewById<TextView>(R.id.pdfSize)
        var pdfDate=itemView?.findViewById<TextView>(R.id.pdfDateTexView)

    }
}