package com.example.pdf_reader_viewer.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.pdf_reader_viewer.R
import com.example.pdf_reader_viewer.UtilClasses.SettingsProp
import java.net.URI
import java.net.URL

class SettingsFragment : PreferenceFragmentCompat() {

     var notificationKey:String?=null
     var show_Page_number:String?=null
     var dark_theme:String?=null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.prefernce_screen, rootKey)

        notificationKey = getString(R.string.Notifications)
        show_Page_number = getString(R.string.Show_Page_Numbers)
        dark_theme = getString(R.string.Dark_Theme)


        var notificationPrefernce =  findPreference(notificationKey)
        var showPagenumberPrefernce =  findPreference(show_Page_number)
        var dark_themePrefernce =  findPreference(dark_theme)

        var sharedPreferences  = activity?.getSharedPreferences(SettingsProp.SETTINGS_SHAREDPREFERNCE,Context.MODE_PRIVATE)
        var editor = sharedPreferences?.edit()



        notificationPrefernce.setOnPreferenceChangeListener(object:Preference.OnPreferenceChangeListener{
            override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
               if(newValue?.toString().equals("true"))
               {
                   Toast.makeText(requireContext(),"Notification Enabled",Toast.LENGTH_SHORT).show()
               }
                else{
                   Toast.makeText(requireContext(),"Notification Disabled",Toast.LENGTH_SHORT).show()
               }
                editor?.putString(SettingsProp.IS_NOTIFICATION_ENABLED,newValue.toString())
                editor?.apply()
                editor?.commit()
                return true
            }
        })

        showPagenumberPrefernce.setOnPreferenceChangeListener { preference, newValue ->

            editor?.putString(SettingsProp.IS_SHOWPAGENUMBERS,newValue.toString())
            editor?.apply()
            editor?.commit()
            return@setOnPreferenceChangeListener true
        }

        dark_themePrefernce?.setOnPreferenceChangeListener { preference, newValue ->
            if(newValue.toString().equals("true"))
            {
                Toast.makeText(requireContext(),"Dark Theme Enabled",Toast.LENGTH_SHORT).show()

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else{
                Toast.makeText(requireContext(),"Dark Theme Disabled",Toast.LENGTH_SHORT).show()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            return@setOnPreferenceChangeListener true
        } }


    }


