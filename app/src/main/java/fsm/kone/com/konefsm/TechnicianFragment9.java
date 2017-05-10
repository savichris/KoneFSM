package fsm.kone.com.konefsm;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

public class TechnicianFragment9 extends Fragment {

    private String productName;
    private TrainingController mController;
    private View characterView;

    public static TechnicianFragment9 getInstance(String productName) {
        TechnicianFragment9 fragment = new TechnicianFragment9();
        Bundle args = new Bundle();
        args.putString("productName", productName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.technician_training_9, null);
        mController = TrainingController.getInstance((AppCompatActivity) getActivity());

        characterView = (ImageView) rootView.findViewById(R.id.technicianImg);

        final TextView customerHeader = (TextView) rootView.findViewById(R.id.technician_a10_customer);
        final TextView customerTxt1 = (TextView) rootView.findViewById(R.id.technician_a10_customer_txt1);
        final TextView customerTxt2 = (TextView) rootView.findViewById(R.id.technician_a10_customer_txt2);
        customerTxt1.setVisibility(View.INVISIBLE);
        customerTxt2.setVisibility(View.INVISIBLE);

        final TextView koneUserHeader = (TextView) rootView.findViewById(R.id.technician_a10_koneUser);
        final TextView koneUserTxt1 = (TextView) rootView.findViewById(R.id.technician_a10_kone_txt1);
        final TextView koneUserTxt2 = (TextView) rootView.findViewById(R.id.technician_a10_kone_txt2);
        final TextView koneUserTxt3 = (TextView) rootView.findViewById(R.id.technician_a10_kone_txt3);
        koneUserTxt1.setVisibility(View.INVISIBLE);
        koneUserTxt2.setVisibility(View.INVISIBLE);
        koneUserTxt3.setVisibility(View.INVISIBLE);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toggleGoalText(koneUserTxt1.getId());
                toggleGoalText(koneUserTxt2.getId());
                toggleGoalText(koneUserTxt3.getId());
            }
        }, 750);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toggleGoalText(customerTxt1.getId());
                toggleGoalText(customerTxt2.getId());
            }
        }, 1500);

        ImageButton nextBtn = (ImageButton) rootView.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customerTxt1.getVisibility() == View.VISIBLE && koneUserTxt1.getVisibility() == View.VISIBLE) {
                    view.animate().translationX(view.getX()+rootView.getWidth())
                            .setDuration(750)
                            .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "animate button to next finished");
                            mController.advanceTraining("technician", 10, characterView);

                        }
                    }).start();
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
