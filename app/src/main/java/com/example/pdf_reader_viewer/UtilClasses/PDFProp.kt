package com.example.pdf_reader_viewer.UtilClasses

import java.io.File

//util class for Intent share keys that are uses for sending pdf properties
class PDFProp()
{
    companion object {
       const val  MY_OPEN_ACTION = "MY_OPEN_ACTION"
        const val PDF_TITLE = "PDFNAME"
        const val PDF_SIZE = "PDF_SIZE"
        const val PDF_APPENDED_URI = "PDF_URI"
        const val PDF_DATEMODIFIED = "PDF_DATAMODIFIED"
        const val PDF_RELATIVEPATH = "PDF_RELATIVEPATH"
        const val PDF_BUCKETNAME = "PDF_BUCKETNAME"

        const val FOLDER_KEY="FOLDER_KEY"   //key for passing folder argument to folder_pdflist_ragment

        const val CREATEDPDF_FOLDER="Created PDFs"
        const val SPLITPDF_FOLDER="Split PDFs"
        const val ENCRYPTEDPDF_FOLDER="Encrypted PDFs"
        const val MERGEPDF_FOLDER="Merge PDFs"

    }
}