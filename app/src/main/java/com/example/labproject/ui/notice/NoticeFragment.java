package com.example.labproject.ui.notice;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class NoticeFragment extends Fragment {

    private RecyclerView deleteRecyclerView;
    private ProgressBar progressBar;

    private ArrayList<NoticeData> list;
    private UserAdapter adapter;
    private DatabaseReference reference;

    public NoticeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_notice, container, false);
        reference= FirebaseDatabase.getInstance().getReference()
                .child("Notice");
        deleteRecyclerView=view.findViewById(R.id.deleteNoticeRecyclerView159);
        progressBar=view.findViewById(R.id.progress_circular159);

        deleteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        deleteRecyclerView.setHasFixedSize(true);

        getNotice();
        return  view;
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

                adapter= new UserAdapter(getContext(),list);

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                deleteRecyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(),"SomethingError"+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}