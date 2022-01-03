 package com.example.pdf_reader_viewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

 class MainActivity : AppCompatActivity() {
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)

         val viewpager2 = findViewById<ViewPager2>(R.id.viewpager2)
         val myFragmentStateAdapter = MyFragmentStateAdapter(this)
         viewpager2.adapter = myFragmentStateAdapter

         val tabLayout = findViewById<TabLayout>(R.id.tablayout)
         TabLayoutMediator(tabLayout, viewpager2) { tab, position ->
             tab.text = "OBJECT"
         }.attach()
         TabLayoutMediator(tabLayout, viewpager2, object : TabLayoutMediator.TabConfigurationStrategy {
                 override fun onConfigureTab(tab: TabLayout.Tab, position: Int)
                 {

                 }
             })//end of TabLayoutMediator
     }
 }