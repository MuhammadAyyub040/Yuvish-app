package com.example.yuvish.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.yuvish.Models.Authorization.UserToken
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentLoginBinding
import com.example.yuvish.retrofit.ApiClient
import com.orhanobut.hawk.Hawk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)

        binding.loginBtn.setOnClickListener {

            val username = binding.edtLogin.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            if (username.isEmpty()) {
                binding.edtLogin.error = "Enter login"
                binding.edtLogin.requestFocus()
            } else if (password.isEmpty()) {
                binding.edtPassword.error = "Enter password"
                binding.edtPassword.requestFocus()
            } else {
                ApiClient.retrofitService.createUser(binding.edtLogin.text.toString(), binding.edtPassword.text.toString()).enqueue(object : Callback<UserToken>{
                    override fun onResponse(call: Call<UserToken>, response: Response<UserToken>) {
                        if (response.code() == 200){
                            Log.d("testLogin", response.body().toString())
                            val userToken = response.body()
                            Hawk.put("token", userToken?.access_token)
                            findNavController().navigate(R.id.homeFragment)
                        }else{
                            Log.d("testLogin", response.toString())
                            Log.d("testLogin", response.body().toString())
                            Log.d("testLogin", response.message())
                            Toast.makeText(requireContext(), "Ma'lumotda xatolik", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<UserToken>, t: Throwable) {
                        t.printStackTrace()
                        Toast.makeText(requireContext(), "Server bilan bog'lanolmadik", Toast.LENGTH_SHORT).show()
                    }
                })

            }
        }

        return binding.root
    }
}