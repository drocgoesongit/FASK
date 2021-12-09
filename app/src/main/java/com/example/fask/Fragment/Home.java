package com.example.fask.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fask.Adapters.CategoriesSmallAdapter;
import com.example.fask.Models.Banner;
import com.example.fask.Models.Categories;
import com.example.fask.R;
import com.example.fask.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;


public class Home extends Fragment {
    private FragmentHomeBinding binding;
    private CategoriesSmallAdapter smallAdapter;
    private ArrayList<CarouselItem> carouselList;
    private ArrayList<Categories> categoriesList;
    private LinearLayoutManager llmForCategories;


    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        carouselList = new ArrayList<>();
        categoriesList = new ArrayList<>();
        llmForCategories = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

        // Methods.
        setCarousel();
        settingCategories();
        settingHotPicks();

        return binding.getRoot();
    }

    private void settingHotPicks() {
    }

    private void settingCategories() {
        FirebaseDatabase.getInstance().getReference().child("Categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoriesList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Categories category = snapshot1.getValue(Categories.class);
                    categoriesList.add(category);
                }
                smallAdapter = new CategoriesSmallAdapter(getContext(), categoriesList);
                binding.categoriesMainRecyclerView.setAdapter(smallAdapter);
                binding.categoriesMainRecyclerView.setLayoutManager(llmForCategories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setCarousel() {
        // Setting homescreen carousel.
        FirebaseDatabase.getInstance().getReference().child("Banner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carouselList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Banner banner = snapshot1.getValue(Banner.class);
                    carouselList.add(
                            new CarouselItem(banner.getImageUrl())
                    );
                }
                Log.i("Info", "carousel list has " + carouselList.size() + " items");
                binding.carousel.setData(carouselList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.carousel.start();
    }
}