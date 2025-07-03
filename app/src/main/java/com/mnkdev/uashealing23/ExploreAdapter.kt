package com.mnkdev.uashealing23

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mnkdev.uashealing23.databinding.CardExploreBinding
import com.squareup.picasso.Picasso
import android.content.Intent

class ExploreAdapter(val exploreList: ArrayList<Explore>, val isFavorite: Boolean = false,
                     val onClick: ((Explore) -> Unit)? = null ) : RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        val binding = CardExploreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExploreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        val explore = exploreList[position]
        val url = explore.image_url
        holder.binding.txtTitle.text = explore.name
        holder.binding.txtCategory.text = explore.category
        holder.binding.txtSummary.text = explore.description

        Picasso.get().load(url).into(holder.binding.imageExplore)

        holder.binding.btnReadMore.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, HealingDetailActivity::class.java)
            intent.putExtra("destination_id", explore.id)
            intent.putExtra("name", explore.name)
            intent.putExtra("category", explore.category)
            intent.putExtra("short_description", explore.short_description)
            intent.putExtra("description", explore.description)
            intent.putExtra("image_url", explore.image_url)

            if (isFavorite) {
                intent.putExtra("from_favorite", true)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return exploreList.size
    }

    class ExploreViewHolder(val binding: CardExploreBinding)
        :RecyclerView.ViewHolder(binding.root)
}