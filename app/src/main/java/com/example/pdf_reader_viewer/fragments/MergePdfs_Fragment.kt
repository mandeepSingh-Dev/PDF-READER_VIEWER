package com.example.pdf_reader_viewer.fragments
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pdf_reader_viewer.R
import com.example.pdf_reader_viewer.UtilClasses.ViewAnimation
import com.example.pdf_reader_viewer.databinding.MergePdfsFragmentBinding

class MergePdfs_Fragment : Fragment() {

    var binding: MergePdfsFragmentBinding? = null
    var isRotate = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = MergePdfsFragmentBinding.inflate(inflater)
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        ViewAnimation.init(binding?.fab2Linearlayout!!)
        ViewAnimation.init(binding?.fab3Linearlayout!!)
        ViewAnimation.init(binding?.fab4Linearlayout!!)


         binding?.topFab?.setOnClickListener {

           binding?.topFab?.animate()?.rotationBy(180f); //to rotate main fab 180 degree
       // to show upper fab buttons
            if(isRotate) {
                ViewAnimation.showIn(binding?.fab2Linearlayout!!)
                ViewAnimation.showIn(binding?.fab3Linearlayout!!)
                ViewAnimation.showIn(binding?.fab4Linearlayout!!)
                isRotate=false
            }//to hide upper fab button
            else{
                Log.d("38thg",isRotate.toString())
                ViewAnimation.showOut(binding?.fab2Linearlayout!!)
                ViewAnimation.showOut(binding?.fab3Linearlayout!!)
                ViewAnimation.showOut(binding?.fab4Linearlayout!!)

                isRotate=true

            }

        }

    }
}