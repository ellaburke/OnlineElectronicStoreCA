package com.example.onlineelectronicstore.UpdateStock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.onlineelectronicstore.Admin.AddProductActivity;
import com.example.onlineelectronicstore.CustomerDetailsAndPurchases.CustomerDetailsDisplayActivity;
import com.example.onlineelectronicstore.LoginAndRegister.MainActivity;
import com.example.onlineelectronicstore.R;
import com.example.onlineelectronicstore.model.Products;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateStockActivity extends AppCompatActivity implements UpdateStockAdapter.OnListingListener{

    //Firebase
    DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;

    //Product
    private List<Products> myProducts;
    String productID;

    //RCV
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    //SearchView
    SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_stock);

        //Init RCV
        mRecyclerView = findViewById(R.id.adminProductRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        myProducts = new ArrayList<>();
        //Init Firebase
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("products");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSearchView = findViewById(R.id.adminSearchView);
        mAuth = FirebaseAuth.getInstance();


        //Init btm nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.admin_navigation_menu);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.UpdateStockNav);

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
                    Products products = postSnapshot.getValue(Products.class);
                    myProducts.add(products);
                }
                mAdapter = new UpdateStockAdapter(UpdateStockActivity.this, (ArrayList<Products>) myProducts, UpdateStockActivity.this);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateStockActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        if (mSearchView != null) {
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });

        }
    }

    public void search(String str) {
        ArrayList<Products> list = new ArrayList<>();
        for (Products obj : myProducts) {
            if (obj.getTitle().toLowerCase().contains(str.toLowerCase()) || obj.getCategory().toLowerCase().contains(str.toLowerCase()) || obj.getManufacturer().toLowerCase().contains(str.toLowerCase())) {
                list.add(obj);
            }
        }
        UpdateStockAdapter adapterClass = new UpdateStockAdapter(UpdateStockActivity.this, (ArrayList<Products>) list, UpdateStockActivity.this);
        mRecyclerView.setAdapter(adapterClass);
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
            Intent backToProfileIntent = new Intent(UpdateStockActivity.this, MainActivity.class);
            startActivity(backToProfileIntent);
            return true;
        }

        return true;
    }

    @Override
    public void onProductUpdateClick(int position) {
        myProducts.get(position);
        productID = myProducts.get(position).getProductId();
        System.out.println("PRODUCT PASSED" + productID);
        Intent viewFullProductIntent = new Intent(this, FullProductToUpdate.class);
        viewFullProductIntent.putExtra("selected_product_to_display", productID);
        startActivity(viewFullProductIntent);


    }
}