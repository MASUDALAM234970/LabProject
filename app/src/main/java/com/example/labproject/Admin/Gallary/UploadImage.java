package com.example.labproject.Admin.Gallary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class UploadImage extends AppCompatActivity {


    private Spinner imageCategory;
    private CardView selectedImage;

    private Button UploadImage;

    private ImageView galleryImageView;

    private  String category;
    private  final int REQ = 1;

    private Bitmap bitmap;

    private ProgressDialog pd;
    private DatabaseReference reference;

    private StorageReference storageReference;
    // private FirebaseAuth mAuth;

    String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Toolbar mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("College_Management");

        selectedImage=findViewById(R.id.addGallery_Image);

        imageCategory=findViewById(R.id.image_category);
        UploadImage =findViewById(R.id.uploadImageBtn123);
        galleryImageView=findViewById(R.id.galleryImageView);
        // mAuth = FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference().child("gallery");
        storageReference= FirebaseStorage.getInstance().getReference().child("gallery");
        pd = new ProgressDialog(this);
        String [] items= new String[]{"Selected Category","Convocation","Independence Dey ","Others ever"};

        imageCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items));

        imageCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category= imageCategory.getSelectedItem().toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        selectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGallery();
            }
        });

        UploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bitmap==null)

                {
                    Toast.makeText(getApplicationContext(),"Please upload_Image",Toast.LENGTH_SHORT).show();



                } else if (category.equals("selected category")) {

                    Toast.makeText(getApplicationContext(),"please selected image category",Toast.LENGTH_SHORT).show();


                }
                else {

                    pd.setTitle("Add New Image");
                    pd.setMessage("Dear Admin, please wait while we are adding the new Image.");
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();

                    uploadImage();


                }

            }
        });

    }


    private void uploadImage()
    {



        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte [] finalimge= baos.toByteArray();

        final StorageReference filepath;
        filepath=storageReference.child(finalimge+"jpg");

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
                                    pd.dismiss();
                                }
                            });
                        }
                    });
                } else
                {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(),"Something is wrong",Toast.LENGTH_LONG).show();
                }

            }
        });

    }   // uploadImage finished

    private void uploadData() {


        reference=reference.child(category);
        final String uniquekey =reference.push().getKey();


        reference.child(uniquekey).setValue(downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(),"Image Update is Successfully",Toast.LENGTH_SHORT).show();

                Intent intent =new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });

    }

    //uploadData finished


    private void openGallery()
    {
        Intent pickImage=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,REQ);
    }
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
            galleryImageView.setImageBitmap(bitmap);
        }

        galleryImageView.setImageBitmap(bitmap);
    }

}