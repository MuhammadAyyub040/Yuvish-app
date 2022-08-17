package com.example.yuvish

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.orhanobut.hawk.Hawk

class MainActivity : AppCompatActivity() {
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.my_navigation_host).navigateUp()
    }

    override fun attachBaseContext(newBase: Context?) {
        Hawk.init(newBase).build()
        super.attachBaseContext(newBase)
    }
}