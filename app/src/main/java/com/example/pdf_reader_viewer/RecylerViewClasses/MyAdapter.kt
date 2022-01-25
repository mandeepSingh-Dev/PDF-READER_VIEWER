package com.example.pdf_reader_viewer.RecylerViewClasses

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pdf_reader_viewer.PdfView_Activity
import com.example.pdf_reader_viewer.PdfsTools_Activity
import com.example.pdf_reader_viewer.R
import com.example.pdf_reader_viewer.fragments.MergePdfs_Fragment
import com.example.pdf_reader_viewer.fragments.PdfTools_Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog

class MyAdapter( context1:Context,pdfList1:ArrayList<Items_pdfs>):RecyclerView.Adapter<MyAdapter.MyViewHolder>()
{
    var context:Context?=context1
    var pdfList:ArrayList<Items_pdfs>?=pdfList1
      var bottomsheetView:View?=null
    var bottomsheetDialogue:BottomSheetDialog?=null
    var pdfNamebottomsheet:TextView?=null
    var customOnClickListener:CustomOnClickListener?=null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        var view=LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        var myviewHolder=MyViewHolder(view)
          /**setting BottomSheetDialogue*/
        bottomsheetView=LayoutInflater.from(context).inflate(R.layout.bottomsheet_dialogue,parent,false)
        bottomsheetDialogue= BottomSheetDialog(context!!,R.style.Theme_Design_BottomSheetDialog)
        bottomsheetDialogue?.setContentView(bottomsheetView!!)
        pdfNamebottomsheet = bottomsheetDialogue?.findViewById(R.id.pdfName1_bottomsheet)

        return myviewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var displayName=pdfList?.get(position)?.title
        holder.pdfName.setText(displayName)
        holder.pdfsize.setText(pdfList?.get(position)?.size+"    "+pdfList?.size)

        holder.itemView.setOnClickListener {
            context?.startActivity(Intent(context, PdfView_Activity::class.java).putExtra("PDF_URI",pdfList?.get(position)?.appendeduri.toString()))
        }
        holder.menubutton.setOnClickListener {
          /*  pdfNamebottomsheet?.text=pdfList?.get(position)?.title
            bottomsheetDialogue?.show()*/
            customOnClickListener?.customOnClick(position)
        }
        pdfNamebottomsheet?.setOnClickListener {
            var intent=Intent(context!!,PdfsTools_Activity::class.java)
            intent.putExtra("pdfName",displayName)
            context?.startActivity(intent)

        }


    }

    override fun getItemCount(): Int {
        return pdfList?.size!!
    }

    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {

        var pdfsize:TextView=itemView.findViewById(R.id.sizePDF)
             var pdfName:TextView=itemView.findViewById(R.id.pdfName1)
        var menubutton=itemView.findViewById<ImageButton>(R.id.threedots_ImageButton)



    }
    //my defined interface to create click response from any class
   interface CustomOnClickListener{

       fun customOnClick(position: Int)
   }

    fun setCustomOnClickListenerr(customOnClickListener: CustomOnClickListener){
        this.customOnClickListener=customOnClickListener
    }

}

