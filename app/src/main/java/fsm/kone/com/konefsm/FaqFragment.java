package fsm.kone.com.konefsm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chris on 4/10/17.
 */

public class FaqFragment extends Fragment {

    public static FaqFragment getInstance() {
        return new FaqFragment();
    }

    public FaqFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }



}
