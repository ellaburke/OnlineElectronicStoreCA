package com.example.onlineelectronicstore.ProductsToShop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineelectronicstore.Customer.CustomerProfileActivity;
import com.example.onlineelectronicstore.R;
import com.example.onlineelectronicstore.model.Order;
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
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class CompletePaymentActivity extends AppCompatActivity {

    //Tag
    private static final String TAG = "CompletePaymentActivity";
    EditText cardNumber;
    Button payNowBtn;
    String cardNo;
    String email;
    String creditCard;
    String productUpdateID;
    private List<String> productUpdateIDList;
    int currentStockAmount;
    int newStockAmount;
    int finalStockAmount;
    int takeAwayOne = 1;
    String checkedOutProductID, checkedOutKeyID;

    DatabaseReference updateRef, productRef, updateStockRef, updateStockRef2, removeFromCartRef, createOrderRef;
    private String userId;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private List<String> myProducts;
    private List<String> checkedOutIds;

    Order order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_payment);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child("user");
        updateRef = FirebaseDatabase.getInstance().getReference("user");
        productRef = FirebaseDatabase.getInstance().getReference("shoppingCart");
        removeFromCartRef = FirebaseDatabase.getInstance().getReference("shoppingCart");
        updateStockRef = FirebaseDatabase.getInstance().getReference("products");
        updateStockRef2 = FirebaseDatabase.getInstance().getReference("products");
        createOrderRef = FirebaseDatabase.getInstance().getReference("orders");
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        //user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        myProducts = new ArrayList<>();
        productUpdateIDList = new ArrayList<>();
        checkedOutIds = new ArrayList<>();

        //Init UI
        cardNumber = (EditText) findViewById(R.id.cardNumberInput);
        payNowBtn = (Button) findViewById(R.id.payNowBtn);

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

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ShoppingCartProducts products = postSnapshot.getValue(ShoppingCartProducts.class);
                    if (products.getUserID().equals(userId)) {
                        myProducts.add(products.getProductID());
                        checkedOutProductID = products.getProductID();
                        checkedOutKeyID = products.getCartID();
                        checkedOutIds.add(checkedOutKeyID);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CompletePaymentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateStockRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Products products = postSnapshot.getValue(Products.class);
                            if (myProducts.contains(products.getProductId())) {
                                currentStockAmount = products.getStockAmount();
                                newStockAmount = currentStockAmount -1;
                                finalStockAmount = newStockAmount;

                                String productName = products.getTitle();
                                String productIDOrder = products.getProductId();
                                String productPrice = products.getPrice();
                                String productImage = products.getImage();

                                System.out.println("NEW STOCK" + newStockAmount);
                                productUpdateID = products.getProductId();
                                productUpdateIDList.add(productUpdateID);
                                for(String keyID: checkedOutIds) {
                                    FirebaseDatabase.getInstance().getReference("shoppingCart").child(keyID).removeValue();
                                }

                                String orderID = createOrderRef.push().getKey();

                                //Log.d(TAG, "onSuccess: firebase download url: " + downloadUrl.toString()); //use if testing...don't need this line.
                                order = new Order(productIDOrder,orderID,productName, productPrice,productImage, userId, email);

                                //String uploadId = mDatabaseRef.push().getKey();
                                createOrderRef.child(orderID).setValue(order);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(CompletePaymentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

//                for(String prodToUpd: productUpdateIDList){
//                    updateStockRef.child(prodToUpd).child("stockAmount").setValue(newStockAmount);
//                }

                if (cardDetailsToSave()) {
                    Toast.makeText(CompletePaymentActivity.this, "Card details saved for next time & confirmation email sent", Toast.LENGTH_LONG).show();

                } else if (!cardDetailsToSave())
                    Toast.makeText(CompletePaymentActivity.this, "Confirmation Email Sent", Toast.LENGTH_LONG).show();


                Intent backToAllProducts = new Intent(CompletePaymentActivity.this, AllProductsForSaleActivity.class);
                startActivity(backToAllProducts);
            }


        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyId : snapshot.getChildren()) {
                    if (keyId.child("email").getValue().equals(email)) {
                        creditCard = keyId.child("cardDetails").getValue(String.class);
                        break;
                    }
                }
                if (!creditCard.equals("No card details saved")) {
                    cardNumber.setText(creditCard);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }

    private boolean cardDetailsToSave() {
        if (!creditCard.equals(cardNumber.getText().toString())) {

            updateRef.child(userId).child("cardDetails").setValue(cardNumber.getText().toString());
            return true;
        } else {
            return false;
        }
    }
//
//    private boolean updateStock() {
//        updateStockRef2.child(productUpdateID).child("stockAmount").setValue(finalStockAmount);
//        return true;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.go_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.go_back_icon) {
            Intent backToProfileIntent = new Intent(CompletePaymentActivity.this, ShoppingCartActivity.class);
            startActivity(backToProfileIntent);
            return true;
        }

        return true;
    }

}
