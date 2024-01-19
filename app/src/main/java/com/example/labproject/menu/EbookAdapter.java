package com.example.labproject.menu;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.labproject.R;

import java.io.File;
import java.util.List;

public class EbookAdapter extends RecyclerView.Adapter<EbookAdapter.MyEbookViewHolder>
{

    private Context context;

    private List<EbookData> list;
    public EbookAdapter(Context context, List<EbookData> list) {
        this.context = context;
        this.list = list;
    }



    @NonNull
    @Override
    public MyEbookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
        return new MyEbookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyEbookViewHolder holder,  int position)
    {
                 final EbookData currentItem=list.get(position);

           holder.ebookName.setText(currentItem.getPdfTitle());


         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent=new Intent(context,PdfViewActivity.class);
                 intent.putExtra("pdfUrl",list.get(position).getPdfUri());
                 context.startActivity(intent);
                 Toast.makeText(context,"Pdf name..."+currentItem.getPdfTitle(),Toast.LENGTH_SHORT).show();
             }
         });


        holder.ebookDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri pdfUri = Uri.parse(list.get(position).getPdfUri());

                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(pdfUri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setTitle("Downloading PDF");

                // Choose a destination folder for the downloaded file
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "downloaded_file.pdf");
                request.setDestinationUri(Uri.fromFile(file));

                // Enqueue the download
                downloadManager.enqueue(request);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyEbookViewHolder extends RecyclerView.ViewHolder
    {
        private TextView ebookName;
        private ImageView ebookDownload;
        public MyEbookViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ebookName=itemView.findViewById(R.id.ebookName12358);
            ebookDownload=itemView.findViewById(R.id.ebookDownload123456);

        }
    }
}
