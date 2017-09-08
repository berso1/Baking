package com.example.android.baking.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.android.baking.R;
import com.example.android.baking.data.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
implements RecipeFragment.OnListFragmentInteractionListener {

    @BindView(R.id.main_toolbar) Toolbar mainToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mainToolbar);
        mainToolbar.setTitle(R.string.app_name);
        RecipeFragment fragment = new RecipeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.recipe_container, fragment)
                .commit();

      //  mainToolbar.setTitle("");
    }

    //Fragment interface
    @Override
    public void onListFragmentInteraction(Recipe recipe) {
        Intent intent = new Intent(this,RecipeListActivity.class);
        intent.putExtra("currentRecipe", recipe);
        startActivity(intent);
    }
}
