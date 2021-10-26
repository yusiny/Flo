package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeTodayAlbum1Iv.setOnClickListener{
            //조각을 어디서 변경하는지
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, AlbumFragment()) //main fragment에서 album fragment로
                .commitAllowingStateLoss()
        }

        //Viewpager를 위한 adapter 가져오기
        val bannerAdapter = BannerViewpagerAdapter(this)
        //리스트에 프래그먼트 추가
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        //메인 배너 Viewpager를 위한 adapter 가져오기
        val mainbannerAdapter = MainbannerViewpagerAdapter(this)
        //리스트에 프래그먼트 추가
        mainbannerAdapter.addFragment(MainbannerFragment(R.drawable.img_default_4_x_1))
        mainbannerAdapter.addFragment(MainbannerFragment(R.drawable.img_default_4_x_2))
        mainbannerAdapter.addFragment(MainbannerFragment(R.drawable.img_default_4_x_3))

        binding.homeMainbannerVp.adapter = mainbannerAdapter
        binding.homeMainbannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        //메인 배너 indicater를 tablayout 연결하기
        TabLayoutMediator(binding.homeMainbannerTb, binding.homeMainbannerVp){
            tab, position->
            //Some implementation
        }.attach()
        return binding.root
    }

}





