package com.example.indocybertest.adapter

import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.indocybertest.R
import com.example.indocybertest.apiservice.BuildConfig
import com.example.indocybertest.models.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_movie_by_genre.view.*
import java.util.ArrayList

class MovieByGenreAdapter: RecyclerView.Adapter<MovieByGenreAdapter.ViewHolder>() {
    var movies: ArrayList<Movie>? = null

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.row_movie_by_genre, null))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getMovies(position), listener)

    fun getMovies(position: Int): Movie? = movies?.get(position)

    override fun getItemCount(): Int = movies?.size ?: 0

    fun addMovie(movies: ArrayList<Movie>) {
        if (this.movies == null) {
            this.movies = ArrayList()
        }
        this.movies?.addAll(movies)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bind(movie: Movie?, listener: Listener?) = with(itemView) {

            textViewName.text = movie?.name
            textViewDesc.text = movie?.desc

            val posterUrl = BuildConfig.BASE_URL_IMAGE+movie?.posterPath

            Picasso.get()
                .load(if (TextUtils.isEmpty(posterUrl)) null else Uri.parse(posterUrl))
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .fit()
                .centerInside()
                .into(imageBackground)

            mainLayout.setOnClickListener {
                listener?.onClick(movie)
            }
        }
    }

    interface Listener {
        fun onClick(movie: Movie?)
    }
}