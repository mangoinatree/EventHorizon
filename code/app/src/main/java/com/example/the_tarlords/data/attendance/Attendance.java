package com.example.the_tarlords.data.attendance;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.the_tarlords.MainActivity;
import com.example.the_tarlords.data.Alert.AlertList;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.data.event.EventList;
import com.example.the_tarlords.data.users.Attendee;
import com.example.the_tarlords.data.users.Profile;
import com.example.the_tarlords.data.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface Attendance {       //could be made into interface

    /**
     * Returns a list of Attendee objects attending the event.
     *
     * @return list of User objects
     */
    public ArrayList<Attendee> getAttendanceList();

    /**
     * Signs up a user to attend an event by adding their name to the attendance list.
     *
     * @param user to add
     */
    public void signUp(User user);

    /**
     * Removes a user from the attendance list of an event.
     *
     * @param user to remove
     */
    public void removeSignUp(User user);

    /**
     * Checks in a user for an event by updating the checked in status for that event.
     * @param user to check in
     */
    public void checkIn(User user);

    /**
     * Removes user's checked in status for an event. Possibly unnecessary.
     * @param user to un check in
     */
    public void removeCheckIn(User user);
}