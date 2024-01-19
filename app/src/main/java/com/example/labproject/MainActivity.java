package com.example.labproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.labproject.Prevalent.Prevalent;
import com.example.labproject.auth.LoginActivity;
import com.example.labproject.auth.RegistarActivity;
import com.example.labproject.databinding.ActivityMainBinding;
import com.example.labproject.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private final String parentDbName = "Users";
    ActivityMainBinding binding;
    private ProgressDialog loadingBar;
    final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child(parentDbName);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Paper.init(this);

        loadingBar=new ProgressDialog(this);

         checkSavedCredentials();
               binding.nothing.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                       finish();
                   }
               });
       
    }



    private void checkSavedCredentials() {

        final   String UserPhoneKey= Paper.book().read(Prevalent.UserPhoneKey);
        final String UsePasswordKey= Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneKey !="" && UsePasswordKey !="")
        {
            if (!TextUtils.isEmpty(UsePasswordKey) && !TextUtils.isEmpty(UsePasswordKey))
            {
                AllowAccess(UserPhoneKey, UsePasswordKey);

                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }

    private void AllowAccess( final String phone, final String password) {


        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDbName).child(phone).exists())
                {
                    Users users =snapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if (users.getPhone().equals(phone))
                    {
                        if (users.getPassword().equals(password))
                        {
                            Toast.makeText(getApplicationContext(), "Smile ! Welcome back....", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            Prevalent.currentOnlineUser= users;
                            startActivity(intent);
                        } else
                        {
                            Toast.makeText(getApplicationContext(), "Sorry!!,InCorrect password....", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();


                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}