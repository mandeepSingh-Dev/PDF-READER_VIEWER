package com.example.pdf_reader_viewer.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pdf_reader_viewer.R
import com.example.pdf_reader_viewer.RecylerViewClasses.MyAdapter_ForBookmarks
import com.example.pdf_reader_viewer.RecylerViewClasses.MyAdapter_RecentLists
import com.example.pdf_reader_viewer.Roomclasses.Room_For_BOOKMARKS.Items_Bookmarks
import com.example.pdf_reader_viewer.Roomclasses.Room_For_BOOKMARKS.MyRoomDatabase2
import com.example.pdf_reader_viewer.databinding.BookMarksListFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookMarks_list_Fragment_ : Fragment() {

    var binding:BookMarksListFragmentBinding? = null

    var observer:Observer<List<Items_Bookmarks>>?=null
    var adapter:MyAdapter_ForBookmarks?=null
    var  liveList:LiveData<List<Items_Bookmarks>>?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = BookMarksListFragmentBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer = object :Observer<List<Items_Bookmarks>>{
            override fun onChanged(it: List<Items_Bookmarks>?) {
                Log.d("378fh3",it?.size.toString())
                    adapter = MyAdapter_ForBookmarks(requireContext(), it as ArrayList<Items_Bookmarks>)
                    binding?.bookmarksRecyclerView?.layoutManager =
                        GridLayoutManager(requireContext(), 2)
                    binding?.bookmarksRecyclerView?.adapter = adapter

            }
        }
        CoroutineScope(Dispatchers.Main).launch {
             liveList=MyRoomDatabase2.getInstance(requireContext()).daoMethods().query()
            liveList?.observe(viewLifecycleOwner,observer!!)
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        liveList?.removeObserver(observer!!)
        Log.d("38fhncs/",liveList?.hasObservers().toString())

    }

}