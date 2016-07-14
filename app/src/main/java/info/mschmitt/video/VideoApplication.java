package info.mschmitt.video;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import info.mschmitt.video.mediaplayer.MediaPlayerViewModel;

/**
 * @author Matthias Schmitt
 */
public class VideoApplication extends Application {
    public static VideoApplication instance;
    public Component component = new VideoApplicationComponent(this);

    public VideoApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public void inject(MainActivity mainActivity) {
        MainActivityComponent mainActivityComponent =
                new MainActivityComponent(component, mainActivity);
        mainActivityComponent.inject(mainActivity);
    }

    public interface Component {
        MediaPlayerViewModel.Router router();
    }
}
