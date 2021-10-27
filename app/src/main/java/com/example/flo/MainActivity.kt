package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding
import java.lang.Thread.sleep


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    //전역 변수
    private var isRepeat: Boolean = false

    val song = Song("라일락","아이유(IU)", 10, 0,false )
    private lateinit var player: Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setMiniPlayer()

        //player seekbar를 위한 스레드
        player = Player(song.playTime, song.isPlaying)
        player.start()

        //play 버튼 상태 변경
        binding.mainMiniplayerBtn.setOnClickListener {
            playbarStatus(true)
            song.isPlaying = true
        }
        binding.mainPauseBtn.setOnClickListener {
            playbarStatus(false)
            song.isPlaying = false
        }

        //미니플레이어 클릭 시 SongActivity로 연결
        binding.mainPlayerLayout.setOnClickListener {
            //startActivity(Intent(this, SongActivity::class.java))

            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("currentTime", song.currentTime)
            //intent.putExtra("playing", playingStatus)
            intent.putExtra("isPlaying", song.isPlaying)
            intent.putExtra("isRepeated", isRepeat)
            startActivity(intent)
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

    //미니 플레이어 set 함수
    fun setMiniPlayer(){

        //SongActivity에서 전달받은 데이터 저장
        if(intent.hasExtra("playTime") && intent.hasExtra("isPlaying") && intent.hasExtra("currentTime") && intent.hasExtra("isRepeated")){
            song.playTime = intent.getIntExtra("playTime", 0)
            song.isPlaying = intent.getBooleanExtra("isPlaying", false)
            song.currentTime = intent.getIntExtra("currentTime", 0)
            isRepeat = intent.getBooleanExtra("isRepeated", false)
        }

        //시크바 현재 progress SongActivity와 동기화
        binding.mainMiniplayerSb.progress = 1000/song.playTime * song.currentTime

        //SongActivity와 플레이 버튼 상태 동기화
        if(song.isPlaying){
            playbarStatus(true)
        }else{
            playbarStatus(false)
        }
    }

    fun playbarStatus(playingStatus: Boolean){
        if(playingStatus){
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
        }else{
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
        }
    }

    //seekbar 스레드를 위한 객체
    inner class Player(private val playTime:Int, var isPlaying: Boolean): Thread(){
        private var second = 0

        //player.start()로 스레드를 시작하면, run이 실행됨
        override fun run(){
            //강제 종료를 위한 try...catch
            try{
                if(song.currentTime!=0) second = song.currentTime
                while(true){
                    //노래가 끝나면 반복하거나, 멈추기
                    if(second > playTime){
                        if(isRepeat) second = 0
                        else break
                    }

                    //재생 중일 때만 타이머 실행
                    if(isPlaying){
                        sleep(1000)
                        second ++

                        //UI에 반영
                        runOnUiThread{
                            binding.mainMiniplayerSb.progress = second*1000/playTime
                            song.currentTime = second
                        }
                    }
                    song.currentTime = second
                }
            }catch (e: InterruptedException){}
        }
    }

    override fun onDestroy() {
        player.interrupt()
        super.onDestroy()
    }
}



