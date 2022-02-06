package com.example.pdf_reader_viewer.RecylerViewClasses

import android.content.ClipData
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.createBitmap
import androidx.recyclerview.widget.RecyclerView
import com.example.pdf_reader_viewer.R
import java.util.*
import kotlin.collections.ArrayList

class MyAdapter_ImagesToPDF(context: Context, arrayList:ArrayList<Bitmap>):RecyclerView.Adapter<MyAdapter_ImagesToPDF.MyViewHolderr>()
{
    var mcontext=context
    var marrayList=arrayList
    var myviewholder:MyViewHolderr?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderr {
        var view=LayoutInflater.from(mcontext).inflate(R.layout.images_list,parent,false)
         myviewholder=MyViewHolderr(view)
        return myviewholder!!
    }

    override fun onBindViewHolder(holder: MyViewHolderr, position: Int) {
        var bitmap=marrayList.get(position)

        holder.imageView.setImageBitmap(bitmap)
        holder.textview.setText(position.toString())

    }

    override fun getItemCount(): Int {
        return marrayList.size
    }
    class MyViewHolderr(itemView: View):RecyclerView.ViewHolder(itemView) {

        var imageView=itemView.findViewById<ImageView>(R.id.selectedImagesBitmap)
        var textview=itemView.findViewById<TextView>(R.id.positionnn)

    }

    fun movetoItem(from:Int,to:Int):ArrayList<Bitmap>{
        Collections.swap(marrayList,from,to)
        return marrayList
    }
    fun getList():ArrayList<Bitmap>{
        return marrayList
    }
}