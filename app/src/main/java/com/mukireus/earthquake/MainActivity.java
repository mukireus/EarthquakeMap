package com.mukireus.earthquake;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

  private MapView mapView;
  private RequestQueue queue;
  MapboxMap mapboxMapV;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Mapbox.getInstance(this, "your_access");
    setContentView(R.layout.activity_main);

    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mapboxMapV = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
          @Override
          public void onStyleLoaded(@NonNull Style style) {

            // Map is set up and the style has loaded. Now you can add data or make other map adjustments
          }
        });
      }
    });
    queue = Volley.newRequestQueue(this);
    getEarthQuakes();
  }

  @Override
  public void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  public void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  public void getEarthQuakes() {
    final Earthquake earthQuake = new Earthquake();

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
        "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.geojson",
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            try {
              JSONArray features = response.getJSONArray("features");
              for (int i = 0; i < 50; i++) {
                JSONObject properties = features.getJSONObject(i).getJSONObject("properties");

                //Get geometry object
                JSONObject geometry = features.getJSONObject(i).getJSONObject("geometry");

                //Get coordinates array
                JSONArray coordinates = geometry.getJSONArray("coordinates");

                double lon = coordinates.getDouble(0);
                double lat = coordinates.getDouble(1);

                earthQuake.setPlace(properties.getString("place"));
                earthQuake.setTime(properties.getLong("time"));
                if (properties.getDouble("mag") > 4) {
                  earthQuake.setMagnitude(properties.getDouble("mag"));
                }

                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(properties.getLong("time"))
                    .getTime());

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(earthQuake.getPlace());
                markerOptions.position(new LatLng(lat, lon));
                markerOptions.snippet("Magnitude:" +
                    earthQuake.getMagnitude() + "\n" +
                    "Date: " + formattedDate);
                Marker marker = mapboxMapV.addMarker(markerOptions);
                mapboxMapV
                    .animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 1));

              }
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }
        }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
      }
    });
    queue.add(jsonObjectRequest);
  }
}