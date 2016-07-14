package info.mschmitt.video;

import android.app.Application;

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

    public void inject(MainActivity mainActivity) {
        MainActivityComponent mainActivityComponent =
                new MainActivityComponent(component, mainActivity);
        mainActivityComponent.inject(mainActivity);
    }

    public interface Component {
        MediaPlayerViewModel.Router router();
    }
}
