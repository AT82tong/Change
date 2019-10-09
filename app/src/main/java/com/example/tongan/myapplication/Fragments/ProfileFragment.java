package com.example.tongan.myapplication.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tongan.myapplication.Activities.OrderActivity;
import com.example.tongan.myapplication.Activities.SettingsPage.ProfileSettingsActivity;
import com.example.tongan.myapplication.Adapters.HorizontalDocumentationsRecyclerViewAdapter;
import com.example.tongan.myapplication.Adapters.PostServiceFoldingCellRecyclerViewAdapter;
import com.example.tongan.myapplication.Adapters.RequestServiceFoldingCellRecyclerViewAdapter;
import com.example.tongan.myapplication.Classes.PostService;
import com.example.tongan.myapplication.Classes.RequestService;
import com.example.tongan.myapplication.Classes.User;
import com.example.tongan.myapplication.Helper.DatabaseHelper;
import com.example.tongan.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements PostServiceFoldingCellRecyclerViewAdapter.OnFoldingCellListener {

    private static final String TAG = "ProfileFragment";
    final User user = new User();
    private ArrayList<PostService> postServicesAL = new ArrayList<PostService>();
    private ArrayList<RequestService> requestServicesAL = new ArrayList<RequestService>();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DatabaseHelper databaseHelper = new DatabaseHelper();
    private DocumentReference documentReference;
    private ImageView settingsImage;
    private CircleImageView profileImage;
    private TextView profileName;
    private TextView profileFollowers;
    private TextView profileRating;
    private TextView documentationText;
    private TextView postServiceText;
    private TextView requestServiceText;
    private Button ordersBtn;
    private String email = databaseHelper.getCurrentUserEmail();
    private ArrayList<String> postNumbers = new ArrayList<>();
    private ArrayList<String> requestNumbers = new ArrayList<>();
    private RecyclerView documentationsRecyclerView;
    private RecyclerView postServiceFoldingCellRecyclerView;
    private RecyclerView requestServiceFoldingCellRecyclerView;
    private String userDisplayName;
    private String userFollowers;
    private String userProfileImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        settingsImage = view.findViewById(R.id.edit_profile_settings);
        profileImage = view.findViewById(R.id.profile_image);

        profileName = view.findViewById(R.id.profile_name);
        profileFollowers = view.findViewById(R.id.profile_followers);
        profileRating = view.findViewById(R.id.profile_rating);
        documentationText = view.findViewById(R.id.documentationsText);
        //postServiceText = view.findViewById(R.id.post_service);
        requestServiceText = view.findViewById(R.id.request_service);
        ordersBtn = view.findViewById(R.id.ordersBtn);

        // documentations recycler view
        documentationsRecyclerView = view.findViewById(R.id.profileDisplayDocumentations);
        documentationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        // folding cell recycler view
        //postServiceFoldingCellRecyclerView = view.findViewById(R.id.postServiceFoldingCellRecyclerView);
        requestServiceFoldingCellRecyclerView = view.findViewById(R.id.requestServiceFoldingCellRecyclerView);
        //postServiceFoldingCellRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        requestServiceFoldingCellRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Activity activity = getActivity();

        if (isAdded() && activity != null) {
            loadProfileInfoFromDatabase();
            loadDocumentationsFromDatabase();
            //loadPostServiceInfoFromDatabase();
            loadRequestServiceInfoFromDatabase();

            settingsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ProfileSettingsActivity.class);
                    startActivity(intent);
                }
            });

            ordersBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), OrderActivity.class);
                    startActivity(intent);
                }
            });

            // post service expand/collapse button
