package com.example.pdf_reader_viewer.Roomclasses.Room_For_BOOKMARKS

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class Items_Bookmarks(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    var pdf_ID:Int,

    @ColumnInfo(name = "pdfName")
    var pdfName:String,

    @ColumnInfo(name = "pdfSize")
    var pdfSize:String,
    @ColumnInfo(name = "date")
    var date:Long,

    @ColumnInfo(name = "pdfUri")
    var pdfUri:String) {

    constructor( pdfUri:String,pdfName: String,pdfSize: String,date:Long):this(0,pdfName,pdfSize,date,pdfUri)
    //constructor( pdfUri: String):this(0,11L,pdfUri)
}