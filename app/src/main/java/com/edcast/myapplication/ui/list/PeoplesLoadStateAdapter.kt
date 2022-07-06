package com.edcast.myapplication.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.edcast.myapplication.databinding.LoadingListItemBinding

class PeoplesLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<PeoplesLoadStateAdapter.LoadingFooterViewHolder>() {
    override fun onBindViewHolder(holder: LoadingFooterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingFooterViewHolder {
        return LoadingFooterViewHolder.create(parent, retry)
    }

    class LoadingFooterViewHolder(
        private val binding: LoadingListItemBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }
            if (loadState is LoadState.Loading) {
                binding.loadingItemProgressBar.visibility = View.VISIBLE
            } else {
                binding.loadingItemProgressBar.visibility = View.GONE
            }
            if (loadState !is LoadState.Loading) {
                binding.retryButton.visibility = View.VISIBLE
                binding.errorMsg.visibility = View.VISIBLE
            } else {
                binding.retryButton.visibility = View.GONE
                binding.errorMsg.visibility = View.GONE
            }
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): LoadingFooterViewHolder {
                val view =
                    LoadingListItemBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                return LoadingFooterViewHolder(view, retry)
            }
        }
    }
}