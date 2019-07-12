package com.example.androidex4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public ArrayList<Petek> peteks;
    private int id;
    //public static DBHelper db;
    private Button btnDonate;
    private Button btnEdit;
    private Button btnNew;
    private ImageView imageView;
    private ListView listView;
    private int itemPos;
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        btnDonate = findViewById(R.id.btn_donate);
        btnEdit = findViewById(R.id.btn_note);
        btnNew = findViewById(R.id.btn_new);
        imageView = findViewById(R.id.pImage);
        listView = findViewById(R.id.listView);
        peteks = new ArrayList<>();
        //db = new DBHelper(this);

        database.getReference(LoginActivity.userUID).child("peteks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<Petek>> g =
                        new GenericTypeIndicator<ArrayList<Petek>>(){};
                if(peteks != null) peteks.clear();
                peteks = dataSnapshot.getValue(g);
                if(peteks != null) {
                    id = peteks.size();
                    Log.i("num of peteks", String.valueOf(id));

                    listView.setAdapter(new ArrayAdapter<Petek>(
                            MainActivity.this,
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1,
                            peteks
                    ));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_donate = new Intent(getApplicationContext(),DonateActivity.class);
                startActivity(intent_donate);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("pos", String.valueOf(position));
                itemPos = position;

                Toast toast = Toast.makeText(getApplicationContext(), "You chose petek " + String.valueOf(position), Toast.LENGTH_SHORT);
                toast.show();

                storage.getReference(LoginActivity.userUID).child("peteks").child(String.valueOf(peteks.get(itemPos).id)).getBytes(4096 * 4096).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Log.i("image", "success");
                        imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                        // Data for "images/island.jpg" is returns, use this as needed
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.i("image","failed");
                        imageView.setImageResource(0);
                        Toast toast = Toast.makeText(getApplicationContext(), "no image for this petek", Toast.LENGTH_SHORT);
                        toast.show();
                        // Handle any errors
                    }
                });
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int time_diff = peteks.get(itemPos).date.getDay() - Calendar.getInstance().getTime().getDay();
                Log.i("diff", String.valueOf(time_diff));
                if(time_diff < 2){
                    Intent intent = new Intent(getApplicationContext(), Edit.class);
                    intent.putExtra("id", peteks.get(itemPos).id);
                    intent.putExtra("title", peteks.get(itemPos).title);
                    intent.putExtra("content", peteks.get(itemPos).content);
                    startActivity(intent);
                }
                else{
                    database.getReference(LoginActivity.userUID).child("peteks").child(String.valueOf(peteks.get(itemPos).id)).child("status").setValue("recieved");
                    Toast toast = Toast.makeText(getApplicationContext(), "two days passed, can't edit", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("petek id", String.valueOf(id));
                Petek p = new Petek(id,
                        "title",
                        "content");

                database.getReference(LoginActivity.userUID).child("peteks").child(String.valueOf(p.id)).setValue(p);
                Intent intent = new Intent(getApplicationContext(), Edit.class);
                intent.putExtra("id", p.id);
                intent.putExtra("title", p.title);
                intent.putExtra("content", p.content);
                startActivity(intent);
            }
        });
    }
}