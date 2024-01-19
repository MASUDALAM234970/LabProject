package com.example.labproject;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labproject.Prevalent.Prevalent;
import com.example.labproject.auth.LoginActivity;
import com.example.labproject.menu.PdfShowActivity;
import com.example.labproject.model.Users;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.A;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private CircleImageView NavProfileImage;
    private TextView NavProfileUserName;
    private final String parentDbName = "Users";
    String phone;

   private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int checkItem;
    private String selected;
    private final String CHEECKITEM="checked_id";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);



        Paper.init(this);

       sharedPreferences=this.getSharedPreferences("themes", Context.MODE_PRIVATE);
       editor=sharedPreferences.edit();


        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Toolbar mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("College_Management");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController= Navigation.findNavController(this,R.id.frame_layout);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        try {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        View navView=navigationView.inflateHeaderView(R.layout.navigation_header);
        NavProfileImage = (CircleImageView) navView.findViewById(R.id.profile_image_header123);
        NavProfileUserName = (TextView) navView.findViewById(R.id.user_name_header123);
        NavProfileUserName.setText(Prevalent.currentOnlineUser.getName());
        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(NavProfileImage);
        navigationView.setNavigationItemSelectedListener(this);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
     

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.navigation_Video)
        {
            Toast.makeText(getApplicationContext(),"video",Toast.LENGTH_SHORT).show();

        } else if (item.getItemId()==R.id.navigation_cse_studentInfo)
        {
            Toast.makeText(getApplicationContext(),"cse_studentInfo",Toast.LENGTH_SHORT).show();

            startActivity(new Intent(getApplicationContext(), UpdateProfileActivity2.class));

        } else if (item.getItemId()==R.id.navigation_ebook)
        {
            startActivity(new Intent(getApplicationContext(), PdfShowActivity.class));
           // Toast.makeText(getApplicationContext(),"Ebook",Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId()==R.id.navigation_theme)
        {
            Toast.makeText(getApplicationContext(),"Theme",Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId()==R.id.navigation_website)
        {
            Toast.makeText(getApplicationContext(),"website",Toast.LENGTH_SHORT).show();
            showOptionsDialog();
        }
        else if (item.getItemId()==R.id.navigation_share)
        {
            Toast.makeText(getApplicationContext(),"share",Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId()==R.id.navigation_about)
        {
            Toast.makeText(getApplicationContext(),"about",Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId()==R.id.navigation_developer)
        {
            ShowDialog();
        }
        else if (item.getItemId()==R.id.navigation_rate_us)
        {
            Paper.book().destroy();

             startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        return true;
    }



    private void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select A WEBSITE")
                .setItems(new CharSequence[]{"DUCMC ", "STEC " ,"YOUTUBE "," FACEBOOK "}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the selected option
                        handleSelectedOption(which);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    private void handleSelectedOption(int option) {
        switch (option) {
            case 0:

                Toast.makeText(getApplicationContext(), "DUCMC ", Toast.LENGTH_SHORT).show();
                String websiteUrlDUCMC = "https://ducmc.com/index.php";
                Intent intent1=new Intent(getApplicationContext(), WebsiteActivity.class);
                intent1.putExtra("ducmc",websiteUrlDUCMC);
                startActivity(intent1);
                break;
            case 1:
                String websiteUrlSTEC  = "https://stec.ac.bd/";
                Intent intent=new Intent(getApplicationContext(), WebsiteActivity.class);
                intent.putExtra("ducmc1",websiteUrlSTEC);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),  "STEC ", Toast.LENGTH_SHORT).show();
                break;

            case 2:

                String websiteUrlYOUTUBE  =  "https://www.youtube.com/";

                Intent intent2=new Intent(getApplicationContext(), WebsiteActivity.class);
                intent2.putExtra("alam",websiteUrlYOUTUBE);
                startActivity(intent2);

                Toast.makeText(getApplicationContext(),  "YOUTUBE ", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(), WebsiteActivity.class));
                break;
            case 3:
                String websiteUrlFACEBOOK = "https://www.facebook.com/";


                Intent intent3=new Intent(getApplicationContext(), WebsiteActivity.class);
                intent3.putExtra("ducmc3",websiteUrlFACEBOOK);
                startActivity(intent3);
                Toast.makeText(getApplicationContext(), " FACEBOOK ", Toast.LENGTH_SHORT).show();
                break;

        }
    }
    private void ShowDialog()
    {
                 String [] themes=this.getResources().getStringArray(R.array.theme);
        MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(this);
        builder.setTitle("Select Theme");
        builder.setSingleChoiceItems(R.array.theme, getCheckedItem(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                selected=themes[which];
                checkItem=which;

            }
        });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (selected==null)
                {
                    selected=themes[which];
                    checkItem=which;
                }
                switch (selected)
                {
                    case "System Default":
                        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM);
                       // AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM;
                        break;

                    case "Dark":
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        break;

                    case "Light":

                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        break;


                }
                setCheckedItem(which);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                    dialog.dismiss();
            }
        });

        AlertDialog dialog=builder.create();

        dialog.show();
    }

     private int getCheckedItem()
    {
        return sharedPreferences.getInt(CHEECKITEM,0);
    }
    private void setCheckedItem (int i)
    {
        editor.putInt(CHEECKITEM, i);
        editor.apply();
    }


}