package com.example.flo

import android.content.Context
import android.content.Intent
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

    private var albumDatas = ArrayList<Album>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initMainBanner()
        initAlbumRV()
        initMiniBanner()

        return binding.root
    }

    private fun initMainBanner() {
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
        TabLayoutMediator(binding.homeMainbannerTb, binding.homeMainbannerVp) { tab, position ->
            //Some implementation
        }.attach()
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

    private fun initMiniBanner() {
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
    }



    private fun initAlbumRV() {
        val songDB = SongDatabase.getInstance(requireContext())!!
        albumDatas = songDB.albumDao().getAlbums() as ArrayList

        //더미데이터랑 어댑터 연결
        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        //리사이클러뷰에 어댑터 연결
        binding.homeAlbumareaRv.adapter = albumRVAdapter
        binding.homeAlbumareaRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //홈프래그먼트_ 앨범 리사이클러뷰의 이벤트 리스너
        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener {
            override fun onItemClick(album: Album, id: Int) {
                startAlbumFragment(album, id)
            }

            override fun onPlayBtnClick(albumId: Int) {
                //앨범의 play 버튼 클릭 시
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }

        })
    }
    private fun startAlbumFragment(album: Album, albumId: Int) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                    putInt("albumId", albumId)
                }
            })
            .commitAllowingStateLoss()
    }



    override fun onDestroy() {
        autopager.interrupt()
        super.onDestroy()
    }

}





