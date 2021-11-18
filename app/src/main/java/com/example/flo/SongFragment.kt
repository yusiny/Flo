package com.example.flo

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSongBinding

//송프래그먼트_ albumId를 인자로 받아옴 <- 앨범프래그먼트의 뷰페이저에서 인자로 받아온 것
class SongFragment(private val albumId: Int) :Fragment() {
    lateinit var binding : FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)

        //Songs 리사이클러뷰 연결
        val songsRVAdapter = SongRVAdapter()
        binding.albumSongsRecyclerViewRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.albumSongsRecyclerViewRv.adapter = songsRVAdapter

        //리사이클러뷰에 노래 넣어주기
        val songDB = SongDatabase.getInstance(requireContext())!!

        //받아온 albumId에 해당하는 노래들 받아오기 _ songDao의 쿼리문 이용
        val songs = songDB.songDao().getSongsByAlbumId(albumId)

        Log.d("albumId", "id는 ${albumId} album는 ${songs}")
        songsRVAdapter.addSongs(songs as ArrayList) //songRVAdpater에 넣어주기

        initClickListener()

        return binding.root
    }

    private fun initClickListener() {
        //내 취향 Mix 버튼 on&off
        binding.albumPreferBtnOffIv.setOnClickListener {
            binding.albumPreferBtnOffIv.visibility = View.GONE
            binding.albumPreferBtnOnIv.visibility = View.VISIBLE
        }
        binding.albumPreferBtnOnIv.setOnClickListener {
            binding.albumPreferBtnOffIv.visibility = View.VISIBLE
            binding.albumPreferBtnOnIv.visibility = View.GONE
        }

        //전체선택 버튼 on & off
        binding.albumBtnAllSelectOffAreaV.setOnClickListener {
            binding.albumAllSelectTextOnTv.visibility = View.VISIBLE
            binding.albumBtnAllSelectOnIv.visibility = View.VISIBLE
            binding.albumBtnAllSelectOffIv.visibility = View.GONE
            binding.albumAllSelectTextOffTv.visibility = View.GONE
            //binding.albumSongListAreaV.setBackgroundColor(Color.parseColor("#eaeaea"))
            binding.albumPreferTextTv.setTextColor(Color.parseColor("#A0A0A0"))
        }

        binding.albumBtnAllSelectOnAreaV.setOnClickListener {
            binding.albumAllSelectTextOnTv.visibility = View.GONE
            binding.albumBtnAllSelectOnIv.visibility = View.GONE
            binding.albumBtnAllSelectOffIv.visibility = View.VISIBLE
            binding.albumAllSelectTextOffTv.visibility = View.VISIBLE
            //binding.albumSongListAreaV.setBackgroundColor(Color.parseColor("#ffffff"))
            binding.albumPreferTextTv.setTextColor(Color.parseColor("#000000"))
        }
    }
}

