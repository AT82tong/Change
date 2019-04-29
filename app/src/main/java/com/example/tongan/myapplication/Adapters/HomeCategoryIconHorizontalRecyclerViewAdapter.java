package com.example.tongan.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tongan.myapplication.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

// CURRENTLY NOT USING
public class HomeCategoryIconHorizontalRecyclerViewAdapter extends RecyclerView.Adapter<HomeCategoryIconHorizontalRecyclerViewAdapter.ViewHolder> {

    public static final String TAG = "HomeCategoryIconHorizontalRecyclersViewAdapter";

    private ArrayList<String> imageAL = new ArrayList<>();
    private ArrayList<String> namesAL = new ArrayList<>();
    private Context context;

    public HomeCategoryIconHorizontalRecyclerViewAdapter(Context context, ArrayList<String> imageAL, ArrayList<String> namesAL) {
        this.imageAL = imageAL;
        this.namesAL = namesAL;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_page_category_icon_recyler_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        if (i % 2 == 0) {
            Glide.with(context).asBitmap().load(imageAL.get(i)).into(viewHolder.imageTop);
            viewHolder.nameTop.setText(namesAL.get(i));
        } else {
            Glide.with(context).asBitmap().load(imageAL.get(i)).into(viewHolder.imageBot);
            viewHolder.nameTop.setText(namesAL.get(i));
        }
    }


    @Override
    public int getItemCount() {
        return imageAL.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageTop;
        CircleImageView imageBot;
        TextView nameTop;
        TextView nameBot;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTop = itemView.findViewById(R.id.image_view_top);
            imageBot = itemView.findViewById(R.id.image_view_bottom);
            nameTop = itemView.findViewById(R.id.name_top);
            nameBot = itemView.findViewById(R.id.name_bottom);
        }
    }
}
