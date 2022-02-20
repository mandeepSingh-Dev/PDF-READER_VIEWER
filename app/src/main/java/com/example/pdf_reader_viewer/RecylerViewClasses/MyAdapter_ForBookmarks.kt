package com.example.pdf_reader_viewer.RecylerViewClasses

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pdf_reader_viewer.MCustomOnClickListener
import com.example.pdf_reader_viewer.PdfView_Activity
import com.example.pdf_reader_viewer.R
import com.example.pdf_reader_viewer.Roomclasses.Room_For_BOOKMARKS.Items_Bookmarks
import com.example.pdf_reader_viewer.Roomclasses.Room_For_RecentPDFs.Items_RecentPdfs
import com.example.pdf_reader_viewer.Roomclasses.Room_For_RecentPDFs.MyRoomDatabase
import com.example.pdf_reader_viewer.UtilClasses.ConversionandUtilsClass
import com.example.pdf_reader_viewer.UtilClasses.PDFProp
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// In this adapter of bookmarks we have used recent_list_Item layout
class MyAdapter_ForBookmarks(context: Context,arrayList:ArrayList<Items_Bookmarks>):RecyclerView.Adapter<MyAdapter_ForBookmarks.MyViewholder>()
{
    var context=context
    var recentlist=arrayList
    var bottomsheetView:View?=null
    var bottomsheetDialogue:BottomSheetDialog?=null
    var pdfNamebottomsheet:TextView?=null
    var mCustomOnClickListener:MCustomOnClickListener?=null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        //here In bookmarks adpater we have used recent_list_Item layout
        var view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        var viewHolder=MyViewholder(view)

        bottomsheetView=LayoutInflater.from(context).inflate(R.layout.bottomsheet_dialogue,parent,false)
        bottomsheetDialogue= BottomSheetDialog(context,R.style.Theme_Design_BottomSheetDialog)
        bottomsheetDialogue?.setContentView(bottomsheetView!!)
        pdfNamebottomsheet = bottomsheetDialogue?.findViewById(R.id.pdfName1_bottomsheet)
        return viewHolder
    }

    @SuppressLint("Range")
    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        var itempdf = recentlist.get(position)

        if(itempdf!=null) {
            var pdfUri = itempdf.pdfUri
            var pdfSize = itempdf.pdfSize
            var pdfName = itempdf.pdfName
            var date = itempdf.date

            holder.pdfNameTextView.text = pdfName
            holder.recentPDFsize.text = ConversionandUtilsClass.bytesToMB(pdfSize)
            // holder.recentpdfDATE.text=ConversionandUtilsClass.convertToDate(System.currentTimeMillis()/1000L).get(1)
            holder.recentpdfDATE.text = ConversionandUtilsClass.convertToDate(date ).get(1)
                    // Log.d("85thrng",ConversionandUtilsClass.convertToDate(System.currentTimeMillis()/1000L).get(1))
               // }
          //  }



            holder.itemView.setOnClickListener {
                context?.startActivity(Intent(context, PdfView_Activity::class.java)
                    .setAction(PDFProp.MY_OPEN_ACTION)
                    .putExtra(PDFProp.PDF_APPENDED_URI, pdfUri)
                    .putExtra(PDFProp.PDF_TITLE, pdfName)
                    .putExtra(PDFProp.PDF_SIZE,pdfSize))
            }//itemview click
            holder.menubutton.setOnClickListener {
                /*  pdfNamebottomsheet?.text=pdfList?.get(position)?.title
                  bottomsheetDialogue?.show()*/

                mCustomOnClickListener?.onClick(position)
            }//menubutton click

        }



    }

    override fun getItemCount(): Int {
        return recentlist.size
    }

    class MyViewholder(itemview: View):RecyclerView.ViewHolder(itemview)
    {
      /*  var pdfNameTextView=itemview.findViewById<TextView>(R.id.recentPdfname)
        var recentPDFsize=itemview.findViewById<TextView>(R.id.recentPDFsize)
        var recentpdfDATE=itemview.findViewById<TextView>(R.id.recentpdfDATE)*/

        var pdfNameTextView=itemview.findViewById<TextView>(R.id.pdfName1)
        var recentPDFsize=itemview.findViewById<TextView>(R.id.sizePDF)
        var recentpdfDATE=itemview.findViewById<TextView>(R.id.dateModifiedText)
        var menubutton=itemView.findViewById<ImageView>(R.id.threedots_ImageButton)

    }
    fun setMCustomClickListenr(mCustomOnClickListener: MCustomOnClickListener)
    {
        this.mCustomOnClickListener=mCustomOnClickListener
    }
    suspend fun insertToRecentDATABASE(pdfname:String,pdfSize:String,uri: String,date:Long)= withContext(
        Dispatchers.IO){
        MyRoomDatabase.getInstance(context)?.daoMethod()?.insert(Items_RecentPdfs(pdfname,pdfSize,uri,date))
    }

}