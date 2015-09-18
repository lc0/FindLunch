package com.brainscode.nearcommunication;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;


class FoursquareLocations extends AsyncTask<String, Void, String> {
    
    public void setStrm(String strm) {
        this.strm = strm;
    }

    private String strm = "22.81,89.55";
    private String client_id = "ZLC3BYW2IPZX0UBBLIVCJJYJLDA5HTN5NQ0011SMU5CHPG3K";
    private String client_secret = "RDNVCMA10IF4GPAZRMPKOCTQVCL4OLF2BQEJFSSSKLEGBBPE";
    private String currentDateandTime = "20130715";  //yyyymmdd
    private String category = "4d4b7105d754a06374d81259";
    String jsonResult;

    static ArrayList<Venue> venueList = new ArrayList<Venue>();

    public FoursquareLocations(String strm) {
        this.strm = strm;
    }

    private ArrayList<Venue> parse(String str) throws Exception {
        JSONObject jObject = new JSONObject(str);

        JSONArray venues = jObject.getJSONObject("response").getJSONArray("venues");

        for (int i=0; i< venues.length(); i++)	{
            JSONObject venue = venues.getJSONObject(i);

            String name = venue.getString("name");

            String category = "";
            String categoryIcon = "";

            if (venue.getJSONArray("categories").length() > 0) {
                JSONObject categoryObj = venue.getJSONArray("categories").getJSONObject(0);
                category = categoryObj.getString("name");
                categoryIcon = categoryObj.getJSONObject("icon").getString("prefix")
                        + '/' + categoryObj.getJSONObject("icon").getString("suffix");
            }

            int distance = venue.getJSONObject("location").getInt("distance");
            double lat = venue.getJSONObject("location").getDouble("lat");
            double lng = venue.getJSONObject("location").getDouble("lng");

            venueList.add(new Venue(name, category, categoryIcon, distance, lat, lng));

        }

        Log.i("JSON", "venues: " + venueList);
        return venueList;


    }
    private static Random randomGenerator = new Random();

    public static Venue getRandomVenue(){
        if(venueList!=null) {
            int index = randomGenerator.nextInt(venueList.size());
            return venueList.get(index);
        }
        return null;
    }

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        DefaultHttpClient httpclient = new DefaultHttpClient();
        final HttpParams httpParams = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
        HttpConnectionParams.setSoTimeout(httpParams, 30000);
        HttpGet httppost = new HttpGet("https://api.foursquare.com/v2/venues/search?intent=checkin&ll="+strm+"&radius=300&categoryId="+category+"&client_id="+client_id+"&client_secret="+client_secret+"&v="+currentDateandTime); //

        try{

            HttpResponse response = httpclient.execute(httppost);  //response class to handle responses
            jsonResult = inputStreamToString(response.getEntity().getContent()).toString();

            try {
                parse(jsonResult);
            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONObject object = new JSONObject(jsonResult);
        }
        catch(ConnectTimeoutException e){
//            Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show();
        }
        catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonResult;
    }

    protected void onPostExecute(String Result){
        try{

//            Toast.makeText(getApplicationContext(), "R E S U L T :" + jsonResult, Toast.LENGTH_LONG).show();
            System.out.println(jsonResult);
            Log.i("JSON", "RESULT: " + jsonResult);
            //showing result

        }catch(Exception E){
            Log.i("JSON", "FAILED");
//            Toast.makeText(getApplicationContext(), "Error:"+E.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }



}
