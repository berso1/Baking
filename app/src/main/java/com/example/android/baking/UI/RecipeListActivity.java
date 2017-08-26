package com.example.android.baking.UI;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.baking.R;
import com.example.android.baking.data.Ingredient;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.data.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity implements RecipeStepFragment.OnChangeStepListener{

    private boolean mTwoPane;
    Recipe currentRecipe;
    List<String> mList;
    List<Step> mSteps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        // Check for savedInstanceState currentMovie
        if(savedInstanceState == null || !savedInstanceState.containsKey("currentRecipe")) {
            Intent intent = getIntent();
            currentRecipe = intent.getExtras().getParcelable("currentRecipe");
        }
        else {
            currentRecipe = savedInstanceState.getParcelable("currentRecipe");
        }


        List<Ingredient> ingredients = currentRecipe.getmIngredients();
        mSteps = currentRecipe.getmSteps();
        mList = setList(ingredients,mSteps);

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView,mList);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("currentRecipe", currentRecipe);
        super.onSaveInstanceState(outState);
    }

    private List<String> setList(List<Ingredient> ingredients, List<Step> steps) {
        List<String> list = new ArrayList<String>(){};
        if(ingredients.size() > 0){
            list.add("INGREDIENTS");
        }
        for(int i = 0; i < steps.size() ; i++){
            final Step thisStep = steps.get(i);
            list.add(thisStep.getShortDescription());
        }
        return list;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView,List<String> mList) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(mList));
    }

    @Override
    public void OnChangeStep(int position) {
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
                .replace(R.id.item_detail_container, nextFragment)
                .commit();

    //    Toast toast = Toast.makeText(this,""+nextStep.getId(),Toast.LENGTH_SHORT);
      //  toast.show();
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<String> mList;

        public SimpleItemRecyclerViewAdapter(List<String> list) {
            mList = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final int mPosition = position;
            //final Recipe recipe = currentRecipe;
            Step step = null;
            if(position>0) {
                 step = mSteps.get(position - 1);
            }
            final Step currentStep = step;
            holder.mItem.setText(mList.get(position));
            holder.mItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(RecipeStepFragment.ARG_ITEM, mList.get(mPosition));
                        arguments.putParcelable("currentRecipe",currentRecipe);
                        arguments.putParcelable("currentStep",currentStep);
                        RecipeStepFragment fragment = new RecipeStepFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
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
            return mList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.item) TextView mItem;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this,view);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mItem.getText() + "'";
            }
        }
    }
}
