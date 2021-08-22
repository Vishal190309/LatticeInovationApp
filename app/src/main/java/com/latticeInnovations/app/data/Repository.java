package com.latticeInnovations.app.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.latticeInnovations.app.R;
import com.latticeInnovations.app.WeatherActivity;
import com.latticeInnovations.app.controller.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Repository {
    String url = "https://api.postalpincode.in/pincode/";
    String weahterUrl = "https://api.weatherapi.com/v1/current.json?key=%1$s&q=%2$s&aqi=no";
    String string;
    private ResourceProvider mResourceProvider;

    public Repository(String string,ResourceProvider mResourceProvider) {
        this.string = string;
        this.mResourceProvider = mResourceProvider;
    }


    public  void getWeatherData(final WeatherAsyncCallback Callback){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, String.format(weahterUrl, mResourceProvider.getString(R.string.api_key), string), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String lat="";
                String lon="";
                String tempF="";
                String tempC="";
                try {
                    JSONObject locationObject = response.getJSONObject("location");
                    JSONObject tempObject = response.getJSONObject("current");
                    lat = locationObject.getString("lat");
                    lon = locationObject.getString("lon");
                    tempF = tempObject.getString("temp_f");
                    tempC = tempObject.getString("temp_c");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (Callback!=null){
                    Callback.getWeatherCity(tempF,tempC,lat,lon);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (Callback!=null){
                    Callback.getWeatherCity("","","","");
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
    public void getPincode(final AsyncCallback asyncCallback){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://api.postalpincode.in/pincode/"+string, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String district="";
                String state="";
                try {
                    JSONObject jsonObject =response.getJSONObject(0);
                    String Status = jsonObject.getString("Status");
                    if (Status.equals("Success")){
                        JSONArray jsonArray = jsonObject.getJSONArray("PostOffice");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        district = jsonObject1.getString("District");
                        state = jsonObject1.getString("State");



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (asyncCallback!=null){
                    asyncCallback.getCity(district ,state);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }
}
