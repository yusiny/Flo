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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.lang.Thread.sleep


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    //전역 변수

    //Song 객체
    private var song:Song = Song()
    private lateinit var songDB: SongDatabase
    private var albumId: Int = 0

    //미디어 플레이어와 Player 스레드
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var player: Player


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
        inputDummyAlbums()
        inputDummySongs()

        initClickListener()


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

    private fun initClickListener() {
        //seekBar 이벤트 리스너
        binding.mainMiniplayerSb.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
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
            editor.putInt("albumId", albumId)
            editor.apply()

            startActivity(Intent(this, SongActivity::class.java))
        }
    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

    }

    override fun onStart() {
        super.onStart()
        //songDB 연결
        val songDB = SongDatabase.getInstance(this)!!

        //SP로 songId, isPlaying 받아오기
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)
        song.isPlaying = spf.getBoolean("isPlaying", false)

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

    //Room_DB setup
    private fun inputDummyAlbums(){
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if(albums.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(
                1,
                "IU 5th Album 'LILAC'", "아이유 (IU)", R.drawable.img_album_exp2
            )
        )
        songDB.albumDao().insert(
            Album(
                2,
                "Butter", "방탄소년단", R.drawable.img_album_exp
            )
        )
        songDB.albumDao().insert(
            Album(
                3,
                "Next Level", "aespa", R.drawable.img_album_exp4
            )
        )
        songDB.albumDao().insert(
            Album(
                4,
                "Weekend", "태연 (TAEYEON)", R.drawable.img_album_exp3
            )
        )
        songDB.albumDao().insert(
            Album(
                5,
                "Butter / Permission To Dance", "방탄소년단", R.drawable.img_album_exp5
            )
        )
        songDB.albumDao().insert(
            Album(
                6,
                "Savage - The 1st Mini Album", "aespa", R.drawable.img_album_exp6
            )
        )
    }
    private fun inputDummySongs(){
        songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()
        Log.d("MainActivity", songs.toString())
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
                1,
                true
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
                1
            )
        )
        songDB.songDao().insert(
            Song(
                "Coin",
                "아이유 (IU)",
                R.drawable.img_album_exp2,
                "music_lilac",
                200,
                0,
                false,
                false,
                false,
                1,
                true
            )
        )
        songDB.songDao().insert(
            Song(
                "봄 안녕 봄",
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
                "Celebrity",
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
                "돌림노래 (Feat. DEAN)",
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
                "빈 컵(Empty Cup)",
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
                "아이와 나의 바다",
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
                "어푸 (Ah puh)",
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
                "에필로그",
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
                "Butter",
                "방탄소년단",
                R.drawable.img_album_exp,
                "music_lilac",
                190,
                0,
                false,
                false,
                false,
                2,
                true
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
                2
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
                3,
                true
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
                4,
                true
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
                5
            )
        )
        songDB.songDao().insert(
            Song(
                "Permission To Dance",
                "방탄소년단",
                R.drawable.img_album_exp5,
                "music_lilac",
                190,
                0,
                false,
                false,
                false,
                5,
                true
            )
        )
        songDB.songDao().insert(
            Song(
                "Butter (Instrumental)",
                "방탄소년단",
                R.drawable.img_album_exp5,
                "music_lilac",
                190,
                0,
                false,
                false,
                false,
                5
            )
        )
        songDB.songDao().insert(
            Song(
                "Permission To Dance (Instrunmental)",
                "방탄소년단",
                R.drawable.img_album_exp5,
                "music_lilac",
                190,
                0,
                false,
                false,
                false,
                5
            )
        )

        songDB.songDao().insert(
            Song(
                "aenergy",
                "aespa",
                R.drawable.img_album_exp6,
                "music_lilac",
                200,
                0,
                false,
                false,
                false,
                6,
            )
        )
        songDB.songDao().insert(
            Song(
                "Savage",
                "aespa",
                R.drawable.img_album_exp6,
                "music_lilac",
                200,
                0,
                false,
                false,
                false,
                6,
                true
            )
        )
        songDB.songDao().insert(
            Song(
                "I'll Make You Cry",
                "aespa",
                R.drawable.img_album_exp6,
                "music_lilac",
                200,
                0,
                false,
                false,
                false,
                6,
            )
        )
        songDB.songDao().insert(
            Song(
                "YEPPI YEPPI",
                "aespa",
                R.drawable.img_album_exp6,
                "music_lilac",
                200,
                0,
                false,
                false,
                false,
                6,
            )
        )
        songDB.songDao().insert(
            Song(
                "ICONIC",
                "aespa",
                R.drawable.img_album_exp6,
                "music_lilac",
                200,
                0,
                false,
                false,
                false,
                6,
            )
        )
        songDB.songDao().insert(
            Song(
                "자각몽 (Lucid Dream)",
                "aespa",
                R.drawable.img_album_exp6,
                "music_lilac",
                200,
                0,
                false,
                false,
                false,
                6,
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
        Log.d("MA", "메인액티비티에서 보내는 currentTime은 ${song.currentTime} 프로그레스는 ${binding.mainMiniplayerSb.progress}")

        songDB.songDao().updateCurrentTimeById(song.currentTime, song.id)
    }
    override fun onDestroy() {
        super.onDestroy()

        player.interrupt() // 스레드 종료
        mediaPlayer?.release() // 미디어 플레이어 종료
        mediaPlayer = null
    }
}



