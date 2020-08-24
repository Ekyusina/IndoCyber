package com.example.indocybertest.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ReviewResponse: Serializable {
    @SerializedName("id")
    var id: Int? = 0
    @SerializedName("page")
    var page: Int? = 0
    @SerializedName("results")
    var result: ArrayList<Review>? = null
    @SerializedName("total_pages")
    var totalPages: Int? = 0
    @SerializedName("total_results")
    var totalResult: Int? = 0
}