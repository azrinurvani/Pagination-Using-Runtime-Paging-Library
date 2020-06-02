package com.mobile.azrinurvani.pagingusingruntimepaginglibrary.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.mobile.azrinurvani.pagingusingruntimepaginglibrary.network.NetworkService
import com.mobile.azrinurvani.pagingusingruntimepaginglibrary.repository.model.News
import com.mobile.azrinurvani.pagingusingruntimepaginglibrary.repository.model.State
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

//TODO 5 - NewsDataSource for source data implement PageKeyDataSource (paging runtime library)
//In this we can request data for page & limit
class NewsDataSource(
    private val networkService : NetworkService,
    private val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Int, News>()
{

    var state : MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, News>
    ) {
        updateState(State.LOADING)
        compositeDisposable.add(
            networkService.getNews(1,params.requestedLoadSize)
                .subscribe(
                    { response->
                        Log.v("ADX","LoadInitial.Response $response")
                        updateState(State.DONE)
                        callback.onResult(response.news,null,2)
                    },{
                        updateState(State.ERROR)
                        setRetry(Action { loadInitial(params,callback) })
                        Log.v("ADX","LoadInitial.exception ${it.localizedMessage}")
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            networkService.getNews(params.key,params.requestedLoadSize)
                .subscribe({response->
                    Log.v("ADX","LoadAfter.Response $response")
                    updateState(State.DONE)
                    callback.onResult(response.news,params.key+1)
                },{
                    updateState(State.ERROR)
                    setRetry(Action{loadAfter(params,callback)})
                    Log.v("ADX","LoadAfter.exception ${it.localizedMessage}")
                })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {

    }

    fun retry(){
        if(retryCompletable!=null){
            compositeDisposable.add(
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }
    private fun updateState(state: State){
        this.state.postValue(state)
    }

    private fun setRetry(action : Action?){
        retryCompletable =
            if (action==null)
                null
            else
                Completable.fromAction(action)
    }

}