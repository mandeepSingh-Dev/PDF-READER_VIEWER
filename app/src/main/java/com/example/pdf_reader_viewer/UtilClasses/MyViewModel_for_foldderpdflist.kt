package com.example.pdf_reader_viewer.UtilClasses

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdf_reader_viewer.RecylerViewClasses.Items_pdfs
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MyViewModel_for_foldderpdflist(val folderName:String,context:Context) : ViewModel() {

    var liveList : MutableLiveData<ArrayList<Items_pdfs>>? = null
     var contextt=context
    init{
        viewModelScope.launch {
            liveList = MutableLiveData()
            val list = Read_Pdf_Files(contextt).getPdfList_Folder(folderName)
            liveList?.value = list
            Log.d("3guj3f",this.isActive.toString())

        }
    }
    fun getPdflIst():MutableLiveData<ArrayList<Items_pdfs>>
    {
        return liveList!!
    }

    override fun onCleared() {
        super.onCleared()
        Toast.makeText(contextt,"folderlist_cleared",Toast.LENGTH_SHORT).show()
    }
}