package com.example.onlineelectronicstore.ProductsToShop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlineelectronicstore.Customer.CustomerProfileActivity;
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
import com.squareup.picasso.Picasso;

public class PurchaseProductActivity extends AppCompatActivity {

    private static final String TAG = "PurchaseProduct";

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    String userId;
    String productToDisplay;
    Products currentItem;
    TextView productName, productDescription, productManufacturer, productCategory, productPrice;
    ImageView productImage;
    String pName, pDescription, pManufacturer, pCategory, pImage, pPrice;
    int totalPrice;
    Button purchaseButton;
    ShoppingCartProducts shopCartProduct;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_product);

        //Get Intent from ViewMyFullListing
        Intent i = getIntent();
        productToDisplay = getIntent().getStringExtra("selected_product_to_display");

        //Firebase
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child("products");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("shoppingCart");
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        //Init UI
        productName = (TextView) findViewById(R.id.fullProductDisplayTitle1);
        productDescription = (TextView) findViewById(R.id.fullProductDisplayDescription1);
        productManufacturer = (TextView) findViewById(R.id.fullProductDisplayManufacturer1);
        productCategory = (TextView) findViewById(R.id.fullProductDisplayCategory1);
        productPrice = (TextView) findViewById(R.id.fullProductDisplayPickUpTimes1);
        productImage = (ImageView) findViewById(R.id.fullProductDisplayImage1);
        purchaseButton = (Button) findViewById(R.id.addToCartBtn);

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


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    currentItem = child.getValue(Products.class);
                    //Log.d(TAG, "LISTING TO UPDATE 3: " + currentItem.getListingId());
                    if (currentItem.getProductId().equals(productToDisplay)) {
                        pName = currentItem.getTitle();
                        pDescription = currentItem.getProductDescription();
                        pManufacturer = currentItem.getManufacturer();
                        pCategory = currentItem.getCategory();
                        pPrice = currentItem.getPrice();
                        pImage = currentItem.getImage();

                        productName.setText(pName);
                        productDescription.setText(pDescription);
                        productManufacturer.setText(pManufacturer);
                        productCategory.setText(pCategory);
                        productPrice.setText("€" + pPrice);
                        productImage.setImageURI(Uri.parse(pImage));
                        Picasso.get().load(pImage).into(productImage);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productToCartID, productToCartName, productToCartPrice, productToCartImage;

                productToCartID = productToDisplay;
                productToCartName = pName;
                //productToCartPrice = pPrice;
                productToCartImage = pImage;

                //totalPrice = pPrice;
                //int totalPrice = Integer.parseInt(pPrice);

                String cartID = mDatabaseRef.push().getKey();

                //Log.d(TAG, "onSuccess: firebase download url: " + downloadUrl.toString()); //use if testing...don't need this line.
                shopCartProduct = new ShoppingCartProducts(productToCartID,cartID,productToCartName, pPrice,productToCartImage, userId);

                //String uploadId = mDatabaseRef.push().getKey();
                mDatabaseRef.child(cartID).setValue(shopCartProduct);

            }
        });
    }
}