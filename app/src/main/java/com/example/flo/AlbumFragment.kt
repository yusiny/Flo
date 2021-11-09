package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {

    lateinit var  binding: FragmentAlbumBinding
    private  var gson = Gson()

    val information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //fragment에서 binding 초기화하는 패턴
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        //Home에서 넘어온 데이터 받기
        val albumData = arguments?.getString("album")
        val album = gson.fromJson(albumData, Album::class.java)
        //뷰들에 반영
        setInit(album)

        binding.albumBtnBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment()) //main fragment에서 album fragment로
                .commitAllowingStateLoss()
        }


        //어댑터 연결
        val albumAdapter = AlbumViewpagerAdapter(this)
        binding.albumContentVp.adapter = albumAdapter

        //tablayout 연결
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp){
            tab, position ->
                tab.text = information[position]
        }.attach()

        return binding.root //setContentView와 같은 역할
    }

    private fun setInit(album: Album) {
    binding.albumAlbumImageIv.setImageResource(album.coverImg!!)
    binding.albumSongTitleTv.text = album.title.toString()
    binding.albumSongSingerTv.text = album.singer.toString()
    }
}