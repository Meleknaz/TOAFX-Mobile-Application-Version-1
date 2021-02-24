package com.farmx.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.farmx.MainActivity;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class CurrentLocation extends MainActivity {

        //Initialize variable
        public static int tvLatitude, tvLongitude;

        public  CurrentLocation(){

            getCurrentLocation();
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
                            //when location result is not null set latitude
                            System.out.println(""+tvLatitude);
                            //tvLatitude.setText(String.valueOf(location.getLatitude()));
                            //set longitude
                            System.out.println(""+tvLongitude);
                            //tvLongitude.setText(String.valueOf(location.getLongitude()));
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
                                    //set latitude
                                    //tvLatitude.setText(String.valueOf(location1.getLatitude()));
                                    System.out.println(""+tvLatitude);
                                    //set longitude
                                    //tvLongitude.setText(String.valueOf(location1.getLongitude()));
                                    System.out.println(""+tvLongitude);
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
