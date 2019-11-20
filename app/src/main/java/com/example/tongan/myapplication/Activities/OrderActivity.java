package com.example.tongan.myapplication.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tongan.myapplication.Adapters.OrdersRecyclerViewAdapter;
import com.example.tongan.myapplication.Classes.User;
import com.example.tongan.myapplication.Helper.DatabaseHelper;
import com.example.tongan.myapplication.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Nullable;

public class OrderActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private DocumentReference documentReference;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private RecyclerView orderList;

    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        firebaseFirestore = FirebaseFirestore.getInstance();
        databaseHelper = new DatabaseHelper();

        backBtn = findViewById(R.id.back_button);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        orderList = findViewById(R.id.orderList);

        loadOrders();
    }

    private void loadOrders() {
        final User user = new User();
        final ArrayList<String> orderNumbers = new ArrayList<>();
        documentReference = firebaseFirestore.collection("Users").document(databaseHelper.getCurrentUserEmail());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    Map<String, Object> map = documentSnapshot.getData();
                    user.setDisplayName(map.get("displayName").toString());

                    if (map.get("profileImage") != null) {
                        user.setProfileImage(map.get("profileImage").toString());
                    }

                    if (null != map.get("orderNumbers")) {
                        for (String orderNumber : (ArrayList<String>) map.get("orderNumbers")) {
                            orderNumbers.add(orderNumber);
                        }
                    }
                }
                OrdersRecyclerViewAdapter adapter = new OrdersRecyclerViewAdapter(OrderActivity.this, user, orderNumbers);
                orderList.setAdapter(adapter);
                orderList.setLayoutManager(new LinearLayoutManager(OrderActivity.this));
            }
        });
    }

}

