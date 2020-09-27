package com.example.bucketlist.model;

import com.google.firebase.firestore.FirebaseFirestore;

public interface ProfileFragmentPart {
    void initializeUi();
    void refreshFragment();
    void getDataFromFireStore(FirebaseFirestore fireStore);
}
