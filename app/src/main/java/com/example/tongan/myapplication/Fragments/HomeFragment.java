package com.example.tongan.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.tongan.myapplication.Activities.SearchActivity;
import com.example.tongan.myapplication.Adapters.HomePageAdsAdapter;
import com.example.tongan.myapplication.Adapters.ServiceFoldingCellRecyclerViewAdapter;
import com.example.tongan.myapplication.Classes.Service;
import com.example.tongan.myapplication.Helper.DatabaseHelper;
import com.example.tongan.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;

public class HomeFragment extends Fragment implements ServiceFoldingCellRecyclerViewAdapter.OnFoldingCellListener {

    private static final String TAG = "HomeFragment";

    //private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private final Context context = this.getActivity();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;
    private DatabaseReference databaseReference;

    private ViewPager homePageAds;
    private HomePageAdsAdapter homePageAdsAdapter;
    private LinearLayout sliderDot;

    private Button searchBtn;

    private RecyclerView foldingCellRecyclerView;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

    private ArrayList<Service> requestServicesAL = new ArrayList<Service>();
    private RecyclerView ServiceFoldingCellRecyclerView;

    private DatabaseHelper databaseHelper = new DatabaseHelper();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        homePageAds = (ViewPager) view.findViewById(R.id.homePage_ads);
        homePageAdsAdapter = new HomePageAdsAdapter(this.getActivity());
        homePageAds.setAdapter(homePageAdsAdapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new imageAutoSlider(), 5000, 5000);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("PostServices");

        searchBtn = view.findViewById(R.id.searchBtn);

        ServiceFoldingCellRecyclerView = view.findViewById(R.id.ServiceFoldingCellRecyclerView);
        ServiceFoldingCellRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadServiceInfoFromDatabase();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "here Page");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "HomeFragment Page");
    }

    @Override
    public void onFoldingCellClick(int position) {
        //Toast.makeText( getContext(), "onFoldingCellClick: Clicked at position " + position, Toast.LENGTH_LONG).show();
        //Log.d(TAG, "onFoldingCellClick: Clicked");
    }


    private void loadServiceInfoFromDatabase() {
        final ArrayList<String> acceptorAL = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("RequestServices").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            Map<String, Object> map = queryDocumentSnapshot.getData();
                            acceptorAL.clear();
                            if (null != map.get("acceptorList")) {
                                for (String acceptorEmail : (ArrayList<String>) map.get("acceptorList")) {
                                    acceptorAL.add(acceptorEmail);
                                }
                            }


                            // only shows service that are not posted by the user
                            if (!map.get("publisherEmail").toString().equals(databaseHelper.getCurrentUserEmail())) {
                                if ((Integer.parseInt(map.get("maxAcceptor").toString()) > acceptorAL.size()) || (Integer.parseInt(map.get("maxAcceptor").toString()) == -1)) {
                                    Service Service = new Service();
                                    DecimalFormat df = new DecimalFormat("0.00");

                                    Service.setId(map.get("id").toString());
                                    Service.setServiceTitle(map.get("serviceTitle").toString());
                                    Service.setAddress(map.get("address").toString());
                                    Service.setDescription(map.get("description").toString());
                                    Service.setPrice(Double.parseDouble(df.format(map.get("price"))));
                                    Service.setPublishTime(map.get("publishTime").toString());
                                    Service.setPublisherEmail(map.get("publisherEmail").toString());
                                    Service.setMaxAcceptor(Integer.parseInt(map.get("maxAcceptor").toString()));

                                    requestServicesAL.add(Service);
                                }
                            }
                            ServiceFoldingCellRecyclerViewAdapter adapter = new ServiceFoldingCellRecyclerViewAdapter(getActivity(), requestServicesAL);
                            ServiceFoldingCellRecyclerView.setAdapter(adapter);
                        }
                    }
                }
            }
        });
    }

    public class imageAutoSlider extends java.util.TimerTask {
        @Override
        public void run() {

            if (homePageAds.getCurrentItem() != homePageAdsAdapter.getCount() - 1) {
                homePageAds.setCurrentItem(homePageAds.getCurrentItem() + 1);
            } else {
                homePageAds.setCurrentItem(0);
            }
        }
    }

    private void firebaseSearch(String searchText) {
        System.out.println("Search Text: " + searchText);
//        Query firebaseSearchQuery = FirebaseDatabase.getInstance().getReference().child("PostServices").orderByChild("serviceTitle")
//                .startAt(searchText)
//                .endAt(searchText + "\uf8ff");
//
//        firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.hasChildren()) {
//                    final ArrayList<PostService> aList = new ArrayList<>();
//                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                        final PostService postService = ds.getValue(PostService.class);
//                        aList.add(postService);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        if (context instanceof Activity){
//            activity = (Activity) context;
//        }
//
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        activity = null;
//    }

}
