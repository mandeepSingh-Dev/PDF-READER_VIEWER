package com.example.pdf_reader_viewer.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pdf_reader_viewer.R
import com.example.pdf_reader_viewer.RecylerViewClasses.MyAdapter_RecentLists
import com.example.pdf_reader_viewer.Roomclasses.Items_RecentPdfs
import com.example.pdf_reader_viewer.Roomclasses.MyRoomDatabase
import com.example.pdf_reader_viewer.databinding.RecentListpdfFragmentBinding
import kotlinx.coroutines.*


class Recent_list_Fragment : Fragment() {

    var binding:RecentListpdfFragmentBinding?=null
    var recentdbList:ArrayList<Items_RecentPdfs>?=null
    var myAdapter:MyAdapter_RecentLists?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         recentdbList= ArrayList()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= RecentListpdfFragmentBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       var job= CoroutineScope(Dispatchers.IO).async {
           var liverecentList =MyRoomDatabase.getInstance(requireContext())?.daoMethod()?.query()

           withContext(Dispatchers.Main) {
               Log.d("3r883hgf3", liverecentList.value?.size.toString())
               Toast.makeText(requireContext(), liverecentList?.value?.size.toString(), Toast.LENGTH_LONG).show()

               liverecentList.observe(viewLifecycleOwner,
                   object : Observer<List<Items_RecentPdfs>> {
                       override fun onChanged(it: List<Items_RecentPdfs>?) {
                          myAdapter= MyAdapter_RecentLists(requireContext(),it as ArrayList<Items_RecentPdfs>)
                           binding?.recentRecyclerView?.layoutManager=GridLayoutManager(requireContext(),2)
                           binding?.recentRecyclerView?.adapter=myAdapter
                       }
                   })
           }
       }

    }

}