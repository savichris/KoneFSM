package fsm.kone.com.konefsm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static fsm.kone.com.konefsm.TrainingController.VIEW_CURRENT;
import static fsm.kone.com.konefsm.TrainingController.VIEW_FAQ;
import static fsm.kone.com.konefsm.TrainingController.VIEW_MANAGE;
import static fsm.kone.com.konefsm.TrainingController.VIEW_MAP;
import static fsm.kone.com.konefsm.TrainingController.VIEW_TRAINING;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FirebaseAuth.AuthStateListener {

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "mainactivity";
    public static final int REQUEST_LOCATION = 1234;

    private TrainingController mController;

    private NavigationView navigationView;
    private boolean instanceStateSaved = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mController = TrainingController.getInstance(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(this);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        instanceStateSaved = false;
        if (needsLocationUpdate) {
            needsLocationUpdate = false;
            mController.updateLocation();
        }
    }

    private void updateForAuthState(FirebaseAuth auth) {
        Log.d(TAG, "update for auth state: " + (auth.getCurrentUser() == null ? "no user" : auth.getCurrentUser().getUid()));
        View headerView = navigationView.getHeaderView(0);
        TextView uEmail = (TextView) headerView.findViewById(R.id.userEmail);
        TextView uName = (TextView) headerView.findViewById(R.id.userName);
        if (uEmail != null && uName != null) {

            String name = "";
            String email = "";
            if (auth.getCurrentUser() != null) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "user signed in: " + user.getUid());

                if (user != null) {
                    // Name, email address, and profile photo Url
                    name = user.getDisplayName();
                    email = user.getEmail();
                }
                mController.showView(TrainingController.VIEW_CURRENT);
            } else {
                Log.d(TAG, "no user signed in");
                // not signed in
                startActivityForResult(
                        // Get an instance of AuthUI based on the default app
                        AuthUI.getInstance().createSignInIntentBuilder()
                                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                                .setLogo(R.drawable.kone_logo_full)
                                .setTheme(R.style.AppTheme)
                                .build(),
                        RC_SIGN_IN);
            }
            uEmail.setText(email);
            uName.setText(name);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.removeAuthStateListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        instanceStateSaved = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        int selectedView = VIEW_CURRENT;
        if (id == R.id.nav_train) {
            selectedView = VIEW_TRAINING;
        } else if (id == R.id.nav_map) {
            selectedView = VIEW_MAP;
        } else if (id == R.id.nav_faq) {
            selectedView = VIEW_FAQ;
        } else if (id == R.id.nav_manage) {
            selectedView = VIEW_MANAGE;
        }
        mController.showView(selectedView);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                // update UI with current user info
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Log.d(TAG, "cancelled login");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Log.d(TAG, "failed login: no network");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.d(TAG, "failed login: unknown eror");
                    return;
                }
            }

            Log.d(TAG, "unknown login response");
        }
    }

    private boolean needsLocationUpdate = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                needsLocationUpdate = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
        if (auth != null) {
            if (!instanceStateSaved)
                updateForAuthState(auth);
        } else {
            Log.d(TAG, "no auth object: failed to update");
        }
    }
}
