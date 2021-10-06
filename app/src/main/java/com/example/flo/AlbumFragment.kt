package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {

    lateinit var  binding: FragmentAlbumBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //fragment에서 binding 초기화하는 패턴
        binding = FragmentAlbumBinding.inflate(inflater, container, false)


        binding.albumBtnBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment()) //main fragment에서 album fragment로
                .commitAllowingStateLoss()
        }

        return binding.root //setContentView와 같은 역할
    }
}