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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.twitter.sdk.android.core.TwitterCore.TAG;

/**
 * Created by chris on 4/10/17.
 */

public class TechnicianFragment6 extends Fragment {

    private String productName;
    private TrainingController mController;
    private View characterView;
    private TextView popupTxt;
    private FrameLayout popin_container;
    private List<Integer> clickedViewIds = new ArrayList<>();


    public static TechnicianFragment6 getInstance(String productName) {
        TechnicianFragment6 fragment = new TechnicianFragment6();
        Bundle args = new Bundle();
        args.putString("productName", productName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.technician_training_6, null);
        mController = TrainingController.getInstance((AppCompatActivity) getActivity());

        characterView = (ImageView) rootView.findViewById(R.id.technicianImg);

        final TextView btn1Txt = (TextView) rootView.findViewById(R.id.technician_a7_btn1);
        final TextView btn2Txt = (TextView) rootView.findViewById(R.id.technician_a7_btn2);
        final TextView btn3Txt = (TextView) rootView.findViewById(R.id.technician_a7_btn3);
        popin_container = (FrameLayout) rootView.findViewById(R.id.popin_container);
        popupTxt = (TextView) popin_container.findViewById(R.id.results);

        popupTxt.setVisibility(View.INVISIBLE);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                clickedViewIds.add(new Integer(id));
                switch (id) {
                    case R.id.technician_a7_btn1:
                        reveal(R.string.technician_a7_popup1_txt);
                        break;
                    case R.id.technician_a7_btn2:
                        reveal(R.string.technician_a7_popup2_txt);
                        break;
                    case R.id.technician_a7_btn3:
                        reveal(R.string.technician_a7_popup3_txt);
                        break;
                    default:
                        break;
                }
            }
        };

        btn1Txt.setOnClickListener(clickListener);
        btn2Txt.setOnClickListener(clickListener);
        btn3Txt.setOnClickListener(clickListener);

        ImageButton nextBtn = (ImageButton) rootView.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickedViewIds.contains(R.id.technician_a7_btn1) &&
                        clickedViewIds.contains(R.id.technician_a7_btn2) &&
                        clickedViewIds.contains(R.id.technician_a7_btn3)) {
                    view.animate().translationX(view.getX() + rootView.getWidth())
                            .setDuration(750)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "animate button to next finished");
                                    mController.advanceTraining("technician", 7, characterView);

                                }
                            }).start();
                } else {
                    Snackbar.make(view, R.string.read_all, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private void reveal(final int id) {
        Log.d("tech5", "reveal" + getResources().getString(id));
        final View v = popupTxt;
        boolean visible = v.getVisibility() == View.VISIBLE;
        if (visible) {
            // hide it
            v.animate().alpha(0f).translationY(v.getY()+300).setDuration(500).scaleXBy(-0.5f).withEndAction(new Runnable() {
                @Override
                public void run() {
                    v.setVisibility(View.GONE);
                    reveal(id);
                }
            }).start();

        } else {
            popupTxt.setText(id);
            // reveal it
            v.setAlpha(0f);
            v.setY(0);
            v.setScaleX(1f);
            v.setVisibility(View.VISIBLE);
            v.animate().alpha(1f).translationY(v.getY()+300).setDuration(500).start();
        }

    }
}
