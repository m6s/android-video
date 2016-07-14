package info.mschmitt.video;

import info.mschmitt.video.mediaplayer.MediaPlayerViewModel;

/**
 * @author Matthias Schmitt
 */
public class VideoApplicationComponent implements VideoApplication.Component {
    private Router router;

    public VideoApplicationComponent(VideoApplication videoApplication) {
        router = new Router();
    }

    public void inject(VideoApplication application) {
        application.component = this;
    }

    @Override
    public Router router() {
        return router;
    }
}
