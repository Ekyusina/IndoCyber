package com.example.indocybertest.adapter

import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.indocybertest.R
import com.example.indocybertest.models.Review
import kotlinx.android.synthetic.main.row_reviews.view.*
import java.util.ArrayList

class ReviewAdapter: RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
    var reviews: ArrayList<Review>? = null
        set(reviews){
            field = reviews
            notifyDataSetChanged()
        }

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.row_reviews, null))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getReviews(position), listener)

    fun getReviews(position: Int): Review? = reviews?.get(position)

    override fun getItemCount(): Int = reviews?.size ?: 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(review: Review?, listener: Listener?) = with(itemView) {

            textViewAuthor.text = review?.author
            textViewContent.text = review?.content
            textViewUrl.text = review?.url
            Linkify.addLinks(textViewUrl, Linkify.WEB_URLS)

            itemView.setOnClickListener {
                review?.let { review -> listener?.onClick(review) }
            }
        }
    }

    interface Listener {
        fun onClick(review: Review?)
    }
}