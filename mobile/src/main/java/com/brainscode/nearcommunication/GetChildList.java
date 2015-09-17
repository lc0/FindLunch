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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


class GetChildList extends AsyncTask<String, Void, String> {

    private String strm = "22.81,89.55";
    private String client_id = "ZLC3BYW2IPZX0UBBLIVCJJYJLDA5HTN5NQ0011SMU5CHPG3K";
    private String client_secret = "RDNVCMA10IF4GPAZRMPKOCTQVCL4OLF2BQEJFSSSKLEGBBPE";
    private String currentDateandTime = "20130715";  //yyyymmdd
    String jsonResult;

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
