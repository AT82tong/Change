package com.example.tongan.myapplication.Helper;

import com.example.tongan.myapplication.Classes.PostService;
import com.example.tongan.myapplication.Classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DatabaseHelper {

    private FirebaseAuth fireBaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    final User user = new User();
    final ArrayList<PostService> postServicesAL = new ArrayList<PostService>();


    public String getCurrentUserEmail() {
        return fireBaseAuth.getCurrentUser().getEmail();
    }

    public String formatedEmail(String email) {
        return email.substring(0, getCurrentUserEmail().indexOf(".com")).replaceAll("\\.", "/");
    }

//    public void loadPostServiceInfoFromDatabase(final TextView postServiceText, final RecyclerView postServiceFoldingCellRecyclerView, final ArrayList<String> postNumbers, final Activity activity) {
//        //final ArrayList<User> userAl = new ArrayList<>();
//        FirebaseFirestore.getInstance().collection("PostServices").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    if (task.getResult().size() == 0) {
//                        postServiceText.setVisibility(View.GONE);
//                        postServiceFoldingCellRecyclerView.setVisibility(View.GONE);
//                    } else {
//                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
//                            for (String postNumber : postNumbers) {
//                                if (queryDocumentSnapshot.getId().equals(postNumber)) {
//                                    PostService postService = new PostService();
//                                    Map<String, Object> map = queryDocumentSnapshot.getData();
//                                    DecimalFormat df = new DecimalFormat("#.00");
//
//                                    postService.setId(map.get("id").toString());
//                                    postService.setpublisherEmail(getCurrentUserEmail());
//                                    postService.setServiceTitle(map.get("serviceTitle").toString());
//                                    postService.setAddress(map.get("address").toString());
//                                    postService.setDescription(map.get("description").toString());
//                                    postService.setPrice(Double.parseDouble(df.format(map.get("price"))));
//                                    postService.setPublishTime(map.get("publishTime").toString());
//
//                                    postServicesAL.add(postService);
//                                    //userAl.add(user); // make sure we have enough user object for each service, or else FoldingCellRecylerViewAdapter will fail. Will need to remodify later.
//
//                                    //Toast.makeText( getContext(), "PostNumber Found: " + queryDocumentSnapshot.getId(), Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        }
//                        // displaying service info
//                        //serviceIndicator.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
//                        PostServiceFoldingCellRecyclerViewAdapter adapter = new PostServiceFoldingCellRecyclerViewAdapter(activity, user, postServicesAL);
//                        postServiceFoldingCellRecyclerView.setAdapter(adapter);
//                    }
//                }
//            }
//        });
//    }

//    public User getUserData (final String email) {
//        firebaseFirestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
//                        if (queryDocumentSnapshot.getId().equals(email)) {
//                            user.setFirstName(queryDocumentSnapshot.getData().get("firstName").toString());
//                            user.setLastName(queryDocumentSnapshot.getData().get("lastName").toString());
//                            user.setEmail(queryDocumentSnapshot.getData().get("email").toString());
//                            user.setPassword(queryDocumentSnapshot.getData().get("password").toString());
//                            break;
//                        }
//                    }
//                    Log.d("DB", "firstName: " + user.getFirstName());
//                    Log.d("DB", "lastName: " + user.getLastName());
//                    Log.d("DB", "email: " + user.getEmail());
//                    Log.d("DB", "password: " + user.getPassword());
//                }
//            }
//        });
//            return user;
//    }
//
//        public void getUserDataDB () {
//        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d(TAG, "document: " + document.getId() + " -> " + document.getData());
//                        Log.d(TAG, "document: " + document.getId() + " -> " + document.getData().get("firstName"));
//                    }
//                }
//            }
//        });
//
//    }
}
