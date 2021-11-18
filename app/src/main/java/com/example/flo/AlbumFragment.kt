package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding
    private var gson = Gson()

    private var isLiked: Boolean = false

    val information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //fragment에서 binding 초기화하는 패턴
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        //Home에서 넘어온 데이터 받기
        val albumData = arguments?.getString("album")
        val albumId = arguments?.getInt("albumId")
        val album = gson.fromJson(albumData, Album::class.java)
        isLiked = isLikedAlbum(album.id)

        //뷰들에 반영
        setInit(album)
        initClickListener()
        setViewPager(albumId!!)
        setLikeListeners(album)

        return binding.root //setContentView와 같은 역할
    }

    private fun setInit(album: Album) {
        binding.albumAlbumImageIv.setImageResource(album.coverImg!!)
        binding.albumSongTitleTv.text = album.title
        binding.albumSongSingerTv.text = album.singer

        if(isLiked){
            binding.albumBtnlikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.albumBtnlikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun initClickListener() {
        binding.albumBtnBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment()) //main fragment에서 album fragment로
                .commitAllowingStateLoss()
        }

        binding.albumSongTitleTv.isSelected = true

    }
    private fun setLikeListeners(album: Album){
        val userId: Int = getJWT()

        //like 버튼 클릭 리스너
        binding.albumBtnlikeIv.setOnClickListener {
            if(isLiked){
                binding.albumBtnlikeIv.setImageResource(R.drawable.ic_my_like_off)
                dislikeAlbum(userId, album.id)
            }else{
                binding.albumBtnlikeIv.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.id)
            }
        }

    }

    //앨범 프래그먼트_ 뷰페이저에 앨범 아이디 넣어줌
    private fun setViewPager(albumId: Int) {
        //어댑터 연결
        val albumAdapter = AlbumViewpagerAdapter(this, albumId)
        binding.albumContentVp.adapter = albumAdapter

        //val songDB = SongDatabase.getInstance(requireContext())!!
        //val songs = songDB.songDao().getSongsByAlbumId(albumId)
        //SongRVAdapter(songs as ArrayList)

        //tablayout 연결
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()
    }

    private fun likeAlbum(userId: Int, albumId: Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        songDB.albumDao().likedAlbum(like)
    }
    private fun dislikeAlbum(userId: Int, albumId: Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        songDB.albumDao().disLikedAlbum(userId, albumId)
    }
    private fun isLikedAlbum(albumId: Int): Boolean{
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJWT()

        val likeId: Int? = songDB.albumDao().isLikedAlbum(userId, albumId)

        //likeId != null -> true == null -> false
        return likeId != null
    }

    //userId 받아오는 함수
    private fun getJWT(): Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

        return spf!!.getInt("jwt", 0)
    }

}