package com.example.indocybertest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.indocybertest.R
import com.example.indocybertest.models.Genre
import kotlinx.android.synthetic.main.row_genre.view.*
import java.util.ArrayList

class GenreAdapter : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {
    var genres: ArrayList<Genre>? = null
        set(genres){
            field = genres
            notifyDataSetChanged()
        }

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.row_genre, null))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getGenres(position), listener)

    fun getGenres(position: Int): Genre? = genres?.get(position)

    override fun getItemCount(): Int = genres?.size ?: 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(genre: Genre?, listener: Listener?) = with(itemView) {

            textViewGenre.text = genre?.name

            itemView.setOnClickListener {
                genre?.let { genre -> listener?.onClick(genre) }
            }
        }
    }

    interface Listener {
        fun onClick(genre: Genre)
    }
}