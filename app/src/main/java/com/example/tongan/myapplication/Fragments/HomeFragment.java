package com.example.tongan.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.viewpager.widget.ViewPager;

import com.example.tongan.myapplication.Activities.SearchActivity;
import com.example.tongan.myapplication.Adapters.HomePageAdsAdapter;
import com.example.tongan.myapplication.Adapters.PostServiceFoldingCellRecyclerViewAdapter;
import com.example.tongan.myapplication.Adapters.RequestServiceFoldingCellRecyclerViewAdapter;
import com.example.tongan.myapplication.Classes.PostService;
import com.example.tongan.myapplication.Classes.RequestService;
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

public class HomeFragment extends Fragment implements PostServiceFoldingCellRecyclerViewAdapter.OnFoldingCellListener {

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

    private ArrayList<PostService> postServicesAL = new ArrayList<>();
    private ArrayList<RequestService> requestServicesAL = new ArrayList<RequestService>();
    private RecyclerView postServiceFoldingCellRecyclerView;
    private RecyclerView requestServiceFoldingCellRecyclerView;

    private DatabaseHelper databaseHelper = new DatabaseHelper();

    private TextView postServiceText;
    private TextView requestServiceText;

//    private int dotsCount;
//    private ImageView[] dots;
//
//    private FirebaseAuth fireBaseAuth;
//    private FirebaseUser fireBaseUser;
//
//    private DatabaseHelper db = new DatabaseHelper();
//
//    private FoldingCell foldingCell;

//    RecyclerView documentationsRecyclerView;
//    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        homePageAds = (ViewPager) view.findViewById(R.id.homePage_ads);
        homePageAdsAdapter = new HomePageAdsAdapter(this.getActivity());
        homePageAds.setAdapter(homePageAdsAdapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new imageAutoSlider(), 5000, 5000);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("PostServices");

        searchBtn = view.findViewById(R.id.searchBtn);

        postServiceText = view.findViewById(R.id.post_service);
        requestServiceText = view.findViewById(R.id.request_service);

        postServiceFoldingCellRecyclerView = view.findViewById(R.id.postServiceFoldingCellRecyclerView);
        postServiceFoldingCellRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        requestServiceFoldingCellRecyclerView = view.findViewById(R.id.requestServiceFoldingCellRecyclerView);
        requestServiceFoldingCellRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadPostServiceInfoFromDatabase();
        loadRequestServiceInfoFromDatabase();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        // post service expand/collapse button
        postServiceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postServiceFoldingCellRecyclerView.getVisibility() == View.VISIBLE) {
                    postServiceFoldingCellRecyclerView.setVisibility(View.GONE);
                    postServiceText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                } else {
                    postServiceFoldingCellRecyclerView.setVisibility(View.VISIBLE);
                    postServiceText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);
                }
                ((SimpleItemAnimator) postServiceFoldingCellRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
            }
        });

        // request service expand/collapse button
        requestServiceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestServiceFoldingCellRecyclerView.getVisibility() == View.VISIBLE) {
                    requestServiceFoldingCellRecyclerView.setVisibility(View.GONE);
                    requestServiceText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
                } else {
                    requestServiceFoldingCellRecyclerView.setVisibility(View.VISIBLE);
                    requestServiceText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);
                }
                ((SimpleItemAnimator) postServiceFoldingCellRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
            }
        });
        //initRecyclerView();

        // foldingCell example
//        foldingCell = view.findViewById(R.id.folding_cell2);
//
//        foldingCell.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                foldingCell.toggle(false);
//            }
//        });

//        Log.d(TAG, "getCurrentUser: " + FirebaseAuth.getInstance().getCurrentUser());
//        Log.d(TAG, "getEmail: " + db.getCurrentUserEmail());
//        Log.d(TAG, "getCurrentUser: " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
//        Log.d(TAG, "getUid: " + FirebaseAuth.getInstance().getUid());

        //User user = db.getUserData(db.getCurrentUserEmail());
//        Log.d(TAG, "firstName: " + user.getFirstName());
//        Log.d(TAG, "lastName: " + user.getLastName());
//        Log.d(TAG, "email: " + user.getEmail());
//        db.getUserData(db.getCurrentUserEmail());
        //Log.d(TAG, "password: " + db.user.getPassword());


