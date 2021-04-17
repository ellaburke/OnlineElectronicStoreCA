package com.example.onlineelectronicstore.CustomerDetailsAndPurchases;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.onlineelectronicstore.AddProductsToDB.AddProductActivity;
import com.example.onlineelectronicstore.LoginAndRegister.LoginActivity;
import com.example.onlineelectronicstore.R;
import com.example.onlineelectronicstore.UpdateStock.UpdateStockActivity;
import com.example.onlineelectronicstore.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerDetailsDisplayActivity extends AppCompatActivity implements CustomerDetailAdapter.OnListingListener{

    //Firebase
    DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    //Product
    private List<User> myCustomers;
    String currentCustomerFName;
    String currentCustomerLName;
    String currentCustomerPhone;
    String currentCustomerAddress;
    String currentCustomerCardDetails;
    String currentCustomerEmail;
    String currentCustomerPassword;

    //RCV
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details_display);

        //Init RCV
        mRecyclerView = findViewById(R.id.customerRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        myCustomers = new ArrayList<>();

        //Init Firebase
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("user");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAuth = FirebaseAuth.getInstance();


        //Init btm nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.admin_navigation_menu);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.CustomerDetailNav);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.AddProductsNav:
                        startActivity(new Intent(getApplicationContext(), AddProductActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.UpdateStockNav:
                        startActivity(new Intent(getApplicationContext(), UpdateStockActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.CustomerDetailNav:
                        startActivity(new Intent(getApplicationContext(), CustomerDetailsDisplayActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User customer = postSnapshot.getValue(User.class);
                    if(customer.getAdmin()== false) {
                        myCustomers.add(customer);
                    }
                }
                mAdapter = new CustomerDetailAdapter(CustomerDetailsDisplayActivity.this, (ArrayList<User>) myCustomers, CustomerDetailsDisplayActivity.this);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomerDetailsDisplayActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout_icon) {
            mAuth.signOut();
            Intent backToProfileIntent = new Intent(CustomerDetailsDisplayActivity.this, LoginActivity.class);
            startActivity(backToProfileIntent);
            return true;
        }

        return true;
    }

    @Override
    public void onCustomerDetailClick(int position) {
        myCustomers.get(position);


        currentCustomerFName = myCustomers.get(position).getFirstName();
        currentCustomerLName = myCustomers.get(position).getLastName();
        currentCustomerPhone = myCustomers.get(position).getPhoneNumber();
        currentCustomerEmail = myCustomers.get(position).getEmail();
        currentCustomerAddress = myCustomers.get(position).getAddress();
        currentCustomerCardDetails = myCustomers.get(position).getCardDetails();
        currentCustomerPassword = myCustomers.get(position).getPassword();

        Intent intent = new Intent(this, FullCustomerDetails.class);
        Bundle bundle = new Bundle();
        bundle.putString("customer_first_name",currentCustomerFName);
        bundle.putString("customer_last_name",currentCustomerLName);
        bundle.putString("customer_phone",currentCustomerPhone);
        bundle.putString("customer_email",currentCustomerEmail);
        bundle.putString("customer_address",currentCustomerAddress);
        bundle.putString("customer_card",currentCustomerCardDetails);
        bundle.putString("customer_password",currentCustomerPassword);
        intent.putExtras(bundle);
        startActivity(intent);
       // Intent viewFullProductIntent = new Intent(this, FullProductToUpdate.class);
       // viewFullProductIntent.putExtra("selected_product_to_display", (Parcelable) currentCustomerID);
        //startActivity(viewFullProductIntent);


    }
}