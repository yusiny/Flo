package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    //정방 선언(선 선언, 후 초기화) "선언하고 나중에 꼭 초기화 해 줄게!"
    lateinit var binding : ActivitySongBinding

    //activity 생성 시, 처음으로 실행되는 함수
    //override 함수: 클래스서 상속받아 사용
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding 초기화
        //inflate: xml에 표기된 뷰들을 객체화
        binding = ActivitySongBinding.inflate(layoutInflater)

        //사용할 view 파일 설정
        //root: activity_song의 최상단
        setContentView(binding.root)


        //택배를 받았다면?
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            binding.songAlbumTitleTv.text = intent.getStringExtra("title")
            binding.songAlbumSingerTv.text = intent.getStringExtra("singer")
        }


        //down 버튼 클릭 시, 이벤트
        var playingStatus : Int = 0
        binding.songBtnDownIv.setOnClickListener{
            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("playing", playingStatus)
            startActivity(intent)
        }

        //like, unlike 버튼 상태 조작
        binding.songBtnLikeOffIv.setOnClickListener {
            setLikeStatus(true)
        }
        binding.songBtnLikeOnIv.setOnClickListener {
            setLikeStatus(false)
        }
        binding.songBtnUnlikeOffIv.setOnClickListener {
            setUnlikeStatus(true)
        }
        binding.songBtnUnlikeOnIv.setOnClickListener {
            setUnlikeStatus(false)
        }

        //player 버튼 상태 조작
        if(intent.hasExtra("playing")){
            playingStatus = intent.getIntExtra("playing", 0)
        }
        if(playingStatus == 1){
            setPlayerStatus(false)
        }else if(playingStatus == 0){
            setPlayerStatus(true)
        }
        binding.songBtnPlayIv.setOnClickListener{
            setPlayerStatus(false)
            playingStatus = 1
        }
        binding.songBtnPauseIv.setOnClickListener{
           setPlayerStatus(true)
            playingStatus = 0
        }


        //repeat 버튼 상태 조작
        binding.songBtnRepeatOffIv.setOnClickListener {
            setRepeatStatus(0)
            Toast.makeText(this, "전체 음악을 반복합니다.", Toast.LENGTH_SHORT).show()
        }
        binding.songBtnRepeatOnIv.setOnClickListener {
            setRepeatStatus(1)
            Toast.makeText(this, "현재 음악을 반복합니다.", Toast.LENGTH_SHORT).show()
        }
        binding.songBtnRepeatOn1Iv.setOnClickListener {
            setRepeatStatus(2)
            Toast.makeText(this, "반복을 사용하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
        
        //random 버튼 상태 조작
        binding.songBtnRandomOffIv.setOnClickListener { 
            setRandomStatus(true)
            Toast.makeText(this, "재생목록을 랜덤으로 재생합니다.", Toast.LENGTH_SHORT).show()
        }
        binding.songBtnRandomOnIv.setOnClickListener { 
            setRandomStatus(false)
            Toast.makeText(this, "재생목록을 순서대로 재생합니다.", Toast.LENGTH_SHORT).show()
        }

    }

    fun setPlayerStatus(isPlaying:Boolean){
        if(isPlaying){
            binding.songBtnPlayIv.visibility = View.VISIBLE
            binding.songBtnPauseIv.visibility = View.GONE
        }else{
            binding.songBtnPlayIv.visibility = View.GONE
            binding.songBtnPauseIv.visibility = View.VISIBLE
        }
    }

    fun setRepeatStatus(isRepeating:Int){
        if(isRepeating == 0){
            binding.songBtnRepeatOffIv.visibility = View.GONE
            binding.songBtnRepeatOnIv.visibility = View.VISIBLE
        }else if(isRepeating == 1){
            binding.songBtnRepeatOnIv.visibility = View.GONE
            binding.songBtnRepeatOn1Iv.visibility = View.VISIBLE
        }else if(isRepeating == 2){
            binding.songBtnRepeatOn1Iv.visibility = View.GONE
            binding.songBtnRepeatOffIv.visibility = View.VISIBLE
        }
    }

    fun setLikeStatus(isPlaying:Boolean){
        if(isPlaying){
            binding.songBtnLikeOnIv.visibility = View.VISIBLE
            binding.songBtnLikeOffIv.visibility = View.GONE
        }else{
            binding.songBtnLikeOnIv.visibility = View.GONE
            binding.songBtnLikeOffIv.visibility = View.VISIBLE
        }
    }
    fun setUnlikeStatus(isPlaying:Boolean){
        if(isPlaying){
            binding.songBtnUnlikeOnIv.visibility = View.VISIBLE
            binding.songBtnUnlikeOffIv.visibility = View.GONE
        }else{
            binding.songBtnUnlikeOnIv.visibility = View.GONE
            binding.songBtnUnlikeOffIv.visibility = View.VISIBLE
        }
    }

    fun setRandomStatus(isPlaying:Boolean){
        if(isPlaying){
            binding.songBtnRandomOnIv.visibility = View.VISIBLE
            binding.songBtnRandomOffIv.visibility = View.GONE
        }else{
            binding.songBtnRandomOnIv.visibility = View.GONE
            binding.songBtnRandomOffIv.visibility = View.VISIBLE
        }
    }
}