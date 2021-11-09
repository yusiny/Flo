package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    private lateinit var autopager: AutoPager //메인배너를 위한 스레드
    private val handler = Handler(Looper.getMainLooper()){
        setPage()
        true
    }
    var currentPosition:Int = 0

    private var albumDatas = ArrayList<Album>();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

//        binding.homeTodayAlbum1Iv.setOnClickListener{
//            //조각을 어디서 변경하는지
//            (context as MainActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.main_frm, AlbumFragment()) //main fragment에서 album fragment로
//                .commitAllowingStateLoss()
//        }

        //앨범 리사이클러 뷰 생성
        //데이터 리스트 생성
        albumDatas.apply {
            add(Album("Butter", "방탄소년단(BTS)", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유(IU)", R.drawable.img_album_exp2))
            add(Album("Weekend", "태연", R.drawable.img_album_exp3))
            add(Album("Next Level", "에스파(aespa)", R.drawable.img_album_exp4))
            add(Album("Butter", "방탄소년단(BTS)", R.drawable.img_album_exp5))
            add(Album("Savage", "에스파(aespa)", R.drawable.img_album_exp6))
        }
        //더미데이터랑 어댑터 연결
        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        //리사이클러뷰에 어댑터 연결
        binding.homeAlbumareaRv.adapter = albumRVAdapter

        //클릭 이벤트느 리스너 연결
        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.myItemClickListener{
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
               albumRVAdapter.removeItem(position)
            }
        })


        //레이아웃 매니저 설정
        binding.homeAlbumareaRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

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

        //메인배너를 위한 스레드
        autopager = AutoPager()
        autopager.start()

        //메인 배너 indicater를 tablayout 연결하기
        TabLayoutMediator(binding.homeMainbannerTb, binding.homeMainbannerVp){
            tab, position->
            //Some implementation
        }.attach()
        return binding.root
    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            }) //main fragment에서 album fragment로
            .commitAllowingStateLoss()
    }

    //메인배너 자동 스크롤을 위한 setPage 함수
    fun setPage(){
        if(currentPosition == 3) currentPosition = 0
        binding.homeMainbannerVp.setCurrentItem(currentPosition, true)
        currentPosition ++
    }

    //메인배너를 위한 스레드
    inner class AutoPager(): Thread(){
        override fun run() {
            try {
                while(true){
                    sleep(2000)
                    handler.sendEmptyMessageAtTime(0, 2000)
                }
            }catch (e: InterruptedException){}
        }
    }

    override fun onDestroy() {
        autopager.interrupt()
        super.onDestroy()
    }

}





