package com.example.indocybertest.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MovieTrailer : Serializable {
    @SerializedName("id")
    var id: String? = null
    @SerializedName("iso_639_1")
    var iso_639_1: String? = null
    @SerializedName("iso_3166_1")
    var iso_3166_1: String? = null
    @SerializedName("key")
    var key: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("site")
    var site: String? = null
    @SerializedName("size")
    var size: Int? = 0
    @SerializedName("type")
    var type: String? = null
}