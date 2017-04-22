package fsm.kone.com.konefsm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by chris on 4/10/17.
 */

public class SplashFragment extends Fragment {

    private FirebaseAuth auth;
    private ILoginDelegate loginDelegate;

    public static SplashFragment getInstance() {
        return new SplashFragment();
    }
    public void setLoginDelegate(ILoginDelegate delegate) {
        loginDelegate = delegate;
    }

    public SplashFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.splash, null);
        auth = FirebaseAuth.getInstance();
        Button logoutBtn = (Button) rootView.findViewById(R.id.loginBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginDelegate != null) {
                    loginDelegate.startLogin();
                }
            }
        });
        return rootView;
    }


}
