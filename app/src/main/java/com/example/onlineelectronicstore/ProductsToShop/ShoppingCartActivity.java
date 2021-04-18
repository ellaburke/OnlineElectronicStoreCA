package com.example.onlineelectronicstore.ProductsToShop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineelectronicstore.Customer.CustomerProfileActivity;
import com.example.onlineelectronicstore.LoginAndRegister.LoginActivity;
import com.example.onlineelectronicstore.R;
import com.example.onlineelectronicstore.model.Products;
import com.example.onlineelectronicstore.model.ShoppingCartProducts;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity implements CartAdapter.OnListingListener{


    //Init UI
    TextView cartTotal, cartTotalWithShipping, cartShipping;
    Button proceedToCheckout;

    //Firebase
    DatabaseReference mDatabaseRef;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    String userId;
    String totalPrice;
    double newTotalPrice;
    double finalTotalPrice;

    //RCV
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    private List<ShoppingCartProducts> myProducts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        //Init RCV
        mRecyclerView = findViewById(R.id.shoppingCartRCV);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        myProducts = new ArrayList<>();
        //Init Firebase
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("shoppingCart");
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Init UI
        cartTotal = (TextView) findViewById(R.id.totalFigureTV);
        cartShipping = (TextView) findViewById(R.id.shippingFigureTV);
        cartTotalWithShipping = (TextView) findViewById(R.id.totalAfterShippingFigureTV);
        proceedToCheckout = (Button) findViewById(R.id.proceedToCheckout);

        //Init btm nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.ProductsNav);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ProductsNav:
                        startActivity(new Intent(getApplicationContext(), AllProductsForSaleActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.MyProfileNav:
                        startActivity(new Intent(getApplicationContext(), CustomerProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.MyCartNav:
                        startActivity(new Intent(getApplicationContext(), ShoppingCartActivity.class));
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
                    ShoppingCartProducts products = postSnapshot.getValue(ShoppingCartProducts.class);
                    if(products.getUserID().equals(userId)) {
                        myProducts.add(products);
                        totalPrice = products.getProductPrice();
                        newTotalPrice = Double.parseDouble(totalPrice);
                        finalTotalPrice += newTotalPrice;
                        System.out.println("TOTOAL PRICE" + newTotalPrice);
                    }
                }
                mAdapter = new CartAdapter(ShoppingCartActivity.this, (ArrayList<ShoppingCartProducts>) myProducts, ShoppingCartActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                cartTotal.setText("€" + finalTotalPrice);
                cartShipping.setText("€2.00");
                double finalTotal = finalTotalPrice + 2.00;
                cartTotalWithShipping.setText("€" + finalTotal);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShoppingCartActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        proceedToCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewFullProductIntent = new Intent(ShoppingCartActivity.this, CompletePaymentActivity.class);
                startActivity(viewFullProductIntent);

            }
        });
    }

    @Override
    public void onProductClick(int position) {

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
            Intent backToProfileIntent = new Intent(ShoppingCartActivity.this, LoginActivity.class);
            mAuth.signOut();
            startActivity(backToProfileIntent);
            return true;
        }

        return true;
    }
}