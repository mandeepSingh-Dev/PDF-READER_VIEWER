package com.example.pdf_reader_viewer.UtilClasses

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MyViewmodel_InstanceFactory_folderlist(val folderName:String, context:Context) : ViewModelProvider.Factory
{

    val context=context

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MyViewModel_for_foldderpdflist(folderName,context) as T

    }
}