package com.example.smarttourapp.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.smarttourapp.R;
import com.example.smarttourapp.model.Hotel;
import com.example.smarttourapp.utils.Utils;

import java.util.List;

public class HotelPreddictionAdapter extends RecyclerView.Adapter<HotelPreddictionAdapter.MyViewHolder>{

    private List<Hotel> articles;
    private Context context;


    public HotelPreddictionAdapter(List<Hotel> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onClickBookNow(View view, int position);
        void onLike(View view, int position);
        void onItemClick(int position);

    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public HotelPreddictionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hotel_prediction_item, parent, false);
        return new HotelPreddictionAdapter.MyViewHolder(view, mListener);
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull HotelPreddictionAdapter.MyViewHolder holders, int position) {
        final HotelPreddictionAdapter.MyViewHolder holder = holders;
        Hotel model = articles.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context)
                .load(model.getImg())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);

        holder.title.setText(model.getTitle());
//        holder.desc.setText(model.getInfo());
        holder.location.setText(model.getLocation());
        holder.price.setText(model.getPrice());
        holder.rating.setRating(Integer.parseInt(model.getStar()));


    }

    @Override
    public int getItemCount() {
        return articles.size();
    }





    public class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView title, desc, location, price, source;
        ImageView imageView;
        ProgressBar progressBar;
        RatingBar rating;
        com.ornach.nobobutton.NoboButton bookNow;
        ImageView like;
        public MyViewHolder(View itemView, final OnItemClickListener listener) {

            super(itemView);

            title = itemView.findViewById(R.id.title);
//            desc = itemView.findViewById(R.id.desc);
            location = itemView.findViewById(R.id.hotel_location);
            price = itemView.findViewById(R.id.price);
            source = itemView.findViewById(R.id.source);
            imageView = itemView.findViewById(R.id.img);
            progressBar = itemView.findViewById(R.id.prograss_load_photo);
            rating=itemView.findViewById(R.id.rating);
            bookNow=itemView.findViewById(R.id.hote_bookNow);
            like=itemView.findViewById(R.id.like_button);

            bookNow.setOnClickListener(v -> {

                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onClickBookNow(v,position);
                    }
                }

            });
            itemView.setOnClickListener(v -> {

                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }

            });

            like.setOnClickListener(v -> {

                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onLike(v,position);
                    }
                }

            });

        }

    }
}

