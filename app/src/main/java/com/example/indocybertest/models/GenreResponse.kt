package com.example.indocybertest.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.ArrayList

class GenreResponse : Serializable{
    @SerializedName("genres")
    var genres: ArrayList<Genre>? = null
}