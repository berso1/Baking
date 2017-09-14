package com.example.android.baking.data;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

//Created by berso on 8/17/17.


public class BakingUtils {

    private static final String LOG_TAG = BakingUtils.class.getName();

    //empty constructor to avoid class to be instantiated
    private BakingUtils() {
    }

    public static List<Recipe> fetchRecipeData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        // Extract relevant fields from the JSON response and create an {@link Event} object
        // Return the {@link Event}
        return extractFeatureFromJson(jsonResponse);
    }


    private static URL createUrl(String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    private static List<Recipe> extractFeatureFromJson(String recipeJson) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(recipeJson)) {
            return null;
        }
        List<Recipe> recipes = new ArrayList<>();
        try {
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
        List<Ingredient> ingredientList = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject thisIngredient = array.getJSONObject(i);
                String     ingredient     = thisIngredient.getString("ingredient");
                String     measure        = thisIngredient.getString("measure");
                int        quantity       = thisIngredient.getInt("quantity");
                Ingredient _Ingredient    = new Ingredient(ingredient,measure,quantity);
                ingredientList.add(_Ingredient);
            }

        }catch (JSONException e){
            Log.v(LOG_TAG," extractIngredientsFromJson "+e.toString());
        }
        return ingredientList;
    }

    private static List<Step> getSteps(JSONArray array) throws JSONException {
        if(array == null) return null;
        List<Step> stepList = new ArrayList<>();
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
            Log.v(LOG_TAG," extractStepsFromJson "+e.toString());

        }
        return stepList;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if( url == null ){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(15 * 1000 /* milliseconds */);
            urlConnection.setConnectTimeout(15 * 1000 /* milliseconds */);
            urlConnection.connect();
            if( urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.v(LOG_TAG,"error:"+urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG+" Invalid Url",e.toString());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //method to calculate # of columns
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) dpWidth / 300;
    }

}




//FIN==================================================================================================




