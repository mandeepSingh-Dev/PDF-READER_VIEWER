package com.example.pdf_reader_viewer.Roomclasses.Room_For_RecentPDFs

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pdf_reader_viewer.Roomclasses.Room_For_BOOKMARKS.Items_Bookmarks

@Dao
interface DaoRecent_Class{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(itemsRecentpdfs: Items_RecentPdfs)

    @Delete
    fun delete(itemsRecentpdfs: Items_RecentPdfs)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(itemsRecentpdfs: Items_RecentPdfs)

    @Query("SELECT * FROM RECENT_PDF_TABLE")
     fun query():LiveData<List<Items_RecentPdfs>>

     @Query("SELECT * FROM Recent_PDF_Table WHERE item_id Like :id")
     fun queryByID(id:Int): Items_RecentPdfs

    @Query("SELECT * FROM Recent_PDF_Table WHERE pdfUri Like :pdfUri")
    fun query(pdfUri: String): Items_RecentPdfs
}