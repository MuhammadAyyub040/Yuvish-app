package com.example.yuvish.models

import android.graphics.Bitmap
import com.example.yuvish.retrofit.ApiClient
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix

class BarcodeManager() {

    @Throws(WriterException::class)
    fun createImage(code: String): Bitmap {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(
            code,
            BarcodeFormat.CODE_128,
            ApiClient.BARCODE_IMAGE_WIDTH,
            ApiClient.BARCODE_IMAGE_HEIGHT
        )

        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)
        for (i in 0 until height) {
            for (j in 0 until width) {
                if (bitMatrix[j, i]) {
                    pixels[i * width + j] = -0x1000000
                } else {
                    pixels[i * width + j] = -0x1
                }
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }

}