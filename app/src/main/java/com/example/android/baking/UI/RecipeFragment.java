package com.example.android.baking.UI;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.baking.R;
import com.example.android.baking.data.BakingUtils;
import com.example.android.baking.data.Recipe;

import java.util.List;

//Fragment to show a list of recipes as cards in a recyclerview load in Main Activity
public class RecipeFragment extends Fragment  {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount;
    private OnListFragmentInteractionListener mListener;

    // Mandatory empty constructor for the fragment manager to instantiate the
    public RecipeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mColumnCount = BakingUtils.calculateNoOfColumns(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_steps_list, container, false);

        //Load JSON into list of recipe
        List<Recipe> recipes = BakingUtils.fetchRecipeData(getContext());
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        // Set the adapter chose layout depending in column count
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new RecipeRecyclerViewAdapter(recipes, mListener,getContext()));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //Fragment interface
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Recipe item);
    }
}
