package com.example.the_tarlords.ui.attendance_page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.users.Attendee;

import java.util.ArrayList;

public class AttendanceArrayAdapter extends ArrayAdapter<Attendee> {
    ArrayList<Attendee> attendees;
    private LayoutInflater inflater;
    public AttendanceArrayAdapter(Context context, ArrayList<Attendee> attendees) {
        super(context, 0, attendees);
        this.attendees = attendees;
        inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        AttendanceArrayAdapter.ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_profile_list_item, parent, false);
            holder = new AttendanceArrayAdapter.ViewHolder();
            //getting the textviews
            holder.firstName = view.findViewById(R.id.firstNameTV);
            holder.lastName = view.findViewById(R.id.lastNameTV);
            holder.email = view.findViewById(R.id.emailTV);
            holder.phoneNum = view.findViewById(R.id.phoneNumberTV);
            holder.profilePic = view.findViewById(R.id.profile_photo_image_view);
            view.setTag(holder);
        } else {
            holder = (AttendanceArrayAdapter.ViewHolder) view.getTag();
        }

        Attendee attendee = attendees.get(position);
        //setting the textviews
        holder.firstName.setText(attendee.getFirstName());
        holder.lastName.setText(attendee.getLastName());
        holder.email.setText(attendee.getEmail());
        holder.phoneNum.setText(attendee.getPhoneNum());
        holder.profilePic.setImageBitmap(attendee.getProfilePhoto().getBitmap());

        return view;
    }

    /**
     * Add the text views you want to display here
     */
    static class ViewHolder {
        TextView firstName;
        TextView lastName;
        TextView email;
        TextView phoneNum;
        ImageView profilePic;
    }

    public int getItemCount() {
        if (attendees == null) {
            return 0;
        }
        return attendees.size();
    }

    public int getCheckInCount() {
        int checkInCount = 0;
        for (Attendee a: attendees) {
            if (a.getCheckInStatus() == true) {
                checkInCount += 1;
            }
        }
        return checkInCount;
    }
}