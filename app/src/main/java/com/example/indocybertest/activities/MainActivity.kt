package com.example.indocybertest.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.example.indocybertest.R
import com.example.indocybertest.adapter.GenreAdapter
import com.example.indocybertest.apiservice.APIInterface
import com.example.indocybertest.apiservice.ApiClient
import com.example.indocybertest.models.GenreResponse
import com.example.indocybertest.models.Genre
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), GenreAdapter.Listener {

    var apiInterface: APIInterface? = null
    var progressDialog: ProgressDialog? = null
    var genresAdapter: GenreAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        genresAdapter = GenreAdapter()
        genresAdapter?.listener = this
        progressDialog = ProgressDialog(this)
        progressDialog?.setMessage("Loading....")
        progressDialog?.show()

        apiInterface = ApiClient().getRetrofitInstance()?.create(APIInterface::class.java)
        getApiListOfOfficialGenresforMovie()
    }

    fun getApiListOfOfficialGenresforMovie(){
        val call: Call<GenreResponse?>? = apiInterface?.getListOfOfficialGenresforMovie()
        call?.enqueue(object : Callback<GenreResponse?> {
            override fun onResponse(call: Call<GenreResponse?>, response: Response<GenreResponse?>) {
                //adapter
                genresAdapter?.genres = response.body()?.genres
                recycleViewGenres.adapter = genresAdapter
                progressDialog?.dismiss()

                if (response?.code().equals(401)!! || response?.code().equals(404)!!){
                    Toast.makeText(applicationContext, response?.errorBody()?.string(), Toast.LENGTH_SHORT).show()
                    return
                }
            }

            override fun onFailure(call: Call<GenreResponse?>, t: Throwable) {
                progressDialog?.dismiss()
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onClick(genre: Genre) {
        startActivity(
            MovieByGenreActivity.getIntent(
                this,
                genre
            )
        )
    }
}