//-----        DOTS         ----- (CURRENTLY NOT USING)
//        sliderDot = (LinearLayout) v.findViewById(R.id.sliderDots);
//        dotsCount = homePageAdsAdapter.getCount();
//        dots = new ImageView[dotsCount];
//        for (int i = 0; i < dotsCount; i++) {
//            dots[i] = new ImageView(this.getActivity());
//            dots[i].setImageDrawable(ContextCompat.getDrawable(this.getActivity(), R.drawable.dots));
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.setMargins(8, 0, 8, 0);
//            sliderDot.addView(dots[i], params);
//        }
//
//        dots[0].setImageDrawable(ContextCompat.getDrawable(this.getActivity(), R.drawable.selected_dots));
//
//        homePageAds.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                                                @Override
//                                                public void onPageScrolled(int i, float v, int i1) {
//
//                                                }
//
//                                                @Override
//                                                public void onPageSelected(int i) {
//
//                                                    for (int position = 0; position < dotsCount; position++) {
//                                                        dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.dots));
//                                                    }
//                                                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.selected_dots));
//                                                }
//
//                                                @Override
//                                                public void onPageScrollStateChanged(int i) {
//
//                                                }
//                                            }
//        );

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

//    private void initRecyclerView() {
//        ArrayList<String> name = new ArrayList<>();
//        name.add("a");
//        name.add("b");
//        name.add("c");
//        //PostServiceFoldingCellRecyclerViewAdapter adapter = new PostServiceFoldingCellRecyclerViewAdapter(getActivity(), name);
//        //foldingCellRecyclerView.setAdapter(adapter);
//    }

    @Override
    public void onFoldingCellClick(int position) {
        //Toast.makeText( getContext(), "onFoldingCellClick: Clicked at position " + position, Toast.LENGTH_LONG).show();
        //Log.d(TAG, "onFoldingCellClick: Clicked");
    }

    private void loadPostServiceInfoFromDatabase() {
        FirebaseFirestore.getInstance().collection("PostServices").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    if (!task.getResult().isEmpty()) {
                        postServiceText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            final PostService postService = new PostService();
                            Map<String, Object> map = queryDocumentSnapshot.getData();
                            DecimalFormat df = new DecimalFormat("#.00");

                            postService.setId(map.get("id").toString());
//                          postService.setpublisherEmail(databaseHelper.getCurrentUserEmail());
                            postService.setServiceTitle(map.get("serviceTitle").toString());
                            postService.setAddress(map.get("address").toString());
                            postService.setDescription(map.get("description").toString());
                            postService.setPrice(Double.parseDouble(df.format(map.get("price"))));
                            postService.setPublishTime(map.get("publishTime").toString());
                            postService.setPublisherEmail(map.get("publisherEmail").toString());

                            postServicesAL.add(postService);
                        }
                        PostServiceFoldingCellRecyclerViewAdapter adapter = new PostServiceFoldingCellRecyclerViewAdapter(getActivity(), postServicesAL);
                        postServiceFoldingCellRecyclerView.setAdapter(adapter);
                    }
                }
            }
        });
    }

    private void loadRequestServiceInfoFromDatabase() {
        FirebaseFirestore.getInstance().collection("RequestServices").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    if (!task.getResult().isEmpty()) {
                        requestServiceText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            RequestService requestService = new RequestService();
                            Map<String, Object> map = queryDocumentSnapshot.getData();
                            DecimalFormat df = new DecimalFormat("#.00");

                            requestService.setId(map.get("id").toString());
                            requestService.setServiceTitle(map.get("serviceTitle").toString());
                            requestService.setAddress(map.get("address").toString());
                            requestService.setDescription(map.get("description").toString());
                            requestService.setPrice(Double.parseDouble(df.format(map.get("price"))));
                            requestService.setPublishTime(map.get("publishTime").toString());
                            requestService.setPublisherEmail(map.get("publisherEmail").toString());

                            requestServicesAL.add(requestService);

                        }
                        RequestServiceFoldingCellRecyclerViewAdapter adapter = new RequestServiceFoldingCellRecyclerViewAdapter(getActivity(), requestServicesAL);
                        requestServiceFoldingCellRecyclerView.setAdapter(adapter);
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
                //dots[homePageAds.getCurrentItem()].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.selected_dots));
                //dots[homePageAds.getCurrentItem() - 1].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.dots));
            } else {
                homePageAds.setCurrentItem(0);
                //dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.selected_dots));
                //dots[homePageAdsAdapter.getCount() - 1].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.dots));

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
