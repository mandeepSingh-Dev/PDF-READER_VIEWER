package com.example.pdf_reader_viewer.RecylerViewClasses

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pdf_reader_viewer.MCustomOnClickListener
import com.example.pdf_reader_viewer.PdfView_Activity
import com.example.pdf_reader_viewer.PdfsTools_Activity
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
import java.text.DecimalFormat
import java.util.*

class MyAdapter( context1:Context,pdfList1:ArrayList<Items_pdfs>):RecyclerView.Adapter<MyAdapter.MyViewHolder>()
{
    var context:Context?=context1
    var pdfList:ArrayList<Items_pdfs>?=pdfList1
      var bottomsheetView:View?=null
    var bottomsheetDialogue:BottomSheetDialog?=null
    var pdfNamebottomsheet:TextView?=null
    var customOnClickListener:CustomOnClickListener?=null
    var mCustomOnClickListener:MCustomOnClickListener?=null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view=LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        val myviewHolder=MyViewHolder(view)

        /**setting BottomSheetDialogue*/

        bottomsheetView=LayoutInflater.from(context).inflate(R.layout.bottomsheet_dialogue,parent,false)
        bottomsheetDialogue= BottomSheetDialog(context!!,R.style.Theme_Design_BottomSheetDialog)
        bottomsheetDialogue?.setContentView(bottomsheetView!!)
        pdfNamebottomsheet = bottomsheetDialogue?.findViewById(R.id.pdfName1_bottomsheet)

        return myviewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var itemsPdfs=pdfList?.get(position)

        val appendeduri  =itemsPdfs?.appendeduri
        val title = itemsPdfs?.title
        val size = itemsPdfs?.size
        val date_modified = itemsPdfs?.date_modified

        holder.pdfName.setText(title)

        var sizemb=ConversionandUtilsClass.bytesToMB(size!!)
        holder.pdfsize.setText(sizemb)
        holder.dateTextView.setText(date_modified)

        var longseconds=date_modified?.toLong()
        if(longseconds!=null) {
            val datelist = ConversionandUtilsClass.convertToDate(longseconds)
            holder.dateTextView.text = datelist.get(0) //here 0 position gives date without time
        }

        holder.itemView.setOnClickListener {
            context?.startActivity(Intent(context, PdfView_Activity::class.java)
                .setAction(PDFProp.MY_OPEN_ACTION)
                .putExtra(PDFProp.PDF_APPENDED_URI,appendeduri?.toString())
                .putExtra(PDFProp.PDF_TITLE,title)
                .putExtra(PDFProp.PDF_SIZE,size))

            CoroutineScope(Dispatchers.Main).launch {
                insertToRecentDATABASE(title!!, size,appendeduri?.toString()!!,System.currentTimeMillis())
            }
        }
        holder.menubutton.setOnClickListener {
          /*  pdfNamebottomsheet?.text=pdfList?.get(position)?.title
            bottomsheetDialogue?.show()*/
            //customOnClickListener?.customOnClick(position)

            mCustomOnClickListener?.onClick(position)
        }
        pdfNamebottomsheet?.setOnClickListener {
            var intent=Intent(context!!,PdfsTools_Activity::class.java)
            intent.putExtra("pdfName",title)
            context?.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return pdfList?.size!!
    }

    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {

        var pdfsize:TextView=itemView.findViewById(R.id.sizePDF)
        var pdfName:TextView=itemView.findViewById(R.id.pdfName1)
        var menubutton=itemView.findViewById<ImageView>(R.id.threedots_ImageButton)
        var dateTextView=itemView.findViewById<TextView>(R.id.dateModifiedText)


    }
    //my defined interface to create click response from any class
   interface CustomOnClickListener{

       fun customOnClick(position: Int)
   }

    fun setCustomOnClickListenerr(customOnClickListener: CustomOnClickListener){
        this.customOnClickListener=customOnClickListener
    }

    fun setMCustomClickListenr(mCustomOnClickListener: MCustomOnClickListener)
    {
        this.mCustomOnClickListener=mCustomOnClickListener
    }

    fun bytesToMB(bytes:String):String{
         var bytess=bytes.toFloat()
        var MEGABYTE = 1024*1024
        var totalmb = bytess/MEGABYTE

        //formatting totalmb eg. 1.40 mb
        var df=DecimalFormat()
            df.maximumFractionDigits=2
        var formatedMB=df.format(totalmb)

        Log.d("43834hb10",formatedMB)
        return formatedMB
    }

    suspend fun insertToRecentDATABASE(pdfName:String,pdfSize:String,uri: String,date:Long)= withContext(Dispatchers.IO){
        MyRoomDatabase.getInstance(context!!)?.daoMethod()?.insert(Items_RecentPdfs(pdfName,pdfSize,uri,date))
    }


}

