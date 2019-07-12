package com.example.androidex4;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private EditText userName;
    private EditText password;
    private Button btnLogin;
    static String userUID;

    FirebaseDatabase database;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        mAuth = FirebaseAuth.getInstance(); // Initialize the FirebaseAuth instance.


        // from Text view, make a clickListener for registering
        TextView tv = findViewById( R.id.register );
        tv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.login, new RegisterFragment()).commit();
            }
        });

        userName =  (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.getpassword);
        btnLogin = findViewById( R.id.loginbutt );
        btnLogin.setOnClickListener( new View.OnClickListener() {
            @Override    
            public void onClick(View v) {
                String user = userName.getText().toString();
                String passwordUser = password.getText().toString();

                if ( user.isEmpty()  ) {
                    userName.setError( "The item Username cannot be empty" );
                    return;
                }
                if( passwordUser.isEmpty() ){
                    password.setError( "The item Password cannot be empty" );
                    return;
                }
                Log.i( " userNameDescription", "onClick: " + user );
                Log.i( " PasswordDescription", "onClick: " + passwordUser );

                mAuth.signInWithEmailAndPassword(user, passwordUser)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Line 101 ", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                  Toast.makeText(LoginActivity.this, "Logged in with user" + user.getEmail(), Toast.LENGTH_LONG).show();
                                  LoginActivity.userUID = user.getUid();
                                  Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                  startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Line 108 ", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "user Authentication failed" , Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}
