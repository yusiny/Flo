package com.example.flo

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentSongBinding

class SongFragment:Fragment() {
    lateinit var binding : FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)

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
        binding.albumBtnAllSelectOffAreaV.setOnClickListener{
            binding.albumAllSelectTextOnTv.visibility = View.VISIBLE
            binding.albumBtnAllSelectOnIv.visibility = View.VISIBLE
            binding.albumBtnAllSelectOffIv.visibility = View.GONE
            binding.albumAllSelectTextOffTv.visibility = View.GONE
            binding.albumSongListAreaV.setBackgroundColor(Color.parseColor("#eaeaea"))
            binding.albumPreferTextTv.setTextColor(Color.parseColor("#A0A0A0"))
        }

        binding.albumBtnAllSelectOnAreaV.setOnClickListener{
            binding.albumAllSelectTextOnTv.visibility = View.GONE
            binding.albumBtnAllSelectOnIv.visibility = View.GONE
            binding.albumBtnAllSelectOffIv.visibility = View.VISIBLE
            binding.albumAllSelectTextOffTv.visibility = View.VISIBLE
            binding.albumSongListAreaV.setBackgroundColor(Color.parseColor("#ffffff"))
            binding.albumPreferTextTv.setTextColor(Color.parseColor("#000000"))
        }



        return binding.root
    }
}

