package com.example.labproject.Admin.faculty;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.labproject.R;
import com.squareup.picasso.Picasso;


import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewAdapter>
{

    private List<TeacherData>list;
    private Context context;

    public TeacherAdapter(List<TeacherData> list, Context context, String category) {
        this.list = list;
        this.context = context;
        this.category = category;
    }

    private String category;



    @NonNull
    @Override
    public TeacherViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.faculty_update_item,parent,false);





        return new TeacherViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewAdapter holder, int position)
    {

        TeacherData item=list.get(position);
        holder.name.setText(item.getName());
        holder.phone.setText(item.getPhone());
        holder.email.setText(item.getEmail());
        holder.post.setText(item.getPost());

        try {

            Picasso.get().load(item.getImage()).into(holder.imageView);
        } catch (Exception e) {

            e.printStackTrace();
        }

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Update_teacher_Activity.class);

                intent.putExtra("name",item.getName());
                intent.putExtra("phone",item.getPhone());
                intent.putExtra("email",item.getEmail());
                intent.putExtra("post",item.getPost());
                intent.putExtra("image",item.getImage());
                intent.putExtra("key",item.getKey());
                intent.putExtra("category",category);
                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TeacherViewAdapter extends RecyclerView.ViewHolder
    {
        private TextView name, phone, email, post;
        private Button update;
        private ImageView imageView;

        public TeacherViewAdapter(@NonNull View itemView) {

            super(itemView);

            name=itemView.findViewById(R.id.teacherName);
            phone=itemView.findViewById(R.id.teacherPhone);
            email=itemView.findViewById(R.id.teacherEmail);
            post=itemView.findViewById(R.id.teacherPost);
            update=itemView.findViewById(R.id.teacherUpdateBtn);
            imageView=itemView.findViewById(R.id.teacherImage);
        }
    }
}
