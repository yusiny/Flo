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

    private var song: Song = Song()
    private lateinit var player: Player
    //private val handler = Handler(Looper.getMainLooper())

    //미디어 플레이어
    private var mediaPlayer: MediaPlayer? = null

     var isRepeat: Boolean = false
     var changedFromUser: Boolean = false

    //Gson
    private var gson: Gson = Gson()

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

        //MainActivity에서 받아온 내용으로 Song
        initSong()

        //thread 초기화 후 시작 명령
        player = Player(song.playTime, song.currentTime, song.isPlaying)
        player.start()

        //down 버튼 클릭 시, 액티비티 종료
        binding.songBtnDownIv.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            song.currentTime = mediaPlayer?.currentPosition!!
            val json = gson.toJson(song)
            intent.putExtra("song", json)
            startActivity(intent)
        }

        //seekBar 이벤트 리스너
        binding.songPlayProgressPv.max = mediaPlayer?.duration!! //노래 길이를 시크바 길이에 적용
        binding.songPlayProgressPv.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    mediaPlayer?.seekTo(progress)
                    song.currentTime = progress
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


        //player 버튼 상태 조작
        if(song.isPlaying){
            setPlayerStatus(true)
        }else if(song.isPlaying){
            setPlayerStatus(false)
        }
        binding.songBtnPlayIv.setOnClickListener{
            setPlayerStatus(true)
            song.isPlaying = true
        }
        binding.songBtnPauseIv.setOnClickListener{
           setPlayerStatus(false)
            song.isPlaying = false
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

        //repeat 버튼 상태 조작
        if(isRepeat) setRepeatStatus(1)
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

    private fun initSong(){
        if(intent.hasExtra("song")){
            song = gson.fromJson(intent.getStringExtra("song"), Song::class.java)

            //mediaPlayer 연결해 주기
            val music = resources.getIdentifier(song.music, "raw", this.packageName)
            mediaPlayer = MediaPlayer.create(this, music)
            binding.songPlayProgressEndTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime%60)
            binding.songAlbumTitleTv.text = song.title
            binding.songAlbumSingerTv.text = song.singer
        }
    }

    fun setPlayerStatus(isPlaying:Boolean){
        if(isPlaying){
            binding.songBtnPlayIv.visibility = View.GONE
            binding.songBtnPauseIv.visibility = View.VISIBLE
            player.isPlaying = true
            mediaPlayer?.start()
        }else{
            binding.songBtnPlayIv.visibility = View.VISIBLE
            binding.songBtnPauseIv.visibility = View.GONE
            player.isPlaying = false
            mediaPlayer?.pause()
        }
    }

    fun setRepeatStatus(isRepeating:Int){
        if(isRepeating == 0){
            binding.songBtnRepeatOffIv.visibility = View.GONE
            binding.songBtnRepeatOnIv.visibility = View.VISIBLE
            binding.songBtnRepeatOn1Iv.visibility = View.GONE
            isRepeat = false
        }else if(isRepeating == 1){
            binding.songBtnRepeatOffIv.visibility = View.GONE
            binding.songBtnRepeatOnIv.visibility = View.GONE
            binding.songBtnRepeatOn1Iv.visibility = View.VISIBLE
            isRepeat = true
        }else if(isRepeating == 2){
            binding.songBtnRepeatOffIv.visibility = View.VISIBLE
            binding.songBtnRepeatOnIv.visibility = View.GONE
            binding.songBtnRepeatOn1Iv.visibility = View.GONE
            isRepeat = false
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

    //쓰레드를 위한 객체
    //생성자 playTime isPlaying 생성
    inner class Player(private val playTime:Int, var currentTime:Int, var isPlaying: Boolean) : Thread(){
        private var second = mediaPlayer?.currentPosition!! //쓰레드 내에서 활용할 타이머

        //player.start()로 쓰레드를 시작하면, run이 실행됨
        override fun run(){
            //강제 종료 위한 try catch
            //try 코드 내에서 오류가 난다면
            //catch InterreuptedExpection 오류를 발견한다면
            try{
                //쓰레드 실행
                while(true){
                    //노래 시간을 넘어가면 종료시킴
                    if(second >= playTime) {
                        if(isRepeat) second = 0
                        else break
                    }

                    //플레이 중에만 타이머 go
                    if(isPlaying){
                        sleep(1000)
                        second++
                        runOnUiThread{
                            binding.songPlayProgressPv.setProgress(mediaPlayer?.currentPosition!!) //현재 재생 위치 시크바에 적용
                            binding.songPlayProgressStartTv.text = String.format("%02d:%02d", second/ 60, second % 60)
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
        song.isPlaying = false
        song.currentTime = binding.songPlayProgressPv.progress * song.playTime / 1000 // 현재 시간 기록
        setPlayerStatus(false) // 버튼 이미지 변경

        //sharedPreferences
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() //sharedPreferences 조작 시 사용
        editor.apply()

        //Gson 이용 데이터 변환
        val json = gson.toJson(song)
        editor.putString("song", json)
    }

    //화면이 꺼질 떄 자동으로 불리는 함수
    override fun onDestroy(){
        super.onDestroy()
        player.interrupt() // 스레드 종료
        mediaPlayer?.release() // 미디어 플레이어 종료
        mediaPlayer = null
    }
}

