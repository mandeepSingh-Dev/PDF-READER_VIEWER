package com.example.pdf_reader_viewer.Roomclasses.Room_For_RecentPDFs

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Recent_PDF_Table")
data class Items_RecentPdfs(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    val pdf_ID:Int,

    @ColumnInfo(name = "pdfName")
    val pdfName:String,

    @ColumnInfo(name = "pdfSize")
    val pdfSize:String,
    @ColumnInfo(name = "date")
    val date:Long,

    @ColumnInfo(name = "pdfUri")
    val pdfUri:String) {

    constructor( pdfName:String,pdfSize: String,pdfUri:String,date:Long):this(0,pdfName,pdfSize,date,pdfUri)
}