package com.example.tongan.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.tongan.myapplication.Adapters.OrdersRecyclerViewAdapter;
import com.example.tongan.myapplication.Classes.Order;
import com.example.tongan.myapplication.Helper.DatabaseHelper;
import com.example.tongan.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Nullable;

public class OrderActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private DocumentReference documentReference;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView orderList;
    private ArrayList<Order> ordersAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        firebaseFirestore = FirebaseFirestore.getInstance();
        databaseHelper = new DatabaseHelper();

        orderList = findViewById(R.id.orderList);

        loadOrders();

        OrdersRecyclerViewAdapter adapter = new OrdersRecyclerViewAdapter(this, ordersAL);
        orderList.setAdapter(adapter);
        orderList.setLayoutManager(new LinearLayoutManager(this));
    }

    // NEEDS MORE WORK
    private void loadOrders() {
        final ArrayList<String> orderNumbers = new ArrayList<>();
        documentReference = firebaseFirestore.collection("Users").document(databaseHelper.getCurrentUserEmail());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    Map<String, Object> map = documentSnapshot.getData();
                    for (String orderNumber : (ArrayList<String>) map.get("orderNumbers")) {
                        orderNumbers.add(orderNumber);
                    }
                }
            }
        });
    }

}

