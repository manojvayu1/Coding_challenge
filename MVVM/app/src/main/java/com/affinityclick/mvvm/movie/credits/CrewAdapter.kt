package com.affinityclick.mvvm.movie.credits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.affinityclick.mvvm.R
import com.bumptech.glide.Glide

class CrewAdapter : RecyclerView.Adapter<CrewAdapter.CrewViewHolder>() {

  private var crewList = emptyList<Crew>()  // Assuming Crew is your data model

  fun setData(newList: List<Crew>) {
    crewList = newList
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrewViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.credit_item, parent, false)
    return CrewViewHolder(itemView)
  }

  override fun onBindViewHolder(holder: CrewViewHolder, position: Int) {
    val crewMember = crewList[position]
    holder.nameTextView.text = crewMember.name
    holder.roleTextView.text = crewMember.job
    Glide.with(holder.profileImageView.context).load(crewMember.profilePath).into(holder.profileImageView)
  }

  override fun getItemCount() = crewList.size

  class CrewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val profileImageView: ImageView = itemView.findViewById(R.id.profileImageView)
    val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
    val roleTextView: TextView = itemView.findViewById(R.id.roleTextView)
  }
}
