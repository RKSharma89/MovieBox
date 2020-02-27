package com.intact.moviesbox.data.model

import com.google.gson.annotations.SerializedName

data class NowPlayingMoviesData(
    @SerializedName("page") val pageNumber: String,
    @SerializedName("total_pages") val totalPages: String,
    @SerializedName("results") val movies: ArrayList<MovieData>
)