package com.example.indocybertest.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MovieResponse : Serializable{
    @SerializedName("id")
    var id: Int? = 0
    @SerializedName("page")
    var page: Int? = 0
    @SerializedName("results")
    var result: ArrayList<Movie>? = null
    @SerializedName("total_pages")
    var totalPages: Int? = 0
    @SerializedName("total_results")
    var totalResult: Int? = 0
}