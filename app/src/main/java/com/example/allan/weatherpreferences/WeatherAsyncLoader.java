package com.example.allan.weatherpreferences;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAsyncLoader extends AsyncTaskLoader<String> {

    private String urlString;

    public WeatherAsyncLoader(@NonNull Context context, String urlString) {
        super(context);
        this.urlString = urlString;
    }

    @Nullable
    @Override
    public String loadInBackground() {
        StringBuilder JsonData = new StringBuilder();
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            Log.v("NETWORK_URL", urlString);
            URL url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                JsonData.append(line);
                line = reader.readLine();
            }
            Log.v("AsyncTask", "Connected" + httpURLConnection.getResponseCode());
            Log.v("AsyncTask", "JSON" + JsonData);

        } catch (Exception e) {
            e.printStackTrace();
            Log.v("AsyncTask", e.getMessage());
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return JsonData.toString();
    }
}
