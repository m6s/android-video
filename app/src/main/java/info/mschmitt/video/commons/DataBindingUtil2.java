package info.mschmitt.video.commons;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ViewDataBinding;
import android.view.View;

/**
 * @author Matthias Schmitt
 */

public class DataBindingUtil2 {
    /**
     * getBinding() is typically used where we know that we have a valid binding.
     */
    public static <T extends ViewDataBinding> T getBindingOrThrow(View view) {
        T binding = DataBindingUtil.getBinding(view);
        if (binding == null) {
            throw new AssertionError();
        }
        return binding;
    }

    /**
     * Support lambdas.
     */
    public static Observable.OnPropertyChangedCallback newCallback(
            OnPropertyChangedCallback callback) {
        return new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                callback.onPropertyChanged(i);
            }
        };
    }

    public interface OnPropertyChangedCallback {
        void onPropertyChanged(int i);
    }
}
