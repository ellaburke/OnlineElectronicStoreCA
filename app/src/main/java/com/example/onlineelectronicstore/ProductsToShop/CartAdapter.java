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
import com.example.onlineelectronicstore.model.Products;
import com.example.onlineelectronicstore.model.ShoppingCartProducts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ExampleViewHolder>{

    private List<ShoppingCartProducts> mProducts;
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
            mImageView = itemView.findViewById(R.id.cartProductImage);
            textViewName = itemView.findViewById(R.id.cartProductName);
            textViewPrice = itemView.findViewById(R.id.cartProductPrice);
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

    public CartAdapter(Context context, ArrayList<ShoppingCartProducts> productsForSale, OnListingListener onListingListener) {
        //super();
        mContext = context;
        mProducts = productsForSale;
        this.mOnListingListener = onListingListener;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cart_card, parent, false);
        return new ExampleViewHolder(v, mOnListingListener);
    }


    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ShoppingCartProducts currentProduct = mProducts.get(position);
        holder.textViewName.setText(currentProduct.getProdcutName());
        holder.textViewPrice.setText(currentProduct.getProductPrice());
        Picasso.get()
                .load(currentProduct.getProductImage())
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