//            NOT GOING TO IMPLEMENT FOR NOW
//            postServiceText.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (postServiceFoldingCellRecyclerView.getVisibility() == View.VISIBLE) {
//                        postServiceFoldingCellRecyclerView.setVisibility(View.GONE);
//                        postServiceText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.expand, 0);
//                    } else {
//                        postServiceFoldingCellRecyclerView.setVisibility(View.VISIBLE);
//                        postServiceText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.collapse, 0);
//                    }
//                }
//            });

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
                }
            });
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() == null) {
            return;
        }
        Log.d(TAG, "ProfileFragment Page");
    }

    private void initRecyclerView(ArrayList<String> images) {
        HorizontalDocumentationsRecyclerViewAdapter adapter = new HorizontalDocumentationsRecyclerViewAdapter(getActivity(), images);
        documentationsRecyclerView.setAdapter(adapter);
    }


    // can be remove in future?
    private void loadProfileInfoFromDatabase() {
        // get profile info from database and update UI
        documentReference = firebaseFirestore.collection("Users").document(email);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    Map<String, Object> map = documentSnapshot.getData();
                    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
                    userDisplayName = map.get("displayName").toString();
                    userFollowers = numberFormat.format(map.get("follower"));

                    DecimalFormat value = new DecimalFormat("0.0");

                    profileName.setText(userDisplayName);
                    profileFollowers.setText(userFollowers);
                    profileRating.setText(value.format(map.get("rating")));

                    if (map.get("profileImage") == null) {
                        profileImage.setImageDrawable(getResources().getDrawable(R.drawable.settings_profile_picture));
                    } else {
                        userProfileImage = map.get("profileImage").toString();
                        Glide.with(getActivity()).load(userProfileImage).into(profileImage);
                    }

//                    if (null != map.get("postNumbers")) {
//                        for (String postNumber : (ArrayList<String>) map.get("postNumbers")) {
//                            postNumbers.add(postNumber);
//                        }
//
//                        if (postNumbers.isEmpty()) {
//                            postServiceText.setVisibility(View.GONE);
//                        } else {
//                            postServiceText.setVisibility(View.VISIBLE);
//                        }
//                    }

                    if (null != map.get("requestNumbers")) {
                        for (String requestNumber : (ArrayList<String>) map.get("requestNumbers")) {
                            requestNumbers.add(requestNumber);
                        }

                        if (requestNumbers.isEmpty()) {
                            requestServiceText.setVisibility(View.GONE);
                        } else {
                            requestServiceText.setVisibility(View.VISIBLE);
                        }
                    }

                    user.setDisplayName(userDisplayName);
                    user.setFollower(Integer.parseInt(userFollowers));
                    user.setRating(Double.parseDouble(value.format(map.get("rating"))));
                    user.setProfileImage(userProfileImage);
                    user.setEmail(email);
                }
            }
        });
    }

    private void loadDocumentationsFromDatabase() {
        documentReference = firebaseFirestore.collection("Users").document(email);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    Map<String, Object> map = documentSnapshot.getData();
                    if (map.get("images") != null) {
                        ArrayList<String> images = new ArrayList<>();
                        images.addAll((ArrayList<String>) map.get("images"));
                        initRecyclerView(images);
                    } else {
                        documentationsRecyclerView.setVisibility(View.GONE);
                        documentationText.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    // display Post Services if have any
    private void loadPostServiceInfoFromDatabase() {
        //databaseHelper.loadPostServiceInfoFromDatabase(postServiceText, postServiceFoldingCellRecyclerView, postNumbers, getActivity());
        //final ArrayList<User> userAl = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("PostServices").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {

                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        for (String postNumber : postNumbers) {
                            if (queryDocumentSnapshot.getId().equals(postNumber)) {
                                PostService postService = new PostService();
                                Map<String, Object> map = queryDocumentSnapshot.getData();
                                DecimalFormat df = new DecimalFormat("#.00");

                                postService.setId(map.get("id").toString());
//                                    postService.setpublisherEmail(email);
                                postService.setServiceTitle(map.get("serviceTitle").toString());
                                postService.setAddress(map.get("address").toString());
                                postService.setDescription(map.get("description").toString());
                                postService.setPrice(Double.parseDouble(df.format(map.get("price"))));
                                postService.setPublishTime(map.get("publishTime").toString());
                                postService.setPublisherEmail(map.get("publisherEmail").toString());

                                postServicesAL.add(postService);
                                //userAl.add(user); // make sure we have enough user object for each service, or else FoldingCellRecylerViewAdapter will fail. Will need to remodify later.

                                //Toast.makeText( getContext(), "PostNumber Found: " + queryDocumentSnapshot.getId(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    // displaying service info
                    //serviceIndicator.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
                    PostServiceFoldingCellRecyclerViewAdapter adapter = new PostServiceFoldingCellRecyclerViewAdapter(getActivity(), postServicesAL);
                    postServiceFoldingCellRecyclerView.setAdapter(adapter);
                }

            }
        });
    }

    // display Requested Services if have any
    private void loadRequestServiceInfoFromDatabase() {
        //final ArrayList<User> userAl = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("RequestServices").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        for (String requestNumber : requestNumbers) {
                            if (queryDocumentSnapshot.getId().equals(requestNumber)) {
                                RequestService requestService = new RequestService();
                                Map<String, Object> map = queryDocumentSnapshot.getData();
                                DecimalFormat df = new DecimalFormat("#.00");

                                requestService.setId(map.get("id").toString());
//                                    requestService.setpublisherEmail(email);
                                requestService.setServiceTitle(map.get("serviceTitle").toString());
                                requestService.setAddress(map.get("address").toString());
                                requestService.setDescription(map.get("description").toString());
                                requestService.setPrice(Double.parseDouble(df.format(map.get("price"))));
                                requestService.setPublishTime(map.get("publishTime").toString());
                                requestService.setPublisherEmail(map.get("publisherEmail").toString());
                                requestService.setMaxAcceptor(Integer.parseInt(map.get("maxAcceptor").toString()));

                                requestServicesAL.add(requestService);
                                //userAl.add(user); // make sure we have enough user object for each service, or else FoldingCellRecylerViewAdapter will fail. Will need to remodify later.

                                //Toast.makeText( getContext(), "PostNumber Found: " + queryDocumentSnapshot.getId(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    // displaying service info
                    //serviceIndicator.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
                    RequestServiceFoldingCellRecyclerViewAdapter adapter = new RequestServiceFoldingCellRecyclerViewAdapter(getActivity(), requestServicesAL);
                    requestServiceFoldingCellRecyclerView.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onFoldingCellClick(int position) {
        Log.d(TAG, "Position: " + position);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}