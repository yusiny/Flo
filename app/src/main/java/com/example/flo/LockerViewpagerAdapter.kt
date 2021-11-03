package com.example.flo

import android.widget.AdapterViewAnimator
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerViewpagerAdapter(fragment : Fragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SavedsongsFragment()
            else -> MusicfilesFragment()
        }
    }
}