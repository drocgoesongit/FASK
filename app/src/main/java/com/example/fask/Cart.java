package com.example.fask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fask.databinding.ActivityCartBinding;

public class Cart extends AppCompatActivity {
    private ActivityCartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


    }
}