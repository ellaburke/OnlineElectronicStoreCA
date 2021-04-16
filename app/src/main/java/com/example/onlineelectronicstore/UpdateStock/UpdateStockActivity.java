package com.example.onlineelectronicstore.UpdateStock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.onlineelectronicstore.Admin.AddProductActivity;
import com.example.onlineelectronicstore.Customer.Adapter;
import com.example.onlineelectronicstore.R;
import com.example.onlineelectronicstore.model.Products;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateStockActivity extends AppCompatActivity {

    //Firebase
    DatabaseReference mDatabaseRef;

    //Product
    private List<Products> myProducts;

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
                mAdapter = new Adapter(UpdateStockActivity.this, (ArrayList<Products>) myProducts);
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
        Adapter adapterClass = new Adapter(UpdateStockActivity.this, (ArrayList<Products>) list);
        mRecyclerView.setAdapter(adapterClass);
    }
}