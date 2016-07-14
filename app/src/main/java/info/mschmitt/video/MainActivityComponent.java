package info.mschmitt.video;

/**
 * @author Matthias Schmitt
 */
public class MainActivityComponent implements MainActivity.Component {
    private VideoApplication.Component parentComponent;

    public MainActivityComponent(VideoApplication.Component parentComponent,
                                 MainActivity activity) {

        this.parentComponent = parentComponent;
    }

    public void inject(MainActivity activity) {
        activity.applicationComponent = parentComponent;
    }
}
