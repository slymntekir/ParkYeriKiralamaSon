package com.example.suleyman.parkyerikiralama;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suleyman.parkyerikiralama.Modules.DirectionFinder;
import com.example.suleyman.parkyerikiralama.Modules.DirectionFinderListener;
import com.example.suleyman.parkyerikiralama.Modules.Route;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class YolTarifiActivity extends AppCompatActivity implements OnMapReadyCallback,DirectionFinderListener{
    GoogleMap haritam;
    String source_location,destination_location;
    Button btn_navigasyon_baslat,btn_navigasyon_durdur;

    // mevcut konumu algılamak için
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null)
        {
            broadcastReceiver = new BroadcastReceiver()
            {
                @Override
                public void onReceive(Context context, Intent intent) {
                    source_location = (String) intent.getExtras().get("coordinates");
                    sendRequest();
                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean var_mi = false;
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        if (api.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS)
            var_mi = true;
        else if (api.isUserResolvableError(api.isGooglePlayServicesAvailable(this)))
            api.getErrorDialog(this, api.isGooglePlayServicesAvailable(this), 0).show();
        else
            Toast.makeText(this, "Api Yok", Toast.LENGTH_LONG).show();
        // api varsa activity yükleniyor
        if (var_mi) {
            setContentView(R.layout.activity_yol_tarifi);
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.yol_tarifi_mapFrgmnt);
            mapFragment.getMapAsync(this);
        }

        btn_navigasyon_baslat = (Button)findViewById(R.id.btnFindPath);
        btn_navigasyon_durdur = (Button) findViewById(R.id.btnFindPathStop);


        destination_location = "";
        /*
        destination_location += String.valueOf(getIntent().getDoubleExtra("enlem",0.0));
        destination_location += ","+String.valueOf(getIntent().getDoubleExtra("boylam",0.0));
        */

        SharedPreferences preferences = getSharedPreferences(Variables.latlang_pref,MODE_PRIVATE);

        destination_location +=preferences.getString("enlem","");
        destination_location +=","+preferences.getString("boylam","");

        if(!runtime_permission())
            enable_buttons();
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Lütfen Bekleyin\n",
                "Rota Hesaplanıyor..!",true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            haritam.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(haritam.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(haritam.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(haritam.addPolyline(polylineOptions));

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        haritam = googleMap;
        LatLng kayseri = new LatLng(38.386278, 35.492189);
        haritam.moveCamera(CameraUpdateFactory.newLatLngZoom(kayseri,18));
        originMarkers.add(haritam.addMarker(new MarkerOptions().title("Lise").position(kayseri)));
        if(ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
            ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        haritam.setMyLocationEnabled(true);
    }

    private void sendRequest() {
        try {
            new DirectionFinder(this,source_location,destination_location).execute();
        }catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void enable_buttons()
    {
        btn_navigasyon_baslat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ı = new Intent(getApplicationContext(),LocationService1.class);
                startService(ı);
                Toast.makeText(getApplicationContext(),source_location,Toast.LENGTH_LONG).show();
            }
        });
        btn_navigasyon_durdur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ı = new Intent(getApplicationContext(),LocationService1.class);
                stopService(ı);
            }
        });
    }

    private boolean runtime_permission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                )
        {
            requestPermissions(new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION
            },100);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                enable_buttons();
            }
            else
                runtime_permission();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.action_cikis)
        {
            SharedPreferences preferences = getSharedPreferences(Variables.giris_pref,MODE_PRIVATE);
            SharedPreferences preferences1 = getSharedPreferences(Variables.common_pref,MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            SharedPreferences.Editor editor1 = preferences1.edit();

            editor.clear();
            editor.commit();
            editor1.clear();
            editor1.commit();

            Toast.makeText(getApplicationContext(),"Çıkış Yapılıyor.",Toast.LENGTH_LONG).show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    }catch (Exception e){}
                }
            }).start();

            Intent ı = new Intent(getApplicationContext(),GirisActivity.class);
            startActivity(ı);
        }
        return true;
    }
}
