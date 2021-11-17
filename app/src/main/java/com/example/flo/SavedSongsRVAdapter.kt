package com.example.flo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemSavedsongsBinding

class SavedSongsRVAdapter(songs: ArrayList<Album>): RecyclerView.Adapter<SavedSongsRVAdapter.ViewHolder>() {
    private val ssongs : ArrayList<Album> = songs

    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSavedsongsBinding = ItemSavedsongsBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ssongs[position])
        holder.binding.itemSongMoreIv.setOnClickListener {
            removeSong(position)
            //mItemClickListener.onRemoveSong(ssongs[position].id)
        }
    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun addSongs(songs: ArrayList<Song>) {
//        this.songs.clear()
//        this.songs.addAll(songs)
//
//        notifyDataSetChanged()
//    }
//
    fun removeSong(position: Int){
        ssongs.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = ssongs.size

    inner class ViewHolder(val binding: ItemSavedsongsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(song: Album){
            binding.itemSongImgIv.setImageResource(song.coverImg!!)
            binding.itemSongTitleTv.text = song.title
            binding.itemSongSingerTv.text = song.singer
        }
    }
}