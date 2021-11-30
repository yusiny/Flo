package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentLookBinding


class LookFragment : Fragment(), LookView {

    private lateinit var binding: FragmentLookBinding
    private lateinit var songRVAdapter: LookChartRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLookBinding.inflate(inflater, container, false)

        initRV()
        getSongs()

        return binding.root
    }


    private fun initRV(){
        songRVAdapter = LookChartRVAdapter(requireContext())
        binding.lookRv.adapter = songRVAdapter
        binding.lookRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    }

    private fun getSongs(){
        val songService = SongService()
        songService.setLookView(this)

        songService.getSongs()
    }

    override fun onGetSongsLoading() {
       //로딩바
    }

    override fun onGetSongsSuccess(songs: ArrayList<Song>) {
        //로딩바 끔

        songRVAdapter.addSongs(songs)

    }

    override fun onGetSongsFailure(code: Int, message: String) {
        //로딩바 끔

        when(code){
            400 -> Log.d("LOOKFRAG/API-ERROR", message)
        }
    }
}