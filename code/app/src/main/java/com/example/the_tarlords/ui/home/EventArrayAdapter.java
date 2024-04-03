package com.example.the_tarlords.ui.home;

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
import com.example.the_tarlords.data.event.Event;

import java.util.ArrayList;

public class EventArrayAdapter extends ArrayAdapter<Event> {
    public EventArrayAdapter(@NonNull Context context,ArrayList<Event> events) {
        super(context, 0, events);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_event_list_item, parent, false);
        } else {
            view = convertView;
        }
        Event event = (Event) getItem(position);
        //this is currently breaking the app bc i don't think i'm parcelling in correctly
        //i have made lines in event that i think correspond to the correct items but i am unsure
        //so they are commented like this
        //ImageView image = view.findViewById(R.id.iv_event_icon);
        //image.setImageBitmap(event.getPoster().getBitmap());
        TextView name = view.findViewById(R.id.tv_event_title);
        name.setText(event.getName());
        TextView location = view.findViewById(R.id.tv_location);
        location.setText(event.getLocation());
        TextView startDate = view.findViewById(R.id.tv_start_date);
        startDate.setText(event.getStartDate());
        return view;
    }
}