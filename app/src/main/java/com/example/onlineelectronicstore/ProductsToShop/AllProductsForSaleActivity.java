package com.example.onlineelectronicstore.ProductsToShop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.onlineelectronicstore.Customer.CustomerProfileActivity;
import com.example.onlineelectronicstore.R;
import com.example.onlineelectronicstore.model.Products;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AllProductsForSaleActivity extends AppCompatActivity {

    //Firebase
    DatabaseReference mDatabaseRef;

    //Product
    private List<Products> myProducts;
    private List<String> productNames;

    //RCV
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    //SearchView
    SearchView mSearchView;
    Spinner sortSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products_for_sale);

        //Init RCV
        mRecyclerView = findViewById(R.id.productRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        myProducts = new ArrayList<>();
        productNames = new ArrayList<>();
        //Init Firebase
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("products");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSearchView = findViewById(R.id.searchView);

        sortSpinner = (Spinner) findViewById(R.id.sort_spinner);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.sort_array,
                android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        sortSpinner.setAdapter(staticAdapter);

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
                        return true;
                    case R.id.MyProfileNav:
                        startActivity(new Intent(getApplicationContext(), CustomerProfileActivity.class));
                        overridePendingTransition(0, 0);
                    case R.id.MyCartNav:
                        //startActivity(new Intent(getApplicationContext(), MyCart.class));
                        //overridePendingTransition(0, 0);
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
                    productNames.add(products.getTitle());
                }
                mAdapter = new Adapter(AllProductsForSaleActivity.this, (ArrayList<Products>) myProducts);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AllProductsForSaleActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

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

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the spinner selected item text
                String selectedItemText = (String) parent.getItemAtPosition(position);
                System.out.println("SELECTED IN SPINNER" + selectedItemText);
                //List<String> names = Arrays.asList("Alex", "Charles", "Brian", "David");


                if(selectedItemText.equals("Ascending")){
                    Collections.sort(productNames);

                }else if(selectedItemText.equals("Descending")){
                    Collections.reverse(productNames);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void search(String str) {
        ArrayList<Products> list = new ArrayList<>();
        for (Products obj : myProducts) {
            if (obj.getTitle().toLowerCase().contains(str.toLowerCase()) || obj.getCategory().toLowerCase().contains(str.toLowerCase()) || obj.getManufacturer().toLowerCase().contains(str.toLowerCase())) {
                list.add(obj);
            }
        }
        Adapter adapterClass = new Adapter(AllProductsForSaleActivity.this, (ArrayList<Products>) list);
        mRecyclerView.setAdapter(adapterClass);
    }
}