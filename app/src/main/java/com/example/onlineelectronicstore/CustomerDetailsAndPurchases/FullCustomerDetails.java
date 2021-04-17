package com.example.onlineelectronicstore.CustomerDetailsAndPurchases;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.onlineelectronicstore.R;

public class FullCustomerDetails extends AppCompatActivity {

    TextView fName, lName, phoneNo, address, cEmail, cardDetails, pWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_customer_details);

        //Init UI
        fName = (TextView) findViewById(R.id.firstNameDisplayed);
        lName = (TextView) findViewById(R.id.lastNameDisplayed);
        phoneNo = (TextView) findViewById(R.id.phoneDisplayed);
        address = (TextView) findViewById(R.id.addressDisplayed);
        cEmail = (TextView) findViewById(R.id.emailDisplayed);
        cardDetails = (TextView) findViewById(R.id.cardDisplayed);
        pWord = (TextView) findViewById(R.id.passwordDisplayed);

//        Bundle bundle = getArguments();
//        String value = bundle.getString(key);

        Intent getIntent = getIntent();
        fName.setText(getIntent.getStringExtra("customer_first_name"));
        lName.setText(getIntent.getStringExtra("customer_last_name"));
        phoneNo.setText(getIntent.getStringExtra("customer_phone"));
        address.setText(getIntent.getStringExtra("customer_address"));
        cEmail.setText(getIntent.getStringExtra("customer_email"));
        cardDetails.setText(getIntent.getStringExtra("customer_card"));
        pWord.setText(getIntent.getStringExtra("customer_password"));

    }
}