package com.example.pdf_reader_viewer.fragments
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pdf_reader_viewer.MCustomOnClickListener
import com.example.pdf_reader_viewer.R
import com.example.pdf_reader_viewer.RecylerViewClasses.Items_pdfs
import com.example.pdf_reader_viewer.RecylerViewClasses.MyAdapter
import com.example.pdf_reader_viewer.RecylerViewClasses.MyAdapter_ForMerge
import com.example.pdf_reader_viewer.UtilClasses.PDFProp
import com.example.pdf_reader_viewer.UtilClasses.PdfOperations
import com.example.pdf_reader_viewer.UtilClasses.ViewAnimation
import com.example.pdf_reader_viewer.databinding.MergePdfsFragmentBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.OutputStream

class MergePdfs_Fragment : Fragment() {

    private var binding: MergePdfsFragmentBinding? = null
    private var isRotate = true
    private var pdfUrifromFileManager: Uri? = null
    private var selectedPdf_list: ArrayList<Items_pdfs>? = null
    private var adapter: MyAdapter_ForMerge? = null
    private var alertDialog: AlertDialog? = null
    private var chip: Chip? = null
    private var getNameinputlayoutMERGE: TextInputLayout? = null
    private var secureinputLayout1: TextInputLayout? = null
    private var mergeDialogueButton: MaterialButton? = null
    private var alertDialogprogress: AlertDialog? = null
    private var importingDailogTextview: TextView? = null
    private var importingnumberDailogText: TextView? = null

    lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedPdf_list = ArrayList()
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

//        var intent=Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
//        launcher.launch(intent)

        //whenever we click menu button of specific pdf then here uri will come
        if (arguments != null) {
            var pdfTitle = arguments?.getString(PDFProp.PDF_TITLE)
            var pdfSize = arguments?.getString(PDFProp.PDF_SIZE)
            var pdfUri = arguments?.getParcelable<Uri>(PDFProp.PDF_APPENDED_URI)
            //var pdfdate=arguments?.getString(PDFProp.PDF_DATEMODIFIED)

             Log.d("efhhegfehd",pdfUri.toString()+pdfSize+pdfTitle)

            //getting newly populated list from Imagetopdf fragment
            var listFromimgtopdf = arguments?.getParcelableArrayList<Items_pdfs>(PDFProp.MERGE_SELECTED_LIST)
           if(listFromimgtopdf!=null) {
               selectedPdf_list?.addAll(listFromimgtopdf!!)
           }

            if (pdfTitle != null && pdfSize != null && pdfUri != null) {
                Log.d("CHECKINGFRAG", "coming from list  ")
                selectedPdf_list?.add(Items_pdfs(pdfTitle!!, pdfSize!!, pdfUri!!))
            } else {
                Log.d("CHECKINGFRAG", "coming from just tool fragment")
            }

            createrecyclerView()
        }
        alertDialog = createDaiolgue()
        binding?.mergeButton?.setOnClickListener {
            // Log.d("34ge",selectedPdf_list?.get(0)?.appendeduri.toString())
            /*mergeSelectedPdfs(selectedPdf_list!!,alertDialog!!)*/
            try {
                alertDialog?.show()
            }catch (e:Exception){}
        }
        var view22 = LayoutInflater.from(requireContext()).inflate(
            R.layout.custom_progress_dialogue,
            activity?.findViewById<ViewGroup>(R.id.content),
            false
        )
        alertDialogprogress = createAlertdialogue(view22)


        Log.d("vdvdvegvd", "dfdsfdfvd")
        ViewAnimation.init(binding?.fab2Linearlayout!!)
        ViewAnimation.init(binding?.fab3Linearlayout!!)
        ViewAnimation.init(binding?.fab4Linearlayout!!)

        binding?.topFab?.setOnClickListener {
            animate_fab_buttons()
        }

        binding?.devicePdfFab?.setOnClickListener {
            //getting pdf document from device \
            var intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("application/pdf")
            launcher.launch(intent)
            // Toast.makeText(requireContext(),pdfUrifromFileManager?.toString(),Toast.LENGTH_LONG).show()
        }
        val manager = activity?.supportFragmentManager
        manager?.popBackStack()
       // manager?.isStateSaved
       // val fragmentPopped: Boolean = manager?.popBackStackImmediate(ImageTo_Pdf_Fragment().javaClass.name, 0)!!

