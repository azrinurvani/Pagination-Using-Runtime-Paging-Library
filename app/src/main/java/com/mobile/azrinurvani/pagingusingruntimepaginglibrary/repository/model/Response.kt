package com.mobile.azrinurvani.pagingusingruntimepaginglibrary.repository.model

import com.google.gson.annotations.SerializedName

//TODO 2 - Create data class
data class Response(
    @SerializedName("articles")
    val news: List<News>
)

data class News(
    val title: String,
    @SerializedName("urlToImage")
    val image: String
)