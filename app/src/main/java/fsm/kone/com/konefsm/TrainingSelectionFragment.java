package fsm.kone.com.konefsm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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


        ImageView technicianBtn = (ImageView) rootView.findViewById(R.id.technicianImg);
        ImageView supervisorBtn = (ImageView) rootView.findViewById(R.id.supervisorImg);

        technicianBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "clicked technician choice");
                if (getActivity().isDestroyed() || getActivity().isFinishing()) {
                    Log.d(TAG, "click technician ignored, bad activity state");
                    return;
                }
                TrainingController controller = TrainingController.getInstance((AppCompatActivity) getActivity());
                controller.beginTechnician(view);
            }
        });

        supervisorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "clicked supervisor choice");
                if (getActivity().isDestroyed() || getActivity().isFinishing()) {
                    Log.d(TAG, "click technician ignored, bad activity state");
                    return;
                }
                TrainingController controller = TrainingController.getInstance((AppCompatActivity) getActivity());
                controller.beginSupervisor(view);
            }
        });

        return rootView;
    }
}
