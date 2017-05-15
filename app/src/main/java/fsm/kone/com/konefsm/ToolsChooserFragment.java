package fsm.kone.com.konefsm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chris on 5/15/17.
 */

public class ToolsChooserFragment extends Fragment {
    private static final String TAG = "ToolsChoice";

    public ToolsChooserFragment() {}

    public static ToolsChooserFragment getInstance() {
        return new ToolsChooserFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tools_choice, null);
        Log.d(TAG, "created view for Tool Selection");
        return rootView;
    }
}
