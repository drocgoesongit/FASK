package com.example.fask.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fask.Models.Product
import com.example.fask.ProductDetails
import com.example.fask.R
import com.squareup.picasso.Picasso

class CategoriesDetailAdapter(private val context: Context, private val productList: ArrayList<Product>): RecyclerView.Adapter<CategoriesDetailAdapter.ViewHolder>() {
    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val image = itemView.findViewById<ImageView>(R.id.product_image_pdttile)
        val name = itemView.findViewById<TextView>(R.id.product_name_pdttile)
        val price = itemView.findViewById<TextView>(R.id.product_price_pdttile)
        val rating = itemView.findViewById<TextView>(R.id.product_rating_pdttile)
        val heart =  itemView.findViewById<ImageView>(R.id.heart_product_pdttlile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_tiles, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        Picasso.get().load(product.image).placeholder(R.drawable.ic_food_ph).into(holder.image)
        holder.name.setText(product.name)
        holder.price.text = "₹ ${product.price}"
        holder.rating.text = product.rating.toString()
        holder.itemView.setOnClickListener(View.OnClickListener {
            val id = product.id
            val intent = Intent(context, ProductDetails::class.java)
            intent.putExtra("id",id)
            context.startActivity(intent)
        })
        holder.heart.setOnClickListener{
            if(holder.heart.getTag().equals("unfilled")){
                holder.heart.setImageResource(R.drawable.ic_filled_heart)
                holder.heart.setTag("filled")
            }else if(holder.heart.getTag().equals("filled")){
                holder.heart.setImageResource(R.drawable.ic_unfilled_heart)
                holder.heart.setTag("unfilled")
            }
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }


}