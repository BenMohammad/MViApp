package com.fukuni.mviapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fukuni.mviapp.R
import com.fukuni.mviapp.model.BlogPost
import kotlinx.android.synthetic.main.layout_blog_list_item.view.*

class MainRecyclerAdapter(private val interaction: Interaction? = null): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<BlogPost>) {
        differ.submitList(list)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is BlogPostViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BlogPostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_blog_list_item, parent, false), interaction
        )
    }
}

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BlogPost>() {
        override fun areItemsTheSame(oldItem: BlogPost, newItem: BlogPost): Boolean {
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(oldItem: BlogPost, newItem: BlogPost): Boolean {
            return oldItem == newItem
        }

    }



    class BlogPostViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
        ): RecyclerView.ViewHolder(itemView) {

            fun bind(item: BlogPost) = with(itemView) {
                itemView.setOnClickListener {
                    interaction?.onItemSelected(adapterPosition, item)
                }

                val requestOptions = RequestOptions
                    .overrideOf(1920, 1000)

                Glide.with(itemView.context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(item.image)
                    .into(itemView.blog_image)

                itemView.blog_title.text = item.title
            }

    }



    interface Interaction {
        fun onItemSelected(posistion: Int, item: BlogPost)
    }

