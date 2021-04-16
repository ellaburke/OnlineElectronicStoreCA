package com.example.onlineelectronicstore.UpdateStock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineelectronicstore.Customer.Adapter;
import com.example.onlineelectronicstore.R;
import com.example.onlineelectronicstore.model.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UpdateStockAdapter extends RecyclerView.Adapter<UpdateStockAdapter.ExampleViewHolder> {
    private List<Products> mProducts;
    private Context mContext;
    private OnListingListener mOnListingListener;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Listing
        public ImageView mImageView;
        public TextView textViewName;
        public Button updateButton;
        OnListingListener mOnListingListener;


        public ExampleViewHolder(@NonNull View itemView, OnListingListener onListingListener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.productImage1);
            textViewName = itemView.findViewById(R.id.productTitle1);
            updateButton = itemView.findViewById(R.id.updateStockBtn);
            mOnListingListener = onListingListener;

            updateButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mOnListingListener.onProductUpdateClick(getAdapterPosition());
        }
    }

    public interface OnListingListener {
        void onProductUpdateClick(int position);


    }


    public UpdateStockAdapter(Context context, ArrayList<Products> productsForSale, OnListingListener onListingListener) {
        //super();
        mContext = context;
        mProducts = productsForSale;
        this.mOnListingListener = onListingListener;
    }
    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.product_update_stock_card, parent, false);
        return new ExampleViewHolder(v, mOnListingListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        Products currentProduct = mProducts.get(position);
        holder.textViewName.setText(currentProduct.getTitle());
        Picasso.get()
                .load(currentProduct.getImage())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.mImageView);

    }

    @Override
    public int getItemCount() {

        return mProducts.size();
    }


}
