package com.example.android.baking.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import butterknife.OnClick;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */


public class RecipeStepFragment extends Fragment {

//INTERFASE=========================================================================================

    public interface OnChangeStepListener {
        void OnChangeStep(int position);
    }

//GLOBAL VARIABLES==================================================================================

    public static final String ARG_ITEM = "item_type";
    private static final String LOG_TAG = "RecipeStepFragment";
    private OnChangeStepListener mListener;
    int position;
    private boolean isIngredients = false;


    private String mItem;
    private Recipe currentRecipe;
    private Step currentStep;
    private View rootView;
    SimpleExoPlayer mExoPlayer;
    Uri mVideoUri;

    //referece layout objects using Butteknife
    @BindView(R.id.item_detail) TextView mTextView;
    @BindView(R.id.playerView) SimpleExoPlayerView mPlayerView;
    @BindView(R.id.next_step_button) Button mNextButton;
    @BindView(R.id.previous_step_button) Button mPreviousButton;


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
        }

    }

//ON CREATE VIEW====================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.item_detail, container, false);
        ButterKnife.bind(this,rootView);
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
        if(position == (currentRecipe.getmSteps().size()-1)){
            mNextButton.setVisibility(View.GONE);
        }
    }

    //Use interface method OnChangeStep to change to next fragment
    @OnClick(R.id.next_step_button)
    public void next_step() {

        if (position == -1) {
            position = 0;
        }else{
            if (position < (currentRecipe.getmSteps().size() - 1)) {
                position++;
                mListener.OnChangeStep(position);
            } else {
                Toast toast = Toast.makeText(getContext(), "No more steps", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
       // Toast toast = Toast.makeText(getContext(),"currentStep "+position,Toast.LENGTH_SHORT);
       // toast.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.next_step_button:
                if (position == -1) {
                    position = 0;
                }else{
                    if (position < (currentRecipe.getmSteps().size() - 1)) {
                        position++;
                        mListener.OnChangeStep(position);
                    } else {
                        Toast toast = Toast.makeText(getContext(), "No more steps", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                return true;
            // Respond to a click on the "Delete all entries" menu option
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
        }
        return super.onOptionsItemSelected(item);
    }
    @OnClick(R.id.previous_step_button)
    public void previous_step() {
        if(position >= 0 ){
            position--;
        }else {
            setStep();
            Toast toast = Toast.makeText(getContext(),"No more steps",Toast.LENGTH_SHORT);
            toast.show();
        }
        mListener.OnChangeStep(position);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            //  Log.v("onStart",""+currentStep.getId());
      //      ScrollView scrollView = (ScrollView) rootView.findViewById(R.id.scrollView);
       //     scrollView.smoothScrollBy(0,0);
            setStep();
            initializePlayer(mVideoUri);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || mPlayerView == null)) {
            setStep();
            initializePlayer(mVideoUri);
        }
    }


    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


    private void initializePlayer(Uri mediaUri) {
        if(!Uri.EMPTY.equals(mediaUri) && mediaUri != null) {
           // mPlayerView.setVisibility(View.VISIBLE);
          //  Toast toast = Toast.makeText(getContext(),"initializePlayer:"+mediaUri.toString()+"|",Toast.LENGTH_SHORT);
           // toast.show();
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
                    //buildMediaSource(mediaUri);//new ExtractorMediaSource(mediaUri, dataSourceFactory, extractor, null, null);
            mExoPlayer.prepare(videoSource);

            mExoPlayer.setPlayWhenReady(true);
        }else{
          //  mPlayerView.setVisibility(View.GONE);
            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                    (getResources(), R.drawable.exo_controls_play));
                Toast toast = Toast.makeText(getContext(), "No Video found", Toast.LENGTH_SHORT);
                toast.show();
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if(mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
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

                /*
                    if (!thumbnailUrl.isEmpty()) {
                        Picasso
                                .with(getContext())
                                .load(thumbnailUrl)
                                .fit()
                                .into(mImageView);

                    } else {
                        Picasso
                                .with(getContext())
                                .load(R.drawable.exo_controls_play)
                                .fit()
                                .into(mImageView);
                    }
                    */
// }