package com.example.flo

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentVideoBinding

class VideoFragment:Fragment() {
    lateinit var binding: FragmentVideoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoBinding.inflate(inflater, container, false)

        binding.videoVideo1Iv.setBackgroundColor(Color.rgb(0, 0, 0))
        binding.videoVideo1Iv.clipToOutline

        return binding.root
    }
}