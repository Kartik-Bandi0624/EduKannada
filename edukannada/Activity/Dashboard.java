package com.example.edukannada.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.edukannada.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class Dashboard extends AppCompatActivity {

    StorageReference storageReference;
    private ImageView add_profile;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        TextView dashboard = findViewById(R.id.dashboard);
        TextView computer_video = findViewById(R.id.computer_video);
        TextView computer_pdf = findViewById(R.id.computer_pdf);
        TextView gk_mcq = findViewById(R.id.gk_mcq);
        TextView pdf_view_only = findViewById(R.id.pdf_view_only);
        TextView news_update = findViewById(R.id.news_update);
        TextView news_update1 = findViewById(R.id.news_update1);
        add_profile = (ImageView) findViewById(R.id.add_profile);
        storageReference = FirebaseStorage.getInstance().getReference();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        add_profile.setOnClickListener(v -> {
            choosePicture();
        });

        dashboard.setOnClickListener(v -> {
            startActivity(new Intent(this, WelcomeActivity.class));
        });

        computer_video.setOnClickListener(v -> {
            startActivity(new Intent(this, Payment.class));
        });
        computer_pdf.setOnClickListener(v -> {
            startActivity(new Intent(this, Payment.class));
        });

        gk_mcq.setOnClickListener(v -> {
            startActivity(new Intent(this, Payment.class));
        });

        pdf_view_only.setOnClickListener(v -> {
            startActivity(new Intent(this, Payment.class));
        });

        news_update.setOnClickListener(v -> {
            startActivity(new Intent(this, Payment.class));
        });
        news_update1.setOnClickListener(v -> {
            startActivity(new Intent(this, Payment.class));
        });

    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            add_profile.setImageURI(imageUri);
            uploadPic();
        }
    }

    private void uploadPic() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading image..");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images" + randomKey);

        riversRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "Image uploaded", Snackbar.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Upload failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                pd.setMessage("Percentage : " + (int) progressPercent + "%");
            }
        });
    }
}