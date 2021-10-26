package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainbannerViewpagerAdapter(fragment:Fragment):FragmentStateAdapter(fragment) {

    private val fragmentlist : ArrayList<Fragment> = ArrayList()

    //item 개수만큼 실행할 거니까, fragment리스트의 사이즈를 받아와서 Int값으로 리턴
    override fun getItemCount():Int = fragmentlist.size

    //0->NUM-1까지 프래그먼트 생성
    override fun createFragment(position:Int): Fragment = fragmentlist[position]

    fun addFragment(fragment:Fragment){
        //position에 추가해 주는 역할
        fragmentlist.add(fragment)
        notifyItemInserted(fragmentlist.size - 1)
    }
}