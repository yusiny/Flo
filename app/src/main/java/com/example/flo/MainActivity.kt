package com.example.flo

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.lang.Thread.sleep


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    //전역 변수

    //Album 객체
    private var album: Album = Album()

    //Song 객체
    private var song:Song = Song()

    //미디어 플레이어
    private var mediaPlayer: MediaPlayer? = null

    //Player 스레드
    private lateinit var player: Player

    //Gson
    private var gson:Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //seekBar 이벤트 리스너
        binding.mainMiniplayerSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    mediaPlayer?.seekTo(progress)
                    player.currentTime = progress / 1000
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        //play 버튼 상태 변경
        binding.mainMiniplayerBtn.setOnClickListener {
            playbarStatus(true)
            song.isPlaying = true
            mediaPlayer?.start()
            player.isPlaying = true
        }
        binding.mainPauseBtn.setOnClickListener {
            playbarStatus(false)
            song.isPlaying = false
            mediaPlayer?.pause()
            player.isPlaying = false
        }

        //미니플레이어 클릭 시 SongActivity로 연결
        binding.mainPlayerLayout.setOnClickListener {
            startActivity(Intent(this, SongActivity::class.java))
        }


        initNavigation()

        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }

    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

    }

    override fun onStart() {
        super.onStart()

        //sharedPrefernces 받아오기
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val jsonSong = sharedPreferences.getString("song", null)
        song = if(jsonSong == null){
            Song("라일락","아이유(IU)", g"music_lilac", R.drawable.img_album_exp2, 215, 0,false, false )
        }else{
            gson.fromJson(jsonSong, Song::class.java)
        }

        setMiniPlayer()

        //player seekbar를 위한 스레드
        player = Player(song.currentTime, song.isPlaying, song.isRepeated)
        player.start()

        //SongActivity와 플레이 버튼 상태 동기화
        if(song.isPlaying){
            playbarStatus(true)
            mediaPlayer?.start()
            player.isPlaying = true
        }else{
            playbarStatus(false)
            player.isPlaying = false
        }
    }

    //미니 플레이어 set 함수
    fun setMiniPlayer(){
        //mediaPlayer 연결해 주기
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
        binding.mainMiniplayerSb.max = mediaPlayer?.duration!! //노래 길이를 시크바 길이에 적용
        mediaPlayer?.seekTo(song.currentTime * 1000)
        binding.mainMiniplayerSb.setProgress(mediaPlayer?.currentPosition!!)

    }

    fun playbarStatus(playingStatus: Boolean){
        if(playingStatus){
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
            //player.isPlaying = true

        }else{
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
            //player.isPlaying = false
        }
    }

    //쓰레드를 위한 객체
    //생성자 playTime isPlaying 생성
    inner class Player(var currentTime:Int, var isPlaying: Boolean, var isRepeat: Boolean) : Thread(){
        override fun run(){
            //강제 종료 위한 try catch
            try{
                //쓰레드 실행
                while(true){

                    //반복 재생
                    if(isRepeat){
                        mediaPlayer?.isLooping = true
                    }else{
                        mediaPlayer?.isLooping = false
                    }

                    //플레이 중에만 타이머 go
                    if(isPlaying){
                        sleep(1000)
                        runOnUiThread{
                            binding.mainMiniplayerSb.setProgress(mediaPlayer?.currentPosition!!) //현재 재생 위치 시크바에 적용
                            song.currentTime = binding.mainMiniplayerSb.progress / 1000
                        }
                    }
                }
            }catch(e : InterruptedException){
                Log.d("interrupt", "쓰레드가 종료되었습니다.")
            }

        }
    }


    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause() // 미디어 플레이어 중지
        player.isPlaying = false // 스레드 중지
        song.currentTime = binding.mainMiniplayerSb.progress / 1000

        //SP
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val json = gson.toJson(song)
        editor.putString("song", json)
        editor.apply()
    }
    override fun onDestroy() {
        super.onDestroy()
        //song.isPlaying = false
        player.interrupt() // 스레드 종료
        mediaPlayer?.release() // 미디어 플레이어 종료
        mediaPlayer = null
    }

//    override fun onAlbumPass(albumPassed: Album) {
//        album = albumPassed
//        //song = album.songs[0]
//    }
}



