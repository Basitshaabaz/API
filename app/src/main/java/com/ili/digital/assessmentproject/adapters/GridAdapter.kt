package com.ili.digital.assessmentproject.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ili.digital.assessmentproject.R
import com.ili.digital.assessmentproject.databinding.ItemGridBinding
import com.ili.digital.assessmentproject.data.model.MarsPhoto
import com.ili.digital.assessmentproject.util.loadUrl

class GridAdapter(
    private val onClickListener: OnClickListener
) :
    PagingDataAdapter<MarsPhoto, GridAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if(item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                onClickListener.clickListener(item)
            }
        }
    }

    fun getItemMars(position: Int):MarsPhoto{
        return  getItem(position)!!
    }

    /**
     * [DiffCallback] used for checking the content same in effective time interval
     */
    class DiffCallback : DiffUtil.ItemCallback<MarsPhoto>() {
        override fun areItemsTheSame(oldItem: MarsPhoto, newItem: MarsPhoto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MarsPhoto, newItem: MarsPhoto): Boolean {
            return oldItem == newItem
        }
    }


    /**
     * ItemView which will render each item with data
     * img_src url from image to be loaded
     * id unique image id got from API response
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemGridBinding.bind(view)

        fun bind(item: MarsPhoto) {
            // Bind data to views in the item layout
            binding.ivImage.loadUrl(item.img_src)
            binding.tvId.text = item.id.toString()
        }
    }

    /**
     * Standard lambda function which is invoked every time item is clicked
     */
    class OnClickListener(val clickListener: (photoList: MarsPhoto) -> Unit) {
        fun onClick(photo: MarsPhoto) = clickListener(photo)
    }
}

