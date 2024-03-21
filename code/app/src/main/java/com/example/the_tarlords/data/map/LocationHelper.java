package com.example.the_tarlords.data.map;

import static com.example.the_tarlords.MainActivity.db;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.the_tarlords.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;

public class LocationHelper {
    private Context context;
    private String eventId;
    private static final int REQUEST_LOCATION_PERMISSION = 123; // You can choose any unique value
    private FusedLocationProviderClient client;

    // Constructor to initialize the context
    public LocationHelper(Context context) {
        this.context = context;
        client = LocationServices.getFusedLocationProviderClient(context); // Initialize FusedLocationProviderClient
    }

    public void getMyLocation(String eventId) {
        this.eventId = eventId;
        if (checkLocationPermission()) {
            // Permission already granted, proceed to get location
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(android.location.Location location) {
                    if (location != null) {
                        Double latitude = location.getLatitude();
                        Double longitude = location.getLongitude();
                        checkInLocation(latitude, longitude, eventId);
                    } else {
                        Log.d("maps", "users location during check-in was null");
                    }
                }
            });
        }
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getMyLocation(eventId);
            } else {
                Log.d("maps", "location permissions denied");
            }
        }
    }

    public void checkInLocation(Double lat, Double lon, String eventId) {
        java.util.Map<String, Object> checkInData = new HashMap<>();
        // Assuming user is a member variable of MainActivity, adjust this accordingly if it's not
        if (MainActivity.user != null) {
            checkInData.put("name", MainActivity.user.getFirstName());
        } else {
            checkInData.put("name", "attendee");
        }
        checkInData.put("latitude", lat);
        checkInData.put("longitude", lon);

        db.collection("Events").document(eventId).collection("checkIns")
                .add(checkInData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("maps", "Check-in added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("maps", "Error adding check-in", e);
                    }
                });
    }
}

