package com.example.fask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fask.Adapters.CartAdapter;
import com.example.fask.Models.Order;
import com.example.fask.databinding.ActivityCartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {
    private ActivityCartBinding binding;
    private ArrayList<Order> orderList;
    private CartAdapter adapter;
    private LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        getSupportActionBar().hide();
        setTitle("Cart");

        orderList = new ArrayList<>();
        llm = new LinearLayoutManager(this);

        getOrders();

        binding.checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderList.size() != 0){
                    Intent intent = new Intent(Cart.this, SelectAddress.class );
                    startActivity(intent);
                }else{
                    Toast.makeText(Cart.this, "Add items to cart first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getOrders() {
        FirebaseDatabase.getInstance().getReference().
                child("Orders").
                child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Order order = snapshot1.getValue(Order.class);
                    orderList.add(order);
                }
                adapter = new CartAdapter(Cart.this, orderList);
                binding.cartRecyclerView.setAdapter(adapter);
                binding.cartRecyclerView.setLayoutManager(llm);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}