package fsm.kone.com.konefsm;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chris on 4/10/17.
 */

public class ResultsMapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "ResultsMapFrag";
    private static ResultsMapFragment sInstance;
    private GoogleMap googleMap;
    private Location lastKnownLocation;
    private TrainingController mController;

    public ResultsMapFragment() {
        super();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static ResultsMapFragment getInstance() {
        sInstance = new ResultsMapFragment();
        return sInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        Log.d(TAG, "created view");
        mController = TrainingController.getInstance((AppCompatActivity) getActivity());
        getMapAsync(this);
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;
        LatLng here = null;
        if (mController.checkPermissions()) {
            here = updateLocation();
        }
        googleMap.setOnMarkerClickListener(this);
        addMarkers();
    }

    private HashMap<String, Marker> markerTable = new HashMap<>();
    private List<TrainingResult> trainingResults = new ArrayList<>();
    private Handler mHandler;

    private void addMarkers() {
        trainingResults.clear();
        DatabaseReference resultsRef = FirebaseDatabase.getInstance().getReference("results");
        resultsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    TrainingResult message = messageSnapshot.getValue(TrainingResult.class);
                    trainingResults.add(message);
                }


                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                    synchronized (trainingResults) {
                        for (TrainingResult message : trainingResults) {
                            String snippet = "Completed " + message.productName + " " + message.role + " @ " + new Date(message.timestamp).toString();
                            LatLng loc = null;
                            try {
                                loc = new LatLng(message.getLatitude(), message.getLongitude());
                            } catch (NullPointerException e) {
                                Log.d(TAG, "result data with no location for user:" + message.username);
                            }
                            if (loc != null) {
                                final MarkerOptions markerOpt = new MarkerOptions().position(loc)
                                        .title(message.username).snippet(snippet).icon(BitmapDescriptorFactory.defaultMarker(colorForProductAndRole(message.getProductName(), message.getRole())));
                                Marker marker = googleMap.addMarker(markerOpt);
                                marker.setTag(message);
                                markerTable.put(message.uid, marker);
                            }
                        }
                    }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        resultsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final TrainingResult message = dataSnapshot.getValue(TrainingResult.class);
                synchronized (trainingResults) {
                    trainingResults.add(message);
                }
                String snippet = "Completed " + message.productName + " " + message.role + " @ " + new Date(message.timestamp).toString();
                LatLng loc = null;
                try {
                    loc = new LatLng(message.getLatitude(), message.getLongitude());
                } catch (NullPointerException e) {
                    Log.d(TAG, "new results, no location data");
                }
                if (loc != null) {
                    final MarkerOptions markerOpt = new MarkerOptions().position(loc)
                            .title(message.username).snippet(snippet).
                    icon(BitmapDescriptorFactory.defaultMarker(colorForProductAndRole(message.getProductName(), message.getRole())));

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Marker marker = googleMap.addMarker(markerOpt);
                            marker.setTag(message);
                            markerTable.put(message.uid, marker);
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private float colorForProductAndRole(String productName, String role) {
        if (productName.equalsIgnoreCase("fsm")) {
            if (role.equalsIgnoreCase("technician")) return BitmapDescriptorFactory.HUE_VIOLET;
            else if (role.equalsIgnoreCase("supervisor")) return BitmapDescriptorFactory.HUE_MAGENTA;
        }
        return BitmapDescriptorFactory.HUE_ORANGE;
    }

    public LatLng updateLocation() {
        LatLng here = null;
        lastKnownLocation = mController.getLastKnownLocation();
        if (lastKnownLocation != null) {
            here = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(here)      // Sets the center of the map to location user
                    .zoom(10)          // Sets the zoom
                    .bearing(0)        // Sets the orientation of the camera to north
                    .tilt(40)          // Sets the tilt of the camera to 40 degrees
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        return here;
    }

    public static ResultsMapFragment current() {
        return sInstance;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG, "clicked map marker:" + ((TrainingResult)marker.getTag()).getUsername());
        return false;
    }
}
