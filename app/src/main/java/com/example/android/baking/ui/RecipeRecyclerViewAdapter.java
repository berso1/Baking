package com.example.android.baking.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.baking.R;
import com.example.android.baking.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//RecyclerViewAdapter for a List of Recipe
public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.ViewHolder> {

    private List<Recipe> mRecipes;
    private final RecipeFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;

    public RecipeRecyclerViewAdapter(List<Recipe> recipes,
                                     RecipeFragment.OnListFragmentInteractionListener listener,
                                     Context context) {
        mRecipes = recipes;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipe, parent, false);
        return new ViewHolder(view);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Recipe recipe = mRecipes.get(position);
        String imageUrl = recipe.getmImage();
        //if recipe have an imageUrl, loads it
        if(imageUrl.equals("")){
            holder.mImageView.setImageResource(R.drawable.ic_cake);
        }else {
            Picasso.with(mContext).load(imageUrl)
                    .placeholder(mContext.getResources().getDrawable(R.drawable.ic_cake))
                    .error(mContext.getResources().getDrawable(R.drawable.ic_cake))
                    .into(holder.mImageView);
        }
        holder.mName.setText(recipe.getmName());
        holder.mServings.setText(mContext.getString(R.string.servings, ""+recipe.getmServings()));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(recipe);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mRecipes == null) return 0;
        return mRecipes.size();
    }

    public void swapData(List<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }
    

    //VIEW HOLDER ======================================================================================
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_view) View mView;
        @BindView(R.id.recipe_image) ImageView mImageView;
        @BindView(R.id.name) TextView mName;
        @BindView(R.id.servings) TextView mServings;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }
}
