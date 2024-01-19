package com.example.labproject.Admin.Ebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labproject.Admin.AdminActivity;
import com.example.labproject.Admin.Notice.NoticeData;
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
import java.util.HashMap;

public class EbookActivity extends AppCompatActivity {

    private CardView addpdfcardview;
    private EditText pdfTitleedittext;
    private TextView pdftextview;
    private Button pdfbutton;
    private String title;
    private ProgressDialog pd;
    private String pdfName;

    private final int REQ = 1;
    private Uri pdfdata;

    private DatabaseReference reference123;
    private StorageReference storageReference;
    private String downloadUrl = " ";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook);
        Toolbar mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("College_Management");

        addpdfcardview = findViewById(R.id.addPdfCard);
        pdftextview = findViewById(R.id.pdftextview);
        pdfTitleedittext = findViewById(R.id.pdf_titletextbox);
        pdfbutton = findViewById(R.id.addpdfbuttom);

        reference123 = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);

        addpdfcardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        pdfbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = pdfTitleedittext.getText().toString();
                if (title.isEmpty()) {
                    pdfTitleedittext.setError("Empty");
                    pdfTitleedittext.requestFocus();
                } else if (pdfdata == null) {
                    Toast.makeText(getApplicationContext(), "Please upload pdf", Toast.LENGTH_LONG).show();
                } else {
                    UploadPdf();
                }
            }
        });

    }

    private void UploadPdf() {
        pd.setTitle("Add New E-Book");
        pd.setMessage("Dear Admin, please wait while we are adding the new E-book.");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        StorageReference reference = storageReference.child("pdf/" + pdfName);
        reference.putFile(pdfdata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadUrl = uri.toString();
                        UploadData();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something is wrong: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UploadData() {
        String unique_key = reference123.child("pdf").push().getKey();
        HashMap<String, Object> data = new HashMap<>();
        data.put("pdfTitle", title);
        data.put("pdfUri", downloadUrl);

        reference123.child("pdf").child(unique_key).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "E-book added successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), AdminActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something is wrong: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, REQ);
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            pdfdata = data.getData();
            if (pdfdata != null) {
                Cursor cursor = null;
                try {
                    cursor = getContentResolver().query(pdfdata, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        pdftextview.setText(pdfName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        }
    }

   }