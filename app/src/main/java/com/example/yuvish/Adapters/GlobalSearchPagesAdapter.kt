package com.example.yuvish.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.yuvish.Fragments.globalSearch.SearchCustomerFragment
import com.example.yuvish.Fragments.globalSearch.SearchReceiptFragment

class GlobalSearchPagesAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {
    
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                SearchReceiptFragment()
            }
            1 -> {
                SearchCustomerFragment()
            }
            else -> {
                SearchReceiptFragment()
            }
        }
    }
    
}