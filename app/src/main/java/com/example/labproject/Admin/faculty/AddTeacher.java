package com.example.labproject.Admin.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class AddTeacher extends AppCompatActivity {


    private Spinner addTCategory;
    private ImageView addTeacherImage;
    private EditText addTName, addTPhone_Number, addTEmail, addTPost;
    private Button addTButton;

    private String category;
    private final int REQ = 1;

    private Bitmap bitmap=null;

    private ProgressDialog pd;
    private DatabaseReference reference;

    private StorageReference storageReference;
    private DatabaseReference dbRef;

    String name, phone, email, post, downloadUrl = "";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        Toolbar mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("College_Management");

        reference = FirebaseDatabase.getInstance().getReference().child("teachers");
        storageReference = FirebaseStorage.getInstance().getReference();

        addTCategory = findViewById(R.id.addTeacher_category);
        addTeacherImage = findViewById(R.id.addTeacher_image);
        addTName = findViewById(R.id.addTeacher_name);
        addTPhone_Number = findViewById(R.id.addTeacher_phone);
        addTEmail = findViewById(R.id.addTeacher_email);
        addTPost = findViewById(R.id.addTeacher_post);
        addTButton = findViewById(R.id.addTeacher_btn);
        pd = new ProgressDialog(this);

        addTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        addTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        String[] items = new String[]{"Selected Category", "CSE","ME","Physics","Chemistry"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        addTCategory.setAdapter(adapter);

        addTCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = addTCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void checkValidation() {
        name = addTName.getText().toString();
        phone= addTPhone_Number.getText().toString();
        email = addTEmail.getText().toString();
        post= addTPost.getText().toString();

        // Define a regular expression pattern for a valid email address
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (name.isEmpty()) {
            addTName.setError("Empty");
            addTName.requestFocus();
        } else if (!phone.isEmpty() && phone.length() != 11) {
            addTPhone_Number.setError("Enter valid P_Number");
            addTPhone_Number.requestFocus();
        } else if (email.isEmpty()) {
            addTEmail.setError("Empty");
            addTEmail.requestFocus();
        } else if (!email.matches(emailPattern)) {
            addTEmail.setError("Invalid Email");
            addTEmail.requestFocus();
        } else if (post.isEmpty()) {
            addTPost.setError("Empty");
            addTPost.requestFocus();
        } else if (category.equals("Selected Category")) {
            Toast.makeText(getApplicationContext(), "Please provide teacher category", Toast.LENGTH_SHORT).show();
        } else if (bitmap == null) {
            insertData();
            Toast.makeText(getApplicationContext(), "Only InsertData", Toast.LENGTH_SHORT).show();

            pd.setTitle("Add New Teacher");
            pd.setMessage("Dear Admin, please wait while we are adding the new Teacher.");
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        } else {
            uploadImage();

            pd.setTitle("Add New Teacher");
            pd.setMessage("Dear Admin, please wait while we are adding the new Teacher.");
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }
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
                                    downloadUrl = uri.toString();
                                    insertData();
                                    pd.dismiss();
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

    private void insertData() {
        dbRef = reference.child(category);
        final String unique_key = dbRef.push().getKey();

        TeacherData teacherData = new TeacherData(name, phone, email, post, downloadUrl, unique_key);

        dbRef.child(unique_key).setValue(teacherData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Teacher Uploaded", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), UpdateFaculty.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Something Wrong: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
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
                addTeacherImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}