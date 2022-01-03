package com.example.pdf_reader_viewer

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pdf_reader_viewer.fragments.Fragment_BookMarks_list
import com.example.pdf_reader_viewer.fragments.Fragment_Recent_list
import com.example.pdf_reader_viewer.fragments.Fragment_pdf_list

class MyFragmentStateAdapter(fragmentAcivity:FragmentActivity):FragmentStateAdapter(fragmentAcivity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {

        if(position==0)
        {
            return Fragment_pdf_list()
        }
        else if(position==1)
        {
            return Fragment_Recent_list()
        }
        else{
            return Fragment_BookMarks_list()
        }

    }
}