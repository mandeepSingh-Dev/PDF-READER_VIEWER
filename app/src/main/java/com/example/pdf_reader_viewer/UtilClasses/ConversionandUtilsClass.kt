package com.example.pdf_reader_viewer.UtilClasses

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import android.widget.Toast
import com.example.pdf_reader_viewer.RecylerViewClasses.Items_pdfs
import com.google.android.material.snackbar.Snackbar
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.InputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class ConversionandUtilsClass
{
    companion object {
        var arrayList: ArrayList<String>? = null
      /*  init {
            arrayList = ArrayList()
        }*/


        var simpleDateFormat: SimpleDateFormat? = null

        //this method for MyAdapter class
        fun bytesToMB(bytes: String): String {
             var formatedMB=""
            try {
                var bytess = bytes.toFloat()
                var MEGABYTE = 1024 * 1024
                var totalmb = bytess / MEGABYTE

                //formatting totalmb eg. 1.40 mb
                var df = DecimalFormat()
                df.maximumFractionDigits = 2
                 formatedMB = df.format(totalmb)

                Log.d("43834hb10", formatedMB)
            }catch (e:Exception){}
            return formatedMB+" mb"
        }

        //this method is for pdfOperation class
        fun convertContentUri_toInputStream(activity: Activity, appendedUri: Uri): InputStream {
            var parcelFileDescriptor =
                activity?.contentResolver?.openFileDescriptor(appendedUri, "r")!!
            val fileDescriptor: FileDescriptor = parcelFileDescriptor?.fileDescriptor!!
            var inputStraem = FileInputStream(fileDescriptor)

            return inputStraem
        }

        //this method for MyAdapter class
        fun convertToDate(seconds: Long): ArrayList<String> {

            arrayList=ArrayList()
            Log.d("3g3g3", seconds.toString())
            var number = 1000L

            var millisconds = seconds?.times(number)
            Log.d("3g8h3v", millisconds.toString())

            simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")  //without hrs and minutes
            var dateonly = simpleDateFormat?.format(millisconds).toString()
            arrayList?.add(dateonly)


            simpleDateFormat = SimpleDateFormat("EEE, d MMM yyyy h:mm a") //with hrs and minutes
            var dateNtime = simpleDateFormat?.format(millisconds).toString()
            arrayList?.add(dateNtime)


            Log.d("hgikenvb999999999910", dateNtime + arrayList?.size)

            return arrayList!!
        }

        fun deleteContent(activity: Activity, uri: Uri): Boolean {
            val takeFlags: Int =
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            // Check for the freshest data.
            activity?.contentResolver?.takePersistableUriPermission(uri, takeFlags)
            var bool = DocumentsContract.deleteDocument(
                activity?.applicationContext?.contentResolver!!,
                uri
            )
            Log.d("867r38fh", bool.toString())

            Toast.makeText(activity?.applicationContext, "PDF deleted", Toast.LENGTH_SHORT).show()
            return bool
        }

        fun formattingof_Pagenumber(atPage: String): List<String> {

            var list: MutableList<String> = mutableListOf()
            //return populated list only if atPage is not empty
            if (!atPage.isEmpty()) {
                var numberList: MutableList<String> = mutableListOf()
                // var pageee=atPage.trim()
                // Log.d("eifnefe",pageee)
                //val pagenumberstr = atPage.replace("\\s", "")
                var pagenumberstr = atPage.replace(" ", "")
                var regex = Regex("[a-zA-Z.+!@#$%^&*():*/><?:]*")
                pagenumberstr = atPage.replace(regex, "")


                Log.d("gfe8ghe", pagenumberstr)
                if (atPage.contains("-")) {
                    val finalpagenumberlist = pagenumberstr.split("-")
                    finalpagenumberlist.forEach {
                        Log.d("893u3ng", it)
                    }
                    return finalpagenumberlist
                } else {
                    numberList.add(pagenumberstr)
                    numberList.forEach {
                        Log.d("ijff8e", it + "\n" + numberList?.size.toString())
                    }
                    return numberList
                }
                list.addAll(numberList)
            }
            //return populated list only if atPage is not empty
            else {
                return list
            }
        }


        fun <T> swap(list: ArrayList<T>, i: Int, j: Int): ArrayList<T> {
            var arr = list.toArray()
            var temp: T
            //  if(i<0 || i<=list.size || j<0)
            if (i >= 0 && j >= 0 && i <= arr.size) {
                temp = arr[i] as T
                arr[i] = arr[j]
                arr[j] = temp
            }
            return arr.toList() as ArrayList<T>
        }
      }//closing of companion object

//    fun sortingList(list:ArrayList<Items_pdfs>){
//        for(i in 0..list.size-1)
//        {
//            var j = i+1
//            if(list.get(i).date_modified?.toInt()!! < list.get(j).date_modified?.toInt()!!)
//            {
//                list.get()
//            }
//        }
//    }
}