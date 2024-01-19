package com.example.labproject.Admin.Delete;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.labproject.Admin.Notice.NoticeData;
import com.example.labproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DeleteNoticeActivity extends AppCompatActivity {

    private RecyclerView deleteRecyclerView;
    private ProgressBar progressBar;

    private ArrayList<NoticeData> list;
    private NoticeAdapter adapter;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_notice);

        Toolbar mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("College_Management");

        reference= FirebaseDatabase.getInstance().getReference()
                .child("Notice");
        deleteRecyclerView=findViewById(R.id.deleteNoticeRecyclerView159);
        progressBar=findViewById(R.id.progress_circular159);

        deleteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        deleteRecyclerView.setHasFixedSize(true);

        getNotice();
    }

    private void getNotice() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot1: snapshot.getChildren())
                {
                    NoticeData data =snapshot1.getValue(NoticeData.class);

                    list.add(data);
                }

                adapter= new NoticeAdapter(DeleteNoticeActivity.this,list);

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                deleteRecyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(),"SomethingError"+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}