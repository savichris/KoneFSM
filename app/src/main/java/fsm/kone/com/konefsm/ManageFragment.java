package fsm.kone.com.konefsm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by chris on 4/10/17.
 */

public class ManageFragment extends Fragment {

    private FirebaseAuth auth;
    private ILoginDelegate delegate;

    public static ManageFragment getInstance() {
        return new ManageFragment();
    }
    public void setLoginDelegate(ILoginDelegate loginDelegate) {
        delegate = loginDelegate;
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
                delegate.startLogout();
            }
        });
        return rootView;
    }


}
