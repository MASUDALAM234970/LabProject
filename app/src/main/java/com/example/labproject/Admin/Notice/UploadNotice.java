package com.example.labproject.Admin.Notice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.labproject.Admin.AdminActivity;
import com.example.labproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class UploadNotice extends AppCompatActivity {

    private Toolbar mToolbar;

    private CardView addImage123;

    private ImageView noticeImageView;
    private EditText noticeTitle;
    private Button button;

    private ProgressDialog loadingBar;


    private final int REQ=1;
    private Bitmap bitmap;



    private DatabaseReference reference;
    private StorageReference storageReference;
    private DatabaseReference dbRef;
    private String downloadUrl=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notice);


        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("College_Management");

        addImage123=findViewById(R.id.addImage);

        noticeImageView=findViewById(R.id.noticeImageView);
        noticeTitle=findViewById(R.id.notice_title);
        button=findViewById(R.id.uploadNoticeBtn);

        reference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        loadingBar = new ProgressDialog(this);




        addImage123.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent pickImage=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickImage,REQ);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noticeTitle.getText().toString().isEmpty()){

                    noticeTitle.setError("Empty");
                    noticeTitle.requestFocus();
                }

                else if (bitmap==null){

                    uploadData();
                }
                else {

                    uploadImage();
                }
            }
        });


    }

    private void uploadImage()
    {

        loadingBar.setTitle("Add New Notice");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new Notice.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte [] finalimge= baos.toByteArray();

        final StorageReference filepath;
        filepath=storageReference.child("Notice").child(finalimge+"jpg");

        final UploadTask uploadTask= filepath.putBytes(finalimge);

        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
            {

                if (task.isSuccessful())
                {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                            {
                                @Override
                                public void onSuccess(Uri uri)
                                {
                                    downloadUrl= String.valueOf(uri);
                                    uploadData();
                                    loadingBar.dismiss();
                                }
                            });
                        }
                    });
                } else
                {
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(),"Something is wrong",Toast.LENGTH_LONG).show();
                }

            }
        });

    }   // uploadImage finished

    private void uploadData()
    {
        dbRef =reference.child("Notice");
        final  String unique_key= dbRef.push().getKey();
        String title =noticeTitle.getText().toString();

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd--MM--yy");
        String date= simpleDateFormat.format(calendar.getTime());


        Calendar calForTime=Calendar.getInstance();
        SimpleDateFormat simpleDateFormattime=new SimpleDateFormat("hh:mm");

        String time= simpleDateFormattime.format(calForTime.getTime());

        NoticeData  noticeData= new NoticeData(title, downloadUrl, date,time,unique_key);

        dbRef.child(unique_key).setValue(noticeData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused)
            {
                loadingBar.dismiss();
                Toast.makeText(getApplicationContext(),"Notice Uploaded",Toast.LENGTH_SHORT).show();


                Intent intent =new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Something Wrong"+e.getMessage(),Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });



    }   //uploadData finished




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQ&& resultCode ==RESULT_OK)
        {
            Uri uri= data.getData();
            try {
                bitmap =MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            noticeImageView.setImageBitmap(bitmap);
        }
    }


}