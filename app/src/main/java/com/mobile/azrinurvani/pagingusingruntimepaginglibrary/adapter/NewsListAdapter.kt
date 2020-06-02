package com.mobile.azrinurvani.pagingusingruntimepaginglibrary.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobile.azrinurvani.pagingusingruntimepaginglibrary.repository.model.News
import com.mobile.azrinurvani.pagingusingruntimepaginglibrary.repository.model.State

class NewsListAdapter(private val retry : ()-> Unit) : PagedListAdapter<News,RecyclerView.ViewHolder>(NewsDiffCallback) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2
    private var state = State.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType== DATA_VIEW_TYPE){
            NewsViewHolder.create(parent)
        }else{
            ListFooterViewHolder.create(retry,parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position)==DATA_VIEW_TYPE){
            (holder as NewsViewHolder).bind(getItem(position))
        }else{
            (holder as ListFooterViewHolder).bind(state)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (position<super.getItemCount()){
            DATA_VIEW_TYPE
        }else{
            FOOTER_VIEW_TYPE
        }
    }

    companion object{
        val NewsDiffCallback = object : DiffUtil.ItemCallback<News>(){
            override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
                return oldItem == newItem
            }

        }
    }

    private fun hasFooter(): Boolean{
        return super.getItemCount() !=0 && (state==State.LOADING||state==State.ERROR)
    }

    fun setState(state: State){
        this.state = state
        notifyItemChanged(super.getItemCount())
    }
}