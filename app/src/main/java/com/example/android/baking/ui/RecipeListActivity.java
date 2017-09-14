package com.example.android.baking.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.baking.R;
import com.example.android.baking.data.Ingredient;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.data.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//Activity to load the step list or step list and step detail in w900dp
public class RecipeListActivity extends AppCompatActivity
        implements RecipeStepFragment.OnChangeStepListener,
                   RecipeIngredientsFragment.OnChangeIngredientsListener{

//GLOBAL VARIABLES==================================================================================
    public static final String INGREDIENTS_TITLE = "INGREDIENTS";
    private boolean mTwoPane;
    private Recipe currentRecipe;
    private List<Step> mSteps;
    private ActionBar actionBar;
    @BindView(R.id.list_toolbar) Toolbar mToolBar;


//ON CREATE=========================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        ButterKnife.bind(this);


        // Check for savedInstanceState currentRecipe
        if(savedInstanceState == null || !savedInstanceState.containsKey("currentRecipe")) {
            Intent intent = getIntent();
            currentRecipe = intent.getExtras().getParcelable("currentRecipe");
        }
        else {
            currentRecipe = savedInstanceState.getParcelable("currentRecipe");
        }

        List<Ingredient> ingredients = currentRecipe.getmIngredients();
        mSteps = currentRecipe.getmSteps();
        List<String> mList = setList(ingredients, mSteps);

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView, mList);

        if (findViewById(R.id.recipe_detail_fragment_container) != null) {
            mTwoPane = true;
        }

        actionBar = getSupportActionBar();
        // Show the Up button in the action bar.
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


//HELPER METHODS====================================================================================
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("currentRecipe", currentRecipe);
        super.onSaveInstanceState(outState);
    }

    private List<String> setList(List<Ingredient> ingredients, List<Step> steps) {
        List<String> list = new ArrayList<String>(){};
        if(ingredients.size() > 0){
            list.add(INGREDIENTS_TITLE);
        }
        for(int i = 0; i < steps.size() ; i++){
            final Step thisStep = steps.get(i);
            list.add(thisStep.getShortDescription());
        }
        return list;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView,List<String> mList) {
        recyclerView.setAdapter(new RecipeStepsRecyclerViewAdapter(mList));
    }


//FRAGMENTS INTERFACES==============================================================================
    @Override
    public void OnChangeStep(int position) {
        if(position == -1){
            OnChangeIngredient(position);
            return;
        }
        Step nextStep = null;
        if(position < currentRecipe.getmSteps().size()){
            nextStep = currentRecipe.getmSteps().get(position);
        }

        Bundle arguments = new Bundle();
        arguments.putString(RecipeStepFragment.ARG_ITEM,
                getIntent().getStringExtra(RecipeStepFragment.ARG_ITEM));
        arguments.putParcelable("currentRecipe",currentRecipe);
        arguments.putParcelable("currentStep",nextStep);
        RecipeStepFragment nextFragment = new RecipeStepFragment();
        nextFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_detail_fragment_container, nextFragment)
                .commit();
    }

    @Override
    public void OnChangeIngredient(int position) {
        if (position == 0) {
            OnChangeStep(position);
            return;
        }
        String mItem = INGREDIENTS_TITLE;
        if (position > -1){
            Step currentStep = currentRecipe.getmSteps().get(position);
            mItem = currentStep.getShortDescription();
        }
        RecipeIngredientsFragment ingredientsFragment = new RecipeIngredientsFragment();
        Bundle arguments = new Bundle();
        arguments.putString(RecipeStepFragment.ARG_ITEM, mItem);
        arguments.putParcelable("currentRecipe", currentRecipe);
        ingredientsFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_detail_fragment_container, ingredientsFragment)
                .commit();
    }

//RECYCLER VIEW ADAPTER=============================================================================

    public class RecipeStepsRecyclerViewAdapter
            extends RecyclerView.Adapter<RecipeStepsRecyclerViewAdapter.ViewHolder> {

        private final List<String> mList;

        public RecipeStepsRecyclerViewAdapter(List<String> list) {
            mList = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_list_item, parent, false);
            setSupportActionBar(mToolBar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            mToolBar.setTitle(currentRecipe.getmName());
            return new ViewHolder(view);

        }

        @SuppressWarnings("deprecation")
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final int mPosition = position;
            Step step = null;
            if(position>0) {
                step = mSteps.get(position - 1);
                String imageUrl = step.getThumbNailUrl();
                if(imageUrl.equals("")){
                    holder.mStepImage.setImageResource(R.drawable.ic_cake);
                }else {
                    Picasso.with(getApplicationContext()).load(imageUrl)
                            .placeholder(getApplicationContext().getResources().getDrawable(R.drawable.ic_cake))
                            .error(getApplicationContext().getResources().getDrawable(R.drawable.ic_cake))
                            .into(holder.mStepImage);
                }
            }
            final Step currentStep = step;

            holder.mStepTextView.setText(mList.get(position));
            holder.mStepTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(RecipeStepFragment.ARG_ITEM, mList.get(mPosition));
                        arguments.putParcelable("currentRecipe",currentRecipe);
                        arguments.putParcelable("currentStep",currentStep);
                        arguments.putBoolean("twoPane",mTwoPane);
                        if(mPosition == 0){
                            RecipeIngredientsFragment ingredientsFragment = new RecipeIngredientsFragment();
                            ingredientsFragment.setArguments(arguments);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.recipe_detail_fragment_container, ingredientsFragment)
                                    .commit();
                        }else{
                            RecipeStepFragment stepFragment = new RecipeStepFragment();
                            stepFragment.setArguments(arguments);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.recipe_detail_fragment_container, stepFragment)
                                    .commit();
                        }

                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, RecipeDetailActivity.class);
                        intent.putExtra("currentRecipe", currentRecipe);
                        intent.putExtra("currentStep",currentStep);
                        intent.putExtra(RecipeStepFragment.ARG_ITEM, mList.get(mPosition));
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if(mList == null) return 0;
            return mList.size();
        }

//VIEWHOLDER========================================================================================

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.step) TextView mStepTextView;
            @BindView(R.id.step_image) ImageView mStepImage;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this,view);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mStepTextView.getText() + "'";
            }
        }
    }

//END===============================================================================================
}
