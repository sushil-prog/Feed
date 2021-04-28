package com.ulu.feed.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.ulu.feed.models.Feed
import com.ulu.feed.R
import com.ulu.feed.databinding.RowItemBinding
import com.ulu.feed.ui.FeedListFragment

class RowItemAdapter(val callback: FeedListFragment.TaskItemClick) :
    RecyclerView.Adapter<RowViewHolder>() {

    /**
     * The feeds items that our Adapter will show
     */

//    var items: List<Feed> = ArrayList()
    private val items = ArrayList<Feed>()


    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val withDataBinding: RowItemBinding = RowItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RowViewHolder(withDataBinding)
    }

    fun setItems(items: ArrayList<Feed>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun addItems(items: ArrayList<Feed>) {
        this.items.clear()
        this.items.addAll(items)
        notifyItemRangeInserted(this.items.size , items.count());
    }

    override fun getItemCount() = items.size

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.item = items[position]
            it.callBack = callback
        }
    }

}

/**
 * ViewHolder for feed items items. All work is done by data binding.
 */
class RowViewHolder(val viewDataBinding: RowItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.row_item
    }
}