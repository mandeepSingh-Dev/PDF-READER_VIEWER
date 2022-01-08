 package com.example.pdf_reader_viewer

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.audiofx.EnvironmentalReverb
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.jar.Manifest

 class MainActivity : AppCompatActivity()
 {

     private var viewpager2:ViewPager2?=null
     private var isReadPermissionGranted:Boolean?=false
     private var isWritePermissionGranted:Boolean?=false
     private lateinit var permissionLauncher:ActivityResultLauncher<Array<String>>

     var launcher=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(),
         ActivityResultCallback {

              // isReadPermissionGranted=it.get(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            // Log.d("cdcdverwv",isReadPermissionGranted.toString())

            /* var yoyo=it[android.Manifest.permission.READ_EXTERNAL_STORAGE] ?:isReadPermissionGranted
             Log.d("vdvhdjv",yoyo.toString())*/
         })
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)

         viewpager2 = findViewById<ViewPager2>(R.id.viewpager2)
         setupViewPager2()
        /**here we check and request Read_External_Storage and WRITE_EXTERNAL_STORAGE*/
         requestPermssions()
         /**Granting MANAGE_EXTERNAL_STORAGE all files access permission*/
         requesting_MANAGE_ALL_DOCUMENT_Permission()

         Log.d("5y5g",viewpager2?.currentItem.toString())
       //  launcher.launch()
       //  var list=ArrayList<String>()
     }

     fun setupViewPager2()
     {
         val myFragmentStateAdapter = MyFragmentStateAdapter(this)
         viewpager2?.adapter = myFragmentStateAdapter

         val tabLayout = findViewById<TabLayout>(R.id.tablayout)
         TabLayoutMediator(tabLayout, viewpager2!!) { tab, position ->
             tab.text = "OBJECT"
         }.attach()

         TabLayoutMediator(tabLayout, viewpager2!!, object : TabLayoutMediator.TabConfigurationStrategy {
             override fun onConfigureTab(tab: TabLayout.Tab, position: Int)
             {

             }
         })//end of TabLayoutMediator
     }

    fun requestPermssions()
     {
         /** Below two lines are checking the permissions are granted or not*/
         var readPermission=ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
         var writePersmission=ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
          var sdkVersion=Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q
         /**here we created a mutablelist to add permissions in list if permissions are not granted*/
         var permissionList= mutableListOf<String>()

         isReadPermissionGranted = readPermission
         isWritePermissionGranted = writePersmission || sdkVersion

         if(!readPermission)
         {
                 permissionList.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
         }
         if(!writePersmission)
         {
             permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
         }
         /**now here we request permissions from resultLauncher... only if permissions are not granted*/
         if(permissionList.isNotEmpty())
         {
             launcher.launch(permissionList.toTypedArray())
         }
     }
     fun requesting_MANAGE_ALL_DOCUMENT_Permission()
     {
         /**REQUESTING MANAGE_EXTERNAL_FILES PERMISSION FOR ACCESS ALL FILES/PDFs*/
         if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R) {
             if (Environment.isExternalStorageManager()) {
                 Toast.makeText(this, "Manage_Permission granted", Toast.LENGTH_SHORT).show()
             } else {
                 Toast.makeText(this, "Manage_Permission NOT granted", Toast.LENGTH_SHORT).show()
                val uri = Uri.parse("package:" + getPackageName())
                 startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,uri))
             }
         }//END OF IF BLOCK WHERE WE USE CONDITION FOR ANDROID R.
     }

     override fun onBackPressed() {
         if(viewpager2?.currentItem!! > 0)
         {
             viewpager2?.setCurrentItem(0,true)
         }
         else{
             super.onBackPressed()
         }

     }
 }