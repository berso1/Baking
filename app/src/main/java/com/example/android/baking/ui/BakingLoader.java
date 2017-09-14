package com.example.android.baking.ui;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import com.example.android.baking.data.BakingUtils;
import com.example.android.baking.data.Recipe;
import java.util.List;

/**
 * Created by berso on 9/8/17.
 */

public class BakingLoader extends AsyncTaskLoader<List<Recipe>> {

    private final String mUrl;

    public BakingLoader(Context context, String Url) {
        super(context);
        mUrl = Url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Recipe> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        return BakingUtils.fetchRecipeData(mUrl);
    }
}
