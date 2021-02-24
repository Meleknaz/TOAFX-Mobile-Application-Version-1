package com.farmx;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.farmx.api.WeatherAPI;
import com.farmx.location.CurrentLocation;
import com.farmx.model.RetrofitInstance;
import com.farmx.model.WeatherApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView tvCityName;
    TextView tvPressure;
    TextView tvHumidity;
    TextView tvDegree;
    TextView tvDesc;
    TextView tvRealFeel;
    TextView tvWind;
    public static Button btnGetData;
    EditText edtCityName;

    public static FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupWidget();

        //Initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check condition
                if (ActivityCompat.checkSelfPermission(MainActivity.this
                        , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //when both permission are granted
                    //call method
                    getCurrentLocation();
                } else {
                    //when permission denied
                    ActivityCompat.requestPermissions(MainActivity.this
                            , new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                                    , Manifest.permission.ACCESS_COARSE_LOCATION}
                            , 100);
                }
            }
        });
    }

    private void setupWidget() {
        tvCityName = findViewById(R.id.tvCityName);
        tvPressure = findViewById(R.id.tvPressure);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvDegree = findViewById(R.id.tvWeather);
        tvDesc = findViewById(R.id.tvDescription);
        tvRealFeel = findViewById(R.id.tvRealFeel);
        tvWind = findViewById(R.id.tvWind);
        btnGetData = findViewById(R.id.btnGetCity);
        edtCityName = findViewById(R.id.edtCityName);
        //TextView tvCityName = findViewById(R.id.tvCityName);
    }


    private void setupApi(WeatherApi items) {
        tvCityName.setText(items.getName());
        tvDegree.setText((items.getMain().getTemp() + "").substring(0, 2));
        tvPressure.setText(items.getMain().getPressure() + "");
        tvHumidity.setText(items.getMain().getHumidity() + "");
        tvRealFeel.setText(items.getMain().getFeelsLike() + "");
        tvDesc.setText(items.getWeather().get(0).getDescription() + "");
        tvWind.setText(items.getWind().getSpeed() + " km/s");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //check condition
        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1]
                == PackageManager.PERMISSION_GRANTED)) {
            //When permission granted
            //call method
            getCurrentLocation();
        } else {
            //when permission are denied
            //display toast
            Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {
        //Initialize location manager
        LocationManager locationManager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE
        );
        //Check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //when location service is enabled
            //Get last location
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //Initialize location
                    Location location = task.getResult();
                    //Check condition
                    if (location != null) {
                        Toast.makeText(getApplicationContext(), "lat ->" + (int)location.getLatitude() + "\n" + "lon ->" + (int)location.getLongitude(), Toast.LENGTH_SHORT).show();
                        Call<WeatherApi> a = RetrofitInstance.instance.makeCall(
                                (int)location.getLatitude(),
                                (int)location.getLongitude()
                        );
                        a.enqueue(new Callback<WeatherApi>() {

                            @Override
                            public void onResponse(Call<WeatherApi> call, Response<WeatherApi> response) {
                                setupApi(response.body());
                            }

                            @Override
                            public void onFailure(Call<WeatherApi> call, Throwable t) {
                                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        //when location result is null
                        //Initialize location request
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        //Initialize location call back
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                //Initialize location
                                Location location1 = locationResult.getLastLocation();

                            }
                        };
                        //Request location updates
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest
                                , locationCallback, Looper.myLooper());
                    }
                }
            });
        } else {
            //when location services is not enabled
            //open location setting
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}