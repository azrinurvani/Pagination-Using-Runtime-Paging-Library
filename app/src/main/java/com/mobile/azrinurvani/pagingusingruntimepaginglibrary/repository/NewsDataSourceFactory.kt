package com.mobile.azrinurvani.pagingusingruntimepaginglibrary.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.mobile.azrinurvani.pagingusingruntimepaginglibrary.network.NetworkService
import com.mobile.azrinurvani.pagingusingruntimepaginglibrary.repository.model.News
import io.reactivex.disposables.CompositeDisposable

//TODO 6 - Create NewsDataSource Factory for provide Data Source
class NewsDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val networkService: NetworkService) : DataSource.Factory<Int, News>()
{
    val newsDataSourceLiveData = MutableLiveData<NewsDataSource>()

    override fun create(): DataSource<Int, News> {
        val newsDataSource = NewsDataSource(networkService,compositeDisposable)
        newsDataSourceLiveData.postValue(newsDataSource)
        return newsDataSource
    }

}