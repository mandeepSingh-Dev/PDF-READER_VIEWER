package com.example.pdf_reader_viewer.RecylerViewClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pdf_reader_viewer.R

class MyAdapter( context1:Context,pdfList1:ArrayList<Items_pdfs>):RecyclerView.Adapter<MyAdapter.MyViewHolder>()
{
    var context:Context?=context1
    var pdfList:ArrayList<Items_pdfs>?=pdfList1



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        var view=LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        var myviewHolder=MyViewHolder(view)
        return myviewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var displayName=pdfList?.get(position)?.displayName
        holder.pdfName.setText(displayName)
        holder.pdfsize.setText(pdfList?.get(position)?.size+"    "+pdfList?.size)


    }

    override fun getItemCount(): Int {
        return pdfList?.size!!
    }

    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {

        var pdfsize:TextView=itemView.findViewById(R.id.sizePDF)
             var pdfName:TextView=itemView.findViewById(R.id.pdfName1)


    }
}

