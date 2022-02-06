package com.example.smarttourapp.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.CursorIndexOutOfBoundsException;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttourapp.R;
import com.example.smarttourapp.model.CurrentLocation;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>{

    private List<CurrentLocation> articles;
    private Context context;


    public SearchAdapter(List<CurrentLocation> articles, Context context) {
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search, parent, false);
        return new MyViewHolder(view, mListener);
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, int position) {
        final MyViewHolder holder = holders;
        CurrentLocation model = articles.get(position);
        holder.title.setText(model.getAddressLine());
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }





    public class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView title;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {

            super(itemView);

            title = itemView.findViewById(R.id.result);


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