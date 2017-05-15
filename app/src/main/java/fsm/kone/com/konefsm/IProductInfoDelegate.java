package fsm.kone.com.konefsm;

import java.util.HashMap;

/**
 * Created by chris on 4/21/17.
 */

public interface IProductInfoDelegate {
    void onProductLoaded(HashMap productInfo);
    void onRoleLoaded(HashMap roleInfo);
}
