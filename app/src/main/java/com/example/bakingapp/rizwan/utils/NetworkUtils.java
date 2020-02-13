package com.example.bakingapp.rizwan.utils;

import android.annotation.SuppressLint;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import timber.log.Timber;


public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public NetworkUtils() {}

    @SuppressLint("TimberArgCount")
    public static URL buildUrl() {
        URL urlRecipe = null;
        try {
            Uri recipeQueryUri = Uri.parse(BASE_URL).buildUpon()
                    .build();
            urlRecipe = new URL(recipeQueryUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Timber.d(TAG, urlRecipe);
        return urlRecipe;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        Timber.i(url.toString(), "URL: ");


        if (url.getHost().equals("")) {
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

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Timber.e("Error response code: %s", urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Timber.e(e, "Problem retrieving recipe JSON results.");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
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
}

