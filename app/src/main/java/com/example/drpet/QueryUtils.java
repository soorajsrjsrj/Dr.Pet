package com.example.drpet;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.example.drpet.Model.DBManager;
import com.google.android.gms.location.places.Place;

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

public  final class QueryUtils {
    public double endlat,endlog;
    private DBManager dbManager;




    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


        private QueryUtils() {

            Log.d("insidequerutil", "lat" + endlat + ",long:"+endlog);



            Cursor cursor = dbManager.fetchlocationData(1);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                endlat= cursor.getDouble(1);
                endlog = cursor.getDouble(2);
                Log.d("pet hospital nearby", "lat" + endlat + ",long:"+endlog);


            }






        }


    public static List<NearbyHospitals> fetchhospitalsdata(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<NearbyHospitals> earthquakes = extractnearByhospital(jsonResponse);

        // Return the list of {@link Earthquake}s
        return earthquakes;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
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

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing the given JSON response.
     */

        /**
         * Return a list of {@link } objects that has been built up from
         * parsing a JSON response.
         */
        public static ArrayList<NearbyHospitals> extractnearByhospital(String earthquakeJSON) {








            // If the JSON string is empty or null, then return early.
            if (TextUtils.isEmpty(earthquakeJSON)) {
                return null;
            }


            // Create an empty ArrayList that we can start adding earthquakes to
            ArrayList<NearbyHospitals> nearbyHospitals = new ArrayList<>();

            // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
            // is formatted, a JSONException exception object will be thrown.
            // Catch the exception so the app doesn't crash, and print the error message to the logs.

            try {

                // Create a JSONObject from the SAMPLE_JSON_RESPONSE string
                JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

                // Extract the JSONArray associated with the key called "features",
                // which represents a list of features (or earthquakes).
                JSONArray hospitalarry = baseJsonResponse.getJSONArray("results");

                // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
                for (int i = 0; i < hospitalarry.length(); i++) {
//                for (int i = 0; i < hospitalarry.length(); i++) {

                    // Get a single earthquake at position i within the list of earthquakes
                    JSONObject currentEarthquake = hospitalarry.getJSONObject(i);

                    // For a given earthquake, extract the JSONObject associated with the
                    // key called "properties", which represents a list of all properties
                    // for that earthquake.

                    String geomaetryObject = currentEarthquake.getString("geometry");
                    JSONObject geometryjson = new JSONObject(geomaetryObject);
                    String geomaetrylocation = geometryjson.getString("location");
                    JSONObject geolatlng = new JSONObject(geomaetrylocation);

                    Double geomaetrylat = geolatlng.getDouble("lat");
                    Double geomaetrylng = geolatlng.getDouble("lng");

//                    String placeId = currentEarthquake.getString("id");


                    String hospitalname = currentEarthquake.getString("name");



                    String hospitalicon = currentEarthquake.getString("icon");
                    String hospitalvicinty = currentEarthquake.getString("vicinity");


                    JSONArray nwphotoarray = currentEarthquake.getJSONArray("photos");
                    JSONObject nwphotoobject = nwphotoarray.getJSONObject(0);
                    String phtoref = nwphotoobject.getString("photo_reference");


                    Log.e("json value ", phtoref);
//                    Log.e("json value ", hospitalname +","+geomaetrylat+","+geomaetrylng+","+hospitalvicinty);


                    // Extract the value for the key called "mag"
//                    double magnitude = properties.getDouble("mag");
//
//                    // Extract the value for the key called "place"
//                    String location = properties.getString("place");
//
//                    // Extract the value for the key called "time"
//                    long time = properties.getLong("time");
//
//                    // Extract the value for the key called "url"
//                    String url = properties.getString("url");

                    // Create a new {@link Earthquake} object with the magnitude, location, time,
                    // and url from the JSON response.
                    NearbyHospitals hospitals = new NearbyHospitals(phtoref,hospitalname,hospitalvicinty,geomaetrylat,geomaetrylng,phtoref);

                    // Add the new {@link Earthquake} to the list of earthquakes.
                       nearbyHospitals.add(hospitals);
                }

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
            }

            // Return the list of earthquakes
            return nearbyHospitals;
        }



}