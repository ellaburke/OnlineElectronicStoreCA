package com.example.onlineelectronicstore.CustomerDetailsAndPurchases;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.onlineelectronicstore.ProductsToShop.CartAdapter;
import com.example.onlineelectronicstore.ProductsToShop.ShoppingCartActivity;
import com.example.onlineelectronicstore.R;
import com.example.onlineelectronicstore.model.Order;
import com.example.onlineelectronicstore.model.ShoppingCartProducts;
import com.example.onlineelectronicstore.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FullCustomerDetails extends AppCompatActivity {

    TextView fName, lName, phoneNo, address, cEmail, cardDetails, pWord;
    String firstNameS, lastNameS, phoneNoS, addressS,cEmailS, cardDetailsS,pWordS;
    User user;

    //RCV
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private List<Order> myOrders;
    private List<User> userList;
    String currentUserID;

    //Firebase
    DatabaseReference mDatabaseRef, mDatabaseUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_customer_details);

        //Init Firebase
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("orders");
        mDatabaseUserRef = FirebaseDatabase.getInstance().getReference("user");


        //Init RCV
        mRecyclerView = findViewById(R.id.customerPurchaseHistoryRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        myOrders = new ArrayList<>();
        userList = new ArrayList<>();
        mRecyclerView.setLayoutManager(mLayoutManager);

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

        //Intent getIntent = getIntent();

        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("customer_id");
        firstNameS = user.getFirstName();
        lastNameS = user.getLastName();
        phoneNoS = user.getPhoneNumber();
        addressS = user.getAddress();
        cEmailS = user.getEmail();
        cardDetailsS = user.getCardDetails();
        pWordS = user.getPassword();

        fName.setText(firstNameS);
        lName.setText(lastNameS);
        phoneNo.setText(phoneNoS);
        address.setText(addressS);
        cEmail.setText(cEmailS);
        cardDetails.setText(cardDetailsS);
        pWord.setText(pWordS);


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Order ord = postSnapshot.getValue(Order.class);
                    System.out.println("USER ID" + user);
                    if (cEmailS.equals(ord.getOrderEmail())) {
                        myOrders.add(ord);
                    }
                }
                mAdapter = new CustomerPurchaseAdapter(FullCustomerDetails.this, (ArrayList<Order>) myOrders);
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}