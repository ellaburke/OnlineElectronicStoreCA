package com.example.onlineelectronicstore.ProductsToShop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineelectronicstore.R;
import com.example.onlineelectronicstore.UpdateStock.UpdateStockAdapter;
import com.example.onlineelectronicstore.model.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ExampleViewHolder> {
    private List<Products> mProducts;
    private Context mContext;
    private OnListingListener mOnListingListener;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //Listing
        public ImageView mImageView;
        public TextView textViewName;
        public TextView textViewPrice;
        OnListingListener mOnListingListener;

        public ExampleViewHolder(@NonNull View itemView, OnListingListener onListingListener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.product_image_display);
            textViewName = itemView.findViewById(R.id.product_name_display);
            textViewPrice = itemView.findViewById(R.id.product_price_display);
            mOnListingListener = onListingListener;

            itemView.setOnClickListener((View.OnClickListener) this);
        }

        @Override
        public void onClick(View v) {
            mOnListingListener.onProductClick(getAdapterPosition());
        }
    }

    public interface OnListingListener {
        void onProductClick(int position);


    }

    public Adapter(Context context, ArrayList<Products> productsForSale, OnListingListener onListingListener) {
        //super();
        mContext = context;
        mProducts = productsForSale;
        this.mOnListingListener = onListingListener;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.product_card, parent, false);
        return new ExampleViewHolder(v, mOnListingListener);
    }


    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        Products currentProduct = mProducts.get(position);
        holder.textViewName.setText(currentProduct.getTitle());
        holder.textViewPrice.setText(currentProduct.getPrice());
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
