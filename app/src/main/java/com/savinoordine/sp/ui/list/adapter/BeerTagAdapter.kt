package com.savinoordine.sp.ui.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.savinoordine.sp.databinding.BeerTagItemBinding
import com.savinoordine.sp.util.recyclerview.StandardDiffUtil

class BeerTagAdapter(private val onItemClick: (String) -> Unit) :
    ListAdapter<String, BeerTagAdapter.ViewHolder>(StandardDiffUtil()) {

    class ViewHolder(binding: BeerTagItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val container = binding.container
        private val name = binding.beerTag

        fun bind(item: String, onItemClick: (String) -> Unit, position: Int) {
            container.setOnClickListener { onItemClick(item) }
            name.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BeerTagItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick, position)
    }
}
