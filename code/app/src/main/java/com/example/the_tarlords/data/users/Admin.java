package com.example.the_tarlords.data.users;

import com.example.the_tarlords.data.Alert.AlertList;
import com.example.the_tarlords.data.event.Event;

import java.util.List;

public class Admin extends User implements AdminPerms {
    public Admin(Integer userId, Profile profile, List<Event> events, AlertList alerts) {
        super(userId, profile, events, alerts);
    }

    @Override
    boolean isAdmin() {
        return true;
    }
}