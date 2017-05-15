package fsm.kone.com.konefsm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chris on 4/10/17.
 */

public class FaqFragment extends Fragment {
    private static final String TAG = "FAQ";


    public static FaqFragment getInstance() {
        return new FaqFragment();
    }

    public FaqFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.faq_main, null);
        Log.d(TAG, "created view for FAQ Frag");
        return rootView;
    }



}
