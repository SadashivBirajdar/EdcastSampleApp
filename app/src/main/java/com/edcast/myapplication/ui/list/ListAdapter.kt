package com.edcast.myapplication.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.edcast.myapplication.data.model.People
import com.edcast.myapplication.databinding.ListItemBinding

class ListAdapter(private val peopleSelectionListener: PeopleSelectionListener) :
    PagingDataAdapter<People, ListAdapter.ItemViewHolder>(DATA_COMPARATOR) {

    companion object {
        private val DATA_COMPARATOR = object : DiffUtil.ItemCallback<People>() {
            override fun areItemsTheSame(
                oldItem: People,
                newItem: People
            ): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: People,
                newItem: People
            ): Boolean =
                oldItem.name == newItem.name && oldItem.created == newItem.created
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        getItem(position)?.let { people ->
            holder.binding.apply {
                root.setOnClickListener {
                    peopleSelectionListener.onPeopleSelected(people)
                }
                txtNameValue.text = people.name
            }
        }
    }

    inner class ItemViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    interface PeopleSelectionListener {
        fun onPeopleSelected(people: People)
    }

}


