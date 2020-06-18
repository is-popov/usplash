package com.ipopov.usplash.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.ipopov.usplash.R
import com.ipopov.usplash.ui.search.model.PhotoSearchVm
import com.squareup.picasso.Picasso
import java.util.*


class PhotoSearchAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    var items: MutableList<PhotoSearchVm?> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    private var itemLayout: Int = R.layout.list_photo_search

    fun setMaterials(items: List<PhotoSearchVm>) {
        this.items = items.toMutableList()
    }

    fun addMaterials(items: List<PhotoSearchVm>) {
        val lastPosition = this.items.size
        this.items.addAll(items)
        notifyItemRangeInserted(lastPosition, items.size)
    }

    fun startLoading() {
        this.items.add(null)
        this.notifyItemInserted(items.size - 1)
    }

    fun stopLoading() {
        val index = items.size - 1
        this.items.removeAt(index)
        this.notifyItemRemoved(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_LOADING) {
            val view = layoutInflater.inflate(R.layout.list_search_loading, parent, false)
            LoadingHolder(view)
        } else {
            val view = layoutInflater.inflate(viewType, parent, false)
            MaterialHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] == null) VIEW_TYPE_LOADING else itemLayout
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is MaterialHolder -> viewHolder.bindViewItem(items[position])
            is LoadingHolder -> viewHolder.bindViewItem()
        }
    }

    private inner class MaterialHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var viewImage: AppCompatImageView? = null

        fun bindViewItem(item: PhotoSearchVm?) {
            item ?: return

            viewImage = itemView.findViewById(R.id.view_photo_search_image)

            if (viewImage != null && item.imageUrl != null) {
                Picasso
                    .get()
                    .load(item.imageUrl)
                    .into(viewImage)
            }
        }
    }

    private inner class LoadingHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindViewItem() {
        }
    }

}

