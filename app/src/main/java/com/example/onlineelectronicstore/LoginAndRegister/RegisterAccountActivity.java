package com.example.onlineelectronicstore.LoginAndRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlineelectronicstore.R;
import com.example.onlineelectronicstore.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterAccountActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private static final String USER = "user";
    private static final String TAG = "RegisterAccountActivity";
    private User user;

    //UI Components
    EditText firstNameET, lastNameET, emailET, phoneET, addressET, passwordET, confirmPasswordET, adminET;
    Button createAccBtn, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(USER);
        mAuth = FirebaseAuth.getInstance();

        //Init UI Components
        firstNameET = (EditText) findViewById(R.id.firstNameET);
        lastNameET = (EditText) findViewById(R.id.lastNameET);
        emailET = (EditText) findViewById(R.id.emailET);
        phoneET = (EditText) findViewById(R.id.phoneET);
        addressET = (EditText) findViewById(R.id.addressET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        adminET = (EditText) findViewById(R.id.adminOptionET);
        confirmPasswordET = (EditText) findViewById(R.id.confirmPasswordET);
        createAccBtn = (Button) findViewById(R.id.registerButton);
        backBtn = (Button) findViewById(R.id.homePageButton);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create account on firebase
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),"Enter email and password", Toast.LENGTH_LONG).show();
                    return;
                }
                String firstName = firstNameET.getText().toString();
                String lastName = lastNameET.getText().toString();
                String phoneNumber = phoneET.getText().toString();
                String address = addressET.getText().toString();
                String admin = adminET.getText().toString();
                String cardDetails = "Empty";
                Boolean isAdmin = true;
                if(admin.equals("12345")) {
                    isAdmin = true;
                }else{
                    isAdmin = false;
                }

                user = new User(email,password,firstName,lastName,phoneNumber, address, cardDetails, isAdmin);
                registerUser(email,password);
            }
        });

    }

    public void registerUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            updateUI(user, userId);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterAccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void updateUI(FirebaseUser currentUser, String userId){
        //String keyId = mDatabase.push().getKey();
        mDatabase.child(userId).setValue(user);
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }
}