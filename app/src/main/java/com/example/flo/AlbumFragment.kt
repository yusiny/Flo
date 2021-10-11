package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment : Fragment() {

    lateinit var  binding: FragmentAlbumBinding

    val information = arrayListOf("수록곡", "상세정보", "영상")

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

        /*binding.albumSongList1V.setOnClickListener{
            Toast.makeText(activity, "라일락", Toast.LENGTH_SHORT).show()
        }*/

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
}