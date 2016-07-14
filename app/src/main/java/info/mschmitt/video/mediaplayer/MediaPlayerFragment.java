package info.mschmitt.video.mediaplayer;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import info.mschmitt.video.BR;
import info.mschmitt.video.commons.Factory;
import info.mschmitt.video.commons.NavigationBarUtils;
import info.mschmitt.video.databinding.MediaPlayerViewBinding;
import rx.Completable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @author Matthias Schmitt
 */

public class MediaPlayerFragment extends Fragment {
    // Makeshift retained instance state
    private static HashMap<UUID, MediaPlayerViewModel> viewModelMap = new HashMap<>();
    @Inject
    public Factory<MediaPlayerViewModel> viewModelFactory;
    public ObservableBoolean systemUIVisible = new ObservableBoolean(true);
    private UUID id = UUID.randomUUID();
    private MediaPlayerViewModel viewModel;
    private Subscription showSystemUISubscription;
    private Observable.OnPropertyChangedCallback onPropertyChangedCallback =
            new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable observable, int i) {
                    MediaPlayerViewBinding binding = DataBindingUtil.getBinding(getView());
                    if (binding != null) {
                        updateBoundValues(binding, i);
                    }
                }
            };

    public static MediaPlayerFragment newInstance() {
        return new MediaPlayerFragment();
    }

    private void updateBoundValues(MediaPlayerViewBinding binding, int i) {
        if (i == BR.controlsVisible || i == BR._all) {
            boolean controlsVisible = viewModel.controlsVisible;
            if (controlsVisible) {
                showSystemUI(binding.getRoot());
            } else {
                hideSystemUI(binding.getRoot());
            }
        }
    }

    private void showSystemUI(View view) {
        if (showSystemUISubscription != null) {
            showSystemUISubscription.unsubscribe();
        }
        if (NavigationBarUtils.hasNavigationBar(getActivity())) {
            showSystemUISubscription = Completable.complete()
                    .delay(200, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                    .subscribe(this::onShowSystemUIComplete);
        } else {
            onShowSystemUIComplete();
        }
        view.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }

    private void hideSystemUI(View view) {
        if (showSystemUISubscription != null) {
            showSystemUISubscription.unsubscribe();
        }
        systemUIVisible.set(false);
        view.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void onShowSystemUIComplete() {
        systemUIVisible.set(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        id = savedInstanceState == null ? UUID.randomUUID() :
                UUID.fromString(savedInstanceState.getString("ID"));
        viewModel = viewModelMap.get(id);
        if (viewModel == null) {
            viewModel = viewModelFactory.create();
            viewModelMap.put(id, viewModel);
        }
        MediaPlayerViewBinding binding = MediaPlayerViewBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setFragment(this);
        viewModel.addOnPropertyChangedCallback(onPropertyChangedCallback);
        updateBoundValues(binding, BR._all);

        View decorView = getActivity().getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(
                visibility -> viewModel.onSystemFullscreenChanged(
                        (visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) != 0));

        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("ID", id.toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.onResume();
    }

    @Override
    public void onPause() {
        viewModel.onPause();
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        viewModelMap.clear();
    }

    @Override
    public void onDestroyView() {
        viewModel.removeOnPropertyChangedCallback(onPropertyChangedCallback);
        super.onDestroyView();
    }
}
