package com.example.android.baking.UI;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.baking.R;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.data.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;


//fragment to load a selected step detail
public class RecipeStepFragment extends Fragment {
    private static final String LOGTAG = "RecipeStepFragment";

//INTERFASE=========================================================================================

    public interface OnChangeStepListener {
        void OnChangeStep(int position);
    }

//GLOBAL VARIABLES==================================================================================

    public static final String ARG_ITEM = "item_type";
    private OnChangeStepListener mListener;
    int position;
    private boolean mTwoPane;

    private String mItem;
    private Recipe currentRecipe;
    private Step currentStep;
    private View rootView;
    SimpleExoPlayer mExoPlayer;
    Uri mVideoUri;

    //referece layout objects using Butteknife
    @BindView(R.id.item_detail) TextView mTextView;
    @BindView(R.id.playerView) SimpleExoPlayerView mPlayerView;
    @BindView(R.id.my_toolbar) Toolbar mToolBar;
//CONSTRUCTOR=======================================================================================

    public RecipeStepFragment() {
    }

//ON CREATE=========================================================================================
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        if (getArguments().containsKey(ARG_ITEM)) {
            mItem = getArguments().getString(ARG_ITEM);
            currentRecipe = getArguments().getParcelable("currentRecipe");
            currentStep = getArguments().getParcelable("currentStep");
            mTwoPane = getArguments().getBoolean("twoPane");
        }
    }

//ON CREATE VIEW====================================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);
        ButterKnife.bind(this,rootView);
        try {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolBar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            if (!mTwoPane) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
        }catch(NullPointerException e){
            Log.v(LOGTAG,"Action bar error");
        }
        setHasOptionsMenu(true);
        if(mToolBar != null) {
            mToolBar.setTitle(mItem);
            mToolBar.setTitleTextColor(getResources().getColor(R.color.title_bar_text_color));
        }
        return rootView;
    }


//ON ATTACH=========================================================================================
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeStepFragment.OnChangeStepListener) {
            mListener = (RecipeStepFragment.OnChangeStepListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    private void setStep() {
        position = currentStep.getId();
        mTextView.setText(currentStep.getDescription());
        mVideoUri = Uri.parse(currentStep.getVideoUrl());
        mItem = currentStep.getShortDescription();
        if(mToolBar != null) {
            mToolBar.setTitle(mItem);
            mToolBar.setTitleTextColor(getResources().getColor(R.color.title_bar_text_color));
        }
    }

//MENU==============================================================================================
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_detail, menu);
        if(position == (currentRecipe.getmSteps().size() - 1)){
            menu.getItem(1).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.next_step_button:
                    if (position < (currentRecipe.getmSteps().size() - 1)) {
                        position++;
                        mListener.OnChangeStep(position);
                    } else {
                        item.setVisible(false);
                    }

                return true;
            case R.id.previous_step_button:
                if(position >= 0 ){
                    position--;
                }else {
                    setStep();
                    Toast toast = Toast.makeText(getContext(),"No more steps",Toast.LENGTH_SHORT);
                    toast.show();
                }
                mListener.OnChangeStep(position);
                return true;
            case android.R.id.home:
                Intent intent = new Intent( getActivity(),RecipeListActivity.class);
                intent.putExtra("currentRecipe",currentRecipe);
                NavUtils.navigateUpTo(getActivity(), intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            setStep();
            initializePlayer(mVideoUri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mPlayerView == null)) {
            setStep();
            initializePlayer(mVideoUri);
        }
    }

    private void initializePlayer(Uri mediaUri) {
        if(!Uri.EMPTY.equals(mediaUri) && mediaUri != null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mPlayerView.getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("ExoPlayerDemo");
            ExtractorsFactory extractor = new DefaultExtractorsFactory();

            MediaSource videoSource = new ExtractorMediaSource(mediaUri,
                    dataSourceFactory, extractor, null, null);
            mExoPlayer.prepare(videoSource);

            mExoPlayer.setPlayWhenReady(true);
        }else{
            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                    (getResources(), R.drawable.exo_controls_play));
                Toast toast = Toast.makeText(getContext(), "No Video found", Toast.LENGTH_SHORT);
                toast.show();
        }
    }

    // Release ExoPlayer.

    private void releasePlayer() {
        if(mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    // Release ExoPlayer.
    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

