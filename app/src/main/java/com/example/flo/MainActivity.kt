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

    //Song 객체
    private var song:Song = Song()
    private lateinit var songDB: SongDatabase

    //미디어 플레이어와 Player 스레드
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var player: Player


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
        //inputDummyAlbums()
        inputDummySongs()

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
        }
        binding.mainPauseBtn.setOnClickListener {
            playbarStatus(false)
        }

        //미니플레이어 클릭 시 SongActivity로 연결
        binding.mainPlayerLayout.setOnClickListener {
            Log.d("nowSongId", song.id.toString())

            //Sending 'song.id' to songActivity
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.putBoolean("isPlaying", song.isPlaying)
            editor.apply()

            startActivity(Intent(this, SongActivity::class.java))
        }


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

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)
        song.isPlaying = spf.getBoolean("isPlaying", false)

        val songDB = SongDatabase.getInstance(this)!!
        song = if(songId == 0){
           songDB.songDao().getSong(1)
        }else{
            songDB.songDao().getSong(songId)
        }

        setMiniPlayer()
        startPlayer()
        setMediaPlayer()

        mediaPlayer?.setOnPreparedListener {
            if (song.isPlaying)
                playbarStatus(true)
        }
    }

    private fun setMediaPlayer() {
        //mediaPlayer 연결해 주기
        val music = resources.getIdentifier(song.music, "raw", this.packageName)

        mediaPlayer = MediaPlayer.create(this, music)
        binding.mainMiniplayerSb.max = mediaPlayer?.duration!! //노래 길이를 시크바 길이에 적용
        mediaPlayer?.seekTo(song.currentTime * 1000)
        binding.mainMiniplayerSb.setProgress(mediaPlayer?.currentPosition!!)
    }

    private fun startPlayer() {
        //player seekbar를 위한 스레드
        player = Player(song.currentTime, song.isPlaying, song.isRepeated)
        player.start()
    }

    //미니 플레이어 set 함수
    fun setMiniPlayer(){
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        //binding.mainMiniplayerSb.progress = (song.currentTime * 1000 / song.playTime)
    }

    fun playbarStatus(playingStatus: Boolean){
        player.isPlaying = playingStatus
        song.isPlaying = playingStatus

        if(playingStatus){
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE

            mediaPlayer?.start()

        }else{
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE

            if(mediaPlayer?.isPlaying!!) mediaPlayer?.pause()
        }
    }

    private fun inputDummySongs(){
        songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if(songs.isNotEmpty()) return

        songDB.songDao().insert(
            Song(
                "Lilac",
                "아이유 (IU)",
                R.drawable.img_album_exp2,
                "music_lilac",
                200,
                0,
                false,
                false,
                false,
                1
            )
        )

        songDB.songDao().insert(
            Song(
                "Flu",
                "아이유 (IU)",
                R.drawable.img_album_exp2,
                "music_lilac",
                200,
                0,
                false,
                false,
                false,
                2
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단",
                R.drawable.img_album_exp,
                "music_lilac",
                190,
                0,
                false,
                false,
                false,
                2
            )
        )
        songDB.songDao().insert(
            Song(
                "Butter (Hotter Remix) ",
                "방탄소년단",
                R.drawable.img_album_exp,
                "music_lilac",
                190,
                0,
                false,
                false,
                false,
                2
            )
        )
        songDB.songDao().insert(
            Song(
                "Butter (Sweeter Remix) ",
                "방탄소년단",
                R.drawable.img_album_exp,
                "music_lilac",
                190,
                0,
                false,
                false,
                false,
                3
            )
        )

        songDB.songDao().insert(
            Song(
                "Next Level (IMALY Remix)",
                "aespa",
                R.drawable.img_album_exp4,
                "music_lilac",
                200,
                0,
                false,
                false,
                false,
                3
            )
        )

        songDB.songDao().insert(
            Song(
                "Weekend",
                "태연",
                R.drawable.img_album_exp3,
                "music_lilac",
                200,
                0,
                false,
                false,
                false,
                4
            )
        )



        val _songs = songDB.songDao().getSongs()
        Log.d("DB DATA", _songs.toString())
    }

    //쓰레드를 위한 객체
    //생성자 playTime isPlaying 생성
    inner class Player(var currentTime:Int, var isPlaying: Boolean, var isRepeat: Boolean) : Thread(){
        override fun run(){
            //강제 종료 위한 try catch
            try{
                //쓰레드 실행
                while(true){

//                    //반복 재생
//                    mediaPlayer?.isLooping = isRepeat

                    //플레이 중에만 타이머 go
                    if(isPlaying){
                        sleep(1000)
                        runOnUiThread{
                            binding.mainMiniplayerSb.setProgress(mediaPlayer?.currentPosition!!) //현재 재생 위치 시크바에 적용
                            //song.currentTime = binding.mainMiniplayerSb.progress / 1000
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

        song.currentTime = binding.mainMiniplayerSb.progress/ 1000
        Log.d("MA", "메인액티비티에서 보내는 currentTime은 ${song.currentTime} 프로그레스는 ${binding.mainMiniplayerSb.progress.toInt()}")

        songDB.songDao().updateCurrentTimeById(song.currentTime, song.id)
    }
    override fun onDestroy() {
        super.onDestroy()

        player.interrupt() // 스레드 종료
        mediaPlayer?.release() // 미디어 플레이어 종료
        mediaPlayer = null
    }
}



