package com.example.labproject.ui.gallery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.labproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class GalleryFragment extends Fragment {

    private RecyclerView recyclerView_con,recyclerView_Inter,recyclerViewOther;
    private galleryAdapter adapter;
    DatabaseReference reference;

    public GalleryFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_gallery, container, false);


       recyclerView_con=view.findViewById(R.id.conRecyclerView);
       recyclerView_Inter=view.findViewById(R.id.interRecyclerView);
        recyclerViewOther=view.findViewById(R.id.otherRecyclerView);

        reference= FirebaseDatabase.getInstance().getReference("gallery");

        getGalleryImageConvocation();
        getGalleryImageOtherEvent();
        getGalleryImageIndependenceDey();

        return view;
    }
    private void getGalleryImageIndependenceDey()
    {
        List<String> imagelist=new ArrayList<>();
        reference.child("Independence Dey ").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot snapshot1:snapshot.getChildren())
                {
                    String data= (String) snapshot1.getValue();
                    imagelist.add(data);
                }
                adapter= new galleryAdapter(getContext(),imagelist);
                recyclerView_Inter.setLayoutManager(new GridLayoutManager(getContext(), 3));

                recyclerView_Inter.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void getGalleryImageOtherEvent()
    {
        List<String>imagelist=new ArrayList<>();
        reference.child("Others ever").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot snapshot1:snapshot.getChildren())
                {
                    String data= (String) snapshot1.getValue();
                    imagelist.add(data);
                }
                adapter= new galleryAdapter(getContext(),imagelist);
                recyclerViewOther.setLayoutManager(new GridLayoutManager(getContext(), 3));

                recyclerViewOther.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getGalleryImageConvocation()
    {
        List<String>imagelist=new ArrayList<>();
        reference.child("Convocation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot snapshot1:snapshot.getChildren())
                {
                    String data= (String) snapshot1.getValue();
                    imagelist.add(data);
                }
                adapter= new galleryAdapter(getContext(),imagelist);
                recyclerView_con.setLayoutManager(new GridLayoutManager(getContext(), 3));

                recyclerView_con.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}