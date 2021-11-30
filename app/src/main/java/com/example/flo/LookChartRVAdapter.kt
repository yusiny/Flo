package com.example.flo

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo.databinding.ItemChartSongBinding
import com.example.flo.databinding.ItemSavedsongsBinding

class LookChartRVAdapter(val context: Context): RecyclerView.Adapter<LookChartRVAdapter.ViewHolder>() {
    private val songs = ArrayList<Song>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LookChartRVAdapter.ViewHolder {
        val binding: ItemChartSongBinding = ItemChartSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position], position)
    }

    override fun getItemCount(): Int = songs.size


    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemChartSongBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(song: Song, position: Int){
            if(song.coverImgUrl == ""){
                //binding.itemChartSongImgIv.setImageResource(song.coverImg!!)
                Glide.with(context).load(song.coverImg!!).into(binding.itemChartSongImgIv)
            }else{
                Glide.with(context).load(song.coverImgUrl).into(binding.itemChartSongImgIv) //이미지 알아서 다운로드

            }

            binding.itemChartSongLankTv.text = String.format("%d", position+1)
            binding.itemChartSongTitleTv.text = song.title
            binding.itemChartSongSingerTv.text = song.singer
        }
    }
}