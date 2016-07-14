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

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import info.mschmitt.video.BR;
import info.mschmitt.video.commons.DataBindingUtil2;
import info.mschmitt.video.commons.Factory;
import info.mschmitt.video.commons.NavigationBarUtils;
import info.mschmitt.video.databinding.MediaPlayerViewBinding;
import me.tatarka.retainstate.RetainState;
import me.tatarka.retainstate.fragment.RetainStateFragment;
import rx.Completable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @author Matthias Schmitt
 */

public class MediaPlayerFragment extends Fragment implements RetainState.Provider {
    @Inject
    public Factory<MediaPlayerViewModel> viewModelFactory;
    public ObservableBoolean systemUIVisible = new ObservableBoolean(true);
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
    private RetainState retainState;

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
        MediaPlayerViewBinding binding = MediaPlayerViewBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        return binding.getRoot();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        retainState = RetainStateFragment.from(this);
        viewModel = retainState.retain(0, () -> viewModelFactory.create());
        viewModel.addOnPropertyChangedCallback(onPropertyChangedCallback);
        MediaPlayerViewBinding binding = DataBindingUtil2.getBinding(getView());
        binding.setViewModel(viewModel);
        updateBoundValues(binding, BR._all);
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(
                visibility -> viewModel.onSystemFullscreenChanged(
                        (visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) != 0));
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        viewModel.removeOnPropertyChangedCallback(onPropertyChangedCallback);
        super.onDestroyView();
    }

    @Override
    public RetainState getRetainState() {
        if (retainState == null) {
            throw new IllegalStateException("RetainState has not yet been initialized");
        }
        return retainState;
    }
}
