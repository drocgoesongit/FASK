package com.example.fask.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fask.Adapters.ProfileElementAdapters;
import com.example.fask.Models.ProfileElement;
import com.example.fask.Models.Users;
import com.example.fask.R;
import com.example.fask.databinding.FragmentHomeBinding;
import com.example.fask.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Profile extends Fragment {
private FragmentProfileBinding binding;
private ProfileElementAdapters adapter;
private GridLayoutManager glm;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                binding.usernameProfileElement.setText(user.getUsername().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // setting the recyclerView.
        adapter = new ProfileElementAdapters(getContext());
        glm = new GridLayoutManager(getContext(),2);
        binding.recyclerViewProfile.setAdapter(adapter);
        binding.recyclerViewProfile.setLayoutManager(glm);
        return binding.getRoot();
    }
}