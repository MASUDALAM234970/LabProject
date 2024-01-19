package com.example.labproject.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.labproject.Admin.Delete.DeleteNoticeActivity;
import com.example.labproject.Admin.Ebook.EbookActivity;
import com.example.labproject.Admin.Gallary.UploadImage;
import com.example.labproject.Admin.ImageSider.ImageSiderActivity;
import com.example.labproject.Admin.Notice.UploadNotice;
import com.example.labproject.Admin.faculty.UpdateFaculty;
import com.example.labproject.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {

    ActivityAdminBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UploadNotice.class));
            }
        });


        binding.addGalleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UploadImage.class));
            }
        });


        binding.addEbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EbookActivity.class));
            }
        });



        binding.addFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UpdateFaculty.class));
            }
        });


        binding.addDeleteNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DeleteNoticeActivity.class));
            }
        });


        binding.SiderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ImageSiderActivity.class));
            }
        });


        binding.SiderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ImageSiderActivity.class));
            }
        });

    }
}