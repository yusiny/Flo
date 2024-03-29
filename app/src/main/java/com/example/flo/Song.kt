package com.example.flo

import androidx.room.Entity
import androidx.room.PrimaryKey

//제목, 가수, 사진, music, 재생시간, 현재 재생시간, isplaying(재생되고 있는지), isRepeated(반복하는지), isLike, albumIdx
@Entity(tableName = "SongTable")
data class Song(
    var title:String = "",
    var singer:String ="",
    var coverImg: Int? = null,
    var coverImgUrl: String? = null,
    var music: String = "",
    var playTime:Int = 0,
    var currentTime: Int = 0,
    var isPlaying:Boolean = false,
    var isRepeated:Boolean = false,
    var isLike: Boolean = false,
    var albumIdx: Int = 0,
    var isTitle: Boolean = false,
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
