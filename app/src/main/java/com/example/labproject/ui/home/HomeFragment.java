package com.example.labproject.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.labproject.Admin.ImageSider.model.DataClass;
import com.example.labproject.Admin.ImageSider.model.MyAdapter;
import com.example.labproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class HomeFragment extends Fragment {
    private ImageSlider imageSlider;
    private DatabaseReference reference;
    private ImageView map;


    private ArrayList<DataClass> dataList;
    private MyAdapter adapter;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SLIDERS");


    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_home, container, false);
        TextView textView=view.findViewById(R.id.text123);

        // maps

        map=view.findViewById(R.id.image_view_maps);
        imageSlider=view.findViewById(R.id.image_slider101010);
        dataList = new ArrayList<>();
        adapter = new MyAdapter(dataList, view.getContext());
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });


        reference= FirebaseDatabase.getInstance().getReference("NEWS12");

        reference.child("notice").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String link=snapshot.getValue(String.class);
                textView.setSelected(true);

                textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);

                textView.setSelected(true); // This is required to start the marquee animation
                textView.setMarqueeRepeatLimit(-1); // -1 for infinite scrolling


                textView.setText(link);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ArrayList<SlideModel>slideModels=new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                slideModels.clear(); // Clear existing images
                dataList.clear();   // Clear existing data

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    DataClass dataClass= dataSnapshot.getValue(DataClass.class);
                 //   DataClass dataClass = dataSnapshot.getValue(DataClass.class);

                    if (dataClass != null) {
                        // Add images to the image slider
                        slideModels.add(new SlideModel(dataClass.getImageURL(), ScaleTypes.FIT));
                        // Add data to the data list
                        dataList.add(dataClass);
                    }
                }

                // Set the image list for the image slider
              imageSlider.setImageList(slideModels, ScaleTypes.FIT);

                // Notify your adapter to update the UI with the data
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error, if needed
            }
        });

        return  view;
    }

    private void openMap()
    {
        Uri uri=Uri.parse("geo:0,0?q=Shyamoli Textile Engineering College (STEC)");
        Intent intent= new Intent(Intent.ACTION_VIEW,uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
}