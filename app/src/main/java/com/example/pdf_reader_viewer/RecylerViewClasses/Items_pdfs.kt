package com.example.pdf_reader_viewer.RecylerViewClasses

import android.net.Uri

data class Items_pdfs(var title:String, var size:String, var appendeduri: Uri?,var date_modified:String?,var relativePath:String,var bucket:String?)
{
    //this constructor made for below android 10 because relative path is not available in below android 10
    constructor(title:String,size:String,appendeduri:Uri?,date_modified:String?,bucket:String?):this( title,  size,  appendeduri, date_modified,"Not available",bucket)
}