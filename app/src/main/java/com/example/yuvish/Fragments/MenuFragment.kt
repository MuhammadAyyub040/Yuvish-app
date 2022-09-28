package com.example.yuvish.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.yuvish.Models.CommonSettings
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentMenuBinding
import com.example.yuvish.retrofit.ApiClient
import com.example.yuvish.retrofit.GlobalData
import com.example.yuvish.retrofit.checkLoadedAllData
import com.orhanobut.hawk.Hawk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuFragment : Fragment() {
    private lateinit var binding: FragmentMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMenuBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (getToken() == null){
            binding.root.postDelayed({
                binding.animationView.pauseAnimation()
                findNavController().navigate(R.id.loginFragment)
            }, 2000)
        }else{
            getCommonSetting()
        }
    }

    private fun getCommonSetting(){
        ApiClient.retrofitService.getCommonSettings().enqueue(object : Callback<CommonSettings>{
            override fun onResponse(call: Call<CommonSettings>, response: Response<CommonSettings>) {
                Log.e("TAG", "onResponse: ${response.code()}" )
                if (response.code() == 200){
                    GlobalData.commonSettings = response.body()
                    checkChangeFragment()
                }
            }

            override fun onFailure(call: Call<CommonSettings>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireActivity(), "Ma'lumot kelmadi", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun checkChangeFragment(){
            binding.animationView.pauseAnimation()
            findNavController().navigate(R.id.debtorsDepartmentFragment)
    }

    private fun getToken(): String?{
        return Hawk.get("token", null)
    }

}