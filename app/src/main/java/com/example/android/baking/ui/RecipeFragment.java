package com.example.android.baking.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.baking.R;
import com.example.android.baking.data.BakingUtils;
import com.example.android.baking.data.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//Fragment to show a list of recipes as cards in a recyclerview load in Main Activity
public class RecipeFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Recipe>> {

    public static final int MOVIE_LOADER_ID = 1;
    public static final String BAKING_DATA_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static final String LOGTAG = "RecipeFragment";
    private int mColumnCount;
    private OnListFragmentInteractionListener mListener;
    private Context mContext;
    private RecipeRecyclerViewAdapter mAdapter;
    private LoaderManager loaderManager;
    private boolean isConnected;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.empty_view)
    TextView mEmptyStateTextView;
    @BindView(R.id.loading_indicator)
    ProgressBar mLoadingIndicator;


    // Mandatory empty constructor for the fragment manager to instantiate the
    public RecipeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getBaseContext();
        mColumnCount = BakingUtils.calculateNoOfColumns(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_steps_list, container, false);
        ButterKnife.bind(this, view);

        List<Recipe> recipes = null;
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        mAdapter = new RecipeRecyclerViewAdapter(recipes, mListener, mContext);

        // Set the adapter chose layout depending in column count
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, mColumnCount));
        }
        recyclerView.setAdapter(mAdapter);

        //Check for connectivity
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
               mLoadingIndicator.setVisibility(View.GONE);
               mEmptyStateTextView.setText(R.string.no_connection);
        }else {
            loaderManager = getLoaderManager();
        }
        // Check for savedInstanceState key recipes, to avoid re load data from internet
        if (savedInstanceState == null || !savedInstanceState.containsKey("recipes")) {
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
            mLoadingIndicator.setVisibility(View.GONE);
        } else {
            recipes = savedInstanceState.getParcelableArrayList("recipes");
            mAdapter.swapData(recipes);
            mLoadingIndicator.setVisibility(View.GONE);
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

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        return new BakingLoader(mContext, BAKING_DATA_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
        if(data != null) {
            mAdapter.swapData(data);
        }else{
            Log.v(LOGTAG,"No data....");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {
        mAdapter.swapData(null);
    }

    //Fragment interface
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Recipe item);
    }
}
