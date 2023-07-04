package com.example.auth_tw;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ImageView fireStoregeImage;
    EditText nameImage;
    Button selectButton, uploadButton;
    TextView nx;
    private static final int SELECT_PICTURE = 1;
    Uri ImageUri;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nx = findViewById(R.id.textView6);
        nx.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,InfoActivity.class)));
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        fireStoregeImage = findViewById(R.id.firebaseimage);
        nameImage = findViewById(R.id.editText);
        selectButton = findViewById(R.id.selectedImage);
        uploadButton = findViewById(R.id.uploadImage);

        selectButton.setOnClickListener(v -> selectImage());
        uploadButton.setOnClickListener(v -> uploadImage());
    }

    private void uploadImage() {
        String name = nameImage.getText().toString().trim();
        if (name.length() == 0){
            Toast.makeText(MainActivity.this, "Укажите название для картинки", Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH);
        Date now = new Date();
        String filename = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference("images/"+ filename);
        storageReference.putFile(ImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fireStoregeImage.setImageURI(null);
                        Toast.makeText(MainActivity.this, "Картинка загружена" ,Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Ошибка загрузки" ,Toast.LENGTH_SHORT).show();
                    }
                });
        Map<String, Object> images = new HashMap<>();
        images.put("name", name);
        images.put("filenameImage", filename+".jpeg");
        db.collection("userScore")
                .add(images)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this, "Запись была добавлена", Toast.LENGTH_SHORT).show();
                        nameImage.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Ошибка записи!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getData() != null){
            ImageUri = data.getData();
            fireStoregeImage.setImageURI(ImageUri);
        }
    }
}