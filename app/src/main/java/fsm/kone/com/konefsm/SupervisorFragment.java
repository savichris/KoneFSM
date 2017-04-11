package fsm.kone.com.konefsm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by chris on 4/10/17.
 */

public class SupervisorFragment extends Fragment {

    private String productName;
    private TrainingController mController;

    public static SupervisorFragment getInstance(String productName) {
        SupervisorFragment fragment = new SupervisorFragment();
        Bundle args = new Bundle();
        args.putString("productName", productName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.supervisor_training, null);
        mController = TrainingController.getInstance((AppCompatActivity) getActivity());

        Button submitBtn = (Button) rootView.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                TrainingResult result = new TrainingResult();
                result.productName = "FSM";
                result.role = "supervisor";
                result.timestamp = System.currentTimeMillis();
                mController.publishTrainingResults(result);
            }
        });


        return rootView;
    }
}
