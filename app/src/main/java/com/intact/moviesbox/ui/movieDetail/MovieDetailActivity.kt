package com.intact.moviesbox.ui.movieDetail

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import com.intact.moviesbox.R
import com.intact.moviesbox.databinding.ActivityMovieDetailBinding
import com.intact.moviesbox.extension.observeLiveData
import com.intact.moviesbox.presentation.model.ErrorDTO
import com.intact.moviesbox.presentation.model.MovieDTO
import com.intact.moviesbox.presentation.viewmodels.MovieDetailViewModel
import com.intact.moviesbox.ui.base.BaseActivity
import com.intact.moviesbox.ui.base.CustomViewModelFactory
import com.intact.moviesbox.util.IMAGE_BASE_URL_500
import com.intact.moviesbox.util.MOVIE_ID
import com.squareup.picasso.Picasso
import dagger.android.DispatchingAndroidInjector
import timber.log.Timber
import javax.inject.Inject


/**
 * Activity to show the movie details
 *
 * Created by Anurag Garg on 2019-04-24.
 *
 * Data binding: A binding class is generated for each layout file. By default,
 * the name of the class is based on the name of the layout file, converting it to Pascal case
 * and adding the Binding suffix to it. The above layout filename is activity_main.xml so the
 * corresponding generated class is ActivityMainBinding.
 *
 * https://developer.android.com/topic/libraries/data-binding/expressions#kotlin
 */
class MovieDetailActivity : BaseActivity() {

    @Inject
    internal lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: CustomViewModelFactory

    @Inject
    lateinit var picasso: Picasso

    private var movieId: Long = 0
    private lateinit var movieDTO: MovieDTO
    private lateinit var binding: ActivityMovieDetailBinding
    private lateinit var movieDetailViewModel: MovieDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeVariables()
    }

    private fun initializeVariables() {
        // set up action bar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.run {
            this.setDisplayHomeAsUpEnabled(true)
            this.setDisplayShowHomeEnabled(true)
        }

        // get the view model
        movieDetailViewModel =
            ViewModelProviders.of(this@MovieDetailActivity, viewModelFactory)
                .get(MovieDetailViewModel::class.java)
        setObservers(movieDetailViewModel)
        getMovieId()

        // get the movie details
        movieDetailViewModel.getMovieDetail(movieId)
    }

    private fun getMovieId() {
        movieId = intent.extras!!.getLong(MOVIE_ID);
    }

    // setting the observers
    private fun setObservers(viewModel: MovieDetailViewModel) {
        observeLiveData(viewModel.getMovieDetailLiveData()) {
            Timber.d("Received the movie detail data, $it")
            movieDTO = it
            updateUI()
        }
        observeLiveData(viewModel.getErrorLiveData()) {
            Timber.d("Error for the movie detail data ${it.message}")
            onError(it)
        }
    }

    // on error received
    private fun onError(dto: ErrorDTO) {
        Timber.d("onError: $dto")
    }

    // update UI
    private fun updateUI() {
        // data binding
        binding.movieDetailDTO = movieDTO

        // view binding
        with(movieDTO) {
            binding.collapsingToolbar.title = title
            binding.releaseDateTV.text = releaseDate
            picasso.load(IMAGE_BASE_URL_500 + backdropPath)
                .placeholder(R.drawable.ic_video_camera).into(binding.posterIV)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}