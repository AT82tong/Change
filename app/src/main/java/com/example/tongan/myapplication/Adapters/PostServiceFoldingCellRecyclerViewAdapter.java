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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tongan.myapplication.Activities.EditServiceActivity;
import com.example.tongan.myapplication.Activities.MainActivity;
import com.example.tongan.myapplication.Classes.Order;
import com.example.tongan.myapplication.Classes.PostService;
import com.example.tongan.myapplication.Classes.Service;
import com.example.tongan.myapplication.Helper.DatabaseHelper;
import com.example.tongan.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ramotion.foldingcell.FoldingCell;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostServiceFoldingCellRecyclerViewAdapter extends RecyclerView.Adapter<PostServiceFoldingCellRecyclerViewAdapter.ViewHolder> {

    public static final String TAG = "PostServiceFoldingCellRecyclerViewAdapter";

    private String randomId;
    private DocumentReference documentReference;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);

    //private String image;
    private ArrayList<PostService> postServicesAL;
//    private String title;
//    private String location;
//    private String price;
//    private String completion;

    private Context context;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DatabaseHelper databaseHelper = new DatabaseHelper();

    private OnFoldingCellListener onFoldingCellListener;

    public PostServiceFoldingCellRecyclerViewAdapter(Context context, ArrayList<PostService> postServicesAL) {
        this.postServicesAL = postServicesAL;
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

                        if (databaseHelper.getCurrentUserEmail().equals(map.get("email").toString())) {
                            viewHolder.removeService.setVisibility(View.VISIBLE);
                            viewHolder.editService.setVisibility(View.VISIBLE);
                            viewHolder.acceptService.setVisibility(View.GONE);
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
        try {
            return postServicesAL.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //OnFoldingCellListener onFoldingCellListener;
        private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        private FoldingCell foldingCell;
        private CircleImageView profileImage;
        private TextView requesterName;
        private TextView serviceTitle;
        private TextView servicePrice;
        private Button removeService;
        private Button editService;
        private Button acceptService;

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
            acceptService = itemView.findViewById(R.id.serviceAccept);
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

            editService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editService(getAdapterPosition(), postServicesAL);
                }
            });

            acceptService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptService(postServicesAL.get(getAdapterPosition()), "PostServices");
                }
            });
        }

    }

    public interface OnFoldingCellListener {
        void onFoldingCellClick(int position);
    }

    private AlertDialog removeService(final int position) {
        //Log.d(TAG, "pressed");
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

    private void editService(final int position, ArrayList<PostService> postServicesAL) {
        Intent intent = new Intent(context, EditServiceActivity.class);
        intent.putExtra("postService", postServicesAL.get(position));
        intent.putExtra("serviceType", "PostServices");
        context.startActivity(intent);
    }

    private void acceptService(final Service service, final String serviceType) {
        randomId = UUID.randomUUID().toString();
        final String acceptor = databaseHelper.getCurrentUserEmail();
        final String serviceId = service.getId();
        documentReference = firebaseFirestore.collection(serviceType).document(service.getId());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    Map<String, Object> map = documentSnapshot.getData();
                    Order order = new Order(serviceId, serviceType, map.get("publisherEmail").toString(), acceptor, "Accepted", dateFormat.format(new Date()), Double.parseDouble(map.get("price").toString()));
                    firebaseFirestore.collection("Orders").document(randomId)
                            .set(order)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "Service Accepted.", Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "Service accept successful.");

                                    firebaseFirestore.collection("Users").document(acceptor).update("orderNumbers", FieldValue.arrayUnion(randomId));
                                    firebaseFirestore.collection("Users").document(service.getPublisherEmail()).update("orderNumbers", FieldValue.arrayUnion(randomId));
                                    Intent intent = new Intent(context, MainActivity.class);
                                    context.startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Error accepting service " + serviceId, e);
                                }
                            });
                }
            }
        });
    }
}
