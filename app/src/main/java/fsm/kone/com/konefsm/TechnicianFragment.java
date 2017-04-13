package fsm.kone.com.konefsm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by chris on 4/10/17.
 */

public class TechnicianFragment extends Fragment {

    private String productName;
    private TrainingController mController;

    public static TechnicianFragment getInstance(String productName) {
        TechnicianFragment fragment = new TechnicianFragment();
        Bundle args = new Bundle();
        args.putString("productName", productName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.technician_training, null);
        mController = TrainingController.getInstance((AppCompatActivity) getActivity());

        ImageView goal1Btn = (ImageView) rootView.findViewById(R.id.technician_goal1_img);
        ImageView goal2Btn = (ImageView) rootView.findViewById(R.id.technician_goal2_img);
        ImageView goal3Btn = (ImageView) rootView.findViewById(R.id.technician_goal3_img);
        ImageView goal4Btn = (ImageView) rootView.findViewById(R.id.technician_goal4_img);

        final TextView goal1Txt = (TextView) rootView.findViewById(R.id.technician_goal1_txt);
        final TextView goal2Txt = (TextView) rootView.findViewById(R.id.technician_goal2_txt);
        final TextView goal3Txt = (TextView) rootView.findViewById(R.id.technician_goal3_txt);
        final TextView goal4Txt = (TextView) rootView.findViewById(R.id.technician_goal4_txt);

        goal1Txt.setVisibility(View.GONE);
        goal2Txt.setVisibility(View.GONE);
        goal3Txt.setVisibility(View.GONE);
        goal4Txt.setVisibility(View.GONE);

        View.OnClickListener goalClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                switch (id) {
                    case R.id.technician_goal1_img:
                        toggleGoalText(goal1Txt.getId());
                        break;
                    case R.id.technician_goal2_img:
                        toggleGoalText(goal2Txt.getId());
                        break;
                    case R.id.technician_goal3_img:
                        toggleGoalText(goal3Txt.getId());
                        break;
                    case R.id.technician_goal4_img:
                        toggleGoalText(goal4Txt.getId());
                        break;
                    default:
                        break;
                }
            }
        };

        goal1Btn.setOnClickListener(goalClickListener);
        goal2Btn.setOnClickListener(goalClickListener);
        goal3Btn.setOnClickListener(goalClickListener);
        goal4Btn.setOnClickListener(goalClickListener);

        ImageButton nextBtn = (ImageButton) rootView.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    private void toggleGoalText(int id) {
        final View v = getView().findViewById(id);
        boolean visible = v.getVisibility() == View.VISIBLE;
        if (visible) {
            // hide it
            v.animate().alpha(0f).setDuration(500).withEndAction(new Runnable() {
                @Override
                public void run() {
                    v.setVisibility(View.INVISIBLE);
                }
            }).start();

        } else {
            // reveal it
            v.setAlpha(0f);
            v.setVisibility(View.VISIBLE);
            v.animate().alpha(1f).setDuration(500).start();
        }
    }
}
