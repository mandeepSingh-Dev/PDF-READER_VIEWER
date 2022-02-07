package com.example.pdf_reader_viewer.Roomclasses

import android.app.Activity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pdf_reader_viewer.R

@Database(entities = arrayOf(Items_RecentPdfs::class), version = 1, exportSchema = false)
abstract class MyRoomDatabase:RoomDatabase() {


    companion object  {

        var instance: MyRoomDatabase? = null
        /**database Name*/
        var databaseName="RECENT_PDF_DATABASE"

        /**singleton class method*/
        @Synchronized
        fun getInstance(context: Context): MyRoomDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, MyRoomDatabase::class.java, databaseName).build()
            }
            return instance!!
        }
    }

    abstract fun daoMethod():DaoRecent_Class

}