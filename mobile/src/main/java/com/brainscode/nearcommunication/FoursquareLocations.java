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


class FoursquareLocations extends AsyncTask<String, Void, String> {

    private String strm = "22.81,89.55";
    private String client_id = "ZLC3BYW2IPZX0UBBLIVCJJYJLDA5HTN5NQ0011SMU5CHPG3K";
    private String client_secret = "RDNVCMA10IF4GPAZRMPKOCTQVCL4OLF2BQEJFSSSKLEGBBPE";
    private String currentDateandTime = "20130715";  //yyyymmdd
    String jsonResult;

    public FoursquareLocations(String strm) {
        this.strm = strm;
    }

    private ArrayList<Venue> parse(String str) throws Exception {
        JSONObject jObject = new JSONObject(str);
        ArrayList<Venue> venueList = new ArrayList<Venue>();

        JSONArray arr = jObject.getJSONObject("response").getJSONArray("venues");

//        for (int i=0; i< arr.length(); i++)	{
//            String name = arr.getJSONObject(i).getString("Name");
//            String desc = arr.getJSONObject(i).getString("Description");
//            double radius = arr.getJSONObject(i).getDouble("Radius");
//            int id = arr.getJSONObject(i).getInt("ID");
//            double coordX = arr.getJSONObject(i).getDouble("PlaceX");
//            double coordY = arr.getJSONObject(i).getDouble("PlaceY");
//            int uid = arr.getJSONObject(i).getInt("UserID");
//            //String parentId = arr.getJSONObject(i).getString("ParentID");
//
//            venueList.add(new ActivityP(coordX, coordY, radius, name, desc, id, uid));
//
//        }

        return venueList;


    }

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        DefaultHttpClient httpclient = new DefaultHttpClient();
        final HttpParams httpParams = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
        HttpConnectionParams.setSoTimeout(httpParams, 30000);
        HttpGet httppost = new HttpGet("https://api.foursquare.com/v2/venues/search?intent=checkin&ll="+strm+"&client_id="+client_id+"&client_secret="+client_secret+"&v="+currentDateandTime); //

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
