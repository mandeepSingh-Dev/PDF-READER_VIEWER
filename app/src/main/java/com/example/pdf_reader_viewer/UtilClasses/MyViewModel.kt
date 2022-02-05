package com.example.pdf_reader_viewer.UtilClasses

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pdf_reader_viewer.RecylerViewClasses.Items_pdfs
import com.example.pdf_reader_viewer.UtilClasses.Read_Pdf_Files
import kotlinx.coroutines.launch

//ViewModel class for getting livedataPDFList from Read_Pdf_Files repositry
class MyViewModel(application: Application):AndroidViewModel(application)
{
       var application1=application
       var pdflist:MutableLiveData<ArrayList<Items_pdfs>>?=null

    init{
        viewModelScope.launch {
            pdflist= MutableLiveData<ArrayList<Items_pdfs>>()
            var pdflisttt= Read_Pdf_Files(application1).getPdfList_2()
            pdflist?.value=pdflisttt
           // Log.d("388ry8uwhfd",pdflist?.size.toString())

        }
        }


      /*  suspend fun getList():ArrayList<Items_pdfs>{
            Log.d("r7gfeuf","udgsgycysc")
          //  pdflist=Read_Pdf_Files(application1).getPdfList_2()
            pdflist=getPdfList_2()
            Log.d("3r8y38f",pdflist?.size.toString())
     return pdflist!!
        }*/
     fun getpdflistttt():MutableLiveData<ArrayList<Items_pdfs>>{
        return pdflist!!
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("2f7h3uvch","cleared")
        Toast.makeText(application1,"cleared",Toast.LENGTH_LONG).show()
    }



}

