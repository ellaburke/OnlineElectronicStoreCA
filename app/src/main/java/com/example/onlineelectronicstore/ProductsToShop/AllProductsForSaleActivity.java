package com.example.onlineelectronicstore.ProductsToShop;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.onlineelectronicstore.Customer.CustomerProfileActivity;
import com.example.onlineelectronicstore.LoginAndRegister.LoginActivity;
import com.example.onlineelectronicstore.R;
import com.example.onlineelectronicstore.model.Products;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AllProductsForSaleActivity extends AppCompatActivity implements Adapter.OnListingListener{

    //Firebase
    DatabaseReference mDatabaseRef;
    private String userId;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    //Product
    private List<Products> myProducts;
    private List<String> productNames;
    String productID;

    //RCV
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    //SearchView
    SearchView mSearchView;
    Spinner sortSpinner;

    //Builder Pattern
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products_for_sale);

        //Init RCV
        mRecyclerView = findViewById(R.id.productRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        myProducts = new ArrayList<>();
        mAdapter = new Adapter(AllProductsForSaleActivity.this, (ArrayList<Products>) myProducts, AllProductsForSaleActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        //Init Firebase
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("products");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSearchView = findViewById(R.id.searchView);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        mAuth = FirebaseAuth.getInstance();

        productNames = new ArrayList<>();

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
                    Products products = postSnapshot.getValue(Products.class);
                    if(products.getStockAmount() != 0) {
                        myProducts.add(products);
                        productNames.add(products.getTitle());
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AllProductsForSaleActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Products> sortedList = sortByOrder(parent.getItemAtPosition(position).toString());
                mAdapter = new Adapter( AllProductsForSaleActivity.this, sortedList,AllProductsForSaleActivity.this);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        Adapter adapterClass = new Adapter(AllProductsForSaleActivity.this, (ArrayList<Products>) list, AllProductsForSaleActivity.this);
        mRecyclerView.setAdapter(adapterClass);
    }

    @Override
    public void onProductClick(int position) {
        System.out.println("CLICKEDDDD");

        myProducts.get(position);
        productID = myProducts.get(position).getProductId();
        System.out.println("PRODUCT PASSED" + productID);
        Intent viewFullProductIntent = new Intent(this, PurchaseProductActivity.class);
        viewFullProductIntent.putExtra("selected_product_to_display", productID);
        startActivity(viewFullProductIntent);



    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Products> sortByOrder(String sort) {
        switch (sort) {
            case "All":
                return (ArrayList<Products>) myProducts;
            case "Name Ascending":
                return (ArrayList<Products>) myProducts.stream()
                        .sorted(Comparator.comparing(Products::getTitle)).collect(Collectors.toList());
            case "Name Descending":
                return (ArrayList<Products>) myProducts.stream()
                        .sorted(Comparator.comparing(Products::getTitle).reversed()).collect(Collectors.toList());
            case "Price Ascending":
                return (ArrayList<Products>) myProducts.stream()
                        .sorted(Comparator.comparing(Products::getPrice)).collect(Collectors.toList());
            case "Price Descending":
                return (ArrayList<Products>) myProducts.stream()
                        .sorted(Comparator.comparing(Products::getPrice).reversed()).collect(Collectors.toList());

            case "Manufacturer Descending":
                return (ArrayList<Products>) myProducts.stream()
                        .sorted(Comparator.comparing(Products::getManufacturer).reversed()).collect(Collectors.toList());
            case "Manufacturer Ascending":
                return (ArrayList<Products>) myProducts.stream()
                        .sorted(Comparator.comparing(Products::getManufacturer)).collect(Collectors.toList());
        }
        return null;
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
            builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you wish to log out?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent backToProfileIntent = new Intent(AllProductsForSaleActivity.this, LoginActivity.class);
                    mAuth.signOut();
                    startActivity(backToProfileIntent);

                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }

        return true;
    }
}