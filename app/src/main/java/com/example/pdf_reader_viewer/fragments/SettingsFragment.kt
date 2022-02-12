package com.example.pdf_reader_viewer.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.pdf_reader_viewer.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.prefernce_screen, rootKey)
    }
}