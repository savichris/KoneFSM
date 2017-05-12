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

public class TechnicianFragment7 extends Fragment {

    private String productName;
    private TrainingController mController;
    private View characterView;
    private TextView btn1Txt;
    private TextView btn2Txt;
    private TextView btn3Txt;
    private TextView btn4Txt;

    public static TechnicianFragment7 getInstance(String productName) {
        TechnicianFragment7 fragment = new TechnicianFragment7();
        Bundle args = new Bundle();
        args.putString("productName", productName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.technician_training_7, null);
        mController = TrainingController.getInstance((AppCompatActivity) getActivity());
        characterView = (ImageView) rootView.findViewById(R.id.technicianImg);
        btn1Txt = (TextView) rootView.findViewById(R.id.technician_a8_txt1);
        btn2Txt = (TextView) rootView.findViewById(R.id.technician_a8_txt2);
        btn3Txt = (TextView) rootView.findViewById(R.id.technician_a8_txt3);
        btn4Txt = (TextView) rootView.findViewById(R.id.technician_a8_txt4);
        btn1Txt.setVisibility(View.INVISIBLE);
        btn2Txt.setVisibility(View.INVISIBLE);
        btn3Txt.setVisibility(View.INVISIBLE);
        btn4Txt.setVisibility(View.INVISIBLE);

        final ImageButton thoughtBubble = (ImageButton) rootView.findViewById(R.id.thought_bubble);
        thoughtBubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reveal();
                view.setEnabled(false);
            }
        });

        ImageButton nextBtn = (ImageButton) rootView.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn1Txt.getVisibility() == View.VISIBLE) {
                    view.animate().translationX(view.getX() + rootView.getWidth())
                            .setDuration(750)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "animate button to next finished");
                                    mController.advanceTraining("technician", 8, characterView);

                                }
                            }).start();
                } else {
                    Snackbar.make(view, R.string.read_all, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private void reveal() {
        Log.d("tech7", "reveal");
        View[] views = {btn1Txt, btn2Txt, btn3Txt, btn4Txt};
        boolean visible = btn1Txt.getVisibility() == View.VISIBLE;
        if (!visible) {
            // reveal it
            for (View v: views) {
                v.setAlpha(0f);
                v.setVisibility(View.VISIBLE);
                v.setX(v.getX()-v.getWidth());
                v.animate().alpha(1f).translationXBy(v.getWidth()).setDuration(500).start();
            }
        }

    }
}
