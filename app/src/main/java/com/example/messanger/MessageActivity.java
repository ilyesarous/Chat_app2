package com.example.messanger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editTextinput;
    private TextView txtchattingwith;
    private ProgressBar progressBar;
    private ImageView imgtoolbar,imgsend;
    private TextView textViewmsg;

    private MessageAdapter messageAdapter;
    private ArrayList<Message> messages;
//    private String [] messagess ={"Racem","Bouaicha","Ktari","Ayman",};



    String usernameoftheromoot,chattingwithuser,chatroomid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        usernameoftheromoot=getIntent().getStringExtra("username de roomte");
        chattingwithuser=getIntent().getStringExtra("email of remote");


        recyclerView=findViewById(R.id.recyclermessage);
        editTextinput=findViewById(R.id.edttext);
        txtchattingwith=findViewById(R.id.txtchattingwith);
        progressBar=findViewById(R.id.progressmessage);
        imgtoolbar=findViewById(R.id.img_tolbab);
        imgsend=findViewById(R.id.imagesend);
        textViewmsg=findViewById(R.id.mytex);
        if (textViewmsg.getText().toString().equals(""))
        {
            progressBar.setVisibility(View.VISIBLE);
        }else
            progressBar.setVisibility(View.GONE);

        txtchattingwith.setText(usernameoftheromoot);
        messages = new ArrayList<>();

        imgsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FirebaseDatabase.getInstance().getReference("message"+chatroomid).push().setValue(new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail(),chattingwithuser,editTextinput.getText().toString()));
                String text=editTextinput.getText().toString();
                textViewmsg.setText(text);
                editTextinput.setText("");
            }
        });
        messageAdapter=new MessageAdapter(messages,getIntent().getStringExtra("my_img"),getIntent().getStringExtra("profil_of_remote"),MessageActivity.this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);
        Glide.with(MessageActivity.this).load(getIntent().getStringExtra("profil_of_remote")).placeholder(R.drawable.account_img).error(R.drawable.account_img).into(imgtoolbar);

        setupchatRoom();

    }

    private void setupchatRoom()
    {
        FirebaseDatabase.getInstance().getReference("user"+ FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String myusername = snapshot.getValue(User.class).getUsername();
                if (usernameoftheromoot.compareTo(myusername)>0)
                    chatroomid = myusername + usernameoftheromoot;
                else if (usernameoftheromoot.compareTo(myusername)==0)
                {
                    chatroomid = myusername + usernameoftheromoot;
                }
                else
                {
                    chatroomid = usernameoftheromoot+ myusername  ;
                }
                attachemsglistnner(chatroomid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void attachemsglistnner(String chatroomid)
    {
        FirebaseDatabase.getInstance().getReference("message"+chatroomid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    messages.add(dataSnapshot.getValue(Message.class));
                }
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size()-1);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}