package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
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

        val cornerRadius = dpToPx(2f, itemView.context)

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .fitCenter()
            .placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(cornerRadius))
            .into(ivCover)
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}