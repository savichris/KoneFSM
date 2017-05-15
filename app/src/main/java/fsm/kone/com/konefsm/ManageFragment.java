package fsm.kone.com.konefsm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

/**
 * Created by chris on 4/10/17.
 */

public class ManageFragment extends Fragment {

    private FirebaseAuth auth;
    private ILoginDelegate delegate;
    private static final String TAG = "training_selection";

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
        View rootView = inflater.inflate(R.layout.product_choice, null);

        auth = FirebaseAuth.getInstance();
        Button logoutBtn = (Button) rootView.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.startLogout();
            }
        });
        Log.d(TAG, "created view for Training Selection");


        Button beginBtn = (Button) rootView.findViewById(R.id.continueBtn);
        TextView instructionsTxt = (TextView) rootView.findViewById(R.id.instructionsTxt);
        final Spinner choicesSpinner = (Spinner) rootView.findViewById(R.id.productSelector);
        final TrainingController controller = TrainingController.getInstance((AppCompatActivity) getActivity());
        HashMap<String, Object> productInfo = controller.getProductInfo();
        boolean foundSelectedProduct = false;
        if (productInfo != null) {
            String name = (String) productInfo.get("name");
            instructionsTxt.setText("You have a session started with " + name);
            instructionsTxt.append("\nPlease choose below to start with an alternate product");
            int count = choicesSpinner.getAdapter().getCount();
            for (int x = 0; x < count; x++) {
                String item = (String) choicesSpinner.getAdapter().getItem(x);
                if (item.equals(name)) {
                    choicesSpinner.setSelection(x);
                    foundSelectedProduct = true;
                    break;
                }
            }
        }
        if (!foundSelectedProduct) {
            choicesSpinner.setSelection(0);
        }

        beginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "clicked continue");
                if (getActivity().isDestroyed() || getActivity().isFinishing()) {
                    Log.d(TAG, "click technician ignored, bad activity state");
                    return;
                }
                String chosenProduct = (String) choicesSpinner.getSelectedItem();
                Log.d(TAG, "selected product: " + chosenProduct);
                controller.showView(TrainingController.VIEW_TRAINING);
                controller.loadProductInfo(chosenProduct);
            }
        });
        return rootView;
    }


}
