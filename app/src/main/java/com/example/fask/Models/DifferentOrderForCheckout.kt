package com.example.fask.Models

data class DifferentOrderForCheckout (
    val address: String? = null,
    val productList: ArrayList<Order>? = null,
    val total: String? = null,
    val status: String? = null,
    val consumer: String? = null,
    val orderId: String? = null
)