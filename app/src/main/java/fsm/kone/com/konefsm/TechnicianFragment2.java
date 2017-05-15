package fsm.kone.com.konefsm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by chris on 4/10/17.
 */

public class TechnicianFragment2 extends Fragment {

    private String productName;
    private TrainingController mController;
    private View characterView;

    public static TechnicianFragment2 getInstance(String productName) {
        TechnicianFragment2 fragment = new TechnicianFragment2();
        Bundle args = new Bundle();
        args.putString("productName", productName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.technician_training_2, null);
        mController = TrainingController.getInstance((AppCompatActivity) getActivity());

        characterView = (ImageView) rootView.findViewById(R.id.technicianImg);

        final ImageView goal1Btn = (ImageView) rootView.findViewById(R.id.technician_goal1_img);


        final TextView goal1Txt = (TextView) rootView.findViewById(R.id.technician_goal1_txt);


        goal1Txt.setVisibility(View.GONE);


        View.OnClickListener goalClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                switch (id) {
                    case R.id.technician_goal1_img:
                        toggleGoalText(goal1Txt.getId());
                        goal1Btn.animate().translationY(rootView.getHeight() - goal1Btn.getY())
                                .setDuration(1750)
                                .setInterpolator(new LinearInterpolator()).start();
                        break;

                    default:
                        break;
                }
            }
        };

        goal1Btn.setOnClickListener(goalClickListener);

        ImageButton nextBtn = (ImageButton) rootView.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (goal1Txt.getVisibility() == View.VISIBLE) {
                    mController.advanceTraining("technician", 3, characterView);
                } else {
                    Snackbar.make(view, R.string.read_all, Snackbar.LENGTH_SHORT).show();
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
