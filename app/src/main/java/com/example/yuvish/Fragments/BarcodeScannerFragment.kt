package com.example.yuvish.Fragments

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentBarcodeScannerBinding
import com.example.yuvish.databinding.FragmentHomeBinding

class BarcodeScannerFragment : Fragment() {

    lateinit var binding: FragmentBarcodeScannerBinding
    lateinit var codescanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBarcodeScannerBinding.inflate(layoutInflater)
        startScanning()

        return binding.root
    }

    private fun startScanning() {
        val scannerView: CodeScannerView = binding.getScanner
        codescanner = CodeScanner(requireActivity(), scannerView)
        codescanner.camera = CodeScanner.CAMERA_BACK
        codescanner.formats = CodeScanner.ALL_FORMATS

        codescanner.autoFocusMode = AutoFocusMode.SAFE
        codescanner.scanMode = ScanMode.SINGLE
        codescanner.isAutoFocusEnabled = true
        codescanner.isFlashEnabled = false

        codescanner.decodeCallback = DecodeCallback {
            requireActivity().runOnUiThread {
                setFragmentResult("barcode", bundleOf("result" to it.text))
                findNavController().popBackStack()
            }
        }

        codescanner.errorCallback = ErrorCallback {
            requireActivity().runOnUiThread {
                Toast.makeText(requireActivity(), "Camera initialization error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.getScanner.setOnClickListener {
            codescanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        if (::codescanner.isInitialized){
            codescanner.startPreview()
        }
    }

    override fun onPause() {
        if (::codescanner.isInitialized){
            codescanner.releaseResources()
        }
        super.onPause()
    }
}