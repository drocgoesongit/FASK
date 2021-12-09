package com.example.fask.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fask.CategoryDetails
import com.example.fask.Models.Categories
import com.example.fask.R
import com.squareup.picasso.Picasso

class CategoriesSmallAdapter(val context: Context, val list:ArrayList<Categories>) : RecyclerView.Adapter<CategoriesSmallAdapter.ViewHolder>(){

    class ViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val image = itemView.findViewById<ImageView>(R.id.categories_image);
        val name = itemView.findViewById<TextView>(R.id.categoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.categories_tiles, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        Picasso.get().load(item.image).placeholder(R.color.dark_gray).into(holder.image)
        holder.name.setText(item.name)
        holder.image.setOnClickListener{
            val intent = Intent(context, CategoryDetails::class.java)
            intent.putExtra("name",item.name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int { return list.size    }
}