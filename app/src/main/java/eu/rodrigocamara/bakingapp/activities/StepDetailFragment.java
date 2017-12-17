package eu.rodrigocamara.bakingapp.activities;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import eu.rodrigocamara.bakingapp.C;
import eu.rodrigocamara.bakingapp.R;
import eu.rodrigocamara.bakingapp.pojos.Recipe;
import eu.rodrigocamara.bakingapp.pojos.Recipe$$Parcelable;

import static com.google.android.exoplayer2.C.VIDEO_SCALING_MODE_SCALE_TO_FIT;
import static eu.rodrigocamara.bakingapp.C.VIDEO_POSITION;
import static eu.rodrigocamara.bakingapp.C.VIDEO_SHOULD_PLAY;

public class StepDetailFragment extends Fragment {

    private int mStep;
    private Recipe recipe;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;

    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private long videoPosition;
    private BandwidthMeter bandwidthMeter;
    int orientation;
    private View rootView;

    public StepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            shouldAutoPlay = savedInstanceState.getBoolean(VIDEO_SHOULD_PLAY);
            videoPosition = savedInstanceState.getLong(VIDEO_POSITION);
        }
        mStep = (getArguments().getInt(C.STEP, 0));
        recipe = Parcels.unwrap(getArguments().getParcelable((C.RECIPE)));
        orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.step_detail, container, false);

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            initializePlayer(rootView);

        } else {
            ((TextView) rootView.findViewById(R.id.step_detail)).setText(recipe.getSteps().get(mStep).getDescription());
            initializePlayer(rootView);

            if (mStep + 1 >= recipe.getNumberSteps()) {
                rootView.findViewById(R.id.btn_next).setVisibility(View.GONE);
            } else if (mStep == 0) {
                rootView.findViewById(R.id.btn_previous).setVisibility(View.GONE);
            }
            rootView.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeFragment(mStep + 1);
                }
            });
            rootView.findViewById(R.id.btn_previous).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeFragment(mStep - 1);
                }
            });
        }

        if (!recipe.getSteps().get(mStep).getThumbnailURL().isEmpty()) {
            Picasso.with(getContext()).load(recipe.getSteps().get(mStep).getThumbnailURL()).into((ImageView) rootView.findViewById(R.id.iv_thumbnail));
        }

        return rootView;
    }

    private void changeFragment(int step) {
        Fragment fragment = new StepDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(C.RECIPE, new Recipe$$Parcelable(recipe));
        arguments.putInt(C.STEP, step);
        fragment.setArguments(arguments);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.step_detail_container, fragment);

        fragmentTransaction.commit();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void initializePlayer(View view) {

        simpleExoPlayerView = view.findViewById(R.id.videoPlayer);
        simpleExoPlayerView.requestFocus();
        mediaDataSourceFactory = new DefaultDataSourceFactory(view.getContext(), Util.getUserAgent(view.getContext(), getString(R.string.app_name)), (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(view.getContext(), trackSelector);

        simpleExoPlayerView.setPlayer(player);

        player.setPlayWhenReady(shouldAutoPlay);
        player.seekTo(videoPosition);
        player.setVideoScalingMode(VIDEO_SCALING_MODE_SCALE_TO_FIT);
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        if (recipe.getSteps().get(mStep).getVideoURL().isEmpty()) {
            simpleExoPlayerView.setVisibility(View.GONE);
            view.findViewById(R.id.iv_thumbnail).setVisibility(View.VISIBLE);
            releasePlayer();
        } else {
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(recipe.getSteps().get(mStep).getVideoURL()),
                    mediaDataSourceFactory, extractorsFactory, null, null);
            player.prepare(mediaSource);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (player != null) {
            outState.putLong(VIDEO_POSITION, player.getContentPosition());
            outState.putBoolean(VIDEO_SHOULD_PLAY, player.getPlayWhenReady());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player == null) {
            initializePlayer(rootView);
        }
        if (player != null && shouldAutoPlay) {
            player.seekTo(videoPosition);
            player.setPlayWhenReady(shouldAutoPlay);
        }
    }


    private void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    @Override
    public void onDetach() {
        releasePlayer();
        super.onDetach();
    }

    @Override
    public void onStop() {
        releasePlayer();
        super.onStop();
    }
}
