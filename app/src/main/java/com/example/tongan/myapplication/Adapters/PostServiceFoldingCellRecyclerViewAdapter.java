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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ramotion.foldingcell.FoldingCell;


import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostServiceFoldingCellRecyclerViewAdapter extends RecyclerView.Adapter<PostServiceFoldingCellRecyclerViewAdapter.ViewHolder> {

    public static final String TAG = "PostServiceFoldingCellRecyclerViewAdapter";

    //private String image;
    private User user;
    private PostService postService;
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

    public PostServiceFoldingCellRecyclerViewAdapter(Context context, ArrayList<PostService> postServicesAL) {
        //this.image = image;
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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        DocumentReference doc = FirebaseFirestore.getInstance().collection("Users").document(postServicesAL.get(i).getPublisherEmail());
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> map = documentSnapshot.getData();

                        //user.setDisplayName(map.get("displayName").toString());
                        //user.setEmail(map.get("email").toString());
                        //user.setProfileImage(map.get("profileImage").toString());
                        //viewHolder.title.setText(postServicesAL.get(i).getServiceTitle());
                        viewHolder.requesterName.setText(map.get("displayName").toString());
                        if (map.get("profileImage") != null) {
                            Glide.with(context).asBitmap().load(map.get("profileImage").toString()).into(viewHolder.profileImage);
                        } else {
                            Glide.with(context).asBitmap().load(R.drawable.settings_profile_picture).into(viewHolder.profileImage);
                        }
                        //System.out.println(user.getDisplayName());
                    }
                }
            }
        });
//
        viewHolder.serviceTitle.setText(postServicesAL.get(i).getServiceTitle());
        viewHolder.servicePrice.setText(Double.toString(postServicesAL.get(i).getPrice()));
////      viewHolder.location.setText(location);
//      viewHolder.completion.setText(completion);
    }


    @Override
    public int getItemCount() {
        return postServicesAL.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        FoldingCell foldingCell;
        CircleImageView profileImage;
        TextView requesterName;
        TextView serviceTitle;
        TextView generalLocation;
        TextView servicePrice;
        TextView completion;
        Button removeService;
        Button editService;

        //OnFoldingCellListener onFoldingCellListener;

        public ViewHolder(@NonNull View itemView, final OnFoldingCellListener onFoldingCellListener) {
            super(itemView);
            //Log.d(TAG, "itemView: " + itemView);
            foldingCell = itemView.findViewById(R.id.folding_cell);
            profileImage = itemView.findViewById(R.id.requesterProfileImage);
            requesterName = itemView.findViewById(R.id.requesterName);
            serviceTitle = itemView.findViewById(R.id.serviceTitle);
//            location = itemView.findViewById(R.id.serviceLocation);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            removeService = itemView.findViewById(R.id.serviceRemove);
            editService = itemView.findViewById(R.id.serviceEdit);
//            completion = itemView.findViewById(R.id.completionBefore);

            Log.d(TAG, "HERE");

            checkVisibility(removeService, editService);

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

            editService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //editService(getAdapterPosition());
                }
            });
        }

    }

    public interface OnFoldingCellListener {
        void onFoldingCellClick(int position);
    }

    // set remove and edit buttons to gone for services that does not belong to the current user
    // should not be working now, need to fix
    private void checkVisibility(final Button removeService, final Button editService) {
        firebaseFirestore.collection("PostServices").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String email = document.get("publisherEmail").toString();
                        if (!email.equals(databaseHelper.getCurrentUserEmail())) {
                            removeService.setVisibility(View.GONE);
                            editService.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }

    private AlertDialog removeService(final int position) {
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

//    public void editService(final int position) {
//        DocumentReference ref = firebaseFirestore.collection("PostServices").document(postServicesAL.get(position).getAddress());
//        ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                if (documentSnapshot != null) {
//                    String email = documentSnapshot.get("publisherEmail").toString();
//                }
//            }
//        });
//    }
}
