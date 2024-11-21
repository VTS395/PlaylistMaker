package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
    private val ivCover: ImageView = itemView.findViewById(R.id.ivCover)
    private val tvtrackName: TextView = itemView.findViewById(R.id.tvTrackName)
    private val tvArtistNameAndTime: TextView = itemView.findViewById(R.id.tvArtistNameAndTime)

    @SuppressLint("SetTextI18n")
    fun bind(model: Track){
        tvtrackName.text = model.trackName
        tvArtistNameAndTime.text = "${model.artistName} • ${model.trackTime}"

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .fitCenter()
            .placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(2))
            .into(ivCover)
    }
}