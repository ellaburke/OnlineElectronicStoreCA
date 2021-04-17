package com.example.onlineelectronicstore.Customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Toast;

import com.example.onlineelectronicstore.ProductsToShop.AllProductsForSaleActivity;
import com.example.onlineelectronicstore.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class CustomerProfileActivity extends AppCompatActivity {

    //Tag
    private static final String TAG = "CustomerProfileActivity";
    private static final String USER = "user";
    private static final String PP = "profilePictures";

    //Firebase
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    DatabaseReference updateRef;
    StorageReference storageRef;
    StorageReference profilerUpdateRef;
    private String userId;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    //UI Components
    EditText profileFName, profileLName, profileAddress, profilePhone, profileEmail, profileCreditCard;
    Button updateButton;
    ImageView uploadProfileImage;
    ImageView firstNameEditImage, secondNameEditImage, phoneEditImage, addressEditImage, creditCardEditImage;

    //Update User Info
    String email;
    String fName, lName, pNumber, address, creditCard;
    private Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        //Init Firebase
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        mStorageRef = FirebaseStorage.getInstance().getReference("profilePictures");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("profilePictures");
        //user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child(USER);
        updateRef = FirebaseDatabase.getInstance().getReference("user");
        storageRef = FirebaseStorage.getInstance().getReference();
        profilerUpdateRef = storageRef.child(user.getUid() + ".jpg");

        //Init UI Components
        uploadProfileImage = (ImageView) findViewById(R.id.profileImageView);
        firstNameEditImage = (ImageView) findViewById(R.id.firstNameEditImage);
        secondNameEditImage = (ImageView) findViewById(R.id.lastNameEditImage);
        phoneEditImage = (ImageView) findViewById(R.id.phoneEditImage);
        addressEditImage = (ImageView) findViewById(R.id.addressEditImage);
        creditCardEditImage = (ImageView) findViewById(R.id.savedCardEditImage);
        profileFName = (EditText) findViewById(R.id.etFirstName);
        profileLName = (EditText) findViewById(R.id.etLastName);
        profilePhone = (EditText) findViewById(R.id.etPhoneNumber);
        profileEmail = (EditText) findViewById(R.id.etEmail);
        profileAddress = (EditText) findViewById(R.id.etAddress);
        profileCreditCard = (EditText) findViewById(R.id.etSavedCard);
        updateButton = (Button) findViewById(R.id.updateProfileButton);

        profileEmail.setEnabled(false);
        profileFName.setEnabled(false);
        profileLName.setEnabled(false);
        profilePhone.setEnabled(false);
        profileAddress.setEnabled(false);
        profileCreditCard.setEnabled(false);


        profilerUpdateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(uploadProfileImage);
            }
        });

        firstNameEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileFName.setEnabled(true);
            }
        });
        secondNameEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileLName.setEnabled(true);
            }
        });
        phoneEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePhone.setEnabled(true);
            }
        });
        addressEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileAddress.setEnabled(true);
            }
        });
        creditCardEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileCreditCard.setEnabled(true);
            }
        });
        uploadProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        userRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyId : snapshot.getChildren()) {
                    if (keyId.child("email").getValue().equals(email)) {
                        fName = keyId.child("firstName").getValue(String.class);
                        lName = keyId.child("lastName").getValue(String.class);
                        pNumber = keyId.child("phoneNumber").getValue(String.class);
                        address = keyId.child("address").getValue(String.class);
                        creditCard = keyId.child("cardDetails").getValue(String.class);
                        break;
                    }
                }
                profileFName.setText(fName);
                profileLName.setText(lName);
                profilePhone.setText(pNumber);
                profileEmail.setText(email);
                profileAddress.setText(address);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        //Init btm nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.MyProfileNav);

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
                        return true;
                    case R.id.MyCartNav:
                        //startActivity(new Intent(getApplicationContext(), MyCart.class));
                        //overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });


    }

    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            selectedImage = data.getData();
            uploadProfileImage.setImageURI(selectedImage);

            uploadProfilePicture();
        }
    }

    private void uploadProfilePicture() {
        StorageReference imgRef = storageRef.child(user.getUid() + ".jpg");

        imgRef.putFile(selectedImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(CustomerProfileActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void update() {
        if (isFNameChanged() || isLNameChanged() || isPhoneNumberChanged() || isAddressNumberChanged() || isCardNumberChanged() || selectedImage != null) {
            uploadProfilePicture();
            Toast.makeText(CustomerProfileActivity.this, "Account Updated", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(CustomerProfileActivity.this, AllProductsForSaleActivity.class);
            startActivity(intent);

        } else
            Toast.makeText(CustomerProfileActivity.this, "Details are the same & Can not be updated!", Toast.LENGTH_LONG).show();
    }


    private boolean isAddressNumberChanged() {
        if (!address.equals(profileAddress.getText().toString())) {

            updateRef.child(userId).child("address").setValue(profileAddress.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isCardNumberChanged() {
        if (!creditCard.equals(profileCreditCard.getText().toString())) {

            updateRef.child(userId).child("cardDetails").setValue(profileCreditCard.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isPhoneNumberChanged() {
        if (!pNumber.equals(profilePhone.getText().toString())) {

            updateRef.child(userId).child("phoneNumber").setValue(profilePhone.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isLNameChanged() {
        if (!lName.equals(profileLName.getText().toString())) {

            updateRef.child(userId).child("lastName").setValue(profileLName.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isFNameChanged() {
        if (!fName.equals(profileFName.getText().toString())) {

            updateRef.child(userId).child("firstName").setValue(profileFName.getText().toString());
            return true;
        } else {
            return false;
        }
    }
}