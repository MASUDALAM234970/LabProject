package com.example.labproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labproject.Prevalent.Prevalent;
import com.example.labproject.auth.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {
    private TextView pageTile, tittleQuestion;

    private EditText phoneNumbersEt,question1,question2;
    private Button verifyButton;


    private String check="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        check=getIntent().getStringExtra("Check");

        pageTile=findViewById(R.id.reset_pageTittle10);
        tittleQuestion=findViewById(R.id.reset_pageTittleQuestion20);
        phoneNumbersEt=findViewById(R.id.reset_phone);
        question1=findViewById(R.id.reset_last);
        question2=findViewById(R.id.reset_favorite_person);
        verifyButton=findViewById(R.id.reset_security_question_btn);

        verifyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setAnswer();

            }
        });

    }


    @Override
    protected void onStart()
    {
        super.onStart();

        phoneNumbersEt.setVisibility(View.GONE);
        if (check.equals("settings"))
        {
            pageTile.setText("Set Question");
            tittleQuestion.setText(" Please ! set Answer for the Following Security Question ?");
            verifyButton.setText("Set");

            DisplayPreviousAnswer();

        }
        else if (check.equals("login"))
        {
            phoneNumbersEt.setVisibility(View.VISIBLE);

            verifyButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // Toast.makeText(getApplicationContext(), "Please enter the Question", Toast.LENGTH_SHORT).show();
                    verifyUser();
                }
            });

        }


    }



    private void verifyUser()
    {


        final   String phone=phoneNumbersEt.getText().toString();
        final   String answer1=question1.getText().toString().toLowerCase();
        final   String answer2=question2.getText().toString().toLowerCase();

        if (!phone.equals("") && !answer1.equals("") && !answer2.equals(""))

        {
            final DatabaseReference ref= FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(phone);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if (snapshot.exists())
                    {
                        String mphone=snapshot.child("phone").getValue().toString();


                        if (snapshot.hasChild("Security Question"))
                        {


                            String ans1=snapshot.child("Security Question").child("answer1").getValue().toString();
                            String ans2=snapshot.child("Security Question").child("answer2").getValue().toString();
                            if (!ans1.equals(answer1))
                            {
                                Toast.makeText(getApplicationContext(),"Sorry ! your 1st answer wrong",Toast.LENGTH_SHORT).show();
                            }

                            else if (!ans2.equals(answer2))
                            {
                                Toast.makeText(getApplicationContext(),"Sorry ! your 2nd answer wrong",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                AlertDialog.Builder builder=new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");

                                final  EditText  NewPassword= new EditText(ResetPasswordActivity.this);
                                NewPassword.setHint("write password here");
                                builder.setView(NewPassword);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        if (!NewPassword.getText().toString().equals(""))
                                        {
                                            ref.child("password")
                                                    .setValue(NewPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task)
                                                        {
                                                            if (task.isSuccessful())
                                                            {
                                                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                                Toast.makeText(getApplicationContext(),"Password Change Successfully!",Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    });

                                        }
                                    }
                                });


                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        dialog.cancel();
                                    }
                                });

                                builder.show();
                            }
                        }

                        else
                        {
                            Toast.makeText(getApplicationContext(),"You have not set the security Question",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"This phone number not exits",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext()," Please  Complete the form",Toast.LENGTH_SHORT).show();
        }


    }

    private void setAnswer()
    {
        String answer1=question1.getText().toString().toLowerCase();
        String answer2=question2.getText().toString().toLowerCase();

        if (TextUtils.isEmpty(answer1))
        {
            question1.setError("Fields is Empty!");
            question1.requestFocus();
            Toast.makeText(getApplicationContext(), "Please enter the Question", Toast.LENGTH_SHORT).show();
        }
        else   if (TextUtils.isEmpty(answer2))
        {
            question2.setError("Fields is Empty!");
            question2.requestFocus();
            Toast.makeText(getApplicationContext(), "Please enter the Question", Toast.LENGTH_SHORT).show();
        }

        else
        {
            DatabaseReference ref= FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("answer1", answer1);
            userdataMap.put("answer2", answer2);

            ref.child("Security Question").updateChildren(userdataMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                Toast.makeText(getApplicationContext(),"You have set Security Question Successfully",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void  DisplayPreviousAnswer()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());

        ref.child("Security Question").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    String ans1=snapshot.child("answer1").getValue().toString();
                    String ans2=snapshot.child("answer2").getValue().toString();
                    question1.setText(ans1);
                    question2.setText(ans2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}