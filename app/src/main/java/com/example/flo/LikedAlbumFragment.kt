package com.example.flo

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentLikedalbumBinding

class LikedAlbumFragment: Fragment() {
    lateinit var binding: FragmentLikedalbumBinding
    lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLikedalbumBinding.inflate(inflater, container, false)

        setLikedAlbumRV()

        var isSelectedAll = false
        binding.likedalbumSelectallAreaV.setOnClickListener {
            isSelectedAll = !isSelectedAll

            if(isSelectedAll){
                binding.likedalbumSelectallBtnOffIv.visibility = View.VISIBLE
                binding.likedalbumSelectallBtnOnIv.visibility = View.GONE
                binding.likedalbumSelectallTitleOffTv.visibility = View.VISIBLE
                binding.likedalbumSelectallTitleOnTv.visibility = View.GONE
                binding.likedalbumRv.setBackgroundColor(Color.parseColor("#ffffff"))
            }else{
                binding.likedalbumSelectallBtnOffIv.visibility = View.GONE
                binding.likedalbumSelectallBtnOnIv.visibility = View.VISIBLE
                binding.likedalbumSelectallTitleOffTv.visibility = View.GONE
                binding.likedalbumSelectallTitleOnTv.visibility = View.VISIBLE
                binding.likedalbumRv.setBackgroundColor(Color.parseColor("#eaeaea"))
            }

        }


        return binding.root
    }

    private fun setLikedAlbumRV() {
        songDB = SongDatabase.getInstance(requireContext())!!

        //더미데이터랑 어댑터 연결
        val likedalbumAdapter = LikedAlbumRVAdapter()
        //리사이클러뷰에 어댑터 연결
        binding.likedalbumRv.adapter = likedalbumAdapter
        //레이아웃 매니저 설정
        binding.likedalbumRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val userId = getUserIdx(requireContext())!!
        val albums = songDB.albumDao().getLikedAlbums(userId)
        likedalbumAdapter.addAlbums(albums as ArrayList)

        //클릭 이벤트 리스너 연결
        likedalbumAdapter.setMyItemClickListener(object : LikedAlbumRVAdapter.MyItemClickListener {
            override fun onRemoveAlbum(albumId: Int) {
                songDB.albumDao().disLikedAlbum(userId, albumId)
            }

        })
    }

    private fun getJWT(): Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

        return spf!!.getInt("jwt", 0)
    }
}