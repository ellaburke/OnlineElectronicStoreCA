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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.onlineelectronicstore.Customer.CustomerProfileActivity;
import com.example.onlineelectronicstore.R;
import com.example.onlineelectronicstore.model.Products;
import com.example.onlineelectronicstore.model.ShoppingCartProducts;
import com.example.onlineelectronicstore.model.productRating;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PurchaseProductActivity extends AppCompatActivity {

    private static final String TAG = "PurchaseProduct";

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    String userId;
    String productToDisplay;
    Products currentItem;
    TextView productName, productDescription, productManufacturer, productCategory, productPrice;
    ImageView productImage;
    RatingBar productRatingBar;
    float rateValue;
    String temp;
    TextView rateCount;
    float rateValue1;
    EditText productReview;
    String pName, pDescription, pManufacturer, pCategory, pImage, pPrice;
    int totalPrice;
    Button purchaseButton, submitReview;
    ShoppingCartProducts shopCartProduct;
    private DatabaseReference mDatabaseRef, mDatabaseRef2, mDatabaseRatingRef;

    //Rating
    productRating mProductRating;

    //Rating Bar
    RatingBar productSetRatingBar;
    private List<Float> mRatings;
    Float floatValue;
    Float sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_product);

        //Get Intent from ViewMyFullListing
        Intent i = getIntent();
        productToDisplay = getIntent().getStringExtra("selected_product_to_display");
        mRatings = new ArrayList<>();

        //Firebase
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child("products");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("shoppingCart");
        mDatabaseRef2 = FirebaseDatabase.getInstance().getReference("shoppingCart");
        mDatabaseRatingRef = FirebaseDatabase.getInstance().getReference("productRating");
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
        productRatingBar = (RatingBar) findViewById(R.id.productRatingBar);
        productReview = (EditText) findViewById(R.id.reviewET);
        submitReview = (Button) findViewById(R.id.submitReview);
        rateCount = (TextView) findViewById(R.id.rateCount);
        productSetRatingBar  = (RatingBar) findViewById(R.id.productRatingBarSet);

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

        productRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                rateValue = ratingBar.getRating();

                if(rateValue<=1 && rateValue > 0) {
                    rateValue1 = rateValue;
                    rateCount.setText("Bad " + rateValue + "/5");
                }
                else if(rateValue<=2 && rateValue > 1) {
                    rateCount.setText("Ok " + rateValue + "/5");
                    rateValue1 = rateValue;
                }
                else if(rateValue<=3 && rateValue > 2) {
                    rateValue1 = rateValue;
                    rateCount.setText("Good " + rateValue + "/5");
                }
                else if(rateValue<=4 && rateValue > 3) {
                    rateValue1 = rateValue;
                    rateCount.setText("Very Good " + rateValue + "/5");
                }
                else if(rateValue<=5 && rateValue > 4) {
                    rateValue1 = rateValue;
                    rateCount.setText("Excellent " + rateValue + "/5");
                }
            }
        });

        submitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = rateCount.getText().toString();
                productRatingBar.setRating(0);
                rateCount.setText("");
                String review = productReview.getText().toString();

                //Init Firebase
                mDatabaseRef2 = FirebaseDatabase.getInstance().getReference("productRating");

                String ratingID = mDatabaseRef.push().getKey();
                mProductRating = new productRating(productToDisplay, userId,rateValue1, review,ratingID);
                mDatabaseRef2.child(ratingID).setValue(mProductRating);

            }
        });

        mDatabaseRatingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    productRating PR = postSnapshot.getValue(productRating.class);
                    if (PR.getProductID().equals(productToDisplay)) {
                        Float rateNo = PR.getRateValue();
                        mRatings.add(rateNo);
                        System.out.println("RATING IS" + mRatings);
                    }
                }
                sum = 0.0f;
                if(!mRatings.isEmpty()) {
                    for (Float mark : mRatings) {
                        sum += mark;
                    }
                    floatValue = sum.floatValue() / mRatings.size();
                    System.out.println("AVERAGE" + floatValue);
                    productSetRatingBar.setRating(floatValue);
                }else{
                    productSetRatingBar.setRating(0f);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}