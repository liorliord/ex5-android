package com.example.androidex4;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DonateActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private String dispalyDonate;
    private int donate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        database = FirebaseDatabase.getInstance();

        database.getReference(LoginActivity.userUID).child("donate").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               donate = dataSnapshot.getValue(Integer.class);
               dispalyDonate = String.valueOf(donate);
                TextView total = findViewById(R.id.amount_total);
                total.setText(dispalyDonate);

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
            });
    }

    public void add_donate(View view) {

        TextView amount =  findViewById(R.id.amount_donate);
        TextView total = findViewById(R.id.amount_total);
        String donation_string = amount.getText().toString();
        try {
            float donate_float = Float.parseFloat(donation_string);
            donate_float += Integer.parseInt(dispalyDonate);
            database.getReference(LoginActivity.userUID).child("donate").setValue(donate_float);
            amount.setText("");
        } catch (NumberFormatException | NullPointerException nfe) {
            return;
        }

    }
}

