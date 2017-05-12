package fsm.kone.com.konefsm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by chris on 4/10/17.
 */

public class TechnicianFragmentSurvey extends Fragment {

    private String productName;
    private TrainingController mController;
    private View characterView;
    private TextView titleTxt;
    private TextView btn1Txt;
    private TextView btn2Txt;
    private TextView btn3Txt;

    public static TechnicianFragmentSurvey getInstance(String productName) {
        TechnicianFragmentSurvey fragment = new TechnicianFragmentSurvey();
        Bundle args = new Bundle();
        args.putString("productName", productName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.technician_survey, null);
        mController = TrainingController.getInstance((AppCompatActivity) getActivity());
        characterView = (ImageView) rootView.findViewById(R.id.technicianImg);

        Button finishedBtn = (Button) rootView.findViewById(R.id.submitBtn);
        finishedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validation
                // submit results
                TrainingResult result = new TrainingResult();
                result.productName = "FSM";
                result.role = "technician";
                result.timestamp = System.currentTimeMillis();
                if (mController.publishTrainingResults(result)) {
                    getActivity().getSupportFragmentManager().popBackStack();
                    mController.showView(TrainingController.VIEW_MAP);
                    view.setEnabled(false);
                }
            }
        });

        return rootView;
    }
}
