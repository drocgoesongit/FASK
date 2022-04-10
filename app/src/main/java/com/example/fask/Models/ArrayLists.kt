package com.example.fask.Models

import com.example.fask.R

class ArrayLists {
    fun loadProfileElements(): List<ProfileElement>{
        return listOf<ProfileElement>(
            ProfileElement("orders", "Your orders", R.drawable.ic_order_svg,"All your recent and old orders"),
            ProfileElement("wishlist", "Wishlist", R.drawable.ic_heart_svg_simple,"List of products you liked"),
            ProfileElement("setting", "Settings", R.drawable.ic_setting_svg,"Change username and profile photo"),
            ProfileElement("logout", "Log out", R.drawable.ic_logout_svg,"LogOut"),
            ProfileElement("about", "About us", R.drawable.ic_about_svg,""),
        )
    }
}