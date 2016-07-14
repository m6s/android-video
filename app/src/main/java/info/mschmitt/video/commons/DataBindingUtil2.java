package info.mschmitt.video.commons;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;

/**
 * @author Matthias Schmitt
 */

public class DataBindingUtil2 {
    public static <T extends ViewDataBinding> T getBinding(View view) {
        T binding = DataBindingUtil.getBinding(view);
        if (binding == null) {
            throw new AssertionError();
        }
        return binding;
    }
}
