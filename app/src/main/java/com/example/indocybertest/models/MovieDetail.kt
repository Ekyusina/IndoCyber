package com.example.indocybertest.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.NumberFormat
import java.util.*

class MovieDetail : Serializable{
    @SerializedName("original_title")
    var title: String? = null
    @SerializedName("overview")
    var overview: String? = null
    @SerializedName("popularity")
    var popularity: Number? = 0
    @SerializedName("release_date")
    var releaseDate: String? = null
    @SerializedName("status")
    var status: String? = null
    @SerializedName("tagline")
    var tagline: String? = null
    @SerializedName("budget")
    var budget: Int? = 0
    @SerializedName("vote_average")
    var voteAverage: Number? = 0
    @SerializedName("vote_count")
    var voteCount: Int? = 0
    @SerializedName("adult")
    var adult: Boolean? = false

    fun budgetInString(): String? {
        return "$" + NumberFormat.getNumberInstance(Locale.US).format(budget)
    }
}