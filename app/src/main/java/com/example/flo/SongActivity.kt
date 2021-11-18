package com.example.flo

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson
import kotlin.properties.Delegates

class SongActivity : AppCompatActivity() {
    //정방 선언(선 선언, 후 초기화) "선언하고 나중에 꼭 초기화 해 줄게!"
    //전역변수
    lateinit var binding : ActivitySongBinding

    private var songs = ArrayList<Song>()
    private var nowPos = 0
    private var albumId: Int = 0
    private lateinit var songDB: SongDatabase

    //미디어 플레이어와 스레드
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var player: Player

    //activity 생성 시, 처음으로 실행되는 함수
    //override 함수: 클래스서 상속받아 사용
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()
        initClickListener()
    }

    override fun onPause() {
        super.onPause()

        mediaPlayer?.pause() // 미디어 플레이어 중지
        player.isPlaying = false

        songs[nowPos].currentTime = binding.songPlayProgressPv.progress /1000

        //현재 time을 DB 에도 반영
        songDB.songDao().updateCurrentTimeById(songs[nowPos].currentTime, songs[nowPos].id)

        //sharedPreferences
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() //sharedPreferences 조작 시 사용

        //Gson 이용 데이터 변환
        editor.putInt("songId", songs[nowPos].id)
        editor.putBoolean("isPlaying", songs[nowPos].isPlaying)
        editor.apply()
    }

    //화면이 꺼질 떄 자동으로 불리는 함수
    override fun onDestroy(){
        super.onDestroy()
        player.interrupt() // 스레드 종료
        mediaPlayer?.release() // 미디어 플레이어 종료
        mediaPlayer = null
    }

    private fun initPlayList(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        albumId = spf.getInt("albumId", 1)

        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongsByAlbumId(albumId))
        Log.d("SongActivity", songs.toString())
    }

    private fun initSong(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        songs[nowPos].isPlaying = spf.getBoolean("isPlaying", false)
        Log.d("now songPos", songs[nowPos].toString())

        //스레드 시작
        startPlayer()
        //미디어 플레이어 시작
        setPlayer(songs[nowPos])
        //상태 적용
        mediaPlayer?.setOnPreparedListener {
         if(songs[nowPos].isPlaying) setPlayerStatus(songs[nowPos].isPlaying)
        }
    }
    private fun startPlayer(){
        player = Player(songs[nowPos].currentTime, songs[nowPos].isPlaying, songs[nowPos].isRepeated)
        player.start()
    }
    private fun setPlayer(song: Song){
        //UI 적용
        Log.d("currentTime", "currentTime은 ${song.currentTime}이고 ${song.currentTime/60}분 ${song.currentTime%60}초")
        binding.songPlayProgressStartTv.text = String.format("%02d:%02d", song.currentTime/ 60, song.currentTime % 60)
        binding.songPlayProgressEndTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime%60)
        binding.songAlbumTitleTv.text = song.title
        binding.songAlbumSingerTv.text = song.singer
        binding.songAlbumImgIv.setImageResource(song.coverImg!!)

        //mediaPlayer 연결해 주기
        val music = resources.getIdentifier(songs[nowPos].music, "raw", this.packageName)

        mediaPlayer = MediaPlayer.create(this, music)
        binding.songPlayProgressPv.max = mediaPlayer?.duration!! //노래 길이를 시크바 길이에 적용
        mediaPlayer?.seekTo(songs[nowPos].currentTime * 1000 )
        binding.songPlayProgressPv.setProgress(mediaPlayer?.currentPosition!!)

        setPlayerStatus(songs[nowPos].isPlaying)

        if(song.isLike)
            binding.songBtnLikeIv.setImageResource(R.drawable.ic_my_like_on)
        else
            binding.songBtnLikeIv.setImageResource(R.drawable.ic_my_like_off)
    }
    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    fun setPlayerStatus(isPlaying:Boolean){
        player.isPlaying = isPlaying
        songs[nowPos].isPlaying = isPlaying

        if(isPlaying){
            binding.songBtnPlayIv.visibility = View.GONE
            binding.songBtnPauseIv.visibility = View.VISIBLE

            mediaPlayer?.start()
        }else{
            binding.songBtnPlayIv.visibility = View.VISIBLE
            binding.songBtnPauseIv.visibility = View.GONE

            if(mediaPlayer?.isPlaying!!) mediaPlayer?.pause()
        }
    }

    private fun initClickListener() {
        //down 버튼 클릭 시, 액티비티 종료
        binding.songBtnDownIv.setOnClickListener{
            finish()
        }

        //seekBar 이벤트 리스너
        binding.songPlayProgressPv.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    mediaPlayer?.seekTo(progress)
                    player.currentTime = progress / 1000
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        //play-pause 버튼
        binding.songBtnPlayIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songBtnPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        //prev-next 버튼
        binding.songBtnPlayPrevIv.setOnClickListener {
            moveSong(-1)
        }
        binding.songBtnPlayNextIv.setOnClickListener {
            moveSong(+1)
        }

        //like, unlike 버튼 상태 조작
        binding.songBtnLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }

        //repeat 버튼 상태 조작
        if (songs[nowPos].isRepeated) setRepeatStatus(1)
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
    fun setRepeatStatus(isRepeating:Int){
        if(isRepeating == 0){
            binding.songBtnRepeatOffIv.visibility = View.GONE
            binding.songBtnRepeatOnIv.visibility = View.VISIBLE
            binding.songBtnRepeatOn1Iv.visibility = View.GONE
            songs[nowPos].isRepeated = false
        }else if(isRepeating == 1){
            binding.songBtnRepeatOffIv.visibility = View.GONE
            binding.songBtnRepeatOnIv.visibility = View.GONE
            binding.songBtnRepeatOn1Iv.visibility = View.VISIBLE
            songs[nowPos].isRepeated = true
            player.isRepeat = true
        }else if(isRepeating == 2){
            binding.songBtnRepeatOffIv.visibility = View.VISIBLE
            binding.songBtnRepeatOnIv.visibility = View.GONE
            binding.songBtnRepeatOn1Iv.visibility = View.GONE
            songs[nowPos].isRepeated = false
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
    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike, songs[nowPos].id)

        if(isLike)
            binding.songBtnLikeIv.setImageResource(R.drawable.ic_my_like_off)
        else
            binding.songBtnLikeIv.setImageResource(R.drawable.ic_my_like_on)
    }
    private fun moveSong(direct: Int){
        if(nowPos + direct < 0){
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos + direct >= songs.size)
        {
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
                return
        }

        nowPos += direct

        player.interrupt() //스레드 종료
        startPlayer() //스레드 재시작
        mediaPlayer?.release() //미디어 플레이어가 가지고 있던 소스 해제
        mediaPlayer = null // 해제

        setPlayer(songs[nowPos])
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
//                    if(isRepeat){
//                        mediaPlayer?.isLooping = true
//                    }else{
//                        mediaPlayer?.isLooping = false
//                    }

                    //플레이 중에만 타이머 go
                    if(isPlaying){
                        sleep(1000)
                        runOnUiThread{
                            binding.songPlayProgressPv.setProgress(mediaPlayer?.currentPosition!!) //현재 재생 위치 시크바에 적용
                            songs[nowPos].currentTime = binding.songPlayProgressPv.progress / 1000
                            binding.songPlayProgressStartTv.text = String.format("%02d:%02d",songs[nowPos].currentTime/ 60, songs[nowPos].currentTime % 60)
                        }
                    }
                }
            }catch(e : InterruptedException){
                Log.d("interrupt", "쓰레드가 종료되었습니다.")
            }

        }
    }

}

