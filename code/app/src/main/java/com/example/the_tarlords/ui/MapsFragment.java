package com.example.the_tarlords.ui;

import static com.example.the_tarlords.MainActivity.db;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;

import com.example.the_tarlords.R;
import com.example.the_tarlords.data.event.Event;
import com.example.the_tarlords.databinding.FragmentMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * The GoogleMaps for each event where
 * Organizers can see where each attendee checks in from
 */
public class MapsFragment extends Fragment implements MenuProvider {


    public static Event event;
    private String eventId;
    private FragmentMapsBinding binding;
    private Boolean hasParent=false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = getArguments().getParcelable("event");
            if(event != null){
                Log.d("maps", event.getId());
                eventId = event.getId();
            }else{
                Log.d("maps", "null event");
            }
        } else if (getParentFragment()!=null&&getParentFragment().getArguments()!=null){
            event = getParentFragment().getArguments().getParcelable("event");
            eventId = event.getId();
            hasParent=true;
        }

    }



    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            //call a method to add markers from locations stored in Firebase
            addMarkers(googleMap);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_maps, container, false);
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //MANDATORY: required for MenuProvider options menu
        if (!hasParent) {
            requireActivity().addMenuProvider(this);
        }
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

    }
    // TODO: not disabling the menu options, should not should any of them
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menu.clear();

        //link options menu xml
        //menuInflater.inflate(R.menu.options_menu, menu);

    }

    /**
     * Mandatory empty method because fragment
     * implement menu provider
     * @param menuItem the menu item that was selected
     * @return
     */
    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    /**
     * Fetches coordinates of check-ins from Firebase
     * Adds location markers of all the check-in attendees at that event
     * @param googleMap
     */

    public void addMarkers(GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(53.526,-113.5)));
        googleMap.setMinZoomPreference(5);
        // get one from firebase
        
         // Query Firestore to get documents from events collection where eventId matches
         Query eventQuery = db.collection("Events").whereEqualTo("id", eventId);
        eventQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    // For each document, get the checkIns subcollection
                    String eventId = documentSnapshot.getString("id");
                    if (eventId != null) {
                        db.collection("Events").document(eventId).collection("Attendance")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot checkInsSnapshots) {
                                        for (DocumentSnapshot checkInSnapshot : checkInsSnapshots) {
                                            // Get name , longitude and latitude fields
                                            Double longitude = checkInSnapshot.getDouble("longitude");
                                            Double latitude = checkInSnapshot.getDouble("latitude");
                                            String name = checkInSnapshot.getString("name");

                                            // Use longitude and latitude to create a marker
                                            if (longitude != null && latitude != null) {
                                                // Do something with longitude and latitude
                                                LatLng position = new LatLng(latitude, longitude);
                                                googleMap.addMarker(new MarkerOptions().position(position).title(name + "'s location"));
                                                googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                                                Log.d("CheckIn", "Longitude: " + longitude + ", Latitude: " + latitude);
                                            }
                                        }
                                    }
                                });
                    }
                }
            }
        });

    }


}