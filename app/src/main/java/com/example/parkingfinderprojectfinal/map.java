package com.example.parkingfinderprojectfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.parkingfinderprojectfinal.PlaceActivity.ARG_PLACE_NAME;
public class map extends AppCompatActivity  implements OnMapReadyCallback, LocationEngineListener, PermissionsListener,
        MapboxMap.OnMapClickListener {
    private static final int ACTIVITY_NUM = 1;
    public  static String[] coords;
    public static  String[] names;
    public static String[] ide;
    Dialog dialog;
    public static String[] slots;
    public static String[] prices;
    public static String[] mno;
    public static String[] oloc;
    public static String[] omno;
    public static String[] onames;
    private Context mContext =map.this;
    private static final String TAG = "map";
    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;
    private Point originPosition;
    private Point destinationPosition;
    private Marker destinationMarker;
    private NavigationMapRoute navigationMapRoute;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private String BASE_URL = MainActivity.u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.access_token));
        setContentView(R.layout.activity_map);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        mapView=findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState );
        mapView.getMapAsync(map.this);
        setupBottomNavigationView();
  }

  @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map=mapboxMap;
      HashMap<String, String> mape = new HashMap<>();
      MainActivity m = new MainActivity();
      mape.put("id", m.id);
      Thread t1 = new Thread(){
          @Override
          public void run() {
              Call<LocFetch> call =retrofitInterface.executegetAllLocs(mape);
              call.enqueue(new Callback<LocFetch>() {
                  @Override
                  public void onResponse(Call<LocFetch> call, Response<LocFetch> response) {
                      if(response.code()==200){
                          LocFetch result = response.body();
                          coords=result.getLocs();
                          names=result.getNames();
                          slots=result.getSlots();
                          prices=result.getPrices();
                          mno=result.getMno();
                          onames=result.getOnames();
                          oloc=result.getOloc();
                          omno=result.getOmno();
                          ide=result.getIde();
                          locator(map,coords,names,slots,prices,mno,onames,oloc,omno,ide);
                      }
                  }
                  @Override
                  public void onFailure(Call<LocFetch> call, Throwable t) {
                      Toast.makeText(map.this, t.getMessage(),
                              Toast.LENGTH_LONG).show();
                  }
              });
          }
      };
      t1.start();
      map.addOnMapClickListener(this);
      mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
          @Override
          public boolean onMarkerClick(@NonNull Marker marker) {
//              Toast.makeText(map.this, marker.getTitle(), Toast.LENGTH_LONG).show();
//              Intent i = new Intent(map.this,Dummy.class);
//              startActivity(i);
//              View view = getLayoutInflater().inflate(R.layout.map_dialog, null);
//
//              AlertDialog.Builder builder = new AlertDialog.Builder(map.this);
//
//              builder.setView(view).show();
              String[] title=(marker.getTitle()).split(" ");
              if((marker.getSnippet()).equals("parking")){
                  dialog=new Dialog(map.this);
                  dialog.setContentView(R.layout.map_dialog);
                  ImageView close =(ImageView)(dialog.findViewById(R.id.close));
                  TextView name =(TextView) dialog.findViewById(R.id.pop_name);
                  TextView num=(TextView) dialog.findViewById(R.id.pop_name2);

                  name.setText(title[0]);
                  num.setText(title[1]);
                  close.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          dialog.dismiss();
                      }
                  });
                  dialog.findViewById(R.id.details).setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          Intent i = new Intent(map.this, PlaceActivity.class);
                          i.putExtra(ARG_PLACE_NAME, title[0]);
                          startActivity(i);
                      }
                  });

                  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                  dialog.show();

              }else if((marker.getSnippet()).equals("food")){
                  dialog=new Dialog(map.this);
                  dialog.setContentView(R.layout.map_dialog);
                  ImageView close =(ImageView)(dialog.findViewById(R.id.close));
                  TextView name =(TextView) dialog.findViewById(R.id.pop_name);
                  TextView num=(TextView) dialog.findViewById(R.id.pop_name2);
                  name.setText(title[0]);
                  num.setText(title[1]);
                  close.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          dialog.dismiss();
                      }
                  });

                  dialog.findViewById(R.id.details).setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          Intent i = new Intent(map.this,foodActivity.class);
                          i.putExtra(foodActivity.ARG_PLACE_NAME, title[0]);
                          startActivity(i);
                      }
                  });
                  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                  dialog.show();

              }else if((marker.getSnippet()).equals("Workshop")){
                  dialog=new Dialog(map.this);
                  dialog.setContentView(R.layout.map_dialog);
                  ImageView close =(ImageView)(dialog.findViewById(R.id.close));
                  TextView num=(TextView) dialog.findViewById(R.id.pop_name2);
                  TextView name =(TextView) dialog.findViewById(R.id.pop_name);
                  name.setText(title[0]);
                  num.setText(title[1]);
                  close.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          dialog.dismiss();
                      }
                  });
                  dialog.findViewById(R.id.details).setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          Intent i = new Intent(map.this,wsActivity.class);
                          i.putExtra(wsActivity.ARG_PLACE_NAME, title[0]);
                          startActivity(i);
                      }
                  });

                  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                  dialog.show();

              }else{
                  dialog=new Dialog(map.this);
                  dialog.setContentView(R.layout.map_dialog);
                  ImageView close =(ImageView)(dialog.findViewById(R.id.close));
                  TextView name =(TextView) dialog.findViewById(R.id.pop_name);
                  TextView num=(TextView) dialog.findViewById(R.id.pop_name2);
                  name.setText(title[0]);
                  num.setText(title[1]);
                  close.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          dialog.dismiss();
                      }
                  });
                  dialog.findViewById(R.id.details).setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          Intent i = new Intent(map.this,HosActivity.class);
                          i.putExtra(HosActivity.ARG_PLACE_NAME, title[0]);
                          startActivity(i);
                      }
                  });
                  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                  dialog.show();

              }
              return true;
          }
      });
      enableLocation();
    }

    private void enableLocation(){
        if(PermissionsManager.areLocationPermissionsGranted(this)){
            initializeLocationEngine();
            initializeLocationLayer();
        }else{
            permissionsManager=new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    private  void initializeLocationEngine(){
        locationEngine=new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();
        if(lastLocation!=null){
            originLocation=lastLocation;
            setCameraPosition(lastLocation);
        }else{
            locationEngine.addLocationEngineListener(this);
        }
    }

    private void locator(MapboxMap mapboxMap, String[] coords, String[] names, String[] slots,String[] prices, String[] mno, String[] onames, String[] oloc, String[] omno,String[] ide){
        IconFactory iconFactory=IconFactory.getInstance(map.this);
        Icon icon=iconFactory.fromResource(R.drawable.parking);
        int a=0;
        for(int i=0;i<coords.length;i=i+2){
            mapboxMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(coords[i+1]),Double.parseDouble(coords[i]))).icon(icon)
                    .title(names[a]+" "+mno[a])
                    .snippet(ide[a]));
            a++;
        }
        int q=0;

        for(int i=0;i<oloc.length;i=i+2){
            if(ide[a].equals("food")){
                icon=iconFactory.fromResource(R.drawable.foodimg);
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(oloc[i+1]),Double.parseDouble(oloc[i]))).icon(icon)
                        .title(onames[q]+" "+omno[q])
                        .snippet(ide[a]));
            }else if(ide[a].equals("Workshop")){
                icon=iconFactory.fromResource(R.drawable.mech);
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(oloc[i+1]),Double.parseDouble(oloc[i]))).icon(icon)
                        .title(onames[q]+" "+omno[q])
                        .snippet(ide[a]));
            }else{
                icon=iconFactory.fromResource(R.drawable.medic);
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(oloc[i+1]),Double.parseDouble(oloc[i]))).icon(icon)
                        .title(onames[q]+" "+omno[q])
                        .snippet(ide[a]));
            }

            q++;
            a++;
        }
    }

    @SuppressWarnings("MissingPermission")
    private void initializeLocationLayer(){
        locationLayerPlugin=new LocationLayerPlugin(mapView,map,locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
    }

    private void setCameraPosition(Location location){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),13.0));
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        if(destinationMarker!=null){
            map.removeMarker(destinationMarker);
        }
        destinationMarker=map.addMarker(new MarkerOptions().position(point));
        destinationPosition=Point.fromLngLat(point.getLongitude(),point.getLatitude());
        originPosition=Point.fromLngLat(originLocation.getLongitude(),originLocation.getLatitude());
        getRoute(originPosition,destinationPosition);
    }

    private void getRoute(Point origin,Point destination){
        NavigationRoute.builder()
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                 @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if(response.body()==null){
                            Log.e(TAG,"No routes found, check right user and access token ");
                            return;
                        }else if(response.body().routes().size()==0){
                            Log.e(TAG,"NO routes found");
                            return;
                        }
                        DirectionsRoute currentRoute=response.body().routes().get(0);
                        if(navigationMapRoute!=null){
                            navigationMapRoute.removeRoute();
                        }else{
                            navigationMapRoute=new NavigationMapRoute(null,mapView,map);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }
                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e(TAG,"Error:"+ t.getMessage());
                    }
                });
        }
    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }
    @Override
    public void onLocationChanged(Location location) {
        if(location!=null){
            originLocation =location;
            setCameraPosition(location);
        }
    }
    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
    }
    @Override
    public void onPermissionResult(boolean granted) {
        if(granted){
            enableLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        if(locationEngine!=null){
            locationEngine.requestLocationUpdates();
        }
        if(locationLayerPlugin!=null){
            locationLayerPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(locationEngine!=null){
            locationEngine.removeLocationUpdates();
        }
        if(locationLayerPlugin!=null){
            locationLayerPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationEngine!=null){
            locationEngine.deactivate();
        }
        mapView.onDestroy();
    }



    //    BottomNavigationView setup
    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


}
