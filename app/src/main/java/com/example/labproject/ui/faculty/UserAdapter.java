package com.example.labproject.ui.faculty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labproject.Admin.faculty.TeacherData;
import com.example.labproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<TeacherData> list;
    private Context context;
    String Category;

    public UserAdapter(List<TeacherData> list, Context context, String category) {
        this.list = list;
        this.context = context;
        Category = category;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.userview,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position)
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



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView name, phone, email, post;

        private ImageView imageView;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.teacherName11);
            phone=itemView.findViewById(R.id.teacherPhone11);
            email=itemView.findViewById(R.id.teacherEmail11);
            post=itemView.findViewById(R.id.teacherPost11);

            imageView=itemView.findViewById(R.id.teacherImage11);
        }
    }
}
