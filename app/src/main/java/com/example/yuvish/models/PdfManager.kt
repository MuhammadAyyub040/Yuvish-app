package com.example.yuvish.models

import android.content.Context
import android.view.View
import com.gkemon.XMLtoPDF.PdfGenerator
import com.gkemon.XMLtoPDF.PdfGeneratorListener

class PdfManager(private val context: Context) {

    fun convertViewToPdf(view: View, fileName: String, pdfGeneratorListener: PdfGeneratorListener){
        PdfGenerator.getBuilder()
            .setContext(context)
            .fromViewSource()
            .fromView(view)
            .setFileName(fileName)
            .setFolderNameOrPath("Pdf files")
            .build(pdfGeneratorListener)
    }

}