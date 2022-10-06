package com.example.yuvish.Fragments.globalSearch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.yuvish.Adapters.GlobalSearchPagesAdapter
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentGlobalSearchBinding
import com.google.android.material.tabs.TabLayoutMediator

class GlobalSearchFragment : Fragment() {

    private lateinit var binding: FragmentGlobalSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGlobalSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager2.adapter = GlobalSearchPagesAdapter(requireActivity())

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.receipt_number_full)
                    tab.setIcon(R.drawable.file_search_svgrepo_com)
                }
                1 -> {
                    tab.text = getString(R.string.mijoz)
                    tab.setIcon(R.drawable.ic_baseline_person_search_24)
                }
            }
        }.attach()

        binding.backStack.setOnClickListener {
            findNavController().popBackStack()
        }

    }
}