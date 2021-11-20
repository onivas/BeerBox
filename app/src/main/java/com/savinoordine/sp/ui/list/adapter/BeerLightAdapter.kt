package com.savinoordine.sp.ui.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.savinoordine.sp.R
import com.savinoordine.sp.databinding.BeerListItemBinding
import com.savinoordine.sp.domain.BeerLight
import com.savinoordine.sp.util.recyclerview.StandardDiffUtil

class BeerLightAdapter(private val onItemClick: (BeerLight) -> Unit) :
    ListAdapter<BeerLight, BeerLightAdapter.ViewHolder>(StandardDiffUtil()) {

    class ViewHolder(binding: BeerListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val container = binding.container
        private val name = binding.name
        private val tagline = binding.tagline
        private val description = binding.description
        private val image = binding.image

        fun bind(item: BeerLight, onItemClick: (BeerLight) -> Unit) {
            container.setOnClickListener { onItemClick(item) }
            name.text = item.name
            tagline.text = item.tagline
            description.text = item.description
            Glide.with(image.context)
                .load(item.imageUrl)
                .centerInside()
                .placeholder(R.drawable.ic_baseline_photo_24)
                .into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BeerListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }
}
