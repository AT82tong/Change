package com.example.tongan.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tongan.myapplication.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FoldingCellRecyclerViewAdapter extends RecyclerView.Adapter<FoldingCellRecyclerViewAdapter.ViewHolder> {

    public static final String TAG = "FoldingCellRecyclerViewAdapter";

    private String image;
    private String name;
    private String title;
    private Context context;

    public FoldingCellRecyclerViewAdapter(Context context, String image, String name, String title) {
        this.image = image;
        this.name = name;
        this.title = title;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.folding_cell_recyler_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

            Glide.with(context).asBitmap().load(image).into(viewHolder.image);
            viewHolder.name.setText(name);
            viewHolder.title.setText(title);
    }


    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView name;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.requesterImage);
            name = itemView.findViewById(R.id.requesterName);
            title = itemView.findViewById(R.id.serviceTitle);
        }
    }

    public interface OnFoldingCellListener {
        void onFoldingCellListerner(int position);
    }
}
