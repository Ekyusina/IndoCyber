package com.example.indocybertest.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import butterknife.ButterKnife
import com.example.indocybertest.R
import com.example.indocybertest.apiservice.APIInterface
import com.example.indocybertest.apiservice.ApiClient
import com.example.indocybertest.models.Movie
import com.example.indocybertest.models.MovieDetail
import com.example.indocybertest.models.MovieTrailerResponse
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.activity_movie_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailActivity : AppCompatActivity(), View.OnClickListener {

    var apiInterface: APIInterface? = null
    var progressDialog: ProgressDialog? = null
    var id: Int? = 0
    var videoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        ButterKnife.bind(this)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras
        if (bundle == null) {
            Toast.makeText(applicationContext, "Bundle Null", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val movie = bundle.get(KEY_MOVIE) as? Movie
        id = movie?.id

        supportActionBar?.title = movie?.name

        progressDialog = ProgressDialog(this)
        progressDialog?.setMessage("Loading....")
        progressDialog?.show()

        apiInterface = ApiClient().getRetrofitInstance()?.create(APIInterface::class.java)
        val call: Call<MovieDetail?>? = apiInterface?.getMovieDetail(id)
        call?.enqueue(object : Callback<MovieDetail?> {
            override fun onResponse(call: Call<MovieDetail?>, response: Response<MovieDetail?>) {

                setupLayout(response?.body())
                progressDialog?.dismiss()

                if (response?.code().equals(401)!! || response?.code().equals(404)!!){
                    layoutDetail.visibility = View.GONE
                    textViewEmpty.visibility = View.VISIBLE
                    Toast.makeText(applicationContext, response?.errorBody()?.string(), Toast.LENGTH_SHORT).show()
                    return
                }
            }

            override fun onFailure(call: Call<MovieDetail?>, t: Throwable) {
                progressDialog?.dismiss()
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })

        getApiMovieTrailer()

        thirdPartyPlayerView.getPlayerUiController().showFullscreenButton(true)
        thirdPartyPlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                videoId?.let { youTubePlayer.cueVideo(it, 0f) }
            }
        })

        thirdPartyPlayerView.getPlayerUiController().setFullScreenButtonClickListener(View.OnClickListener {
            if (thirdPartyPlayerView.isFullScreen()) {
                thirdPartyPlayerView.exitFullScreen()
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                // Show ActionBar
                if (supportActionBar != null) {
                    supportActionBar!!.show()
                }
            } else {
                thirdPartyPlayerView.enterFullScreen()
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                // Hide ActionBar
                if (supportActionBar != null) {
                    supportActionBar!!.hide()
                }
            }
        })

        thirdPartyPlayerView.getPlayerUiController().showVideoTitle(true)
    }

    fun getApiMovieTrailer(){
        val call: Call<MovieTrailerResponse?>? = apiInterface?.getMovieTrailer(id)
        call?.enqueue(object : Callback<MovieTrailerResponse?> {
            override fun onResponse(call: Call<MovieTrailerResponse?>, response: Response<MovieTrailerResponse?>) {

                if (response?.code().equals(401)!! || response?.code().equals(404)!!){
                    Toast.makeText(applicationContext, response?.errorBody()?.string(), Toast.LENGTH_SHORT).show()
                    return
                }

                if (response?.body()?.result?.size != 0){
                    for (movieTrailer in response?.body()?.result!!){
                        if (movieTrailer?.type == "Trailer"){
                            videoId = movieTrailer?.key
                            return
                        }
                    }
                }else{
                    thirdPartyPlayerView.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<MovieTrailerResponse?>, t: Throwable) {
                progressDialog?.dismiss()
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun setupLayout(movieDetail: MovieDetail?){
        layoutDetail.visibility = View.VISIBLE
        textViewEmpty.visibility = View.GONE

        textViewTitle.text = movieDetail?.title
        textViewOverview.text = movieDetail?.overview
        textViewPopularity.text = movieDetail?.popularity?.toString()
        textViewReleaseDate.text = movieDetail?.releaseDate
        textViewStatus.text = movieDetail?.status
        textViewTagline.text = movieDetail?.tagline
        textViewBudget.text = movieDetail?.budgetInString()
        textViewVoteAverage.text = movieDetail?.voteAverage?.toString()
        textViewVoteCount.text = movieDetail?.voteCount?.toString()
        textViewAdult.visibility = if (movieDetail?.adult == true) View.VISIBLE else View.GONE

        buttonViewAllReviews?.setOnClickListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object{
        private val KEY_MOVIE = "MovieDetailActivity.movie"

        fun getIntent(context: Context, movie: Movie?): Intent {
            val intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra(KEY_MOVIE, movie)

            return intent
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.buttonViewAllReviews -> {
                startActivity(ReviewsActivity.getIntent(this, id))
            }
        }
    }
}
