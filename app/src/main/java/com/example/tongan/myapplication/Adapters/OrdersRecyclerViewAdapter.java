package com.example.tongan.myapplication.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tongan.myapplication.Activities.ChatActivity;
import com.example.tongan.myapplication.Activities.OrderDetailActivity;
import com.example.tongan.myapplication.Classes.User;
import com.example.tongan.myapplication.Helper.DatabaseHelper;
import com.example.tongan.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class OrdersRecyclerViewAdapter extends RecyclerView.Adapter<OrdersRecyclerViewAdapter.ViewHolder> {

    public static final String TAG = "OrdersRecyclerViewAdapter";

    private Context context;
    private User user;
    private ArrayList<String> orderNumbers;

    private DatabaseHelper databaseHelper = new DatabaseHelper();

    public OrdersRecyclerViewAdapter(Context context, User user, ArrayList<String> orderNumbers) {
        this.context = context;
        this.user = user;
        this.orderNumbers = orderNumbers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_recycler_view_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        if (user.getProfileImage() != null) {
            Glide.with(context).asBitmap().load(user.getProfileImage()).into(holder.profileImage);
        } else {
            Glide.with(context).asBitmap().load(R.drawable.settings_profile_picture).into(holder.profileImage);
        }

        DocumentReference doc = FirebaseFirestore.getInstance().collection("Orders").document(orderNumbers.get(position));
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> map = documentSnapshot.getData();
                        String serviceId = map.get("serviceId").toString();
                        String serviceType = map.get("serviceType").toString();
                        String originalPoster = map.get("originalPoster").toString();
                        holder.serviceStatus.setText(map.get("status").toString());

                        final DocumentReference usersDoc = FirebaseFirestore.getInstance().collection("Users").document(originalPoster);
                        usersDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot ds = task.getResult();
                                    if (ds.exists()) {
                                        Map<String, Object> map = ds.getData();
                                        holder.profileDisplayName.setText(map.get("displayName").toString());


                                    }
                                }
                            }
                        });

                        final DocumentReference serviceTypeDoc = FirebaseFirestore.getInstance().collection(serviceType).document(serviceId);
                        serviceTypeDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot ds = task.getResult();
                                    if (ds.exists()) {
                                        Map<String, Object> m = ds.getData();
                                        DecimalFormat df = new DecimalFormat("0.00");

                                        holder.serviceTitle.setText(m.get("serviceTitle").toString());
                                        holder.serviceDescription.setText(m.get("description").toString());
                                        holder.servicePrice.setText("$: " + df.format(Double.parseDouble(m.get("price").toString())));
                                    }
                                }
                            }
                        });

                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        try {
            return orderNumbers.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private DocumentReference documentReference;
        private FirebaseFirestore firebaseFirestore;

        private ImageView profileImage;
        private ImageView serviceImage;
        private TextView profileDisplayName;
        private TextView serviceTitle;
        private TextView serviceDescription;
        private TextView servicePrice;
        private TextView serviceStatus;
        //private Button chatBtn;
        private Button cancelBtn;
        private Button payNowBtn;

        private RelativeLayout orderDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            firebaseFirestore = FirebaseFirestore.getInstance();

            profileImage = itemView.findViewById(R.id.profileImage);
            profileDisplayName = itemView.findViewById(R.id.profileDisplayName);
            serviceImage = itemView.findViewById(R.id.serviceImage);
            serviceTitle = itemView.findViewById(R.id.serviceTitle);
            serviceDescription = itemView.findViewById(R.id.serviceDescription);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            serviceStatus = itemView.findViewById(R.id.serviceStatus);
            //chatBtn = itemView.findViewById(R.id.chatBtn);
            cancelBtn = itemView.findViewById(R.id.cancelBtn);
            payNowBtn = itemView.findViewById(R.id.payNowBtn);
            orderDetail = itemView.findViewById(R.id.banner);

            orderDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    context.startActivity(intent);
                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog = cancelService(getAdapterPosition());
                    dialog.show();
                }
            });
        }

        private AlertDialog cancelService(final int position) {
            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setTitle("Cancel Service?")
                    .setMessage("Do you want to cancel the service?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            firebaseFirestore.collection("Orders").document(orderNumbers.get(position)).delete();
                            final DocumentReference userRef = firebaseFirestore.collection("Users").document(databaseHelper.getCurrentUserEmail());
                            userRef.update("orderNumbers", FieldValue.arrayRemove(orderNumbers.get(position)));

                            DocumentReference orderDoc = FirebaseFirestore.getInstance().collection("Orders").document(orderNumbers.get(position));
                            orderDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        if (documentSnapshot.exists()) {
                                            Map<String, Object> map = documentSnapshot.getData();
                                            String serviceId = map.get("serviceId").toString();

                                        }
                                    }
                                }
                            });

                            Toast.makeText(context, "Order canceled", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "Order canceled failed", Toast.LENGTH_LONG).show();
                        }
                    })
                    .create();
            alertDialog.show();

            return alertDialog;
        }
    }

}
