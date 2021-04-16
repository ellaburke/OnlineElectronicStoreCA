package com.example.onlineelectronicstore.LoginAndRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineelectronicstore.Admin.AddProductActivity;
import com.example.onlineelectronicstore.Customer.AllProductsForSaleActivity;
import com.example.onlineelectronicstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    //Firebase
    FirebaseAuth mAuth;

    //UI Variables
    EditText emailET, passwordET, adminET;
    Button adminBtn, loginBtn;
    TextView createAccountTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        //Init UI Variables
        emailET = (EditText) findViewById(R.id.emailET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        adminET = (EditText) findViewById(R.id.adminIDET);
        adminBtn = (Button) findViewById(R.id.adminBtn);
        loginBtn = (Button) findViewById(R.id.logInBtn);
        createAccountTV = (TextView) findViewById(R.id.createAccountTV);

        adminET.setEnabled(false);

        createAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, RegisterAccountActivity.class);
                startActivity(registerIntent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Home Page
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                String adminID = adminET.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(adminID.equals("12345")) {
                                Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
                                startActivity(intent);
                            }else if(!adminID.equals("12345")){
                                Intent intent = new Intent(MainActivity.this, AllProductsForSaleActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            adminET.setError("Incorrect Admin Code");
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Admin log in
                adminET.setEnabled(true);
            }
        });
    }
}