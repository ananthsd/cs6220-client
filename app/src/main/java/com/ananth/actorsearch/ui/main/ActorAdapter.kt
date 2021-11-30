package com.ananth.actorsearch.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ananth.actorsearch.Celebrity
import com.ananth.actorsearch.R

class ActorAdapter() : RecyclerView.Adapter<ActorAdapter.ActorViewHolder>() {
    private var celebrity:Celebrity? = null

    fun updateData(celebrity: Celebrity){
        this.celebrity = celebrity
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.article_item, parent, false)
        // Return a new holder instance
        return ActorViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        celebrity?.let { celebrity ->
            val article = celebrity.wikipedia.articles[position]
            holder.titleTextView.text = article.title
            holder.urlTextView.text = article.url
        }

    }

    override fun getItemCount(): Int {
        celebrity?.let { celebrity ->
            return celebrity.wikipedia.articles.size
        }
        return 0
    }
    inner class ActorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView = itemView.findViewById<TextView>(R.id.articleTitle)
        val urlTextView = itemView.findViewById<TextView>(R.id.articleUrl)
    }
}

