package com.example.pdf_reader_viewer.Roomclasses.Room_For_BOOKMARKS

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class Items_Bookmarks(
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

    constructor( pdfUri:String,pdfName: String,pdfSize: String,date:Long):this(0,pdfName,pdfSize,date,pdfUri)
    //constructor( pdfUri: String):this(0,11L,pdfUri)
}