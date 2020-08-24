package com.example.indocybertest.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MovieTrailerResponse : Serializable {
    @SerializedName("id")
    var id: Int? = 0
    @SerializedName("results")
    var result: ArrayList<MovieTrailer>? = null
}