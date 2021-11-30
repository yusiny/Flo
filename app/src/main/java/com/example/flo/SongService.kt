package com.example.flo

import android.annotation.SuppressLint
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SongService {
    private lateinit var lookView: LookView

    fun setLookView(lookView: LookView){
        this.lookView = lookView
    }

    fun getSongs(){
        val songService = getRetrofit().create(SongRetrofitInterface::class.java)

        lookView.onGetSongsLoading()

        songService.getSongs().enqueue(object : Callback<SongResponse> {
            @SuppressLint("LongLogTag")
            override fun onResponse(call: Call<SongResponse>, response: Response<SongResponse>) {
                Log.d("LOOKFRAG/API-RESPONSE", response.toString())

                val resp = response.body()!!

                Log.d("LOOKFRAG/API-RESPONSE-FLO", resp.toString())

                when(resp.code){
                    1000 -> lookView.onGetSongsSuccess(resp.result!!.songs)
                    else -> lookView.onGetSongsFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<SongResponse>, t: Throwable) {
                Log.d("LOOKFRAG/API-ERROR", t.message.toString())

                lookView.onGetSongsFailure(400, "네트워크 오류가 발생했습니다.")
            }

        })
        Log.d("LOOKFRAG/ASYNC", "hello")
    }
}