        binding?.imagTopdfFab?.setOnClickListener {
           //sending populated or empty selectedPdf_list to Imagetopdf fragment to get newly populated or empty list
            var bundle = Bundle()
            bundle.putString(PDFProp.COMING_FROM_MERGEFRAG,PDFProp.COMING_FROM_MERGEFRAG)
            bundle.putParcelableArrayList(PDFProp.MERGE_SELECTED_LIST,selectedPdf_list)
            var imagetoPdfFragment = ImageTo_Pdf_Fragment()
            imagetoPdfFragment.arguments = bundle

           // if(!fragmentPopped) {
                manager?.beginTransaction()
                    ?.replace(R.id.fragmentContainerView, imagetoPdfFragment)
                    ?.addToBackStack(imagetoPdfFragment.javaClass.name)?.commit()
           // }
                //here we clear this selectedPdf_list because after coming back
            // from imagetopdf fragment duplicates recylerview items was created
            //so whenever we go imagetopdf fragment then new list will be come
           selectedPdf_list?.removeAll(selectedPdf_list!!)
          /*  val imagetoPdfFragment = ImageTo_Pdf_Fragment()
            imagetoPdfFragment.show(activity?.supportFragmentManager!!.beginTransaction(), "ImageTo_Pdf_Fragment")
   */     }


    }

    fun animate_fab_buttons() {
        binding?.topFab?.animate()?.rotationBy(180f); //to rotate main fab 180 degree
        // to show upper fab buttons
        if (isRotate) {
            ViewAnimation.showIn(binding?.fab2Linearlayout!!)
            ViewAnimation.showIn(binding?.fab3Linearlayout!!)
            ViewAnimation.showIn(binding?.fab4Linearlayout!!)
            isRotate = false
        }//to hide upper fab button
        else {
            Log.d("38thg", isRotate.toString())
            ViewAnimation.showOut(binding?.fab2Linearlayout!!)
            ViewAnimation.showOut(binding?.fab3Linearlayout!!)
            ViewAnimation.showOut(binding?.fab4Linearlayout!!)
            isRotate = true
        }
    }

    //getting uri from another intent Activity means from flemanager
    var activityResultContracts = object : ActivityResultContract<Intent, Uri>() {
        override fun createIntent(context: Context, input: Intent?): Intent {
            return input!!
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri {
            var uri = intent?.data
            if (uri != null) {
                pdfUrifromFileManager = uri
            } else {
                pdfUrifromFileManager = Uri.parse("null uri")
            }
            return pdfUrifromFileManager!!
        }
    }
    var launcher =
        registerForActivityResult(activityResultContracts, object : ActivityResultCallback<Uri> {
            override fun onActivityResult(result: Uri?) {
                Log.d("3tfr3wgfv", "Ss")
                if (result.toString().equals("null uri")) {
                    //do nothing bcoz uri is null
                } else {
                    Log.d("3ty378hfg", result.toString() + "yhuigjr")



                    Log.d(
                        "585gydjk",
                        activity?.contentResolver?.openInputStream(result!!).toString() + "sfsdf"
                    )
                    var list = getMetadata_fromUri(pdfUrifromFileManager!!)

                    selectedPdf_list?.add(
                        Items_pdfs(
                            list.get(0),
                            list.get(1),
                            pdfUrifromFileManager
                        )
                    )
                    //   Log.d("3f3f3w",selectedPdf_list?.size.toString())
                    createrecyclerView()
                    // Log.d("3scsdf56565",getMetadata_fromUri(pdfUrifromFileManager!!)+"fsdfdfd")
                    //  PdfOperations(activity!!).splittingPdf(requireActivity(),pdfUrifromFileManager!!)

                }

                // Toast.makeText(requireContext(),pdfUrifromFileManager?.toString(),Toast.LENGTH_LONG).show()

            }
        })

    //creating adapter and set to recyclerView
    fun createrecyclerView() {
        adapter = MyAdapter_ForMerge(requireContext(), selectedPdf_list!!)
        binding?.mergePdfListRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
        binding?.mergePdfListRecyclerView?.adapter = adapter

        myAdapterClickListner(adapter!!,selectedPdf_list!!)



    }

    fun mergeSelectedPdfs(pdflist: ArrayList<Items_pdfs>, pdfName: String, password: String,outputStream: OutputStream) {
        Log.d("48hgh4g4", pdflist.size.toString() + "wiodhwuedhwdiwwdwwfwfoh7ufg37j9")
        // PdfOperations(requireActivity()).mergePdfs(pdflist,"mymergesFinal")

        CoroutineScope(Dispatchers.Main).launch {
            Log.d("4g93yhg3g", password)
            importingDailogTextview =
                alertDialogprogress?.findViewById<TextView>(R.id.importingtextview)
            importingnumberDailogText =
                alertDialogprogress?.findViewById<TextView>(R.id.importedNumberTextview)
            //these dailog textview had importing and importing number texts
            importingDailogTextview?.text = "please wait..."
            importingnumberDailogText?.visibility = View.GONE
            alertDialogprogress?.show()

            PdfOperations(requireActivity()).myCustomNativeMergePdf(pdflist, pdfName, password,outputStream)

            alertDialogprogress?.hide()


        }

        // myCustomNativeMergePdf(pdflist)

    }


    @SuppressLint("Range")
    fun getMetadata_fromUri(uri: Uri): ArrayList<String> {

        var arraylist: ArrayList<String> = ArrayList()
        var resolver = requireContext().contentResolver

        var cursor = resolver.query(uri, null, null, null, null)
        if (cursor?.moveToFirst()!!) {
            var displayName =
                cursor?.getString(cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))!!
            var size =
                cursor?.getString(cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE))!!
            arraylist.add(displayName)
            arraylist.add(size)
        }
        //  var displayName=metadata.extractMetadata(MediaMetadata.METADATA_KEY_DISPLAY_TITLE.toInt())

        //  metadata.release()
        return arraylist!!
    }

    fun createDaiolgue(): AlertDialog {
        var dailogueBuilder =
            AlertDialog.Builder(requireContext(), R.style.Theme_AppCompat_Dialog_Alert)
        var viewGroup = activity?.findViewById<ViewGroup>(R.id.content)
        var view = LayoutInflater.from(requireContext())
            .inflate(R.layout.custom_merge_dialgue, viewGroup, false)

        chip = view.findViewById(R.id.secureChip)
        getNameinputlayoutMERGE = view.findViewById(R.id.getNameinputlayoutMERGE)
        secureinputLayout1 = view.findViewById(R.id.secureinputlayoutMERGE)
        mergeDialogueButton = view.findViewById(R.id.mergeDialgueButton)
        // getNameinputlayoutMERGE = view.findViewById(R.id.getNameinputlayoutMERGE)
        chip?.setOnClickListener {

            if (!chip?.isChecked!!) {
                secureinputLayout1?.visibility = View.GONE
                //Toast.makeText(requireContext(), "close icon visible", Toast.LENGTH_LONG).show()
            } else {
                secureinputLayout1?.visibility = View.VISIBLE
                //  Toast.makeText(requireContext(), " close not visible", Toast.LENGTH_LONG).show()
            }
        }

        mergeDialogueButton?.setOnClickListener {
            //hiding dialogue when click merge dialogue button

            var pdfNamee = getNameinputlayoutMERGE?.editText?.text.toString()

            //checking pdfname is empty or not
            if (pdfNamee.isEmpty()) {
                Log.d("38fg3bf", pdfNamee)
                Toast.makeText(requireContext(), "name is null", Toast.LENGTH_SHORT).show()
                getNameinputlayoutMERGE?.error = "Invalid Name"
            } else {
                //creating intent for launcher with pdfnamee
                var intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
                intent.type = "application/pdf"
                intent.putExtra(Intent.EXTRA_TITLE,pdfNamee)

                //checking if chip is checked and if checked then merge with encryptedpassword
                if (chip?.isChecked!!) {
                  //  Toast.makeText(requireContext(), "chip is checked", Toast.LENGTH_SHORT).show()
                    password = secureinputLayout1?.editText?.text.toString()
                    //checking password is empty or not
                    if (password.isEmpty()) {
                        secureinputLayout1?.error = "please enter password."
                        Toast.makeText(requireContext(), "password is null", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        alertDialog?.hide()
                        Log.d("38fh3ifgv3fg3f37fgh", selectedPdf_list?.size.toString())

                        //mergeSelectedPdfs(selectedPdf_list!!,pdfNamee,password)

                        //launcherMethod(password).launch(intent)
                        Toast.makeText(requireContext(),"${selectedPdf_list?.size}  PDFs selected",Toast.LENGTH_LONG).show()
                        if(selectedPdf_list?.size!! > 1) {
                            launcher4.launch(intent)
                        }else {
                            Toast.makeText(requireContext(),"please select Two or more PDFs",Toast.LENGTH_LONG).show()
                        }
                        //launcher4.launch(intent)

                        Toast.makeText(requireContext(), "password is not null", Toast.LENGTH_SHORT)
                            .show()

                    }
                }
                //else merge without encrypted password
                else {
                    password = "NULL"
                    Toast.makeText(requireContext(), "chip is not checked", Toast.LENGTH_SHORT).show()
                    alertDialog?.hide()
                    Log.d("38fh3ifgv3fg3f37fgh", selectedPdf_list?.size.toString())
                    //here NULL means password and pdf will merge without password Encrypted
                   // launcherMethod("NULL").launch(intent)
                    Toast.makeText(requireContext(),"${selectedPdf_list?.size} PDFs selected",Toast.LENGTH_LONG).show()
                    if(selectedPdf_list?.size!! > 1) {
                        launcher4.launch(intent)
                    }else {
                        Toast.makeText(requireContext(),"please select Two or more PDFs",Toast.LENGTH_LONG).show()
                    }


                    //  mergeSelectedPdfs(selectedPdf_list!!, pdfNamee, "NULL")
                }
            }


        }
        dailogueBuilder.setCancelable(true)
        dailogueBuilder.setView(view)
        return dailogueBuilder.create()
    }

    fun createAlertdialogue(view: View): androidx.appcompat.app.AlertDialog {
        var alertbuilder2 = MaterialAlertDialogBuilder(
            requireContext(),
            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
        )
        alertbuilder2.setView(view)
        alertbuilder2.setCancelable(false)

        return alertbuilder2.create()
    }

    //custom Contracts for creating pdf document
    var contract = object : ActivityResultContract<Intent, Uri>() {
        override fun createIntent(context: Context, input: Intent?): Intent {
            return input!!
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri {
            var uri = intent?.data
            if (uri != null) {
                return uri
            } else {
                return Uri.EMPTY
            }
        }
    }
    var launcher4 = registerForActivityResult(contract, ActivityResultCallback {

        try{
        var outputStream: OutputStream? = null
        if (it != null) {
            outputStream = activity?.contentResolver?.openOutputStream(it)!!

            Log.d("of66jd", it.path.toString())
            Log.d("34g93jg", outputStream.toString())

            if (outputStream != null) {

                Log.d("3g893hjg",password)

                    mergeSelectedPdfs(selectedPdf_list!!, "pdfNamee", password, outputStream)

            }//if block for output stream null or not
        }
            }catch (e:Exception){
                Toast.makeText(requireContext(),e.message.toString(),Toast.LENGTH_LONG).show()
            }
    })

    fun myAdapterClickListner(myAdapter: MyAdapter_ForMerge, pdflist: java.util.ArrayList<Items_pdfs>)
    {
/*
        myAdapter?.setCustomOnClickListenerr(object:MyAdapter.CustomOnClickListener{
            override fun customOnClick(position: Int) {
                Log.d("3iegnv3me,wv",position.toString())
                pdfName1_bottomsheet?.setText(position.toString())
                bottomSheetDialog?.show()
                clickOnbottomSheetViews(pdflist,position,myAdapter)

            }
        })
*/
        myAdapter?.setMCustomClickListenr(object: MCustomOnClickListener {
            override fun onClick(position: Int) {
//
//               var view =  LayoutInflater.from(requireContext()).inflate(R.layout.list_merge_item,activity?.findViewById(R.id.content),false)
//               var dragimage =  view.findViewById<ImageView>(R.id.dragMenuimage)


                }
            })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        adapter = null
    }


    }
