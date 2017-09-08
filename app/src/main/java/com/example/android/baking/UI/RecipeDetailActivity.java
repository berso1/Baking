package com.example.android.baking.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.baking.R;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.data.Step;

//Activity to host either Ingredient or Step fragment
public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeStepFragment.OnChangeStepListener,
            RecipeIngredientsFragment.OnChangeIngredientsListener{


    private static final String INGREDIENTS_TAG = "ingredients_fragment";
    private static final String STEP_TAG = "step_fragment " ;
    private Recipe currentRecipe;
    private Step currentStep;
    private String mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        Intent intent = getIntent();
        currentRecipe = intent.getExtras().getParcelable("currentRecipe");
        currentStep = intent.getExtras().getParcelable("currentStep");
        ActionBar actionBar = getSupportActionBar();

            // Show the Up button in the action bar.
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

        if (savedInstanceState == null) {
            mItem = getIntent().getStringExtra(RecipeStepFragment.ARG_ITEM);
            if (mItem.equals(RecipeListActivity.INGREDIENTS_TITLE)) {
                loadIngredientsFragment();
            }else {
                loadStepFragment();
            }
        }

    }

    //fragment loaders
    private void loadIngredientsFragment(){
        RecipeIngredientsFragment ingredientsFragment = new RecipeIngredientsFragment();
        Bundle arguments = new Bundle();
        arguments.putString(RecipeStepFragment.ARG_ITEM, mItem);
        arguments.putParcelable("currentRecipe", currentRecipe);
        ingredientsFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_detail_fragment_container, ingredientsFragment, INGREDIENTS_TAG)
                .commit();
    }

    private void loadStepFragment(){
        RecipeStepFragment stepFragment = new RecipeStepFragment();
        Bundle arguments = new Bundle();
        arguments.putString(RecipeStepFragment.ARG_ITEM, mItem);
        arguments.putParcelable("currentRecipe", currentRecipe);
        arguments.putParcelable("currentStep", currentStep);
        stepFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_detail_fragment_container, stepFragment, STEP_TAG)
                .commit();
    }


    //interfaces to step or ingrediets methods
    @Override
    public void OnChangeStep(int position) {
        if(position == -1){
            mItem = RecipeListActivity.INGREDIENTS_TITLE;
            loadIngredientsFragment();
        }else {
            currentStep = currentRecipe.getmSteps().get(position);
            loadStepFragment();
        }
    }

    @Override
    public void OnChangeIngredient(int position) {
        currentStep = currentRecipe.getmSteps().get(position);
        mItem = currentStep.getShortDescription();
        loadStepFragment();
    }

    //MENU METHODS----------------------------------------------------------------------------------


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }
    

    //Return to previous activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(this,RecipeListActivity.class);
            intent.putExtra("currentRecipe",currentRecipe);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
