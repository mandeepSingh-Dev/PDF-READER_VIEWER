 package com.example.pdf_reader_viewer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager2.widget.ViewPager2
import com.example.pdf_reader_viewer.UtilClasses.FragmentNames
import com.example.pdf_reader_viewer.UtilClasses.ViewAnimation
import com.example.pdf_reader_viewer.ViewPagerAdapter.MyFragmentStateAdapter
import com.example.pdf_reader_viewer.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.jar.Manifest

 class MainActivity_ViewPagerHolder : AppCompatActivity()
 {
     var binding:ActivityMainBinding?=null
     private var isRotate = true

     private var isReadPermissionGranted:Boolean?=false
     private var isWritePermissionGranted:Boolean?=false

     /**requesting permissions request*/
     var launcher=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(),
         ActivityResultCallback {


             var intent = Intent("PERMISSION_GRANTED")
             intent.putExtra("PERMISSIONGRANTED",true)
             LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
         })
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
         setContentView(binding?.root)

         supportActionBar?.hide()
        // getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//         supportActionBar?.setDisplayShowHomeEnabled(true)
//         supportActionBar?.setDisplayUseLogoEnabled(true)
//         supportActionBar?.setLogo(R.drawable.custom_pdf_free)
       //  getSupportActionBar()?.setDisplayShowCustomEnabled(true);
        // supportActionBar?.setCustomView(R.layout.list_images__item)

         setupViewPager2()

         /**here we check and request Read_External_Storage and WRITE_EXTERNAL_STORAGE*/
         //requestPermssions()
         /**Granting MANAGE_EXTERNAL_STORAGE all files access permission*/
       //  requesting_MANAGE_ALL_DOCUMENT_Permission()
      /*   var intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
         intent.setData(Uri.parse("package:" + getPackageName()))
         intent.putExtra("INTTN",1)
         launcherSetting.launch(intent)*/

         binding?.threedotsImageButtonMain?.setOnClickListener {
             var popupMenu = PopupMenu(this,it)
            // popupMenu.inflate(R.menu.bottombar_icons)
             popupMenu.getMenuInflater().inflate(R.menu.bottombar_icons, popupMenu.getMenu());
             popupMenu.setOnMenuItemClickListener {
                 when(it.itemId){

                     R.id.settingMenuitem -> {
                         startActivity(Intent(this,SettingsActviity::class.java))
                         return@setOnMenuItemClickListener true
                     }
                   /*  R.id.darkmodeMenuitem -> {
                         Log.d("3g9h34g",it.isChecked.toString())
                       //  it.setChecked(true)


                         Log.d("3g9h34g",it.isChecked.toString())
                         // Log.d("3f8h3f","sidhsidns")
                          *//* if(it.isChecked) {
                               Log.d("3f8h3f","isChecked")*//*
                             //  it.setCheckable(false)
                            // it.setChecked(false)
                              // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            //   it.setChecked(true)

                          // }else{
                             //  it.setCheckable(true)
                             //  it.setChecked(true)

                               Log.d("3f8h3f","isCheckedNOT")
                            //   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            //   it.setChecked(false)
                          // }
                         return@setOnMenuItemClickListener true

                     }*/
                     else ->{
                      return@setOnMenuItemClickListener onOptionsItemSelected(it)
                          }

                          }//when block
             }
             popupMenu.show()
         }

         ViewAnimation.init(binding?.fab2Linearlayout!!)
         ViewAnimation.init(binding?.fab3Linearlayout!!)

         binding?.topFab?.setOnClickListener {
             animate_fab_buttons()
         }
         binding?.fab2ImagetoPdf?.setOnClickListener {
             var intent=Intent(applicationContext,PdfsTools_Activity::class.java)

             intent.putExtra(FragmentNames.OPEN_IMGTOPDF_FRAGMENT, FragmentNames.OPEN_IMGTOPDF_FRAGMENT)
             startActivity(intent)
             Log.d("3igwn3bg","mskmsk")
         }
         binding?.fab3Createpdf?.setOnClickListener {
             var intent= Intent(applicationContext,PdfsTools_Activity::class.java)
             intent.putExtra(FragmentNames.OPEN_TEXTTOPDF_FRAGMENT, FragmentNames.OPEN_TEXTTOPDF_FRAGMENT)
             startActivity(intent)
         }
       //  launcher.launch()
       //  var list=ArrayList<String>()


     }  //oncreate closed here

     fun setupViewPager2()
     {
         val myFragmentStateAdapter = MyFragmentStateAdapter(this)
         binding?.viewpager2?.adapter = myFragmentStateAdapter

         var texts= arrayOf("Recent","starred","All Files","Tools")
         val tabLayout = findViewById<TabLayout>(R.id.tablayout)
         TabLayoutMediator(tabLayout, binding?.viewpager2!!) { tab, position ->
             tab.text=texts[position]
         }.attach()
         binding?.viewpager2?.offscreenPageLimit=2
         //end of TabLayoutMediator
     }

    fun requestPermssions()
     {
         /** Below two lines are checking the permissions are granted or not*/
         var readPermission=ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
         var writePersmission=ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
         var manage_documentsPermission=ContextCompat.checkSelfPermission(this,android.Manifest.permission.MANAGE_DOCUMENTS) == PackageManager.PERMISSION_GRANTED

         var sdkVersion = Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q
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

                 var sharedprefrence = getSharedPreferences("managed", Context.MODE_PRIVATE)
                   sharedprefrence.edit().putBoolean("MANGEEEGED",true).commit()

             } else {
                 Toast.makeText(this, "Manage_Permission NOT granted", Toast.LENGTH_SHORT).show()
                val uri = Uri.parse("package:" + getPackageName())
                 startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,uri))
             }
         }//END OF IF BLOCK WHERE WE USE CONDITION FOR ANDROID R.
     }

     fun animate_fab_buttons() {
         binding?.topFab?.animate()?.rotationBy(90f); //to rotate main fab 180 degree
         // to show upper fab buttons
         if (isRotate) {
             ViewAnimation.showIn(binding?.fab2Linearlayout!!)
             ViewAnimation.showIn(binding?.fab3Linearlayout!!)
             isRotate = false
         }//to hide upper fab button
         else {
             Log.d("38thg", isRotate.toString())
             ViewAnimation.showOut(binding?.fab2Linearlayout!!)
             ViewAnimation.showOut(binding?.fab3Linearlayout!!)
             isRotate = true
         }
     }

     override fun onBackPressed() {
         if(binding?.viewpager2?.currentItem!! > 0)
         {
             binding?.viewpager2?.setCurrentItem(0,true)
         }
         else{
             Log.d("3g3gf3wg",binding?.viewpager2?.currentItem.toString())

             super.onBackPressed()
         }

     }

     var contracts =object:ActivityResultContract<Intent,Int>(){
         override fun createIntent(context: Context, input: Intent?): Intent {
             return input!!
         }

         override fun parseResult(resultCode: Int, intent: Intent?): Int {
            // return intent?.data!!
             return intent?.getIntExtra("INTTN",0)!!
         }
     }
     var launcherSetting = registerForActivityResult(contracts, ActivityResultCallback {
         Log.d("efhwef",it.toString())

     })

   /*  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       //  var popupMenu = PopupMenu(this,findViewById(R.menu.bottombar_icons))
          menuInflater.inflate(R.menu.bottombar_icons,menu)
         return super.onCreateOptionsMenu(menu)
     }

     override fun onOptionsItemSelected(item: MenuItem): Boolean {

         when(item.itemId){

             R.id.settingMenuitem -> {
                                   startActivity(Intent(this,SettingsActviity::class.java))
                                     return true
                                      }
*//*
             R.id.darkmodeMenuitem -> {
                                      if(item.isChecked)
                                      {
                                          item.setChecked(false)
                                          //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                                      }
                                      else{
                                          item.setChecked(true)
                                         // item.set
                                         // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                                            }
                                       return true
                                        }
*//*
             else ->{
                 return super.onOptionsItemSelected(item)
             }
         }
     }*/


 }