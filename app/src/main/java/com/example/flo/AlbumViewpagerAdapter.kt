package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

//앨범 뷰페이저 어댑더_ songFragment로 넘어가면서 albumId 넘겨줌
class AlbumViewpagerAdapter(fragment: Fragment, id: Int):FragmentStateAdapter(fragment) {
    private val albumId = id
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SongFragment(albumId)
            1 -> DetailFragment()
            else -> VideoFragment()
        }
    }
}