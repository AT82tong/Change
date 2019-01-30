package com.example.tongan.myapplication.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.example.tongan.myapplication.Activities.MainActivity;
import com.example.tongan.myapplication.Adapters.HomePageAdsAdapter;
import com.example.tongan.myapplication.Classes.User;
import com.example.tongan.myapplication.Helper.DatabaseHelper;
import com.example.tongan.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    //private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private final Context context = this.getActivity();

    ViewPager homePageAds;
    HomePageAdsAdapter homePageAdsAdapter;
    LinearLayout sliderDot;
    private int dotsCount;
    private ImageView[] dots;

    private FirebaseAuth fireBaseAuth;
    private FirebaseUser fireBaseUser;

    private DatabaseHelper db = new DatabaseHelper();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        homePageAds = (ViewPager) v.findViewById(R.id.homePage_ads);
        homePageAdsAdapter = new HomePageAdsAdapter(this.getActivity());
        homePageAds.setAdapter(homePageAdsAdapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new imageAutoSlider(), 5000, 5000);

//        Log.d(TAG, "getCurrentUser: " + FirebaseAuth.getInstance().getCurrentUser());
//        Log.d(TAG, "getEmail: " + db.getCurrentUserEmail());
//        Log.d(TAG, "getCurrentUser: " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
//        Log.d(TAG, "getUid: " + FirebaseAuth.getInstance().getUid());
        User user = db.getUserData(db.getCurrentUserEmail());
        Log.d(TAG, "firstName: " + user.getFirstName());
        Log.d(TAG, "lastName: " + user.getLastName());
        Log.d(TAG, "email: " + user.getEmail());
        Log.d(TAG, "password: " + user.getPassword());





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


        return v;
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
