package com.dhananjay.exposyslabs.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhananjay.exposyslabs.DataContent;
import com.dhananjay.exposyslabs.HomeFragment;
import com.dhananjay.exposyslabs.R;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    private final Context context;
    private final List<DataContent> dataContentList;

    public RecycleViewAdapter(Context context, List<DataContent> dataContentList) {
        this.context = context;
        this.dataContentList= dataContentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataContent dataContent = dataContentList.get(position);
        holder.imageView.setImageResource(dataContent.getPhotoId());
        holder.mTitle.setText(dataContent.getTitle());
        holder.mDesc.setText(dataContent.getDesc());
    }


    @Override
    public int getItemCount() {
        return dataContentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTitle, mDesc;
        public ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitle = itemView.findViewById(R.id.dataTitle);
            mDesc = itemView.findViewById(R.id.dataDesc);
            imageView= itemView.findViewById(R.id.imageView);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = this.getAdapterPosition();
            Toast.makeText(context,"the position is " +String.valueOf(pos), Toast.LENGTH_SHORT).show();
        }
    }
}
