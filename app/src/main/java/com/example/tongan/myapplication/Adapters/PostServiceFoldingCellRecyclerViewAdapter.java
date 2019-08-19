package com.example.tongan.myapplication.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tongan.myapplication.Classes.PostService;
import com.example.tongan.myapplication.Classes.User;
import com.example.tongan.myapplication.Helper.DatabaseHelper;
import com.example.tongan.myapplication.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostServiceFoldingCellRecyclerViewAdapter extends RecyclerView.Adapter<PostServiceFoldingCellRecyclerViewAdapter.ViewHolder> {

    public static final String TAG = "PostServiceFoldingCellRecyclerViewAdapter";

    //private String image;
    private User user;
    private ArrayList<PostService> postServicesAL;
//    private String title;
//    private String location;
//    private String price;
//    private String completion;
    private Context context;

    private ArrayList<String> postNumbers;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DatabaseHelper databaseHelper = new DatabaseHelper();

    private OnFoldingCellListener onFoldingCellListener;


    public PostServiceFoldingCellRecyclerViewAdapter(Context context, User user, ArrayList<PostService> postServicesAL) {
        //this.image = image;
        this.user = user;
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_service_foldin_cell_recyler_view, viewGroup, false);
        return new ViewHolder(view, onFoldingCellListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        if (user.getProfileImage() != null) {
            Glide.with(context).asBitmap().load(user.getProfileImage()).into(viewHolder.profileImage);
        } else {
            Glide.with(context).asBitmap().load(R.drawable.settings_profile_picture).into(viewHolder.profileImage);
        }
        viewHolder.name.setText(user.getDisplayName());
        viewHolder.title.setText(postServicesAL.get(i).getServiceTitle());
//      viewHolder.location.setText(location);
        viewHolder.price.setText(Double.toString(postServicesAL.get(i).getPrice()));
//      viewHolder.completion.setText(completion);
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
        Button removeService;

        //OnFoldingCellListener onFoldingCellListener;

        public ViewHolder(@NonNull View itemView, final OnFoldingCellListener onFoldingCellListener) {
            super(itemView);
            //Log.d(TAG, "itemView: " + itemView);
            foldingCell = itemView.findViewById(R.id.folding_cell);
            profileImage = itemView.findViewById(R.id.requesterProfileImage);
            name = itemView.findViewById(R.id.requesterName);
            title = itemView.findViewById(R.id.serviceTitle);
//            location = itemView.findViewById(R.id.serviceLocation);
            price = itemView.findViewById(R.id.servicePrice);
            removeService = itemView.findViewById(R.id.serviceRemove);
//            completion = itemView.findViewById(R.id.completionBefore);

            //this.onFoldingCellListener = onFoldingCellListener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //onFoldingCellListener.onFoldingCellClick(getAdapterPosition());
                    //Log.d(TAG, "Position: " + onFoldingCellListener.onFoldingCellClick(););
                    foldingCell.toggle(false);
                }
            });

            removeService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog = removeService(getAdapterPosition());
                    dialog.show();
                }
            });
        }

    }

    public interface OnFoldingCellListener {
        void onFoldingCellClick(int position);
    }

    public AlertDialog removeService(final int position) {
        //Log.d(TAG, "pressed");
        postNumbers = new ArrayList<>();
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Do you want to delete?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseFirestore.collection("PostServices").document(postServicesAL.get(position).getId()).delete();
                        final DocumentReference ref = firebaseFirestore.collection("Users").document(databaseHelper.getCurrentUserEmail());
                        ref.update("postNumbers", FieldValue.arrayRemove(postServicesAL.get(position).getId()));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "Cancel: " + postServicesAL.get(position).getId());
                    }
                })
                .create();
        alertDialog.show();

        return alertDialog;
    }
}
