package com.example.onlineelectronicstore.CustomerDetailsAndPurchases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineelectronicstore.ProductsToShop.CartAdapter;
import com.example.onlineelectronicstore.R;
import com.example.onlineelectronicstore.model.Order;
import com.example.onlineelectronicstore.model.ShoppingCartProducts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomerPurchaseAdapter extends RecyclerView.Adapter<CustomerPurchaseAdapter.ExampleViewHolder> {

    private List<Order> mOrders;
    private Context mContext;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        //Listing
        public ImageView mImageView;
        public TextView textViewName;
        public TextView textViewPrice;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.cartProductImage);
            textViewName = itemView.findViewById(R.id.cartProductName);
            textViewPrice = itemView.findViewById(R.id.cartProductPrice);

        }

    }


    public CustomerPurchaseAdapter(Context context, ArrayList<Order> productsForSale) {
        //super();
        mContext = context;
        mOrders = productsForSale;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cart_card, parent, false);
        return new ExampleViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        Order currentProduct = mOrders.get(position);
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

        return mOrders.size();
    }


}
