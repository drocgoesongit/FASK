package com.example.fask.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fask.Models.ArrayLists
import com.example.fask.R

class ProfileElementAdapters(val context: Context) : RecyclerView.Adapter<ProfileElementAdapters.ViewHolder>(){

    val listOfProfileElement = ArrayLists().loadProfileElements()

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val image = itemView.findViewById<ImageView>(R.id.imageProfileElement)
        val name = itemView.findViewById<TextView>(R.id.name_profileElement)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.profile_element_tile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOfProfileElement[position]
        holder.name.setText(item.fullName)
        holder.image.setImageResource(item.image)
    }

    override fun getItemCount(): Int {
        return  listOfProfileElement.size  }
}