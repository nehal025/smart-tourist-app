package com.example.smarttourapp.ui.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.example.smarttourapp.model.Place;
import com.example.smarttourapp.utils.Utils;

import java.util.ArrayList;

public class FeaturedPlaceAdapter extends RecyclerView.Adapter<FeaturedPlaceAdapter.FeaturedViewHolder> {

    ArrayList<Place> featuredLocations;
    Context context;
    private OnItemClickListener mListener;

    public FeaturedPlaceAdapter(ArrayList<Place> featuredLocations, Context context) {
        this.featuredLocations = featuredLocations ;
        this.context=context;
    }


    public interface OnItemClickListener {
        void onClickBookNow(View view, int position);
        void onItemClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_design,parent,false);
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view,mListener);
        return featuredViewHolder;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {

        Place featuredViewHomeAdapter = featuredLocations.get(position);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context)
                .load(featuredViewHomeAdapter.getImg().get(3))
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
                .into(holder.image);

        holder.title.setText(featuredViewHomeAdapter.getName());
        holder.city.setText(featuredViewHomeAdapter.getLocation());

    }

    @Override
    public int getItemCount() {
        return featuredLocations.size();
    }

    public static  class FeaturedViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title,city;
        ProgressBar progressBar;



        public FeaturedViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);

            //hooks
            image = itemView.findViewById(R.id.featured_image);
            title = itemView.findViewById(R.id.featured_title);
//            rating = itemView.findViewById(R.id.featured_rating);
            city = itemView.findViewById(R.id.featured_city);
            progressBar = itemView.findViewById(R.id.featuredPlace_load_photo);


            itemView.setOnClickListener(v -> {

                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }

            });
        }
    }
}
