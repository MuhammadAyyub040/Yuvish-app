package com.example.yuvish.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {
    private lateinit var binding: FragmentMenuBinding
    private lateinit var a: Animation

    inner class MyClass:Thread(){
        override fun run() {
            sleep(3000)
            binding.animationView.startAnimation(a)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMenuBinding.inflate(layoutInflater)

        a = AnimationUtils.loadAnimation(context, R.anim.scale)

        MyClass().start()

        a.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                findNavController().navigate(R.id.homeFragment)
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })

        return binding.root
    }

}