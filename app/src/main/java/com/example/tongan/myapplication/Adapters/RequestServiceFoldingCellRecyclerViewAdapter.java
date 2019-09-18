package com.example.tongan.myapplication.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tongan.myapplication.Activities.EditServiceActivity;
import com.example.tongan.myapplication.Classes.PostService;
import com.example.tongan.myapplication.Classes.RequestService;
import com.example.tongan.myapplication.Helper.DatabaseHelper;
import com.example.tongan.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestServiceFoldingCellRecyclerViewAdapter extends RecyclerView.Adapter<RequestServiceFoldingCellRecyclerViewAdapter.ViewHolder> {

    public static final String TAG = "RequestServiceFoldingCellRecyclerViewAdapter";

    //private String image;
    private ArrayList<RequestService> requestServiceAL;
//    private String title;
//    private String location;
//    private String price;
//    private String completion;

    private Context context;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DatabaseHelper databaseHelper = new DatabaseHelper();

    private OnFoldingCellListener onFoldingCellListener;


    public RequestServiceFoldingCellRecyclerViewAdapter(Context context, ArrayList<RequestService> requestServiceAL) {
        this.requestServiceAL = requestServiceAL;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.request_service_foldin_cell_recyler_view, viewGroup, false);
        return new ViewHolder(view, onFoldingCellListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        DocumentReference doc = FirebaseFirestore.getInstance().collection("Users").document(requestServiceAL.get(i).getPublisherEmail());
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

//        viewHolder.name.setText(user.getDisplayName());
//
        viewHolder.serviceTitle.setText(requestServiceAL.get(i).getServiceTitle());
        viewHolder.servicePrice.setText(Double.toString(requestServiceAL.get(i).getPrice()));
////      viewHolder.location.setText(location);
//      viewHolder.completion.setText(completion);
    }


    @Override
    public int getItemCount() {
        return requestServiceAL.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private FoldingCell foldingCell;
        private CircleImageView profileImage;
        private TextView requesterName;
        private TextView serviceTitle;
        private TextView generalLocation;
        private TextView servicePrice;
        private TextView completion;
        private Button removeService;
        private Button editService;
        private Button acceptService;

        OnFoldingCellListener onFoldingCellListener;

        public ViewHolder(@NonNull View itemView, OnFoldingCellListener onFoldingCellListener) {
            super(itemView);
            foldingCell = itemView.findViewById(R.id.folding_cell);
            profileImage = itemView.findViewById(R.id.requesterProfileImage);
            requesterName = itemView.findViewById(R.id.requesterName);
            serviceTitle = itemView.findViewById(R.id.serviceTitle);
//            location = itemView.findViewById(R.id.serviceLocation);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            removeService = itemView.findViewById(R.id.serviceRemove);
            editService = itemView.findViewById(R.id.serviceEdit);
            acceptService = itemView.findViewById(R.id.serviceAccept);
//            completion = itemView.findViewById(R.id.completionBefore);

            // can be improve later (don't think passing buttons as arguments is correct way to do this)
            checkVisibility(removeService, editService, acceptService);

            this.onFoldingCellListener = onFoldingCellListener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                    editService(getAdapterPosition(), requestServiceAL);
                }
            });
        }
    }

    public interface OnFoldingCellListener {
        void onFoldingCellClick(int position);
    }

    private void checkVisibility(final Button removeService, final Button editService, final Button acceptService) {
        firebaseFirestore.collection("RequestServices").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String email = document.get("publisherEmail").toString();
                        if (email.equals(databaseHelper.getCurrentUserEmail())) {
                            removeService.setVisibility(View.VISIBLE);
                            editService.setVisibility(View.VISIBLE);
                            acceptService.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }

    public AlertDialog removeService(final int position) {
        //Log.d(TAG, "pressed");
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Do you want to delete?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseFirestore.collection("RequestServices").document(requestServiceAL.get(position).getId()).delete();
                        final DocumentReference ref = firebaseFirestore.collection("Users").document(databaseHelper.getCurrentUserEmail());
                        ref.update("requestNumbers", FieldValue.arrayRemove(requestServiceAL.get(position).getId()));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "Cancel: " + requestServiceAL.get(position).getId());
                    }
                })
                .create();
        alertDialog.show();

        return alertDialog;
    }

    private void editService(final int position, ArrayList<RequestService> requestServiceAL) {
        Intent intent = new Intent(context, EditServiceActivity.class);
        intent.putExtra("requestService", requestServiceAL.get(position));
        intent.putExtra("serviceType", "RequestServices");
        context.startActivity(intent);
    }
}
