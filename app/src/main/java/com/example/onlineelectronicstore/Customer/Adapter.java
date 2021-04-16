package com.example.onlineelectronicstore.Customer;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ExampleViewHolder> {
    private List<Products> mProducts;
    private Context mContext;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        //Listing
        public ImageView mImageView;
        public TextView textViewName;
        public TextView textViewPrice;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.product_image_display);
            textViewName = itemView.findViewById(R.id.product_name_display);
            textViewPrice = itemView.findViewById(R.id.product_price_display);

            //itemView.setOnClickListener((View.OnClickListener) this);
        }
    }


    public Adapter(Context context, ArrayList<Products> productsForSale) {
        //super();
        mContext = context;
        mProducts = productsForSale;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.product_card, parent, false);
        return new ExampleViewHolder(v);
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
