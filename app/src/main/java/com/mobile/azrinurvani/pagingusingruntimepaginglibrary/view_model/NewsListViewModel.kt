package com.mobile.azrinurvani.pagingusingruntimepaginglibrary.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.mobile.azrinurvani.pagingusingruntimepaginglibrary.network.NetworkService
import com.mobile.azrinurvani.pagingusingruntimepaginglibrary.repository.NewsDataSource
import com.mobile.azrinurvani.pagingusingruntimepaginglibrary.repository.NewsDataSourceFactory
import com.mobile.azrinurvani.pagingusingruntimepaginglibrary.repository.model.News
import com.mobile.azrinurvani.pagingusingruntimepaginglibrary.repository.model.State

import io.reactivex.disposables.CompositeDisposable
import java.util.*

class NewsListViewModel : ViewModel() {

    private val networkService = NetworkService.getService()
    var newsList : LiveData<PagedList<News>>
    private val compositeDisposable = CompositeDisposable()
    private val pageSize = 5
    private val newsDataSourceFactory : NewsDataSourceFactory


    init {
        newsDataSourceFactory = NewsDataSourceFactory(compositeDisposable,networkService)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize*2)
            .setEnablePlaceholders(false)
            .build()
        newsList = LivePagedListBuilder(newsDataSourceFactory,config).build()
    }

    fun getState(): LiveData<State> = Transformations.
        switchMap(newsDataSourceFactory.newsDataSourceLiveData,NewsDataSource::state)

    fun retry(){
        newsDataSourceFactory.newsDataSourceLiveData.value?.retry()
    }

    fun listEmpty(): Boolean{
        return newsList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}