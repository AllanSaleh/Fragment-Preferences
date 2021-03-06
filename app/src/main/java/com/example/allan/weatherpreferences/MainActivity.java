package com.example.allan.weatherpreferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    public static final String BASE_WEATHER_URL = "https://openweathermap.org/data/2.5/weather?appid=b6907d289e10d714a6e88b30761fae22";

    TextView location_tv;
    TextView weather_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        location_tv = findViewById(R.id.location);
        weather_tv = findViewById(R.id.weather);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader loader = loaderManager.getLoader(0);
        if(loader == null){
            loaderManager.initLoader(0, null, MainActivity.this).forceLoad();
        }else {
            loaderManager.restartLoader(0, null, MainActivity.this).forceLoad();
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String location = sharedPreferences.getString(getString(R.string.shared_preferences_key), "London");

        Uri baseUri = Uri.parse(BASE_WEATHER_URL);

        Uri.Builder builder = baseUri.buildUpon();

        builder.appendQueryParameter("q", location);

        builder.build();

        return new WeatherAsyncLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        try {
            JSONObject root = new JSONObject(s);
            String name = root.getString("name");
            JSONArray weather = root.getJSONArray("weather");
            JSONObject currentItem = weather.getJSONObject(0);
            String main = currentItem.getString("main");

            location_tv.setText(name);
            weather_tv.setText(main);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
