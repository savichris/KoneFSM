package fsm.kone.com.konefsm;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by chris on 4/10/17.
 */

class TrainingController {

    private AppCompatActivity mHostActivity;
    private String productName;
    private static TrainingController sInstance;
    FragmentManager fragmentManager;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private Location lastKnownLocation;

    public static final int VIEW_CURRENT = 0;
    public static final int VIEW_TRAINING = 1;
    public static final int VIEW_MAP = 2;
    public static final int VIEW_FAQ = 3;
    public static final int VIEW_MANAGE = 4;
    private int currentView = 1;

    private TrainingController(AppCompatActivity c, String productName) {
        this.mHostActivity = c;
        this.productName = productName;
        fragmentManager = c.getSupportFragmentManager();
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public static TrainingController getInstance(AppCompatActivity c) {
        if (sInstance == null) {
            sInstance = new TrainingController(c, "demo");
        }
        return sInstance;
    }

    public void beginSupervisor() {
        try {
            fragmentManager.beginTransaction().replace(R.id.container, SupervisorFragment.getInstance(productName)).addToBackStack("trainSupervisor").commit();
        } catch (IllegalStateException e) {
            Log.e("TrainingController", "failed to update UI", e);
        }
    }

    public void beginTechnician() {
        try {
            fragmentManager.beginTransaction().replace(R.id.container, TechnicianFragment.getInstance(productName)).addToBackStack("trainTechnician").commit();
        } catch (IllegalStateException e) {
            Log.e("TrainingController", "failed to update UI", e);
        }
    }

    private void beginTraining() {
        try {
            fragmentManager.beginTransaction().replace(R.id.container, TrainingSelectionFragment.getInstance()).addToBackStack("trainingChoice").commit();
        } catch (IllegalStateException e) {
            Log.e("TrainingController", "failed to update UI", e);
        }
    }

    private void beginMap() {
        try {
        fragmentManager.beginTransaction().replace(R.id.container, ResultsMapFragment.getInstance()).addToBackStack("resultMap").commit();
        } catch (IllegalStateException e) {
            Log.e("TrainingController", "failed to update UI", e);
        }
    }

    private void beginFaq() {
        try {
        fragmentManager.beginTransaction().replace(R.id.container, FaqFragment.getInstance()).addToBackStack("faq").commit();
        } catch (IllegalStateException e) {
            Log.e("TrainingController", "failed to update UI", e);
        }
    }

    private void beginAdmin() {
        try {
            fragmentManager.beginTransaction().replace(R.id.container, ManageFragment.getInstance()).addToBackStack("admin").commit();
        } catch (IllegalStateException e) {
            Log.e("TrainingController", "failed to update UI", e);
        }
    }

    public void showView(int viewType) {
        switch (viewType) {
            case VIEW_TRAINING:
                beginTraining();
                break;
            case VIEW_MANAGE:
                beginAdmin();
                break;
            case VIEW_FAQ:
                beginFaq();
                break;
            case VIEW_MAP:
                beginMap();
                break;
            case VIEW_CURRENT:
                showView(currentView);
                return; // don't update current view
            default:
                break;

        }
        currentView = viewType;
    }

    public void publishTrainingResults(TrainingResult results) {
        // Create new results at /user-results/$userid/$result-id and at
        // /results/$result-id simultaneously
        if (mAuth.getCurrentUser() != null) {
            results.email = mAuth.getCurrentUser().getEmail();
            results.username = mAuth.getCurrentUser().getDisplayName();
            getLastKnownLocation();
            results.latitude = lastKnownLocation.getLatitude();
            results.longitude = lastKnownLocation.getLongitude();
            DatabaseReference reference = mDatabase.getReference();
            String key = reference.child("results").push().getKey();
            results.uid = key;
            Map<String, Object> postValues = results.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/results/" + key, postValues);
            childUpdates.put("/user-results/" + mAuth.getCurrentUser().getUid() + "/" + key, key);

            reference.updateChildren(childUpdates);
        }
    }

    public boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(mHostActivity,
                ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation? skip for now
            ActivityCompat.requestPermissions(mHostActivity,
                    new String[]{ACCESS_FINE_LOCATION},
                    MainActivity.REQUEST_LOCATION);
            return false;
        }
        return true;
    }

    public void updateLocation() {
        LocationManager locationManager = (LocationManager) mHostActivity.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        try {
            lastKnownLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        } catch (SecurityException e) {

        }
    }

    public Location getLastKnownLocation() {
        if (checkPermissions()) {
            updateLocation();
        }
        return lastKnownLocation;
    }
}
