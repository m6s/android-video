package info.mschmitt.video;

import info.mschmitt.video.mediaplayer.MediaPlayerFragment;
import info.mschmitt.video.mediaplayer.MediaPlayerViewModel;

/**
 * @author Matthias Schmitt
 */
public class MediaPlayerFragmentComponent {
    private final VideoApplication.Component parentComponent;

    public MediaPlayerFragmentComponent(VideoApplication.Component parentComponent,
                                        MediaPlayerFragment fragment) {
        this.parentComponent = parentComponent;
    }

    public void inject(MediaPlayerFragment fragment) {
        fragment.viewModelFactory = () -> new MediaPlayerViewModel(parentComponent.router());
    }
}
