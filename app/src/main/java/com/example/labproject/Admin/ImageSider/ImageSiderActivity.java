package com.example.labproject.Admin.ImageSider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.labproject.Admin.ImageSider.UploadImageSiderActivity;
import com.example.labproject.Admin.ImageSider.model.DataClass;
import com.example.labproject.Admin.ImageSider.model.MyAdapter;
import com.example.labproject.R;
import com.example.labproject.databinding.ActivityImageSiderBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ImageSiderActivity extends AppCompatActivity {

    ActivityImageSiderBinding binding;
    private ArrayList<DataClass> dataList;
    private MyAdapter adapter;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SLIDERS");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityImageSiderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fab789.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getApplicationContext(), UploadImageSiderActivity.class));

            }
        });
        Toolbar mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("প্রদর্শিত নোটিশ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); // or implement your custom back action here
            }
        });

       // recyclerView = findViewById(R.id.recyclerView789);
        binding.recyclerView789.setHasFixedSize(true);
        binding.recyclerView789.setLayoutManager(new LinearLayoutManager(this));
        dataList = new ArrayList<>();
        adapter = new MyAdapter(dataList, this);
        binding.recyclerView789.setAdapter(adapter);

       // imageSlider=findViewById(R.id.image_slider159753789);



        ArrayList<SlideModel>slideModels=new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                slideModels.clear(); // Clear existing images
                dataList.clear();   // Clear existing data

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DataClass dataClass = dataSnapshot.getValue(DataClass.class);

                    if (dataClass != null) {
                        // Add images to the image slider
                        slideModels.add(new SlideModel(dataClass.getImageURL(), ScaleTypes.FIT));
                        // Add data to the data list
                        dataList.add(dataClass);
                    }
                }

                // Set the image list for the image slider
                binding.imageSlider159753789.setImageList(slideModels, ScaleTypes.FIT);

                // Notify your adapter to update the UI with the data
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error, if needed
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DataClass dataClass = dataSnapshot.getValue(DataClass.class);


                    dataList.add(dataClass);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}