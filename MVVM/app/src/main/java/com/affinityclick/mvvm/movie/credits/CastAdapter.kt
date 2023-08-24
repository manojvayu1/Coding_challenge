package com.affinityclick.mvvm.movie.credits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.affinityclick.mvvm.R
import com.bumptech.glide.Glide

class CastAdapter : RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

  private var castList = emptyList<Cast>()  // Assuming Cast is your data model

  fun setData(newList: List<Cast>) {
    castList = newList
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.credit_item, parent, false)
    return CastViewHolder(itemView)
  }

  override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
    val castMember = castList[position]
    holder.nameTextView.text = castMember.name
    holder.roleTextView.text = castMember.character
    Glide.with(holder.profileImageView.context).load(castMember.profilePath).into(holder.profileImageView)
  }

  override fun getItemCount() = castList.size

  class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val profileImageView: ImageView = itemView.findViewById(R.id.profileImageView)
    val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
    val roleTextView: TextView = itemView.findViewById(R.id.roleTextView)
  }
}
