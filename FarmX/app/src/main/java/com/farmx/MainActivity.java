package com.farmx;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.farmx.api.RetrofitInstance;
import com.farmx.arduino.ArduinoConnection;
import com.farmx.model.WeatherApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity {

    //TextView tvCityName;
    TextView tvPressure;
    TextView tvHumidity;
    TextView tvDegree;
    TextView tvDesc;
    TextView tvRealFeel;
    TextView tvWind;
    public static Button btnGetData;
    EditText edtCityName;

    public static FusedLocationProviderClient fusedLocationProviderClient;

    LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupWidget();

        double x,y;
        x=-0.5;
        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>();
        for (int i = 0 ; i < 500 ; i++){
            x = x +0.1;
            y = Math.sin(x) ;
            series.appendData(new DataPoint(x,y), true, 500);
        }

        graph.addSeries(series);

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
        //tvCityName = findViewById(R.id.tvCityName);
        tvPressure = findViewById(R.id.tvPressure);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvDegree = findViewById(R.id.tvWeather);
        tvDesc = findViewById(R.id.tvDescription);
        tvRealFeel = findViewById(R.id.tvRealFeel);
        tvWind = findViewById(R.id.tvWind);
        btnGetData = findViewById(R.id.btnGetCity);
        edtCityName = findViewById(R.id.edtCityName);
        TextView tvCityName = findViewById(R.id.tvCityName);
    }


    private void setupApi(WeatherApi items) {
        //tvCityName.setText(items.getName());
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
                        //Toast.makeText(getApplicationContext(), "lat ->" + (int)location.getLatitude() + "\n" + "lon ->" + (int)location.getLongitude(), Toast.LENGTH_SHORT).show();
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

    public void sendData(View view){
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.weather_alert){
            Intent weather_intent = new Intent(MainActivity.this, WeatherAlert.class);
            startActivity(weather_intent);
        }

        else if (item.getItemId() == R.id.cihaz_baglantısı){
            Intent arduino_intent = new Intent(MainActivity.this, ArduinoConnection.class);
            startActivity(arduino_intent);
        }

        else if (item.getItemId() == R.id.satellite_image){
            Intent uydu_intent = new Intent(MainActivity.this, SatelliteImage.class);
            startActivity(uydu_intent);
        }

        else if (item.getItemId() == R.id.arazi_durumu){
            Intent arazi_intent = new Intent(MainActivity.this,AraziDurumu.class);
            startActivity(arazi_intent);
        }

        return super.onOptionsItemSelected(item);
    }

}