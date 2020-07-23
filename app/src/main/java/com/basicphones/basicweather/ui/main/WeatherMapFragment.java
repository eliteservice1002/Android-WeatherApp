package com.basicphones.basicweather.ui.main;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.basicphones.basicweather.MainActivity;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import com.basicphones.basicweather.R;

import java.util.StringJoiner;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeatherMapFragment} interface
 * to handle interaction events.
 * Use the {@link WeatherMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherMapFragment extends Fragment {

  // map view support
  private MapView mapView;

  private static final String LATITUDE = "lat";
  private static final String LONGITUDE = "lon";
  private static final String TAG = "WeatherMapFragment";
  private WebView webView;
  private Bundle webViewBundle;

  private double mLatitude, mLongitude;
  private String defaultLayer = "asset://precipitation_style_file.json";
  private String prefix_url = "https://maps.darksky.net/@radar,";
  private String behind_url = "?domain=\"+encodeURIComponent(window.location.href)+\"&auth=1580620810_c103add154fcb4fa9494f705e963048b&embed=true&timeControl=false&fieldControl=false&defaultField=radar&defaultUnits=_f";
  private int zoom = 6;
  private ProgressDialog progressDialog;
  private static WeatherMapFragment instance = null;
  private boolean url_loadflag = false;
  private MainActivity activity;

//  @Override
//  public void onStart() {
//    super.onStart();
////        mapView.onStart();
//  }

//  @Override
//  public void onResume() {
//    super.onResume();
////        mapView.onResume();
//
//  }

  @Override
  public void onPause() {
    super.onPause();
    if(webView != null && webViewBundle.isEmpty()){
      webView.saveState(webViewBundle);
    }
//        mapView.onPause();
  }

//  @Override
//  public void onStop() {
//    super.onStop();
////        mapView.onStop();
//  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
//        mapView.onLowMemory();
  }

//  @Override
//  public void onDestroy() {
//    super.onDestroy();
////        mapView.onDestroy();
//  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
  }

  public WeatherMapFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @return A new instance of fragment WeatherMapFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static WeatherMapFragment newInstance(Double lat, Double lon) {
    WeatherMapFragment fragment = new WeatherMapFragment();
    Bundle args = new Bundle();
    args.putDouble(LATITUDE, lat);
    args.putDouble(LONGITUDE, lon);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    instance = this;

    webViewBundle = new Bundle();
    Log.i(TAG, "On create");

    if (getArguments() != null) {
      mLatitude = getArguments().getDouble(LATITUDE);
      mLongitude = getArguments().getDouble(LONGITUDE);
    }

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    Log.i(TAG, "On Create view");
    // Inflate the layout for this fragment
    View root = inflater.inflate(R.layout.fragment_weather_map, container, false);


    // bottom app bar init
//        BottomAppBar bottomAppBar = root.findViewById(R.id.bottomAppBar);
//        bottomAppBar.replaceMenu(R.menu.map_layer_menu);

//        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//                FragmentTransaction ft = null;
//                if (getFragmentManager() != null) {
//                    ft = getFragmentManager().beginTransaction();
//                }
//                if (ft != null) {
//                    switch (item.getItemId()) {
//                        case R.id.menu_item_clouds:
////                            defaultLayer = "asset://cloud_style_file.json";
////                            ft.detach(WeatherMapFragment.this).attach(WeatherMapFragment.this).commit();
//                            webView.loadUrl("https://maps.darksky.net/@cloud_cover,47.457,-121.928,8?domain=\"+encodeURIComponent(window.location.href)+\"&auth=1580620810_c103add154fcb4fa9494f705e963048b&embed=true&timeControl=false&fieldControl=false&defaultField=cloud_cover&defaultUnits=_f");
//                            return true;
//                        case R.id.menu_item_precipitation:
////                            defaultLayer = "asset://precipitation_style_file.json";
////                            ft.detach(WeatherMapFragment.this).attach(WeatherMapFragment.this).commit();
//                            webView.loadUrl("https://maps.darksky.net/@precipitation_rate,47.457,-121.928,8?domain=\"+encodeURIComponent(window.location.href)+\"&auth=1580620810_c103add154fcb4fa9494f705e963048b&embed=true&timeControl=false&fieldControl=false&defaultField=precipitation_rate&defaultUnits=_f");
//                            return true;
//                        case R.id.menu_item_sea_pressure:
////                            defaultLayer = "asset://sea_pressure_style_file.json";
////                            ft.detach(WeatherMapFragment.this).attach(WeatherMapFragment.this).commit();
//                            webView.loadUrl("https://maps.darksky.net/@sea_level_pressure,47.457,-121.928,8?domain=\"+encodeURIComponent(window.location.href)+\"&auth=1580620810_c103add154fcb4fa9494f705e963048b&embed=true&timeControl=false&fieldControl=false&defaultField=sea_level_pressure&defaultUnits=_f");
//                            return true;
//                        case R.id.menu_item_temp:
////                            defaultLayer = "asset://temp_style_file.json";
////                            ft.detach(WeatherMapFragment.this).attach(WeatherMapFragment.this).commit();
//                            webView.loadUrl("https://maps.darksky.net/@temperature,47.457,-121.928,8?domain=\"+encodeURIComponent(window.location.href)+\"&auth=1580620810_c103add154fcb4fa9494f705e963048b&embed=true&timeControl=false&fieldControl=false&defaultField=temperature&defaultUnits=_f");
//                            return true;
//                        case R.id.menu_item_wind_speed:
////                            defaultLayer = "asset://wind_speed_style_file.json";
////                            ft.detach(WeatherMapFragment.this).attach(WeatherMapFragment.this).commit();
//                            webView.loadUrl("https://maps.darksky.net/@wind_speed,47.457,-121.928,8?domain=\"+encodeURIComponent(window.location.href)+\"&auth=1580620810_c103add154fcb4fa9494f705e963048b&embed=true&timeControl=false&fieldControl=false&defaultField=wind_speed&defaultUnits=_f");
//                            return true;
//                        /*case R.id.menu_item_satellite:
//                            defaultLayer = "asset://satelite_style_file.json";
//                            ft.detach(WeatherMapFragment.this).attach(WeatherMapFragment.this).commit();
//                            return true;*/
//                    }
//                } else {
//                    Log.e(TAG, "Fragment transaction is null");
//                }
//
//                return false;
//            }
//        });

//        mapView = root.findViewById(R.id.mapView);
//        mapView.onCreate(savedInstanceState);
//        mapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(@NonNull MapboxMap mapboxMap) {
//                CameraPosition position = new CameraPosition.Builder()
////                        .target(new LatLng(mLatitude, mLongitude))
//                        .target(new LatLng(47.457, -121.928))
//                        .zoom(4)
//                        .tilt(20)
//                        .build();
//                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 3000);
//
//                mapboxMap.setStyle(Style.LIGHT);
//                mapboxMap.setStyle(new Style.Builder().fromUri(defaultLayer));
//            }
//        });

//        MainActivity activity = ((MainActivity)getActivity());
//        int position = activity.tabs.getSelectedTabPosition();
//      String msg = "sdfsdf";
//      Toast.makeText(getContext(), msg, msg.length()).show();
    webView = (WebView) root.findViewById(R.id.webView);
    webView.getSettings().setJavaScriptEnabled(true);

    if(webView != null){
      webView.setWebViewClient(new WebViewClient(){
        @Override
        public void onPageFinished(WebView webView, String url){
          super.onPageFinished(webView, url);
          closeDialog();
        }
      });
      activity = (MainActivity) getActivity();
      if (webViewBundle == null || webViewBundle.isEmpty()) {
          if(activity.tabs.getSelectedTabPosition() != 2) {
              WeatherMapThread mapThread = new WeatherMapThread();
              mapThread.execute();
          }
        webView.loadUrl(prefix_url + mLatitude + "," + mLongitude + "," + zoom + behind_url);
      } else {
        webView.restoreState(webViewBundle);
        webViewBundle.clear();
      }
    }
//            new WeatherMapThread1().execute();
//        webView.loadUrl("https://darksky.net/map-embed/@radar,47.457,-121.928,8.js?embed=true&timeControl=false&fieldControl=false&defaultField=radar&defaultUnits=_f");
//        webView.loadUrl("https://maps.darksky.net/@radar,47.457,-121.928,6?domain=\"+encodeURIComponent(window.location.href)+\"&auth=1580620810_c103add154fcb4fa9494f705e963048b&embed=true&timeControl=false&fieldControl=false&defaultField=radar&defaultUnits=_f");
//            webView.loadUrl(prefix_url + mLatitude + "," + mLongitude + "," + zoom + behind_url);
//            webView.loadUrl("https://maps.darksky.net/@radar,47.457,-121.928,8");
    return root;
  }
  public void closeDialog(){
      if(activity.tabs.getSelectedTabPosition() != 2) {
          if(progressDialog != null) {
              progressDialog.dismiss();
          }
          url_loadflag = true;
          if (url_loadflag) {
              String msg = "page done loading";
              Toast.makeText(getContext(), msg, msg.length()).show();
              url_loadflag = false;
          }
      }
  }


  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {

    return super.onOptionsItemSelected(item);
  }

    public class WeatherMapThread extends AsyncTask<Object, Integer, Void> {

    @Override
    protected void onPreExecute(){
      super.onPreExecute();
        progressDialog = ProgressDialog.show(getActivity(), "", "map is loading...");

    }

    @Override
    protected Void doInBackground(Object...objects) {
      return null;
    }
    @Override
    protected void onPostExecute(Void aVoid){
      super.onPostExecute(aVoid);

    }
  }

  public static WeatherMapFragment getInstance(){
    if(instance == null){
      instance = new WeatherMapFragment();
    }
    return instance;
  }

}


