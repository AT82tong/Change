package com.example.tongan.myapplication.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.tongan.myapplication.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HorizontalDocumentationsRecyclerViewAdapter extends RecyclerView.Adapter<HorizontalDocumentationsRecyclerViewAdapter.ViewHolder> {

    public static final String TAG = "documentationsRecyclerViewAdapter";

    private ArrayList<String> imageAL = new ArrayList<>();
    private Context context;

    public HorizontalDocumentationsRecyclerViewAdapter(Context context, ArrayList<String> imageAL) {
        this.imageAL = imageAL;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_documentations_recycler_view_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(context).asBitmap().load(imageAL.get(i)).into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return imageAL.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profileDocumentationImages);
        }
    }
}
