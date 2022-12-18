package com.example.messanger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText edtUsername,edtEmail,edtPassword;
    private Button btnSubmit;
    private TextView textLogininfo;
    private boolean isSigningUp=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        edtEmail=findViewById(R.id.edtEmail);
        edtUsername=findViewById(R.id.edtUsername);
        edtPassword=findViewById(R.id.edtPassword);
        btnSubmit=findViewById(R.id.btnSubmit);
        textLogininfo=findViewById(R.id.textLogininfo);
        //firebase
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            startActivity(new Intent(MainActivity.this,Friends.class));
            finish();

        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (edtEmail.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty())
                {
                    if (isSigningUp && edtUsername.getText().toString().isEmpty())
                    {
                        Toast.makeText(MainActivity.this,"invalid input",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (isSigningUp)
                    handleSignUp();
                else
                    handleLogin();
            }
        });

// bech nbadel el message ta7t el button fil page btaa login
        textLogininfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSigningUp) {
                    isSigningUp = false;
                    edtUsername.setVisibility(View.GONE);
                    btnSubmit.setText("login");
                    textLogininfo.setText("don't have account ? sign up ");
                } else {
                    isSigningUp=true;
                    edtUsername.setVisibility(View.VISIBLE);
                    btnSubmit.setText("login");
                    textLogininfo.setText("already  have account? login ");
                }
            }
        });
    }
    private void handleSignUp(){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference("user/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new User(edtUsername.getText().toString(),edtEmail.getText().toString(),""));
                    startActivity(new Intent(MainActivity.this,Friends.class));
                    Toast.makeText(MainActivity.this,"Signed up is Successfully",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void handleLogin()
    {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(MainActivity.this, Friends.class));
                    Toast.makeText(MainActivity.this, "Loged is successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}