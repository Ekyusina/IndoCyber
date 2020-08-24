package com.example.indocybertest.apiservice

import com.example.indocybertest.models.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIInterface {

    //list of official genres for movies
    @GET("genre/movie/list")
    fun getListOfOfficialGenresforMovie(@Query("api_key") api_key: String = BuildConfig.API_KEY): Call<GenreResponse?>

    //list of discover movies by genre
    @GET("movie/{movie_id}/lists")
    fun getListOfDiscoverMoviesbyGenre(@Path("movie_id") movieId: Int?,
                                       @Query("api_key") api_key: String?,
                                       @Query("page") page: Int?): Call<MovieResponse?>

    //movie detail
    @GET("movie/{movie_id}")
    fun getMovieDetail(@Path("movie_id") movieId: Int?,
                       @Query("api_key") api_key: String = BuildConfig.API_KEY): Call<MovieDetail?>

    //list reviews
    @GET("movie/{movie_id}/reviews")
    fun getListReview(@Path("movie_id") movieId: Int?,
                      @Query("api_key") api_key: String?,
                      @Query("page") page: Int?): Call<ReviewResponse?>

    //get movie trailer video
    @GET("movie/{movie_id}/videos")
    fun getMovieTrailer(@Path("movie_id") movieId: Int?,
                        @Query("api_key") api_key: String = BuildConfig.API_KEY): Call<MovieTrailerResponse?>
}