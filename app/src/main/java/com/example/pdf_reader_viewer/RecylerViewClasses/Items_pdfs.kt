package com.example.pdf_reader_viewer.RecylerViewClasses

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class Items_pdfs(var title:String="N.A", var size:String="1000", var appendeduri: Uri?,var date_modified:String?="1644593839",var relativePath:String="N.A",var bucket:String?="N.A"):Parcelable
{
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readParcelable(Uri::class.java.classLoader),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString()
    ) {
    }

    //this constructor made for below android 10 because relative path is not available in below android 10
    constructor(title:String,size:String="1000",appendeduri:Uri?,date_modified:String?="1644593839",bucket:String?):this( title,  size,  appendeduri, date_modified,"Not available",bucket)

    //this constructor is for selectedPdfs for Merging Fragment recycler View
    constructor(title:String,size:String,appendeduri:Uri?):this(title,size,appendeduri,"1644593839","NO Relative Path","Bucket not available")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(size)
        parcel.writeParcelable(appendeduri, flags)
        parcel.writeString(date_modified)
        parcel.writeString(relativePath)
        parcel.writeString(bucket)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Items_pdfs> {
        override fun createFromParcel(parcel: Parcel): Items_pdfs {
            return Items_pdfs(parcel)
        }

        override fun newArray(size: Int): Array<Items_pdfs?> {
            return arrayOfNulls(size)
        }
    }
}