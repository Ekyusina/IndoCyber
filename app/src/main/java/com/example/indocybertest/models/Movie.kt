package com.example.indocybertest.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Movie : Serializable{
    @SerializedName("description")
    var desc : String? = null
    @SerializedName("favorite_count")
    var favCount: Int? = 0
    @SerializedName("id")
    var id: Int? = 0
    @SerializedName("item_count")
    var itemCount: Int? = 0
    @SerializedName("iso_639_1")
    var iso: String? = null
    @SerializedName("list_type")
    var listType: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("poster_path")
    var posterPath: String? = null
}