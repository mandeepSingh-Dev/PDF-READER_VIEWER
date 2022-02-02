package com.example.pdf_reader_viewer.RecylerViewClasses

import android.net.Uri

data class Items_pdfs(var title:String="N.A", var size:String="N.A", var appendeduri: Uri?,var date_modified:String?="N.A",var relativePath:String="N.A",var bucket:String?="N.A")
{
    //this constructor made for below android 10 because relative path is not available in below android 10
    constructor(title:String,size:String="N.A",appendeduri:Uri?,date_modified:String?="N.A",bucket:String?):this( title,  size,  appendeduri, date_modified,"Not available",bucket)

    //this constructor is for selectedPdfs for Merging Fragment recycler View
    constructor(title:String,size:String,appendeduri:Uri?):this(title,size,appendeduri,"00.00.0000","NO Relative Path","Bucket not available")
}