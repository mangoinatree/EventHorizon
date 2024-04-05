package com.example.the_tarlords.data.Alert;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.the_tarlords.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MilestoneHelper {
    String id;
    public MilestoneHelper(String id){
        this.id = id;
    }
    public ArrayList<Alert> getMilestoneList(AlertCallback callback) {
        CollectionReference milestoneRef = MainActivity.db.collection("Events/" + id + "/milestones");
        ArrayList<Alert> milestoneList = new ArrayList<>();
        milestoneRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot milestoneDoc : task.getResult()) {
                        Milestone milestone = new Milestone(milestoneDoc.getString("title"), milestoneDoc.getString("message"), milestoneDoc.getString("count"));
                        //alert.setCurrentDateTime(alertDoc.getString("currentDateTime"));
                        milestone.setId(milestoneDoc.getId());
                        milestone.setCurrentDateTime(milestoneDoc.getString("currentDateTime"));
                        milestoneList.add(milestone);

                    }
                    callback.onAlertsLoaded(milestoneList);
                } else {
                    Log.d("firestore", "Error getting documents: ", task.getException());
                }
            }
        });
        // placeholder
        milestoneList.add(new Milestone("Milestone title","Milestone message","0"));
        milestoneList.add(new Milestone("Milestone title2", "Milestone message 2","1"));
        return milestoneList;
    }
    
}