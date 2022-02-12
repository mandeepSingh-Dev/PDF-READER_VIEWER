package com.example.pdf_reader_viewer.RecylerViewClasses

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pdf_reader_viewer.MCustomOnClickListener
import com.example.pdf_reader_viewer.PdfView_Activity
import com.example.pdf_reader_viewer.R
import com.example.pdf_reader_viewer.Roomclasses.Room_For_RecentPDFs.Items_RecentPdfs
import com.example.pdf_reader_viewer.Roomclasses.Room_For_RecentPDFs.MyRoomDatabase
import com.example.pdf_reader_viewer.UtilClasses.ConversionandUtilsClass
import com.example.pdf_reader_viewer.UtilClasses.PDFProp
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyAdapter_RecentLists(context: Context,arrayList:ArrayList<Items_RecentPdfs>):RecyclerView.Adapter<MyAdapter_RecentLists.MyViewholder>()
{
    var context=context
    var recentlist=arrayList
    var mCustomOnClickListener:MCustomOnClickListener?=null
    var bottomsheetView:View?=null
    var bottomsheetDialogue:BottomSheetDialog?=null
    var pdfNamebottomsheet:TextView?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
            var view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
            var viewHolder=MyViewholder(view)

        bottomsheetView=LayoutInflater.from(context).inflate(R.layout.bottomsheet_dialogue,parent,false)
        bottomsheetDialogue= BottomSheetDialog(context!!,R.style.Theme_Design_BottomSheetDialog)
        bottomsheetDialogue?.setContentView(bottomsheetView!!)
        pdfNamebottomsheet = bottomsheetDialogue?.findViewById(R.id.pdfName1_bottomsheet)
        return viewHolder
    }

    @SuppressLint("Range")
    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
         var itempdf=recentlist.get(position)
        var uri=itempdf.pdfUri
        var size1 = itempdf.pdfSize
        var name1= itempdf.pdfName
       // CoroutineScope(Dispatchers.IO).launch {
          //  var cursor = context.contentResolver?.query(Uri.parse(uri), null, null, null, null)
          //  if (cursor != null) {
               // if (cursor?.moveToFirst()!!) {
                  /*  name = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                    if (name == null) {
                        name = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE))
                    }
                    var size = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE))
*/
                   // withContext(Dispatchers.Main) {
                    holder.pdfNameTextView.text = name1
                    holder.recentPDFsize.text = ConversionandUtilsClass.bytesToMB(size1)
                    // holder.recentpdfDATE.text=ConversionandUtilsClass.convertToDate(System.currentTimeMillis()/1000L).get(1)
                    holder.recentpdfDATE.text = ConversionandUtilsClass.convertToDate(itempdf.date / 1000L).get(0)

                        holder.itemView.setOnClickListener {
                            context?.startActivity(Intent(context, PdfView_Activity::class.java).putExtra(PDFProp.PDF_APPENDED_URI,uri.toString()).putExtra(PDFProp.PDF_TITLE,name1))
                          //this is already recent list so no add in database
                           /* CoroutineScope(Dispatchers.Main).launch {
                                insertToRecentDATABASE(itemsPdfs?.appendeduri.toString(),System.currentTimeMillis())
                            }*/
                        }//itemview click
                        holder.menubutton.setOnClickListener {
                            /*  pdfNamebottomsheet?.text=pdfList?.get(position)?.title
                              bottomsheetDialogue?.show()*/

                            mCustomOnClickListener?.onClick(position)
                        }//menubutton click
                        holder.itemView.setOnClickListener {

                            context?.startActivity(Intent(context, PdfView_Activity::class.java)
                                .putExtra(PDFProp.PDF_APPENDED_URI,uri)
                                .putExtra(PDFProp.PDF_TITLE,name1))
                        }
              //  } //withcontext
              //  } //cursor.movetonext
          //  } //cursor!=null
      //  }//corotuinescope closes
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
        var menubutton=itemView.findViewById<ImageView>(R.id.threedots_ImageButton)
    }

    fun setMCustomClickListenr(mCustomOnClickListener: MCustomOnClickListener)
    {
        this.mCustomOnClickListener=mCustomOnClickListener
    }
    suspend fun insertToRecentDATABASE(pdfname:String,pdfSize:String,uri: String,date:Long)= withContext(Dispatchers.IO){
        MyRoomDatabase.getInstance(context!!)?.daoMethod()?.insert(Items_RecentPdfs(pdfname,pdfSize,uri,date))
    }

}