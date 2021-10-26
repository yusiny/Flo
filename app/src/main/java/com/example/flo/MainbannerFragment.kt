package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentBannerBinding
import com.example.flo.databinding.FragmentMainbannerBinding

class MainbannerFragment(val imgRes: Int): Fragment() {
    lateinit var binding : FragmentMainbannerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainbannerBinding.inflate(inflater, container, false)

        binding.homeBackgroundIv.setImageResource(imgRes)

        return binding.root
    }
}