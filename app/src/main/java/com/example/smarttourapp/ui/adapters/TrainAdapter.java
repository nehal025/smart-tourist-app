package com.example.smarttourapp.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttourapp.R;
import com.example.smarttourapp.model.Train;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Random;

public class TrainAdapter extends RecyclerView.Adapter<TrainAdapter.MyViewHolder>{

    private List<Train> articles;
    private Context context;
    int[] myImageList = new int[]{R.drawable.train_blue, R.drawable.train_yellow, R.drawable.train_green};


    public TrainAdapter(List<Train> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    private OnItemClickListener mListener;


    public interface OnItemClickListener {

        void onItemClick(int position);

    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.train_item, parent, false);
        return new MyViewHolder(view, mListener);
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, int position) {
        final MyViewHolder holder = holders;
        Train model = articles.get(position);




        holder.title.setText(model.getTitle());
        holder.time.setText(model.getTime());
        holder.imageView.setImageResource(myImageList[new Random().nextInt(2)]);
        holder.cost.setText("₹ "+model.getCost().toString());


    }

    @Override
    public int getItemCount() {
        return articles.size();
    }





    public class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView title ,time,cost,trainClass;
        RoundedImageView imageView;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {

            super(itemView);

            title = itemView.findViewById(R.id.train_title);
            time = itemView.findViewById(R.id.train_time);
            cost = itemView.findViewById(R.id.train_cost);
            trainClass = itemView.findViewById(R.id.train_class);
            imageView = itemView.findViewById(R.id.train_img);




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

