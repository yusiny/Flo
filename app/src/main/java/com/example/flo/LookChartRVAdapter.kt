package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemChartSongBinding
import com.example.flo.databinding.ItemSavedsongsBinding

class LookChartRVAdapter(): RecyclerView.Adapter<LookChartRVAdapter.ViewHolder>() {
    private val songs = ArrayList<Song>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LookChartRVAdapter.ViewHolder {
        val binding: ItemChartSongBinding = ItemChartSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    override fun getItemCount(): Int = songs.size

    inner class ViewHolder(val binding: ItemChartSongBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(song: Song){
            binding.itemChartSongImgIv.setImageResource(song.coverImg!!)
            binding.itemChartSongTitleTv.text = song.title
            binding.itemChartSongSingerTv.text = song.singer
        }
    }
}