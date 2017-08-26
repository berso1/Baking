package com.example.android.baking.UI;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.baking.R;
import com.example.android.baking.data.Ingredient;
import com.example.android.baking.data.Recipe;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */


public class RecipeIngredientsFragment extends Fragment {

//INTERFASE=========================================================================================

    public interface OnChangeIngredientsListener {
        void OnChangeIngredient(int position);
    }

//GLOBAL VARIABLES==================================================================================

    public static final String ARG_ITEM = "item_type";
    private static final String LOG_TAG = "RecipeStepFragment";
    private OnChangeIngredientsListener mListener;
    int position;


    private Recipe currentRecipe;
    private View rootView;

    //referece layout objects using Butteknife
    @BindView(R.id.item_detail) TextView mTextView;
    @BindView(R.id.playerView) SimpleExoPlayerView mPlayerView;
    @BindView(R.id.next_step_button) Button mNextButton;
    @BindView(R.id.previous_step_button) Button mPreviousButton;


//CONSTRUCTOR=======================================================================================

    public RecipeIngredientsFragment() {
    }

//ON CREATE=========================================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        if (getArguments().containsKey(ARG_ITEM)) {
            currentRecipe = getArguments().getParcelable("currentRecipe");
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
        if (context instanceof RecipeIngredientsFragment.OnChangeIngredientsListener) {
            mListener = (RecipeIngredientsFragment.OnChangeIngredientsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }



    private void setIngredients() {
        position=-1;
        List<Ingredient> ingredients = currentRecipe.getmIngredients();
        String ingredientList = "INGREDIENTS: \n\n";
        for(int i = 0; i< ingredients.size(); i++){
            Ingredient currentIngredient = ingredients.get(i);
            ingredientList = ingredientList+currentIngredient.getmIngredient()+"\n";
        }
        mPlayerView.setVisibility(View.GONE);
        mPreviousButton.setVisibility(View.GONE);
        ((TextView) rootView.findViewById(R.id.item_detail)).setText(ingredientList);
    }


    //Use interface method OnChangeStep to change to next fragment
    @OnClick(R.id.next_step_button)
    public void next_step() {
        position = 0;
        mListener.OnChangeIngredient(position);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            setIngredients();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mPlayerView == null)) {
            setIngredients();
        }
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
