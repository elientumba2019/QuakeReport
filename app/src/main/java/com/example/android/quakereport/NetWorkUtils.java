package com.example.android.quakereport;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ntumba on 17-12-31.
 */

public class NetWorkUtils {


    public static List<EarthQuake> getEarthQuakeList(String url) throws JSONException {

        InputStream inputStream = getInputStream(url);
        String response = getResponseFromStream(inputStream);
        List<EarthQuake> earthQuakeList = parseResponse(response);

        return earthQuakeList;
    }




    private static InputStream getInputStream(String url) {

        InputStream stream = null;

        try {

            URL address = new URL(url);
            HttpURLConnection connection = (HttpURLConnection)address.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.connect();

            if(connection.getResponseCode() == 200){
                Log.d("NET : " , "Connected !!!");
                stream = connection.getInputStream();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return stream;
    }




    private static String getResponseFromStream(InputStream inputStream) {

        StringBuilder result = new StringBuilder();
        String line = "null";

       InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
       BufferedReader reader = new BufferedReader(inputStreamReader);

        try {

            line = reader.readLine();
            while (line != null){
                result.append(line);
                line = reader.readLine();
            }

            Log.d("JSON : " , result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }



    private static List<EarthQuake> parseResponse(String response) throws JSONException {

        List<EarthQuake> list = new LinkedList<>();

       JSONObject object = new JSONObject(response);
       JSONArray arrayOfEartQuake = object.optJSONArray("features");

       for(int c = 0 ; c < arrayOfEartQuake.length() ; c++){

           JSONObject earthQuakeJson = arrayOfEartQuake.optJSONObject(c);
           JSONObject properties = earthQuakeJson.optJSONObject("properties");

           String mag = properties.optString("mag");
           String location = properties.optString("place");
           String time = getTime(Long.valueOf(properties.optString("time")));
           String web = properties.optString("url");

           EarthQuake e = new EarthQuake(mag , location , time);
           e.setWebAddress(web);
           list.add(e);


       }

        return list;
    }


    private static String getTime(long time) {

        Date date = new Date(time);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("MMM DD, yyyy");
        return format.format(date);
    }
}
