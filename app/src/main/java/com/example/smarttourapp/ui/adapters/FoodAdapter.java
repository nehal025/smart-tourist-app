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
import com.example.smarttourapp.model.Food;
import com.example.smarttourapp.model.Place;
import com.example.smarttourapp.utils.Utils;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder>{

private List<Food> articles;
private Context context;


public FoodAdapter(List<Food> articles, Context context) {
        this.articles = articles;
        this.context = context;
        }

private OnItemClickListener mListener;


public interface OnItemClickListener {
    void onClickBookNow(View view, int position);
    void onItemClick(int position);

}
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public FoodAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_item, parent, false);
        return new FoodAdapter.MyViewHolder(view, mListener);
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.MyViewHolder holders, int position) {
        final FoodAdapter.MyViewHolder holder = holders;
        Food model = articles.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context)
                .load(model.getImg().get(0))
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

        holder.title.setText(model.getName());
        holder.food_info.setText(model.getInfo());
        holder.state.setText(model.getState());



    }

    @Override
    public int getItemCount() {
        return articles.size();
    }





public class MyViewHolder extends RecyclerView.ViewHolder  {

    TextView title, food_info, state;
    ImageView imageView;
    ProgressBar progressBar;

    public MyViewHolder(View itemView, final OnItemClickListener listener) {

        super(itemView);

        title = itemView.findViewById(R.id.food_title);
        food_info = itemView.findViewById(R.id.food_info);
        state = itemView.findViewById(R.id.food_state);

        imageView = itemView.findViewById(R.id.img);
        progressBar = itemView.findViewById(R.id.food_img_load_photo);



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
