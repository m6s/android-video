package info.mschmitt.video;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import info.mschmitt.video.databinding.MainActivityViewBinding;
import info.mschmitt.video.mediaplayer.MediaPlayerFragment;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {
    public VideoApplication.Component applicationComponent;

    public MainActivity() {
        VideoApplication.instance.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivityViewBinding binding =
                DataBindingUtil.setContentView(this, R.layout.main_activity_view);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(binding.fragmentContainer.getId(), MediaPlayerFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        MediaPlayerFragment mediaPlayerFragment = (MediaPlayerFragment) fragment;
        MediaPlayerFragmentComponent fragmentComponent =
                new MediaPlayerFragmentComponent(applicationComponent, mediaPlayerFragment);
        fragmentComponent.inject(mediaPlayerFragment);
    }

    public interface Component {
    }
}
