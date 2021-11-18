package com.example.flo

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSavedsongsBinding

class SavedsongsFragment: Fragment() {
    lateinit var binding: FragmentSavedsongsBinding
    lateinit var songDB: SongDatabase

    private var isSelectedAll: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedsongsBinding.inflate(inflater, container, false)

        setSavedSongsRV()
        initClickListener()

        return binding.root
    }

    private fun setSavedSongsRV() {
        songDB = SongDatabase.getInstance(requireContext())!!

        //더미데이터랑 어댑터 연결
        val savedsongsRVAdaper = SavedSongsRVAdapter()
        //리사이클러뷰에 어댑터 연결
        binding.savedsongsRv.adapter = savedsongsRVAdaper
        //레이아웃 매니저 설정
        binding.savedsongsRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        savedsongsRVAdaper.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList)

        //클릭 이벤트 리스너 연결
        savedsongsRVAdaper.setMyItemClickListener(object : SavedSongsRVAdapter.MyItemClickListener {
            override fun onRemoveSong(songId: Int) {
                songDB.songDao().updateIsLikeById(false, songId)
            }

        })
    }

    private fun initClickListener() {

        //전제선택 버튼
        binding.savedsongsSelectallAreaV.setOnClickListener {
            if (isSelectedAll) {
                isSelectedAll = false
                binding.savedsongsSelectallBtnOffIv.visibility = View.VISIBLE
                binding.savedsongsSelectallBtnOnIv.visibility = View.GONE
                binding.savedsongsSelectallTitleOffTv.visibility = View.VISIBLE
                binding.savedsongsSelectallTitleOnTv.visibility = View.GONE
                binding.savedsongsRv.setBackgroundColor(Color.parseColor("#ffffff"))

            } else {
                isSelectedAll = true
                binding.savedsongsSelectallBtnOffIv.visibility = View.GONE
                binding.savedsongsSelectallBtnOnIv.visibility = View.VISIBLE
                binding.savedsongsSelectallTitleOffTv.visibility = View.GONE
                binding.savedsongsSelectallTitleOnTv.visibility = View.VISIBLE
                binding.savedsongsRv.setBackgroundColor(Color.parseColor("#eaeaea"))
            }
        }

        //로그인 버튼
    }
}


