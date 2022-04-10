package com.example.fask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.fask.Adapters.CategoriesDetailAdapter;
import com.example.fask.Models.Product;
import com.example.fask.databinding.ActivityCategoryDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryDetails extends AppCompatActivity {
    private ActivityCategoryDetailsBinding binding;
    private ArrayList<Product> productList;
    private LinearLayoutManager glm;
    private CategoriesDetailAdapter adapter;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        // Getting the intent extras.
        category = getIntent().getStringExtra("name");

        binding.CategoryNameText.setText(category);
        productList = new ArrayList<>();
        glm = new LinearLayoutManager(this);
        adapter = new CategoriesDetailAdapter(this,productList);

        getList();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getList() {
        FirebaseDatabase.getInstance().getReference().child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Product product = snapshot1.getValue(Product.class);
                    Log.i("Info", "Product name" + product.getName());
                    if(product.getCategories() != null){
                        for(String categoryName: product.getCategories()){
                            Log.i("Info", "Category name" + categoryName);
                            if(categoryName.equals(category)){
                                productList.add(product);
                                Log.i("Info", "Product added" + product.getName());
                            }
                        }
                    }else{
                        // If there is no category add all.
                        productList.add(product);
                    }
                }
                binding.recyclerView.setAdapter(adapter);
                binding.recyclerView.setLayoutManager(glm);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CategoryDetails.this, "Can't load Products.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}