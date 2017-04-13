package fsm.kone.com.konefsm;

import android.annotation.TargetApi;
import android.transition.ChangeBounds;
import android.transition.TransitionSet;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;

/**
 * Created by chris on 4/12/17.
 */

@TargetApi(21)
public class CharacterTransform extends TransitionSet {

    public CharacterTransform() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds())
                    .addTransition(new ChangeTransform())
                    .addTransition(new ChangeImageTransform());
    }
}
