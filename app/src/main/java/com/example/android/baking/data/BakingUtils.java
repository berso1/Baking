package com.example.android.baking.data;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by berso on 8/17/17.
 */

public class BakingUtils {

    private static final String LOG_TAG = BakingUtils.class.getName();
    private static final String URL_PRE_STRING = "http://image.tmdb.org/t/p/w185/";

    private BakingUtils() { // Do not remove
    }

    //Read recipes.json file from assets folder
    public static List<Recipe> fetchRecipeData(Context context) {
        String jsonResponse = null;
        jsonResponse = readRecipesFromJson(context);
        return extractFeatureFromJson(jsonResponse,context);
    }

    public static String readRecipesFromJson(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("recipes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private static List<Recipe> extractFeatureFromJson(String recipeJson, Context context) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(recipeJson)) {
            return null;
        }
        List<Recipe> recipes = new ArrayList<>();
        try {
            // JSONObject baseJsonResponse = new JSONObject(recipeJson);
            // JSONArray results = baseJsonResponse.getJSONArray("results");
            JSONArray results = new JSONArray(recipeJson);

            for (int i = 0; i < results.length(); i++) {
                JSONObject thisRecipe = results.getJSONObject(i);

                int id = thisRecipe.getInt("id");
                String name = thisRecipe.getString("name");
                List<Ingredient> ingredients = getIngredientList(thisRecipe.getJSONArray("ingredients"));
                List<Step> steps = getSteps(thisRecipe.getJSONArray("steps"));
                int servings = thisRecipe.getInt("servings");
                String image = thisRecipe.getString("image");

                Recipe _recipe = new Recipe(id,name,ingredients,steps,servings,image);
                recipes.add(_recipe);
            }
            return recipes;
        } catch (JSONException e) {
            Log.v(LOG_TAG," extractFeatureFromJson "+e.toString());
        }
        return null;
    }


    private static List<Ingredient> getIngredientList(JSONArray array) throws JSONException {
        if(array==null) return null;
        List<Ingredient> ingredientList = new ArrayList<Ingredient>();
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject thisIngredient = array.getJSONObject(i);
                String     ingredient     = thisIngredient.getString("ingredient");
                String     measure        = thisIngredient.getString("measure");
                int        quantity       = thisIngredient.getInt("quantity");
                Ingredient _Ingredient    = new Ingredient(ingredient,measure,quantity);

                if(_Ingredient != null) {
                    ingredientList.add(_Ingredient);
                }
            }

        }catch (JSONException e){

        }
        return ingredientList;
    }

    private static List<Step> getSteps(JSONArray array) throws JSONException {
        if(array == null) return null;
        List<Step> stepList = new ArrayList<Step>();
        try{
            for(int i = 0; i < array.length() ; i++) {
                JSONObject thisStep         = array.getJSONObject(i);
                int        id               = thisStep.getInt("id");
                String     shortDescription = thisStep.getString("shortDescription");
                String     description      = thisStep.getString("description");
                String     videoUrl         = thisStep.getString("videoURL");
                String     thumbnailUrl     = thisStep.getString("thumbnailURL");
                Step       _step            = new Step(id,shortDescription,description,videoUrl,thumbnailUrl);

                stepList.add(_step);
            }
        }catch (JSONException e){

        }
        return stepList;
    }

}
//FIN==================================================================================================




