package com.example.tongan.myapplication.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.tongan.myapplication.Adapters.FoldingCellRecyclerViewAdapter;
import com.example.tongan.myapplication.Adapters.HomePageAdsAdapter;
import com.example.tongan.myapplication.Helper.DatabaseHelper;
import com.example.tongan.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ramotion.foldingcell.FoldingCell;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;

public class HomeFragment extends Fragment implements FoldingCellRecyclerViewAdapter.OnFoldingCellListener {

    private static final String TAG = "HomeFragment";

    //private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private final Context context = this.getActivity();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;

    private ViewPager homePageAds;
    private HomePageAdsAdapter homePageAdsAdapter;
    private LinearLayout sliderDot;

    private RecyclerView foldingCellRecyclerView;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

    private int dotsCount;
    private ImageView[] dots;

    private FirebaseAuth fireBaseAuth;
    private FirebaseUser fireBaseUser;

    private DatabaseHelper db = new DatabaseHelper();

    private FoldingCell foldingCell;

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


        loadServicesFromDatabase();

        foldingCellRecyclerView = view.findViewById(R.id.foldingCellRecyclerView);
        foldingCellRecyclerView.setLayoutManager(linearLayoutManager);
        initRecyclerView();

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

    private void loadServicesFromDatabase() {
        firebaseFirestore.collection("PostServices").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DecimalFormat df = new DecimalFormat("#.00");
                    ArrayList<String> titleAL = new ArrayList<>();
                    ArrayList<String> priceAL = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Log.d(TAG, "onComplete: " + documentSnapshot.getId());
                        Map<String, Object> map = documentSnapshot.getData();
                        titleAL.add(map.get("serviceTitle").toString());
                        priceAL.add(df.format(map.get("price")));
                    }
                }
            }
        });
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

    private void initRecyclerView() {
        ArrayList<String> name = new ArrayList<>();
        name.add("a");
        name.add("b");
        name.add("c");
        FoldingCellRecyclerViewAdapter adapter = new FoldingCellRecyclerViewAdapter(getActivity(), name, this);
        foldingCellRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onFoldingCellClick(int position) {
        //Toast.makeText( getContext(), "onFoldingCellClick: Clicked at position " + position, Toast.LENGTH_LONG).show();
        //Log.d(TAG, "onFoldingCellClick: Clicked");
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
