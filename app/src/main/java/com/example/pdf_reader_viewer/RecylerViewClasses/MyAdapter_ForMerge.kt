package com.example.pdf_reader_viewer.RecylerViewClasses

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pdf_reader_viewer.PdfView_Activity
import com.example.pdf_reader_viewer.R
import com.example.pdf_reader_viewer.UtilClasses.PDFProp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        Log.d("sizeeifhigv",pdflist?.size.toString())

         holder.dragmenuimage?.visibility = View.GONE

        var itemPdf=pdflist.get(position)
        holder.pdfName?.setText(itemPdf?.title)
      //  holder.pdfSize?.setText(itemPdf?.size)
       // holder.pdfDate?.setText(itemPdf?.date_modified)

        holder.itemView?.setOnClickListener {
            contextt?.startActivity(
                Intent(contextt, PdfView_Activity::class.java)
                .setAction(PDFProp.MY_OPEN_ACTION)
                .putExtra(PDFProp.PDF_APPENDED_URI,itemPdf.appendeduri.toString())
                .putExtra(PDFProp.PDF_TITLE,"fdf")
                .putExtra(PDFProp.PDF_SIZE,"df"))

            Log.d("wifhwedf",itemPdf.appendeduri.toString())

            CoroutineScope(Dispatchers.Main).launch {
              //  insertToRecentDATABASE(title!!,size!!,appendeduri?.toString()!!,System.currentTimeMillis())
            }
        }



    }

    override fun getItemCount(): Int {
        return  pdflist?.size
    }

    class MyViewholderMerge(itemView: View):RecyclerView.ViewHolder(itemView) {

       var pdfName = itemView?.findViewById<TextView>(R.id.pdfNameTextView)
       var pdfImageView = itemView?.findViewById<ImageView>(R.id.pdfImageView)
        var pdfSize = itemView?.findViewById<TextView>(R.id.pdfSize)
        var pdfDate = itemView?.findViewById<TextView>(R.id.pdfDateTexView)
        var dragmenuimage = itemView?.findViewById<ImageView>(R.id.dragMenuimage)


    }
}