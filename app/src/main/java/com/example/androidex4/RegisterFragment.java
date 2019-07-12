package com.example.androidex4;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterFragment<mAuth> extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private String userReg;
//    private  String passwordReg;
//    private String passwordRegReEnter;
    private  Button btnReg;
    private EditText username;
    private EditText PassWord;
    private EditText PassWordCheck;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {

        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments( args );

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
           View view = inflater.inflate( R.layout.fragment_register, container, false );
           mAuth = FirebaseAuth.getInstance(); // Initialize the FirebaseAuth instance.

         username = (EditText)view.findViewById(R.id.userNameReg);
         PassWord = (EditText)view.findViewById(R.id.passWordReg);
         PassWordCheck = (EditText)view.findViewById(R.id.passWordRegCheck);
         btnReg = view.findViewById( R.id.regiButt );

        btnReg.setOnClickListener( new View.OnClickListener() {
       @Override

       public void onClick(View v) {
           String userReg = username.getText().toString();
           String passwordReg = PassWord.getText().toString();
           String passwordRegReEnter = PassWordCheck.getText().toString();

           if ( TextUtils.isEmpty( userReg )  ) {
               username.setError( "The item Username cannot be empty" );
               return;
           }
           if ( !( passwordReg.equals( passwordRegReEnter) ) ){
               PassWordCheck.setError( "The item Password and password Re-Enter not equal" );
               return;
           }

           mAuth.createUserWithEmailAndPassword( userReg, passwordReg)
                   .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if (task.isSuccessful()) {
                       FirebaseUser user = mAuth.getCurrentUser();
                       // Sign in success, update UI with the signed-in user's information
                       Toast.makeText(getActivity(), "Created user" + user.getEmail(), Toast.LENGTH_LONG).show();
                       database = FirebaseDatabase.getInstance();
                       database.getReference(user.getUid()).child("donate").setValue(0);
                   } else {
                       // If sign in fails, display a message to the user.
                       Log.i( "createUser: failure", task.getException().toString());
                       Toast.makeText(getActivity(), "Created user failed", Toast.LENGTH_SHORT).show();
                   }
               }
           });
           }
       });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
