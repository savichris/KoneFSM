package fsm.kone.com.konefsm;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by chris on 4/10/17.
 */

class TrainingController {
    private static final String TAG = "TrainingController";

    public void setActivity(AppCompatActivity mHostActivity) {
        this.mHostActivity = mHostActivity;
    }

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
        Log.d(TAG, "created controller");
        this.mHostActivity = c;
        this.productName = productName;
        fragmentManager = c.getSupportFragmentManager();
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public static TrainingController getInstance(AppCompatActivity c) {
        if (sInstance == null) {
            sInstance = new TrainingController(c, "demo");
        } else {
            Log.d(TAG, "reusing instance");
            sInstance.setActivity(c);
            sInstance.fragmentManager = c.getSupportFragmentManager();
        }
        return sInstance;
    }

    public void beginSupervisor(View sharedView) {
        Log.d(TAG, "begin Supervisor");
        try {
            Fragment frag = SupervisorFragment.getInstance(productName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                frag.setSharedElementEnterTransition(new CharacterTransform());
                frag.setEnterTransition(new Fade());
                frag.setExitTransition(new Fade());
            }
            fragmentManager.beginTransaction()
                    .addSharedElement(sharedView, ViewCompat.getTransitionName(sharedView))
                    .replace(R.id.container, frag)
                    .addToBackStack("trainSupervisor").commit();
        } catch (IllegalStateException e) {
            Log.e("TrainingController", "failed to transition to supervisor training", e);
        }
    }

    public void beginTechnician(View sharedView) {
        Log.d(TAG, "begin Technician");
        try {
            Fragment frag = TechnicianFragment.getInstance(productName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                frag.setSharedElementEnterTransition(new CharacterTransform());
                frag.setEnterTransition(new Fade());
                frag.setExitTransition(new Fade());
            }
            fragmentManager.beginTransaction()
                    .replace(R.id.container, frag)
                    .addSharedElement(sharedView, ViewCompat.getTransitionName(sharedView))
                    .addToBackStack("trainTechnician").commit();
        } catch (IllegalStateException e) {
            Log.e("TrainingController", "failed to transition to technician training", e);
        }
    }

    private void beginTraining() {
        Log.d(TAG, "begin training selection");
        try {
            fragmentManager.beginTransaction().replace(R.id.container, TrainingSelectionFragment.getInstance()).addToBackStack("trainingChoice").commit();
        } catch (IllegalStateException e) {
            Log.e("TrainingController", "failed to update UI", e);
        }
    }

    private void beginMap() {
        Log.d(TAG, "begin map");
        try {
        fragmentManager.beginTransaction().replace(R.id.container, ResultsMapFragment.getInstance()).addToBackStack("resultMap").commit();
        } catch (IllegalStateException e) {
            Log.e("TrainingController", "failed to update UI", e);
        }
    }

    private void beginFaq() {
        Log.d(TAG, "begin faq");
        try {
        fragmentManager.beginTransaction().replace(R.id.container, FaqFragment.getInstance()).addToBackStack("faq").commit();
        } catch (IllegalStateException e) {
            Log.e("TrainingController", "failed to update UI", e);
        }
    }

    private void beginAdmin() {
        Log.d(TAG, "begin admin");
        try {
            fragmentManager.beginTransaction().replace(R.id.container, ManageFragment.getInstance()).addToBackStack("admin").commit();
        } catch (IllegalStateException e) {
            Log.e("TrainingController", "failed to update UI", e);
        }
    }

    public void showView(int viewType) {
        Log.d(TAG, "show view: " + viewType);
        assert(Thread.currentThread() == Looper.getMainLooper().getThread());
        if (mHostActivity.isDestroyed() || mHostActivity.isFinishing()) {
            Log.d(TAG, "activity is shutting down, abort");
            return;
        }
        if (currentView == viewType) {
            int currentFragType = -1;
            try {
                FragmentManager fragmentManager = mHostActivity.getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.container);
                if (fragment instanceof TrainingSelectionFragment) {
                    currentFragType = VIEW_TRAINING;
                } else if (fragment instanceof ManageFragment) {
                    currentFragType = VIEW_MANAGE;
                } else if (fragment instanceof FaqFragment) {
                    currentFragType = VIEW_FAQ;
                } else if (fragment instanceof ResultsMapFragment) {
                    currentFragType = VIEW_MAP;
                }
                if (currentFragType == currentView) {
                    Log.d(TAG, "don't show same view again");
                    return;
                }
            } catch (NullPointerException e) {
                Log.d(TAG, "unable to determin current fragment type");
            }
        }
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

    public boolean publishTrainingResults(TrainingResult results) {
        // Create new results at /user-results/$userid/$result-id and at
        // /results/$result-id simultaneously
        if (mAuth.getCurrentUser() != null) {
            Log.d(TAG, "publish results: for user=" + results.uid);
            results.email = mAuth.getCurrentUser().getEmail();
            results.username = mAuth.getCurrentUser().getDisplayName();
            results.uid = mAuth.getCurrentUser().getUid();
            getLastKnownLocation();
            if (lastKnownLocation == null) {
                String msg = "location not available";
                Log.e(TAG, msg);
                Toast.makeText(mHostActivity, msg, Toast.LENGTH_LONG).show();
            } else {
                results.latitude = lastKnownLocation.getLatitude();
                results.longitude = lastKnownLocation.getLongitude();
            }
            DatabaseReference reference = mDatabase.getReference();
            String key = reference.child("results").push().getKey();
            Map<String, Object> postValues = results.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/results/" + key, postValues);
            childUpdates.put("/user-results/" + mAuth.getCurrentUser().getUid() + "/" + key, key);

            reference.updateChildren(childUpdates);
            return true;
        }
        return false;
    }

    public boolean checkPermissions() {
        Log.d(TAG, "check location permission");
        if (ContextCompat.checkSelfPermission(mHostActivity,
                ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "asking for permission to use location");
            // Should we show an explanation? skip for now
            ActivityCompat.requestPermissions(mHostActivity,
                    new String[]{ACCESS_FINE_LOCATION},
                    MainActivity.REQUEST_LOCATION);
            return false;
        }
        return true;
    }

    public void updateLocation() {
        Log.d(TAG, "update location");
        LocationManager locationManager = (LocationManager) mHostActivity.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        try {
            String provider = locationManager.getBestProvider(criteria, true);
            if (provider == null) {
                Snackbar.make(mHostActivity.findViewById(R.id.container), "Please enable location services", Snackbar.LENGTH_INDEFINITE).show();
                return;
            }
            Iterator<String> providerIt = locationManager.getAllProviders().iterator();
            while (providerIt.hasNext()) {
                Log.d(TAG, "location provider: " + providerIt.next());
            }
            Log.d(TAG, "chosen loc provider: " + provider);
            lastKnownLocation = locationManager.getLastKnownLocation(provider);
            Log.d(TAG, "last known location:" + lastKnownLocation.getLatitude() + ", " + lastKnownLocation.getLongitude());
        } catch (SecurityException | IllegalArgumentException | NullPointerException e) {
            Log.e(TAG, "unable to get location", e);
        }
    }

    public Location getLastKnownLocation() {
        Log.d(TAG, "getting last known location");
        if (checkPermissions()) {
            updateLocation();
        }
        return lastKnownLocation;
    }
}
