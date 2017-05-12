package fsm.kone.com.konefsm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import static com.twitter.sdk.android.core.TwitterCore.TAG;

/**
 * Created by chris on 4/10/17.
 */

public class TechnicianFragment4 extends Fragment {

    private String productName;
    private TrainingController mController;
    private View characterView;

    public static TechnicianFragment4 getInstance(String productName) {
        TechnicianFragment4 fragment = new TechnicianFragment4();
        Bundle args = new Bundle();
        args.putString("productName", productName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.technician_training_4, null);
        mController = TrainingController.getInstance((AppCompatActivity) getActivity());

        characterView = (ImageView) rootView.findViewById(R.id.technicianImg);

        final TextView cause1Txt = (TextView) rootView.findViewById(R.id.technician_cause1_txt);
        final TextView mitigation1Txt = (TextView) rootView.findViewById(R.id.mitigation1_txt);
        mitigation1Txt.setVisibility(View.INVISIBLE);

        final TextView cause2Txt = (TextView) rootView.findViewById(R.id.technician_cause2_txt);
        final TextView mitigation2Txt = (TextView) rootView.findViewById(R.id.mitigation2_txt);
        mitigation2Txt.setVisibility(View.INVISIBLE);

        final TextView cause3Txt = (TextView) rootView.findViewById(R.id.technician_cause3_txt);
        final TextView mitigation3Txt = (TextView) rootView.findViewById(R.id.mitigation3_txt);
        mitigation3Txt.setVisibility(View.INVISIBLE);

        final TextView cause4Txt = (TextView) rootView.findViewById(R.id.technician_cause4_txt);
        final TextView mitigation4Txt = (TextView) rootView.findViewById(R.id.mitigation4_txt);
        mitigation4Txt.setVisibility(View.INVISIBLE);


        View.OnClickListener causeClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                switch (id) {
                    case R.id.technician_cause1_txt:
                        toggleGoalText(mitigation1Txt.getId());
                        break;
                    case R.id.technician_cause2_txt:
                        toggleGoalText(mitigation2Txt.getId());
                        break;
                    case R.id.technician_cause3_txt:
                        toggleGoalText(mitigation3Txt.getId());
                        break;
                    case R.id.technician_cause4_txt:
                        toggleGoalText(mitigation4Txt.getId());
                        break;
                    default:
                        break;
                }
            }
        };

        cause1Txt.setOnClickListener(causeClickListener);
        cause2Txt.setOnClickListener(causeClickListener);
        cause3Txt.setOnClickListener(causeClickListener);
        cause4Txt.setOnClickListener(causeClickListener);

        ImageButton nextBtn = (ImageButton) rootView.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mitigation1Txt.getVisibility() == View.VISIBLE &&
                        mitigation2Txt.getVisibility() == View.VISIBLE &&
                        mitigation3Txt.getVisibility() == View.VISIBLE &&
                        mitigation4Txt.getVisibility() == View.VISIBLE) {
                    view.animate().translationX(view.getX()+rootView.getWidth())
                            .setDuration(750)
                            .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "animate button to next finished");
                            mController.advanceTraining("technician", 5, characterView);

                        }
                    }).start();
                } else {
                    Snackbar.make(view, R.string.read_all, Snackbar.LENGTH_SHORT).show();
                }
//                TrainingResult result = new TrainingResult();
//                result.productName = "FSM";
//                result.role = "technician";
//                result.timestamp = System.currentTimeMillis();
//                if (mController.publishTrainingResults(result)) {
//                    getActivity().getSupportFragmentManager().popBackStack();
//                    mController.showView(TrainingController.VIEW_MAP);
//                    view.setEnabled(false);
//                }
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
