package com.example.pdf_reader_viewer.Roomclasses.Room_For_BOOKMARKS

import android.content.Context
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(Items_Bookmarks::class), version = 2, exportSchema = false)
abstract class MyRoomDatabase2(): RoomDatabase(){

    companion object{
        var instance:MyRoomDatabase2?=null
        var databaseName="BOOKMARK_PDF_DATABASE"

        fun getInstance(context: Context):MyRoomDatabase2{
            if(instance==null)
            {
               instance = Room.databaseBuilder(context,MyRoomDatabase2::class.java, databaseName).build()
            }
            return instance!!
        }
    }

   abstract fun daoMethods():Dao_Bookmarks
}