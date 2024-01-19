package com.example.labproject.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.labproject.MainActivity;
import com.example.labproject.R;
import com.example.labproject.databinding.ActivityRegistarBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegistarActivity extends AppCompatActivity {


    ActivityRegistarBinding binding;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding=ActivityRegistarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadingBar=new ProgressDialog(this);
        PasswordHidden();
        binding.RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CreateAccount();
            }
        });

      binding.RegLogin.setOnClickListener(v ->
      {
          Intent regIntent = new Intent(getApplicationContext(), LoginActivity.class);
          startActivity(regIntent);
          regIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
          finish();

      });

    }

    private void CreateAccount() {

        String name = binding.RegName.getText().toString();
        String phone = binding.RegPhone.getText().toString();
        String password =binding.RegPassword.getText().toString();
        String id =binding.RegID.getText().toString();

        if (TextUtils.isEmpty(name))
        {

            binding.RegName.setError("Please Insert name");
            binding.RegName.requestFocus();

        }
        else if (TextUtils.isEmpty(id))
        {
            binding.RegID.setError("Please Insert ID");
            binding.RegID.requestFocus();
        }

        else if (TextUtils.isEmpty(phone)&& phone.length()==11)       //  (password.length()<6)
        {
            binding.RegPhone.setError("Please Insert phoneNumber");
            binding.RegPhone.requestFocus();
        }
        else if (TextUtils.isEmpty(password))
        {
            binding.RegPassword.setError("Please Insert password");
            binding.RegPassword.requestFocus();
        }


        else
        {
            if (phone.length()==11 && password.length()>=6)
            {
                loadingBar.setTitle("Create Account");
                loadingBar.setMessage("Please wait, while we are checking the credentials.");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                ValidatephoneNumber(name, phone, password,id);

            }
            else

            {
                if (phone.length()<11 && phone.length()<=12)
                {
                    Toast.makeText(this, "Please write your valid phone number...", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(this, "Password Should be six Digit euqal or gretter than!!...", Toast.LENGTH_SHORT).show();
                }

            }


        }
    }

    private void ValidatephoneNumber(String name, String phone, String password, String id)
    {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                if (!(snapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);
                    userdataMap.put("id", id);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegistarActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegistarActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegistarActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(RegistarActivity.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegistarActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegistarActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                   Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void PasswordHidden()
    {
        // Set initial input type to textPassword
        binding.RegPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

// Toggle password visibility
        binding.RegPassword.setOnTouchListener(new View.OnTouchListener() {
            final int DRAWABLE_RIGHT = 2;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (  binding.RegPassword.getRight() -   binding.RegPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // Toggle password visibility
                        if (  binding.RegPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                            binding.RegPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        } else {
                            binding.RegPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        }

                        // Move cursor to the end
                        binding.RegPassword.setSelection(  binding.RegPassword.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });
    }
}