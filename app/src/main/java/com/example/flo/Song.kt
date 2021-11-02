package com.example.flo

//제목, 가수, 사진, 재생시간, 현재 재생시간, isplaying(재생되고 있는지), isRepeated(반복하는지)
data class Song(
    var title:String = "",
    var singer:String ="",
    var music: String = "",
    var playTime:Int = 0,
    var currentTime: Int = 0,
    var isPlaying:Boolean = false,
    var isRepeated:Boolean = false,
)
