package com.example.android.baking.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.baking.R;
import com.example.android.baking.data.Recipe;

public class MainActivity extends AppCompatActivity
implements RecipeFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecipeFragment fragment = new RecipeFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.recipe_container, fragment)
                .commit();

    }

    @Override
    public void onListFragmentInteraction(Recipe recipe) {
      //  Toast toast = Toast.makeText(this,""+recipe.getmId(),Toast.LENGTH_SHORT);
       // toast.show();
        Intent intent = new Intent(this,RecipeListActivity.class);
        intent.putExtra("currentRecipe", recipe);
        startActivity(intent);
    }
}
