package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentLookBinding


class LookFragment : Fragment() {

    lateinit var binding: FragmentLookBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLookBinding.inflate(inflater, container, false)



        return binding.root
    }

    private fun setSavedSongsRV(){
        //roomDB
        val songDB = SongDatabase.getInstance(requireContext())!!

        //더미데이터랑 어댑터 연결
        val chartRVAdapter = SavedSongsRVAdapter()
        //리사이클러뷰에 어댑터 연결
        binding.lookRv.adapter = chartRVAdapter
        //레이아웃 매니저 설정
        binding.lookRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        chartRVAdapter.addSongs(songDB.songDao().getSongs() as ArrayList)
    }
}