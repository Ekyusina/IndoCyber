package com.example.indocybertest.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Review: Serializable {
    @SerializedName("id")
    var id: String? = null
    @SerializedName("author")
    var author: String? = null
    @SerializedName("content")
    var content: String? = null
    @SerializedName("url")
    var url: String? = null
}