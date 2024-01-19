package com.example.labproject.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import com.example.labproject.Admin.Ebook.EbookActivity;
import com.example.labproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PdfShowActivity extends AppCompatActivity {

    private RecyclerView EbookrecyclerView;
    private DatabaseReference reference;
    private List<EbookData> list;
    private EbookAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_show);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Toolbar mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("College_Management");

        EbookrecyclerView=findViewById(R.id.ebookRecyclerView);

        reference= FirebaseDatabase.getInstance().getReference("pdf");


        getEbookData();
    }

    private void getEbookData()
    {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                list=new ArrayList<>();
                for (DataSnapshot snapshot1: snapshot.getChildren())
                {
                    EbookData data=snapshot1.getValue(EbookData.class);
                    list.add(data);
                }
                adapter= new EbookAdapter(PdfShowActivity.this,list);

                EbookrecyclerView.setLayoutManager(new LinearLayoutManager(PdfShowActivity.this));

                EbookrecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(getApplicationContext(),""
                        +error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}