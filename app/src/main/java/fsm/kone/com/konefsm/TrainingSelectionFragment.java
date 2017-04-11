package fsm.kone.com.konefsm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by chris on 4/10/17.
 */

public class TrainingSelectionFragment extends Fragment {
    private static final String TAG = "training_selection";

    public static TrainingSelectionFragment getInstance() {
        return new TrainingSelectionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.training_choice, null);

        Log.d(TAG, "created view for Training Selection");
        final TrainingController controller = TrainingController.getInstance((AppCompatActivity)getActivity());

        ImageButton technicianBtn = (ImageButton) rootView.findViewById(R.id.technicialButton);
        ImageButton supervisorBtn = (ImageButton) rootView.findViewById(R.id.supervisorButton);

        technicianBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "clicked technician choice");
                controller.beginTechnician();
            }
        });

        supervisorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "clicked supervisor choice");
                controller.beginSupervisor();
            }
        });

        return rootView;
    }
}
