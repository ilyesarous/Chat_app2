package com.example.messanger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class Profile extends AppCompatActivity {

    private Button btnLogout,btnUpload;
    private ImageView ImgProfile;
    private Uri ImagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnLogout = findViewById(R.id.btnLogOut);
        ImgProfile = findViewById(R.id.Profile_img);
        btnUpload = findViewById(R.id.btnUploadImage);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImage();
            }


        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Profile.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        ImgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent,1);
            }
        });
    }

    @Override
    //fonction bech tara ken famech teswira fil profil w ente nzelt 3ala teswira tod5ol bech tajem tjib teswira
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode== RESULT_OK && data!=null)
        {
            ImagePath = data.getData();
            getImageInImageView();
        }
    }
    //fonction bech njib teswira mel galerie btaa el profil
    private void getImageInImageView() {

        Bitmap bitmap= null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),ImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImgProfile.setImageBitmap(bitmap);
    }
    //fonction bech ya3mel upload lel teswira bech tetsajal fil base de donnée storage (base de donnée just lel teswira khw)
    private void UploadImage() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("uploading...");

        progressDialog.show();

        FirebaseStorage.getInstance().getReference("Image/"+ UUID.randomUUID().toString()).putFile(ImagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                {
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful())
                            {
                                UpdateProfilePicture(task.getResult().toString());
                            }
                        }
                    });
                    Toast.makeText(Profile.this,"| Image Uploaded",Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(Profile.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
            //taw bech na3mel el pourcentage eli todhhor fil upload btaa teswira
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                Double progress=100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                progressDialog.setMessage("Upload"+progress +"%");
            }
        });
    }
    //kifeh nda5al el teswira lel base de donnée m3a el email w el mdp
    private void UpdateProfilePicture(String url)
    {
        FirebaseDatabase.getInstance().getReference("User/"+FirebaseAuth.getInstance().getCurrentUser().getUid() + "profilepicture").setValue(url);
    }
}