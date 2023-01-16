package com.example.yuvish.models

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.yuvish.R
import com.example.yuvish.databinding.CustomsnackbarlayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File

class PrintManager(private val context: Context) {

    private val printerAppPackageName = "ru.a402d.rawbtprinter"

    private companion object{
        const val OPEN = "open"
        const val PRINT = "print"
    }

    fun showCustomSnackBar(file: File){
        val bottomSheetDialog = BottomSheetDialog(context, R.style.bottomDialogStyle)
        val dialogBinding = CustomsnackbarlayoutBinding.inflate(LayoutInflater.from(context))
        bottomSheetDialog.setContentView(dialogBinding.root)

        dialogBinding.open.setOnClickListener {
            openFile(file)
        }

        dialogBinding.print.setOnClickListener {
            printFile(file)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    fun openFile(file: File){
        printOrOpenFile(OPEN, file)
    }

    fun printFile(file: File) {
        printOrOpenFile(PRINT, file)
    }

    private fun printOrOpenFile(status: String, file: File){
        val intent = Intent(Intent.ACTION_VIEW)
        val apkURI = FileProvider.getUriForFile(context, context.packageName, file)

        val myMime = MimeTypeMap.getSingleton()
        val mimeType =
            myMime.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(apkURI.toString())) //It will return the mimetype

        if (status == PRINT) {
            intent.setPackage(printerAppPackageName)
        }
        intent.setDataAndType(apkURI, mimeType)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            context.startActivity(intent)

            if (status == PRINT){
                Toast.makeText(context, context.getString(R.string.printing), Toast.LENGTH_SHORT).show()
            }else if (status == OPEN){
                Toast.makeText(context, context.getString(R.string.opening), Toast.LENGTH_SHORT).show()
            }
        } catch (e: ActivityNotFoundException) {
            if (status == PRINT) {
                Toast.makeText(context, context.getString(R.string.printer_app_not_found), Toast.LENGTH_SHORT).show()
                printerAppNotFound()
            }else if (status == OPEN){
                Toast.makeText(context, context.getString(R.string.not_have_apps_open_pdf), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun printerAppNotFound(){
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$printerAppPackageName")
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$printerAppPackageName")
                )
            )
        }
    }

}