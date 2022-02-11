package com.example.pdf_reader_viewer.UtilClasses

import java.io.File

//util class for Intent share keys that are uses for sending pdf properties
class PDFProp()
{
    companion object {

        var PDF_TITLE = "PDFNAME"
        var PDF_SIZE = "PDF_SIZE"
        var PDF_APPENDED_URI = "PDF_URI"
        var PDF_DATEMODIFIED = "PDF_DATAMODIFIED"
        var PDF_RELATIVEPATH = "PDF_RELATIVEPATH"
        var PDF_BUCKETNAME = "PDF_BUCKETNAME"

        var FOLDER_KEY="FOLDER_KEY"   //key for passing folder argument to folder_pdflist_ragment

        var CREATEDPDF_FOLDER="Created PDFs"
        var SPLITPDF_FOLDER="Split PDFs"
        var ENCRYPTEDPDF_FOLDER="Encrypted PDFs"
        var MERGEPDF_FOLDER="Merge PDFs"

    }
}