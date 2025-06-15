package com.mnkdev.uashealing23

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mnkdev.uashealing23.databinding.CardExploreBinding
import com.squareup.picasso.Picasso

class ExploreAdapter(val exploreList: ArrayList<Explore>) : RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        val binding = CardExploreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExploreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        val url = exploreList[position].image_url
        holder.binding.txtTitle.text = exploreList[position].title
        holder.binding.txtCategory.text = exploreList[position].category
        holder.binding.txtSummary.text = exploreList[position].summary

        Picasso.get().load(url).into(holder.binding.imageExplore)

        holder.binding.btnReadMore.setOnClickListener {
            // Tambahkan kode untuk membuka halaman detail di sini
            // Anda dapat menggunakan Intent atau navigasi lainnya

        }
    }

    override fun getItemCount(): Int {
        return exploreList.size
    }

    class ExploreViewHolder(val binding: CardExploreBinding)
        :RecyclerView.ViewHolder(binding.root)
}