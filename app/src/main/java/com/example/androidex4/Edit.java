package com.example.androidex4;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class Edit extends AppCompatActivity {
    private ImageView image;
    private int GALLERY = 1;
    private int id;
    private EditText title;
    private EditText Editor;
    private Button btnSavePetek;
    private Button btnUploadImage;
    private FirebaseDatabase database;
    private Bitmap bitmap;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent intent = getIntent();
        //db = new DBHelper(this);
        database = FirebaseDatabase.getInstance();
        title = findViewById(R.id.title);
        Editor = findViewById(R.id.editor);
        btnSavePetek = findViewById(R.id.savePetek);
        btnUploadImage = findViewById(R.id.uploadImage);
        image = findViewById(R.id.im);
        storage = FirebaseStorage.getInstance();
        id = intent.getIntExtra("id", 0 );
        Log.i("id", String.valueOf(intent.getIntExtra("id", 0)));
        Log.i("title", intent.getStringExtra("title"));
        if(!intent.getStringExtra("title").isEmpty()){
            title.setText( intent.getStringExtra("title"));
        }
        Editor.setText(intent.getStringExtra("content"));
        storage.getReference(LoginActivity.userUID).child("peteks").child(String.valueOf(id)).getBytes(4096 * 4096).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.i("image", "success");
                image.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                // Data for "images/island.jpg" is returns, use this as needed
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("image","failed");
                // Handle any errors
            }
        });


        btnSavePetek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                String setTitle = title.getText().toString();
                String setContent = Editor.getText().toString();
                Log.i("setTitle", setTitle);
                Log.i("setContent", setContent);
                database.getReference(LoginActivity.userUID).child("peteks").child(String.valueOf(id)).child("title").setValue(setTitle);
                database.getReference(LoginActivity.userUID).child("peteks").child(String.valueOf(id)).child("content").setValue(setContent);

                if (bitmap != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = storage.getReference(LoginActivity.userUID).child("peteks").child(String.valueOf(id)).putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                        }
                    });
                }
                startActivity(intent);
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromGallary();
            }
        });
    }



    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);

    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    Toast.makeText(Edit.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();
                    image.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Edit.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
    }


    //protected void onDestroy() {
    //    super.onDestroy();
    //    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    //    startActivity(intent);
    //}
}
