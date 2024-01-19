package com.example.labproject.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.labproject.FullViewActivity;
import com.example.labproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class galleryAdapter extends RecyclerView.Adapter<galleryAdapter.MYGalleryViewAdapter>
{
    private Context context;
    private List<String>images;
    public galleryAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }




    @NonNull
    @Override
    public MYGalleryViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.gallery_image_item,parent,false);

        return new MYGalleryViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MYGalleryViewAdapter holder, int  position)
    {

        Picasso.get().load(images.get(position)).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(context, FullViewActivity.class);
                intent1.putExtra("image",images.get(position));

                context.startActivity(intent1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class MYGalleryViewAdapter extends RecyclerView.ViewHolder {

        private ImageView imageView;
        public MYGalleryViewAdapter(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image15978);
        }
    }
}
