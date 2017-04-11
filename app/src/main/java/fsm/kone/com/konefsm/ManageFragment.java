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

public class ManageFragment extends Fragment {

    private FirebaseAuth auth;

    public static ManageFragment getInstance() {
        return new ManageFragment();
    }

    public ManageFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.manage, null);
        auth = FirebaseAuth.getInstance();
        Button logoutBtn = (Button) rootView.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auth != null) {
                    if (auth.getCurrentUser() != null) {
                        auth.signOut();
                    }
                }
            }
        });
        return rootView;
    }


}
