package com.example.bucketlist.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.bucketlist.VolleySingleton;
import com.google.gson.JsonObject;
import com.google.logging.type.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Helper class to get the list of cities acc to the query .
 * The api is used from the website of LocationIq
 */

public abstract class CityListHelper {
    /**
     * Token for the api
     */
    final String token ="pk.6594a14765cc8da44679e78bad670adf";

    Context context;


    public CityListHelper(Context context,String searchQuery) {
        this.context = context;
        try{
            String url = "https://api.locationiq.com/v1/autocomplete.php?key="+token+"&tag=place:city&q=" + searchQuery;
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        List<CityQuery> queryResult = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {

                            JSONObject object1 = response.getJSONObject(i).getJSONObject("address");
                            CityQuery query = new CityQuery();
                            query.setCountry(object1.getString("country"));
                            query.setCityName( object1.getString("name"));
                            query.setStateName( object1.getString("state"));
                            query.setDisplayName(query.cityName + ", " + query.stateName + ", " + query.country);
                            queryResult.add(query);
                        }
                        onCompleteListener(queryResult);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "onErrorResponse: " + error.toString());
                }
            });

            // adding to request queue
            VolleySingleton.getInstance(context).getRequestQueue().add(jsonArrayRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Response", "onCreate: Failed");
        }
    }

    protected abstract void onCompleteListener(List<CityQuery> queryResult);


    /**
     * This is the helper class for getting the city details and is only used for seaching
     */

    public class CityQuery {
        private String stateName;
        private String displayName;
        private String cityName;
        private String country;

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getPassedName() {
            return cityName;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public void setPassedName(String passedName) {
            this.cityName = passedName;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        @Override
        public String toString() {
            return "CityQuery{" +
                    "stateName='" + stateName + '\'' +
                    ", displayName='" + displayName + '\'' +
                    ", cityName='" + cityName + '\'' +
                    ", country='" + country + '\'' +
                    '}';
        }
    }
}
