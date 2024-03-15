package com.example.the_tarlords.data.users;

import android.util.Log;

import com.example.the_tarlords.data.Alert.AlertList;
import com.example.the_tarlords.data.event.Event;

import java.io.IOException;
import java.util.List;

public class Admin extends User implements AdminPerms {

    public Admin(String userId, String firstName, String lastName, String phoneNum, String email) throws IOException {
        super(userId, firstName, lastName, phoneNum, email);
    }

    @Override
    boolean isAdmin() {
        return true;
    }

}