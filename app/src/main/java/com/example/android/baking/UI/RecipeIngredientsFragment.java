package com.example.android.baking.UI;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.baking.R;
import com.example.android.baking.data.Ingredient;
import com.example.android.baking.data.Recipe;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private String mItem;

    private Recipe currentRecipe;
    private View rootView;
    private boolean mTwoPane;


    //referece layout objects using Butteknife
    @BindView(R.id.item_detail) TextView mTextView;
    @BindView(R.id.playerView) SimpleExoPlayerView mPlayerView;
    @BindView(R.id.my_toolbar) Toolbar mToolBar;


//CONSTRUCTOR=======================================================================================

    public RecipeIngredientsFragment() {
    }

//ON CREATE=========================================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        if (getArguments().containsKey(ARG_ITEM)) {
            mItem = getArguments().getString(ARG_ITEM);
            currentRecipe = getArguments().getParcelable("currentRecipe");
            mTwoPane = getArguments().getBoolean("twoPane");
        }

    }

//ON CREATE VIEW====================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);
        ButterKnife.bind(this,rootView);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolBar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(!mTwoPane) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        if(mToolBar != null) {
            mToolBar.setTitle(mItem);
            mToolBar.setTitleTextColor(getResources().getColor(R.color.title_bar_text_color));
        }
        setHasOptionsMenu(true);
        return rootView;
    }



//ON ATTACH=========================================================================================


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeIngredientsFragment.OnChangeIngredientsListener) {
            mListener = (RecipeIngredientsFragment.OnChangeIngredientsListener) context;
            if(mToolBar != null) {
                mToolBar.setTitle(mItem);
                mToolBar.setTitleTextColor(getResources().getColor(R.color.title_bar_text_color));
            }
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

//MENU==============================================================================================


    /**
     * Initialize the contents of the Fragment host's standard options menu.  You
     * should place your menu items in to <var>menu</var>.  For this method
     * to be called, you must have first called {@link #setHasOptionsMenu}.  See
     * {@link Activity#onCreateOptionsMenu(Menu) Activity.onCreateOptionsMenu}
     * for more information.
     *
     * @param menu     The options menu in which you place your items.
     * @param inflater
     * @see #setHasOptionsMenu
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_detail, menu);
        //hide the previousButton
       // MenuItem previousButton = menu.getItem(1);
       // previousButton.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.next_step_button:
                mListener.OnChangeIngredient(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.previous_step_button);
        item.setVisible(false);
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
       // mPreviousButton.setVisibility(View.GONE);
        ((TextView) rootView.findViewById(R.id.item_detail)).setText(ingredientList);
    }

/*
    //Use interface method OnChangeStep to change to next fragment
    @OnClick(R.id.next_step_button)
    public void next_step() {
        position = 0;
        mListener.OnChangeIngredient(position);
    }
*/

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
