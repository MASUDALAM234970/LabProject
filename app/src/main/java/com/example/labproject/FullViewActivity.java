package com.example.labproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class FullViewActivity extends AppCompatActivity {


    private PhotoView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_view);
        imageView=findViewById(R.id.fullImage);
        String image=getIntent().getStringExtra("image");

       Glide.with(this).load(image).into(imageView);
       // Picasso.get().load(image).into(imageView);



    }
}