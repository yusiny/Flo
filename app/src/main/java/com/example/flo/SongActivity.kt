package com.example.flo

import android.os.Bundle
import android.view.View
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

        //down 버튼 클릭 시, 이벤트
        binding.songBtnDownIv.setOnClickListener{
            finish()
        }

        //player 버튼 상태 조작
        binding.songBtnPlayIv.setOnClickListener{
            setPlayerStatus(false)
        }
        binding.songBtnPauseIv.setOnClickListener{
           setPlayerStatus(true)
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
}