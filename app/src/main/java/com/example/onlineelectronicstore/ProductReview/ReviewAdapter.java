package com.example.onlineelectronicstore.ProductReview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineelectronicstore.R;
import com.example.onlineelectronicstore.model.productRating;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ExampleViewHolder> {

    private List<productRating> mProducts;
    private Context mContext;


    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        //Review
        public TextView textViewReview;
        public RatingBar ratingBar;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewReview = itemView.findViewById(R.id.cartProductPrice);
            ratingBar = itemView.findViewById(R.id.productRatingBarSet1);


        }
    }

    public ReviewAdapter(Context context, ArrayList<productRating> productsForSale) {
        //super();
        mContext = context;
        mProducts = productsForSale;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.review_card, parent, false);
        return new ExampleViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        productRating currentProduct = mProducts.get(position);
        holder.textViewReview.setText(currentProduct.getReview());
        holder.ratingBar.setRating(currentProduct.getRateValue());
    }

    @Override
    public int getItemCount() {

        return mProducts.size();
    }


}
