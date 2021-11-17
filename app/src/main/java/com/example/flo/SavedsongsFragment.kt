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


    private var savedSongsDatas = ArrayList<Album>()
    private var isSelectedAll: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedsongsBinding.inflate(inflater, container, false)

        //더미데이터
        savedSongsDatas.apply {
            add(Album("Butter", "방탄소년단(BTS)", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유(IU)", R.drawable.img_album_exp2))
            add(Album("Weekend", "태연", R.drawable.img_album_exp3))
            add(Album("Next Level", "에스파(aespa)", R.drawable.img_album_exp4))
            add(Album("Butter", "방탄소년단(BTS)", R.drawable.img_album_exp5))
            add(Album("Savage", "에스파(aespa)", R.drawable.img_album_exp4))
            add(Album("Butter", "방탄소년단(BTS)", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유(IU)", R.drawable.img_album_exp2))
            add(Album("Weekend", "태연", R.drawable.img_album_exp3))
            add(Album("Next Level", "에스파(aespa)", R.drawable.img_album_exp4))
            add(Album("Butter", "방탄소년단(BTS)", R.drawable.img_album_exp5))
            add(Album("Savage", "에스파(aespa)", R.drawable.img_album_exp4))
        }

        //더미데이터랑 어댑터 연결
        val savedsongsRVAdaper = SavedSongsRVAdapter(savedSongsDatas)
        //리사이클러뷰에 어댑터 연결
        binding.savedsongsRv.adapter = savedsongsRVAdaper

        //클릭 이벤트 리스너 연결
        savedsongsRVAdaper.setMyItemClickListener(object : SavedSongsRVAdapter.MyItemClickListener{
            override fun onRemoveSong(position: Int) {
              //
            }

        })
        //레이아웃 매니저 설정
        binding.savedsongsRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        binding.savedsongsSelectallAreaV.setOnClickListener{
            if(isSelectedAll){
                isSelectedAll = false
                binding.savedsongsSelectallBtnOffIv.visibility = View.VISIBLE
                binding.savedsongsSelectallBtnOnIv.visibility = View.GONE
                binding.savedsongsSelectallTitleOffTv.visibility = View.VISIBLE
                binding.savedsongsSelectallTitleOnTv.visibility = View.GONE
                binding.savedsongsRv.setBackgroundColor(Color.parseColor("#ffffff"))

            }else {
                isSelectedAll = true
                binding.savedsongsSelectallBtnOffIv.visibility = View.GONE
                binding.savedsongsSelectallBtnOnIv.visibility = View.VISIBLE
                binding.savedsongsSelectallTitleOffTv.visibility = View.GONE
                binding.savedsongsSelectallTitleOnTv.visibility = View.VISIBLE
                binding.savedsongsRv.setBackgroundColor(Color.parseColor("#eaeaea"))
            }
        }

        return binding.root
    }
}


