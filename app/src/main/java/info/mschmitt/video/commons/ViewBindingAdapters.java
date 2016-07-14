package info.mschmitt.video.commons;

import android.databinding.BindingAdapter;
import android.view.View;

/**
 * @author Matthias Schmitt
 */
public class ViewBindingAdapters {
    @BindingAdapter("onTouch")
    public static void setOnTouch(View view, View.OnTouchListener listener) {
        view.setOnTouchListener(listener);
    }
}
