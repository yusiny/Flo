package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerViewpagerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {

    //list를 만들어서
    //list의 이미지들을 adapter를 이용해 fragment로
    //viewpager에 넘겨준다!
    private val fragmentlist : ArrayList<Fragment> = ArrayList()

    //전달할 아이템의 개수
    override fun getItemCount(): Int = fragmentlist.size

    //?
    override fun createFragment(position: Int): Fragment = fragmentlist[position]

    //homeFragment에서 직접 접근 X, 여기서 홈프래그먼트에 추가해줌
    fun addFragment(fragment: Fragment){
        fragmentlist.add(fragment)
        notifyItemInserted(fragmentlist.size -1) //새롭게 프래그먼트 추가 시, viewpager에게 갱신해줘!라는 의미 //viewpager
    }
}