package com.example.onlineelectronicstore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.onlineelectronicstore.model.Products;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddProductActivity extends AppCompatActivity {

    //TAGS
    private static final String TAG = "AddProductActivity";
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_LOAD_LOCATION = 2;


    //Firebase
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private String userId;
    private FirebaseUser user;

    //UI Components
    EditText productTitle, productDescription, productManufacturer, productPrice;
    Spinner productCategorySpinner;
    ImageView productImage;
    private Uri selectedImage;
    Button addProductBtn;

    //Strings for uploading product to database
    String productTitleUpload, productDescriptionUpload,productManufacturerUpload, productPriceUpload, productCategoryUpload;
    Products myProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //Init Firebase
        mStorageRef = FirebaseStorage.getInstance().getReference("products");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("products");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        //Init UI
        productTitle = (EditText) findViewById(R.id.title_et);
        productDescription = (EditText) findViewById(R.id.description_et);
        productManufacturer = (EditText) findViewById(R.id.manufacturer_et);
        productPrice = (EditText) findViewById(R.id.price_et);
        productCategorySpinner = (Spinner) findViewById(R.id.product_category_spinner);
        productImage = (ImageView) findViewById(R.id.uploadImageView);
        addProductBtn = (Button) findViewById(R.id.addProductButton);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.product_category_array,
                android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        productCategorySpinner.setAdapter(staticAdapter);

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
                Intent addProductActivity = new Intent(AddProductActivity.this, AddProductActivity.class);
                startActivity(addProductActivity);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMAGE) {
            selectedImage = data.getData();
            Picasso.get().load(selectedImage).into(productImage);
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        productTitleUpload = productTitle.getText().toString();
        productDescriptionUpload = productDescription.getText().toString();
        productManufacturerUpload = productManufacturer.getText().toString();
        productPriceUpload = productPrice.getText().toString();
        productCategoryUpload = productCategorySpinner.getSelectedItem().toString();

        if (selectedImage != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(selectedImage));

            fileReference.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddProductActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();

                    String productId = mDatabaseRef.push().getKey();

                    //Log.d(TAG, "onSuccess: firebase download url: " + downloadUrl.toString()); //use if testing...don't need this line.
                    myProducts = new Products(productTitleUpload, productManufacturerUpload, productPriceUpload, productCategoryUpload, downloadUrl.toString(), productDescriptionUpload, productId);

                    //String uploadId = mDatabaseRef.push().getKey();
                    mDatabaseRef.child(productId).setValue(myProducts);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

}