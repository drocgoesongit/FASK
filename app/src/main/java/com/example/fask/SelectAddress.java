package com.example.fask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fask.Models.Address;
import com.example.fask.databinding.ActivitySelectAddressBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SelectAddress extends AppCompatActivity {
    private ActivitySelectAddressBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        Objects.requireNonNull(getSupportActionBar()).hide();
        setTitle("Address");

        checkForAddress();

        binding.addAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectAddress.this, AddAddress.class);
                startActivity(intent);
            }
        });

        binding.proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectAddress.this, Checkout.class);
                startActivity(intent);
            }
        });
    }

    private void checkForAddress() {
        FirebaseDatabase.getInstance().getReference()
                .child("Address")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren()){
                            binding.card.setVisibility(View.VISIBLE);
                            binding.textView3.setText("Confirm your address.");
                            binding.proceedButton.setClickable(true);
                            for (DataSnapshot snapshot1: snapshot.getChildren()){
                                Address address = snapshot1.getValue(Address.class);
                                assert address != null;
                                binding.cityNameAddress.setText(address.getCity());
                                binding.address.setText(address.getAddress());
                                binding.phoneNumberAddress.setText(address.getPhoneNo());
                                binding.nameAddress.setText(address.getName());
                            }
                        }else{
                            binding.proceedButton.setClickable(false);
                            binding.card.setVisibility(View.INVISIBLE);
                            binding.textView3.setText("Add address by clicking on edit address.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}