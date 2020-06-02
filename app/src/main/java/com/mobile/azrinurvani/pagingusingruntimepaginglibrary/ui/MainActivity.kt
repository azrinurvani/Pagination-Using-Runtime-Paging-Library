package com.mobile.azrinurvani.pagingusingruntimepaginglibrary.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.azrinurvani.pagingusingruntimepaginglibrary.R
import com.mobile.azrinurvani.pagingusingruntimepaginglibrary.adapter.NewsListAdapter
import com.mobile.azrinurvani.pagingusingruntimepaginglibrary.repository.model.State
import com.mobile.azrinurvani.pagingusingruntimepaginglibrary.view_model.NewsListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel : NewsListViewModel
    private lateinit var newsListAdapter : NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(NewsListViewModel::class.java)

        initAdapter()
        initState()
    }

    private fun initAdapter() {
        newsListAdapter = NewsListAdapter { viewModel.retry() }
        recycler_view.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        recycler_view.adapter = newsListAdapter
        viewModel.newsList.observe(this, Observer {
            newsListAdapter.submitList(it)
        })
    }

    private fun initState() {
       txt_error.setOnClickListener { viewModel.retry() }
        viewModel.getState().observe(this, Observer {state->

            progress_bar.visibility = if (viewModel.listEmpty()&& state == State.LOADING){
                VISIBLE
            }else{
                GONE
            }

            txt_error.visibility = if (viewModel.listEmpty()&&state == State.ERROR){
                VISIBLE
            }else{
                GONE
            }

            if (!viewModel.listEmpty()){
                newsListAdapter.setState(state?:State.LOADING)
            }

        })
    }
}
