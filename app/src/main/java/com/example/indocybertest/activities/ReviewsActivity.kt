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
import com.example.indocybertest.adapter.ReviewAdapter
import com.example.indocybertest.apiservice.APIInterface
import com.example.indocybertest.apiservice.ApiClient
import com.example.indocybertest.apiservice.BuildConfig
import com.example.indocybertest.models.Review
import com.example.indocybertest.models.ReviewResponse
import kotlinx.android.synthetic.main.activity_reviews.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewsActivity : AppCompatActivity(), ReviewAdapter.Listener {

    var apiInterface: APIInterface? = null
    var progressDialog: ProgressDialog? = null
    var reviewAdapter: ReviewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)
        ButterKnife.bind(this)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras
        if (bundle == null) {
            Toast.makeText(applicationContext, "Bundle Null", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        reviewAdapter = ReviewAdapter()
        reviewAdapter?.listener = this

        val id = bundle.getInt(KEY_MOVIE_ID)

        progressDialog = ProgressDialog(this)
        progressDialog?.setMessage("Loading....")
        progressDialog?.show()

        apiInterface = ApiClient().getRetrofitInstance()?.create(APIInterface::class.java)
        getApiListReview(id)
    }

    fun getApiListReview(id: Int?, page: Int = 1){
        val call: Call<ReviewResponse?>? = apiInterface?.getListReview(id, BuildConfig.API_KEY, page)
        call?.enqueue(object : Callback<ReviewResponse?> {
            override fun onResponse(call: Call<ReviewResponse?>, response: Response<ReviewResponse?>) {
                recycleViewReviews.visibility = if (response?.body()?.result?.size?.equals(0)!!) View.GONE else View.VISIBLE
                textViewEmpty.visibility = if (response?.body()?.result?.size?.equals(0)!!) View.VISIBLE else View.GONE

                progressDialog?.dismiss()
                //adapter
                reviewAdapter?.reviews = response.body()?.result
                recycleViewReviews.adapter = reviewAdapter

                if (response?.code().equals(401)!! || response?.code().equals(404)!!){
                    recycleViewReviews.visibility = View.GONE
                    textViewEmpty.visibility = View.VISIBLE
                    Toast.makeText(applicationContext, response?.errorBody()?.string(), Toast.LENGTH_SHORT).show()
                    return
                }
            }

            override fun onFailure(call: Call<ReviewResponse?>, t: Throwable) {
                progressDialog?.dismiss()
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object{
        private val KEY_MOVIE_ID = "MovieDetailActivity.movie.id"

        fun getIntent(context: Context, movieId: Int?): Intent {
            val intent = Intent(context, ReviewsActivity::class.java)
            intent.putExtra(KEY_MOVIE_ID, movieId)

            return intent
        }
    }

    override fun onClick(review: Review?) {

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
