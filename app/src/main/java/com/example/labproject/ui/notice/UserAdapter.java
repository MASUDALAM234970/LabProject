package com.example.labproject.ui.notice;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labproject.Admin.Notice.NoticeData;
import com.example.labproject.FullViewActivity;
import com.example.labproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserNoticeView>
{
    private Context context;
    private ArrayList<NoticeData> list;

    public UserAdapter(Context context, ArrayList<NoticeData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserAdapter.UserNoticeView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.noticeview,parent,false);

        return new UserNoticeView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserNoticeView holder, int position)
    {
        final   NoticeData currentItem=list.get(position);

        holder.deleteTittle.setText(currentItem.getTitle());
        holder.date.setText(currentItem.getData());
        holder.time.setText(currentItem.getTime());

        try {
            if (currentItem.getImage()!=null)
                Picasso.get().load(currentItem.getImage()).into(holder.deleteNoticeImage);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

               holder.deleteNoticeImage.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent intent1=new Intent(context, FullViewActivity.class);
                       intent1.putExtra("image",currentItem.getImage());
                      //  intent1=new Intent("image", Uri.parse(list.get(position).getImage()));
                       context.startActivity(intent1);
                   }
               });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class UserNoticeView extends RecyclerView.ViewHolder {
        private TextView deleteTittle,date, time;

        private ImageView deleteNoticeImage;
        public UserNoticeView(@NonNull View itemView) {
            super(itemView);
            deleteTittle=itemView.findViewById(R.id.deleteNoticeTittle15901);
            deleteNoticeImage=itemView.findViewById(R.id.deleteNoticeImage15901);
            date=itemView.findViewById(R.id.date01);
            time=itemView.findViewById(R.id.time01);
        }
    }
}
