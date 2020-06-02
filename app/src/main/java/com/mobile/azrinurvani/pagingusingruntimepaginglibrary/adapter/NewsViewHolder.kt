package com.mobile.azrinurvani.pagingusingruntimepaginglibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.azrinurvani.pagingusingruntimepaginglibrary.R
import com.mobile.azrinurvani.pagingusingruntimepaginglibrary.repository.model.News
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_news.view.*

class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item : News?){
        if (item!=null){
            itemView.txt_news_name.text = item.title
            Picasso.get().load(item.image).into(itemView.img_news_banner)
        }
    }

    companion object{
        fun create(parent:ViewGroup): NewsViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false)
            return NewsViewHolder(view)
        }
    }
}