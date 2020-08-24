package com.example.indocybertest.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.example.indocybertest.EndlessScrollListener
import com.example.indocybertest.R
import com.example.indocybertest.adapter.MovieByGenreAdapter
import com.example.indocybertest.apiservice.APIInterface
import com.example.indocybertest.apiservice.ApiClient
import com.example.indocybertest.apiservice.BuildConfig
import com.example.indocybertest.models.Genre
import com.example.indocybertest.models.Movie
import com.example.indocybertest.models.MovieResponse
import kotlinx.android.synthetic.main.activity_movie_by_genre.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieByGenreActivity : AppCompatActivity(), MovieByGenreAdapter.Listener {
    var apiInterface: APIInterface? = null
    var progressDialog: ProgressDialog? = null
    var movieAdapter: MovieByGenreAdapter? = null
    var call: Call<MovieResponse?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_by_genre)
        ButterKnife.bind(this)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras
        if (bundle == null) {
            Toast.makeText(applicationContext, "Bundle Null", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val genre = bundle.get(KEY_GENRE) as? Genre
        val id = genre?.id

        supportActionBar?.title = "List of "+ genre?.name + " Movies"

        movieAdapter = MovieByGenreAdapter()
        movieAdapter?.listener = this
        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(this, (recycleViewMovie?.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).orientation)
        recycleViewMovie?.addItemDecoration(dividerItemDecoration)

        progressDialog = ProgressDialog(this)
        progressDialog?.setMessage("Loading....")
        progressDialog?.show()

        apiInterface = ApiClient().getRetrofitInstance()?.create(APIInterface::class.java)
        getApiListOfDiscoverMoviesbyGenre(id)

        recycleViewMovie?.addOnScrollListener(object : EndlessScrollListener(recycleViewMovie?.layoutManager as LinearLayoutManager?){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                call = apiInterface?.getListOfDiscoverMoviesbyGenre(id, BuildConfig.API_KEY,page+1)
                call?.request()
            }
        })
    }

    fun getApiListOfDiscoverMoviesbyGenre(id: Int?, page: Int = 1){
        call = apiInterface?.getListOfDiscoverMoviesbyGenre(id,BuildConfig.API_KEY, page)
        call?.enqueue(object : Callback<MovieResponse?> {
            override fun onResponse(call: Call<MovieResponse?>, response: Response<MovieResponse?>) {
                progressDialog?.dismiss()

                //adapter
                response?.body()?.result?.let { movieAdapter?.addMovie(it) }
                recycleViewMovie.adapter = movieAdapter

                if (response?.code().equals(401)!! || response?.code().equals(404)!!){
                    Toast.makeText(applicationContext, response?.errorBody()?.string(), Toast.LENGTH_SHORT).show()
                    return
                }
            }

            override fun onFailure(call: Call<MovieResponse?>, t: Throwable) {
                progressDialog?.dismiss()
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object{
        private val KEY_GENRE = "MovieByGenreActivity.genre"

        fun getIntent(context: Context, genre: Genre?): Intent {
            val intent = Intent(context, MovieByGenreActivity::class.java)
            intent.putExtra(KEY_GENRE, genre)

            return intent
        }
    }

    override fun onClick(movie: Movie?) {
        startActivity(MovieDetailActivity?.getIntent(this, movie))
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
