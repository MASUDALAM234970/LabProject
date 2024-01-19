package com.example.labproject.Admin.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Update_teacher_Activity extends AppCompatActivity {

    private ImageView updateTeacher_ImageView;
    private EditText updateName, updatePhone, updateEmail, updatePost;

    private Button updateTeacherBtn, DeleteTeacherBtn;

    private    String name, phone, email, post, image ;

    private final int REQ = 1;

    private Bitmap bitmap=null;


    private ProgressDialog pd;
    private DatabaseReference reference;

    private StorageReference storageReference;
    private DatabaseReference dbRef;
    private String category, uniqueKey, downloadUrl ;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);
        Toolbar mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("College_Management");

        reference = FirebaseDatabase.getInstance().getReference().child("teachers");
        storageReference = FirebaseStorage.getInstance().getReference();


        name=getIntent().getStringExtra("name");
        phone=getIntent().getStringExtra("phone");
        email=getIntent().getStringExtra("email");
        post=getIntent().getStringExtra("post");
        image=getIntent().getStringExtra("image");

        uniqueKey= getIntent().getStringExtra("key");
        category=getIntent().getStringExtra("category");



        updateTeacher_ImageView=findViewById(R.id.updateTeacherImage14);
        updateTeacherBtn=findViewById(R.id.updateTeacher_btn14);
        DeleteTeacherBtn=findViewById(R.id.deleteTeacher_btn14);

        updateName=findViewById(R.id.updateTeacher_name14);
        updatePhone  =findViewById(R.id.updateTeacher_phone14);
        updateEmail=findViewById(R.id.updateTeacher_email14);
        updatePost=findViewById(R.id.updateTeacher_post14);

        try {
            Picasso.get().load(image).into(updateTeacher_ImageView);
        } catch (Exception e) {

            e.printStackTrace();
        }

        updateName.setText(name);
        updatePhone.setText(phone);
        updateEmail.setText(email);
        updatePost.setText(post);

        updateTeacher_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        updateTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                name = updateName.getText().toString();
                phone= updatePhone.getText().toString();
                email = updateEmail.getText().toString();
                post= updatePost.getText().toString();

                checkValidation();

                pd.setTitle("Update Teacher");
                pd.setMessage("Dear Admin, please wait while we are Updating the new Teacher Data.");
                pd.setCanceledOnTouchOutside(false);
                pd.show();
                Toast.makeText(getApplicationContext(),"Successfully",Toast.LENGTH_SHORT).show();
            }
        });
        DeleteTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Update_teacher_Activity.this);
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to delete this teacher data?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User confirmed, proceed with deletion
                        DeleteTeacherData();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User canceled deletion
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    private void DeleteTeacherData() {
        // Your existing code for deleting teacher data goes here
        reference.child(category).child(uniqueKey).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            startActivity(new Intent(getApplicationContext(),UpdateFaculty.class));
                        }
                        // Show AlertDialog on successful deletion
                        //showSuccessAlertDialog();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Show AlertDialog with error message on failure
                        //showFailureAlertDialog(e.getMessage());
                    }
                });
    }


    private void checkValidation() {


        // Define a regular expression pattern for a valid email address
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (name.isEmpty()) {
            updateName.setError("Empty");
            updateName.requestFocus();
        } else if (!phone.isEmpty() && phone.length() != 11) {
            updatePhone.setError("Enter valid P_Number");
            updatePhone.requestFocus();
        } else if (email.isEmpty()) {
            updateEmail.setError("Empty");
            updateEmail.requestFocus();
        } else if (!email.matches(emailPattern)) {
            updateEmail.setError("Invalid Email");
            updateEmail.requestFocus();
        } else if (post.isEmpty()) {
            updatePost.setError("Empty");
            updatePost.requestFocus();
        }
        else if (bitmap == null) {
            UpdateData(image);
            Toast.makeText(getApplicationContext(), "Only InsertData", Toast.LENGTH_SHORT).show();


            pd.show();
        } else {
            uploadImage();


            pd.show();
        }
    }

    private void UpdateData(String s)
    {
        HashMap hashMap= new HashMap();
        hashMap.put("name",name);
        hashMap.put("phone",phone);
        hashMap.put("email",email);
        hashMap.put("post",post);
        hashMap.put("image",image);


        reference.child(category).child(uniqueKey).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o)
            {
                Toast.makeText(getApplicationContext(),"Teacher Update data Successfully !"

                        ,Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(getApplicationContext(),UpdateFaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"SomeThing is wrong"
                        +e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void uploadImage() {
        pd.show();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalImage = baos.toByteArray();

        final StorageReference filepath = storageReference.child("Teachers").child(UUID.randomUUID() + ".jpg");

        final UploadTask uploadTask = filepath.putBytes(finalImage);

        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl =String.valueOf(uri);
                                    UpdateData(downloadUrl);
                                    pd.show();

                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                updateTeacher_ImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}