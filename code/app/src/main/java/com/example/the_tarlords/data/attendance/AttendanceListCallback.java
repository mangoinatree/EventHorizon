package com.example.the_tarlords.data.attendance;

import com.example.the_tarlords.data.users.Attendee;

import java.util.ArrayList;

/**
 * Interface for classes that implement callbacks
 */

public interface AttendanceListCallback {
    void onAttendanceLoaded(ArrayList<Attendee> attendanceList);
}
