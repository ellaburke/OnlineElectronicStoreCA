package com.example.onlineelectronicstore.UpdateStock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineelectronicstore.AddProductsToDB.AddProductActivity;
import com.example.onlineelectronicstore.CustomerDetailsAndPurchases.CustomerDetailsDisplayActivity;
import com.example.onlineelectronicstore.R;
import com.example.onlineelectronicstore.model.Products;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FullProductToUpdate extends AppCompatActivity {

    //Set value for listing being passed from intent
    String productToDisplay;
    Products currentItem;

    //String values
    String FPTitle, FPDescription, FPCategory, FPManufacturer, FPImage, FPPrice,FPID;

    int FPStock;

    //UI Components
    ImageView productImage;
    TextView productTitle;
    EditText productDescription, productCategory,productManufacturer,productPrice, productStock;
    Button updateDoneBtn;

    DatabaseReference updateRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_product_to_update);

        //Get Intent from ViewMyFullListing
        Intent i = getIntent();
        productToDisplay = getIntent().getStringExtra("selected_product_to_display");
        System.out.println("PRODUCT RECIVED" + productToDisplay);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference productRef = rootRef.child("products");
        updateRef = FirebaseDatabase.getInstance().getReference("products");

        //Init UI
        productImage = (ImageView) findViewById(R.id.fullProductDisplayImage1);
        productTitle = (TextView) findViewById(R.id.fullProductDisplayTitle1);
        productDescription = (EditText) findViewById(R.id.fullProductDisplayDescription1);
        productCategory = (EditText) findViewById(R.id.fullProductDisplayCategory1);
        productManufacturer = (EditText) findViewById(R.id.fullProductManufacturer1);
        productPrice = (EditText) findViewById(R.id.fullProductDisplayPrice1);
        productStock = (EditText) findViewById(R.id.fullProductDisplayStock);
        updateDoneBtn = (Button) findViewById(R.id.updateStockBtn);




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


        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    currentItem = child.getValue(Products.class);
                    if (currentItem.getProductId().equals(productToDisplay)) {
                        FPTitle = currentItem.getTitle();
                        FPDescription = currentItem.getProductDescription();
                        FPCategory = currentItem.getCategory();
                        FPPrice = currentItem.getPrice();
                        FPManufacturer = currentItem.getManufacturer();
                        FPStock = currentItem.getStockAmount();
                        FPImage = currentItem.getImage();
                        FPID = currentItem.getProductId();


                        productTitle.setText(FPTitle);
                        productDescription.setText(FPDescription);
                        productCategory.setText(FPCategory);
                        productPrice.setText(FPPrice);
                        productManufacturer.setText(FPManufacturer);
                        productStock.setText(String.valueOf(FPStock));

                        productImage.setImageURI(Uri.parse(FPImage));
                        Picasso.get().load(FPImage).into(productImage);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // || isManufacturerChanged() || isProductPriceChanged() || isProductDescriptionChanged()
        updateDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isProductStockChanged() || isProductDescriptionChanged()) {
                    Toast.makeText(FullProductToUpdate.this, "Product Updated", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(FullProductToUpdate.this, UpdateStockActivity.class);
                    startActivity(intent);

                } else
                    Toast.makeText(FullProductToUpdate.this, "Details are the same & Can not be updated!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isProductStockChanged() {
        if (!String.valueOf(FPStock).equals(Integer.parseInt(productStock.getText().toString()))) {

            updateRef.child(productToDisplay).child("stockAmount").setValue(Integer.parseInt(productStock.getText().toString()));
            return true;
        } else {
            return false;
        }
    }

    private boolean isProductDescriptionChanged() {
        if (!(FPDescription).equals(productDescription.getText().toString())) {

            updateRef.child(productToDisplay).child("productDescription").setValue(productDescription.getText().toString());
            return true;
        } else {
            return false;
        }
    }

//    private boolean isManufacturerChanged() {
//        if (!FPManufacturer.equals(productManufacturer.getText().toString())) {
//
//            updateRef.child(productToDisplay).child("manufacturer").setValue(productManufacturer.getText().toString());
//            return true;
//        } else {
//            return false;
//        }
//    }

//    private boolean isProductPriceChanged() {
//        if (!FPPrice.equals(productPrice.getText().toString())) {
//
//            updateRef.child(productToDisplay).child("price").setValue(productPrice.getText().toString());
//            return true;
//        } else {
//            return false;
//        }
//    }

//    private boolean isProductDescriptionChanged() {
//        if (!FPDescription.equals(productDescription.getText().toString())) {
//
//            updateRef.child(productToDisplay).child("productDescription").setValue(productDescription.getText().toString());
//            return true;
//        } else {
//            return false;
//        }
 //   }
}
