package com.example.tongan.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tongan.myapplication.Activities.SettingsPage.ProfileSettingsActivity;
import com.example.tongan.myapplication.Adapters.FoldingCellRecyclerViewAdapter;
import com.example.tongan.myapplication.Adapters.HorizontalDocumentationsRecyclerViewAdapter;
import com.example.tongan.myapplication.Classes.PostService;
import com.example.tongan.myapplication.Classes.Service;
import com.example.tongan.myapplication.Helper.DatabaseHelper;
import com.example.tongan.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements FoldingCellRecyclerViewAdapter.OnFoldingCellListener {

    private static final String TAG = "ProfileFragment";

    private FirebaseAuth fireBaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DatabaseHelper databaseHelper = new DatabaseHelper();
    private DocumentReference documentReference;

    private ImageView settingsImage;
    private CircleImageView profileImage;

    private TextView profileName;
    private TextView profileFollowers;
    private TextView profileRating;
    private TextView documentationText;

    private String email = databaseHelper.getCurrentUserEmail();
    private ArrayList<String> postNumbers = new ArrayList<>();
    private ArrayList<String> requestNumbers = new ArrayList<>();

    private RecyclerView documentationsRecyclerView;
    private LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

    private RecyclerView foldingCellRecyclerView;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

    private String userDisplayName;
    private String userFollowers;
    private String userProfileImage;
    private String serviceTitle;
    private Double servicePrice;
    private String serviceAddress;
    private String serviceDescription;
    private String servicePublishTime;

    // for testing
    final ArrayList<String> nameAL = new ArrayList<>();
    //

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

        // documentations recycler view
        documentationsRecyclerView = view.findViewById(R.id.profileDisplayDocumentations);
        documentationsRecyclerView.setLayoutManager(layoutManager);

        // folding cell recylcler view
        foldingCellRecyclerView = view.findViewById(R.id.foldingCellRecyclerView);
        foldingCellRecyclerView.setLayoutManager(linearLayoutManager);

//        // foldingCell example
//        foldingCell = view.findViewById(R.id.folding_cell);
//
//        foldingCell.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                foldingCell.toggle(false);
//            }
//        });

        loadProfileInfoFromDatabase();
        loadDocumentationsFromDatabase();
        loadServiceInfoFromDatabase();

        settingsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileSettingsActivity.class);
                startActivity(intent);
            }
        });
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

                    if (null != map.get("postNumbers")) {
                        for (String postNumber : (ArrayList<String>) map.get("postNumbers")) {
                            postNumbers.add(postNumber);
                        }
                    }
                    if (null != map.get("requestNumbers")) {
                        for (String requestNumber : (ArrayList<String>) map.get("requestNumbers")) {
                            requestNumbers.add(requestNumber);
                        }
                    }
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

    private void loadServiceInfoFromDatabase() {
        final ArrayList<PostService> postServicesAL = new ArrayList<>();

        firebaseFirestore.collection("PostServices").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot: task.getResult()) {
                        for (String postNumber : postNumbers) {
                            if (queryDocumentSnapshot.getId().equals(postNumber)) {
                                PostService postService = new PostService();
                                Map<String, Object> map = queryDocumentSnapshot.getData();
                                DecimalFormat df = new DecimalFormat("#.00");

                                serviceTitle = map.get("serviceTitle").toString();
                                serviceAddress = map.get("address").toString();
                                serviceDescription = map.get("description").toString();
                                servicePrice = Double.parseDouble(df.format(map.get("price")));
                                servicePublishTime = map.get("publishTime").toString();

                                postService.setpublisherEmail(email);
                                postService.setServiceTitle(serviceTitle);
                                postService.setAddress(serviceAddress);
                                postService.setDescription(serviceDescription);
                                postService.setPrice(servicePrice);
                                postService.setPublishTime(servicePublishTime);

                                postServicesAL.add(postService);

                                // for testing
                                nameAL.add(userDisplayName);
                                //

                                Toast.makeText( getContext(), "PostNumber Found: " + queryDocumentSnapshot.getId(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }

                // displaying service info
                // nameAL is for current testing only
                FoldingCellRecyclerViewAdapter adapter = new FoldingCellRecyclerViewAdapter(getActivity(), nameAL);
                foldingCellRecyclerView.setAdapter(adapter);

            }
        });
    }

    @Override
    public void onFoldingCellClick(int position) {

    }


//        firebaseFirestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
//                        if (queryDocumentSnapshot.getId().equals(databaseHelper.getCurrentUserEmail())) {
//
//                            Map<String, Object> map = queryDocumentSnapshot.getData();
//                            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
//                            String followers = numberFormat.format(map.get("follower"));
//                            profileName.setText(map.get("displayName").toString());
//                            profileFollowers.setText(followers);
//
//                            DecimalFormat value = new DecimalFormat("0.0");
//                            profileRating.setText(value.format(map.get("rating")));
//                        }
//                    }
//                }
//            }
//        });

}
