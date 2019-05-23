package com.example.tongan.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tongan.myapplication.Classes.PostService;
import com.example.tongan.myapplication.Classes.User;
import com.example.tongan.myapplication.R;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostServiceFoldingCellRecyclerViewAdapter extends RecyclerView.Adapter<PostServiceFoldingCellRecyclerViewAdapter.ViewHolder> {

    public static final String TAG = "PostServiceFoldingCellRecyclerViewAdapter";

    //private String image;
    private ArrayList<User> userAL;
    private ArrayList<PostService> postServicesAL;
//    private String title;
//    private String location;
//    private String price;
//    private String completion;
    private Context context;

    private OnFoldingCellListener onFoldingCellListener;


    public PostServiceFoldingCellRecyclerViewAdapter(Context context, ArrayList<User> userAL, ArrayList<PostService> postServicesAL) {
        //this.image = image;
        this.userAL = userAL;
        this.postServicesAL = postServicesAL;
//        this.title = title;
//        this.location = location;
//        this.price = price;
//        this.completion = completion;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.folding_cell_recyler_view, viewGroup, false);
        return new ViewHolder(view, onFoldingCellListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

            Glide.with(context).asBitmap().load(userAL.get(i).getProfileImage()).into(viewHolder.profileImage);
            viewHolder.name.setText(userAL.get(i).getDisplayName());
            viewHolder.title.setText(postServicesAL.get(i).getServiceTitle());
//            viewHolder.location.setText(location);
            viewHolder.price.setText(Double.toString(postServicesAL.get(i).getPrice()));
//            viewHolder.completion.setText(completion);
    }


    @Override
    public int getItemCount() {
        return postServicesAL.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        FoldingCell foldingCell;
        CircleImageView profileImage;
        TextView name;
        TextView title;
        TextView generalLocation;
        TextView price;
        TextView completion;

        OnFoldingCellListener onFoldingCellListener;

        public ViewHolder(@NonNull View itemView, OnFoldingCellListener onFoldingCellListener) {
            super(itemView);
            foldingCell = itemView.findViewById(R.id.folding_cell);
            profileImage = itemView.findViewById(R.id.requesterProfileImage);
            name = itemView.findViewById(R.id.requesterName);
            title = itemView.findViewById(R.id.serviceTitle);
//            location = itemView.findViewById(R.id.serviceLocation);
            price = itemView.findViewById(R.id.servicePrice);
//            completion = itemView.findViewById(R.id.completionBefore);

            this.onFoldingCellListener = onFoldingCellListener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    foldingCell.toggle(false);
                }
            });
        }

    }

    public interface OnFoldingCellListener {
        void onFoldingCellClick(int position);
    }
}
