 package com.example.pdf_reader_viewer

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.ActionMode
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.pdf_reader_viewer.ViewPagerAdapter.MyFragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

 class MainActivity_ViewPagerHolder : AppCompatActivity()
 {

     private var viewpager2:ViewPager2?=null
     private var isReadPermissionGranted:Boolean?=false
     private var isWritePermissionGranted:Boolean?=false
     private lateinit var permissionLauncher:ActivityResultLauncher<Array<String>>

     /**requesting permissions request*/
     var launcher=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(),
         ActivityResultCallback {

         })
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)

         supportActionBar?.hide()

         viewpager2 = findViewById<ViewPager2>(R.id.viewpager2)
         setupViewPager2()

         /**here we check and request Read_External_Storage and WRITE_EXTERNAL_STORAGE*/
         requestPermssions()
         /**Granting MANAGE_EXTERNAL_STORAGE all files access permission*/
         requesting_MANAGE_ALL_DOCUMENT_Permission()

       //  launcher.launch()
       //  var list=ArrayList<String>()
     }

     fun setupViewPager2()
     {
         val myFragmentStateAdapter = MyFragmentStateAdapter(this)
         viewpager2?.adapter = myFragmentStateAdapter

         var texts= arrayOf("All Files","Recent","Bookmark","Tools")
         val tabLayout = findViewById<TabLayout>(R.id.tablayout)
         TabLayoutMediator(tabLayout, viewpager2!!) { tab, position ->
             tab.text=texts[position]
         }.attach()
         viewpager2?.offscreenPageLimit=2
         //end of TabLayoutMediator
     }

    fun requestPermssions()
     {
         /** Below two lines are checking the permissions are granted or not*/
         var readPermission=ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
         var writePersmission=ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
         var manage_documentsPermission=ContextCompat.checkSelfPermission(this,android.Manifest.permission.MANAGE_DOCUMENTS) == PackageManager.PERMISSION_GRANTED

         var sdkVersion=Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q
         /**here we created a mutablelist to add permissions in list if permissions are not granted*/
         var permissionList= mutableListOf<String>()

         isReadPermissionGranted = readPermission
         isWritePermissionGranted = writePersmission || sdkVersion

         if(!readPermission)
         {
                 permissionList.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
             Log.d("dkhnd","read")
         }
         if(!writePersmission)
         {
             permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
             Log.d("dkhnd","writePersmission")

         }
         if(!manage_documentsPermission)
         {
             permissionList.add(android.Manifest.permission.MANAGE_DOCUMENTS)
             Log.d("dkhnd","manage_documentsPermission")

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
             Log.d("3g3gf3wg",viewpager2?.currentItem.toString())

             super.onBackPressed()
         }

     }

     override fun onWindowStartingActionMode(callback: ActionMode.Callback?): ActionMode? {
         return super.onWindowStartingActionMode(callback)
         Log.d("34t3g3","efdiheiehn")
     }


 }