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

import java.util.ArrayList;
import java.util.List;

import static com.twitter.sdk.android.core.TwitterCore.TAG;

/**
 * Created by chris on 4/10/17.
 */

public class TechnicianFragment5 extends Fragment {

    private String productName;
    private TrainingController mController;
    private View characterView;
    private TextView popupTxt;
    private List<Integer> clickedViewIds = new ArrayList<>();

    public static TechnicianFragment5 getInstance(String productName) {
        TechnicianFragment5 fragment = new TechnicianFragment5();
        Bundle args = new Bundle();
        args.putString("productName", productName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.technician_training_5, null);
        mController = TrainingController.getInstance((AppCompatActivity) getActivity());

        characterView = (ImageView) rootView.findViewById(R.id.technicianImg);

        final TextView btn1Txt = (TextView) rootView.findViewById(R.id.technician_a6_btn1);
        final TextView btn2Txt = (TextView) rootView.findViewById(R.id.technician_a6_btn2);
        final TextView btn3Txt = (TextView) rootView.findViewById(R.id.technician_a6_btn3);
        final TextView btn4Txt = (TextView) rootView.findViewById(R.id.technician_a6_btn4);
        popupTxt  = (TextView) rootView.findViewById(R.id.popup_text);
        popupTxt.setVisibility(View.INVISIBLE);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                clickedViewIds.add(new Integer(id));
                switch (id) {
                    case R.id.technician_a6_btn1:
                        reveal(R.string.technician_a6_popup1_txt);
                        break;
                    case R.id.technician_a6_btn2:
                        reveal(R.string.technician_a6_popup2_txt);
                        break;
                    case R.id.technician_a6_btn3:
                        reveal(R.string.technician_a6_popup3_txt);
                        break;
                    case R.id.technician_a6_btn4:
                        reveal(R.string.technician_a6_popup4_txt);
                        break;
                    default:
                        break;
                }
            }
        };

        btn1Txt.setOnClickListener(clickListener);
        btn2Txt.setOnClickListener(clickListener);
        btn3Txt.setOnClickListener(clickListener);
        btn4Txt.setOnClickListener(clickListener);

        ImageButton nextBtn = (ImageButton) rootView.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickedViewIds.contains(R.id.technician_a6_btn1) &&
                        clickedViewIds.contains(R.id.technician_a6_btn2) &&
                        clickedViewIds.contains(R.id.technician_a6_btn3) &&
                        clickedViewIds.contains(R.id.technician_a6_btn4)) {
                    view.animate().translationX(view.getX() + rootView.getWidth())
                            .setDuration(750)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "animate button to next finished");
                                    mController.advanceTraining("technician", 6, characterView);

                                }
                            }).start();
                } else {
                    Snackbar.make(view, R.string.read_all, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private void reveal(int id) {
        Log.d("tech5", "reveal" + getResources().getString(id));
        final View v = popupTxt;
        boolean visible = v.getVisibility() == View.VISIBLE;
        if (visible) {
            // hide it
            v.animate().alpha(0f).setDuration(500).withEndAction(new Runnable() {
                @Override
                public void run() {
                    v.setVisibility(View.INVISIBLE);
                }
            }).start();

        }
        popupTxt.setText(id);
        // reveal it
        v.setAlpha(0f);
        v.setVisibility(View.VISIBLE);
        v.animate().alpha(1f).setDuration(500).start();

    }
}
