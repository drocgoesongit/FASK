package com.example.fask.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fask.Models.ArrayLists
import com.example.fask.R
import com.example.fask.SignUp
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

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

        holder.itemView.setOnClickListener{
            if(item.name.equals("logout")){
                val builder = MaterialAlertDialogBuilder(context)
                builder.setTitle("Logout")
                builder.setMessage("Are you sure to logout from the app.")
                builder.setPositiveButton("Yes"){ dialog, which ->
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(context, SignUp::class.java)
                    context.startActivity(intent)
                }
                builder.setNegativeButton("Cancel"){ dialog, which ->
                    // Do nothing.
                }
                builder.show()


            }
        }
    }

    override fun getItemCount(): Int {
        return  listOfProfileElement.size  }
}