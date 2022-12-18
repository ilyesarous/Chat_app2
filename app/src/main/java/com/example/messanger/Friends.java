package com.example.messanger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Friends extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<User> users;
    private ProgressBar progressBar;
    private Usersadapter usersadapter;
    private Button btnSubmit;
    private Button btnSubmite;
    private SwipeRefreshLayout swipeRefreshLayout;
    Usersadapter.OnUserClickListener onUserClickListener;

    String myImageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        progressBar = findViewById(R.id.progressBar);
        users = new ArrayList<User>();
        recyclerView = findViewById(R.id.recycler);
        swipeRefreshLayout = findViewById(R.id.swipelayout);
        recyclerView = findViewById(R.id.recycler);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmite = findViewById(R.id.btnSubmite);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(Friends.this,MessageActivity.class));

            }
        });

        btnSubmite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(Friends.this,MessageActivity.class));

            }
        });

        // fil putExtra bech n5ali esem eli na7ki m3ah fil entete

        onUserClickListener = new Usersadapter.OnUserClickListener() {
            @Override
            public void onUserclicked(int position) {
                startActivity(new Intent(Friends.this,MessageActivity.class)
                                .putExtra("username de roomte",users.get(position).getUsername())
                                .putExtra("email of remote ",users.get(position).getEmail())
                                .putExtra("profil_of_remote ",users.get(position).getProfilePicture())
                                .putExtra("my_img ",myImageUrl)

                );
            }
        };

        getUsers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    //kifh mel page friends temchi lel page profile ki tenzel 3al icon
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_profile)
        {
            startActivity(new Intent(Friends.this,Profile.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUsers ()
    {
        users.clear();
        FirebaseDatabase.getInstance().getReference("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    users.add(dataSnapshot.getValue(User.class));
                }
                usersadapter = new Usersadapter(users,Friends.this,onUserClickListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(Friends.this));
                recyclerView.setAdapter(usersadapter);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                for (User user:users)
                {
                    if (user.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                    {
                         myImageUrl= user.getProfilePicture();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}