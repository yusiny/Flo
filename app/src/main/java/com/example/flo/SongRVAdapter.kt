package com.example.flo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemSavedsongsBinding
import com.example.flo.databinding.ItemSongsBinding

class SongRVAdapter(): RecyclerView.Adapter<SongRVAdapter.ViewHolder>(){
    private val songs = ArrayList<Song>()



    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSongsBinding = ItemSongsBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    override fun getItemCount(): Int = songs.size

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemSongsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(song: Song){
            binding.itemSongsIndexTv.text = String.format("%02d", song.id)
            binding.itemSongsTitleTv.text = song.title
            binding.itemSongsSingerTv.text = song.singer
            if(song.isTitle) binding.itemSongsTitlelogoIv.visibility = View.VISIBLE
        }
    }
}