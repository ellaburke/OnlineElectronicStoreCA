package com.example.onlineelectronicstore.CustomerDetailsAndPurchases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineelectronicstore.R;
import com.example.onlineelectronicstore.UpdateStock.UpdateStockAdapter;
import com.example.onlineelectronicstore.model.Products;
import com.example.onlineelectronicstore.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomerDetailAdapter extends RecyclerView.Adapter<CustomerDetailAdapter.ExampleViewHolder>{

    private List<User> mCustomers;
    private Context mContext;
    private OnListingListener mOnListingListener;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Listing
        public TextView textViewName;
        public Button updateButton;
        OnListingListener mOnListingListener;


        public ExampleViewHolder(@NonNull View itemView, OnListingListener onListingListener) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.customerNameTV);
            updateButton = itemView.findViewById(R.id.updateUserBtn);
            mOnListingListener = onListingListener;

            updateButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mOnListingListener.onCustomerDetailClick(getAdapterPosition());
        }
    }

    public interface OnListingListener {
        void onCustomerDetailClick(int position);


    }


    public CustomerDetailAdapter(Context context, ArrayList<User> customersInDB, OnListingListener onListingListener) {
        //super();
        mContext = context;
        mCustomers = customersInDB;
        this.mOnListingListener = onListingListener;
    }
    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.customer_detail_card, parent, false);
        return new ExampleViewHolder(v, mOnListingListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        User currentProduct = mCustomers.get(position);
        holder.textViewName.setText(currentProduct.getFirstName() + " " + currentProduct.getLastName());


    }

    @Override
    public int getItemCount() {

        return mCustomers.size();
    }

}
