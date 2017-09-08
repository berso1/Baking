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

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */
public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeStepFragment.OnChangeStepListener,
            RecipeIngredientsFragment.OnChangeIngredientsListener{

    private static final String INGREDIENTS_TAG = "ingredients_fragment";
    private static final String STEP_TAG = "step_fragment " ;
    Recipe currentRecipe;
    Step currentStep;
    String mItem;
    ActionBar actionBar;
    RecipeStepFragment stepFragment;
    RecipeIngredientsFragment ingredientsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        Intent intent = getIntent();
        currentRecipe = intent.getExtras().getParcelable("currentRecipe");
        currentStep = intent.getExtras().getParcelable("currentStep");
        actionBar = getSupportActionBar();

            // Show the Up button in the action bar.
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }


        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            mItem = getIntent().getStringExtra(RecipeStepFragment.ARG_ITEM);
            if (mItem.equals("INGREDIENTS")) {
                loadIngredientsFragment();
            }else {
                loadStepFragment();
            }
        }

    }


    private void loadIngredientsFragment(){
 //       actionBar.setTitle(mItem);
        ingredientsFragment = new RecipeIngredientsFragment();
        Bundle arguments = new Bundle();
        arguments.putString(RecipeStepFragment.ARG_ITEM, mItem);
        arguments.putParcelable("currentRecipe", currentRecipe);
        ingredientsFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_detail_fragment_container, ingredientsFragment, INGREDIENTS_TAG)
                .commit();
    }

    private void loadStepFragment(){
    //    actionBar.setTitle(mItem);
        stepFragment = new RecipeStepFragment();
        Bundle arguments = new Bundle();
        arguments.putString(RecipeStepFragment.ARG_ITEM, mItem);
        arguments.putParcelable("currentRecipe", currentRecipe);
        arguments.putParcelable("currentStep", currentStep);
        stepFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_detail_fragment_container, stepFragment, STEP_TAG)
                .commit();
    }

    @Override
    public void OnChangeStep(int position) {
        if(position == -1){
            mItem = "INGREDIENTS";
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
