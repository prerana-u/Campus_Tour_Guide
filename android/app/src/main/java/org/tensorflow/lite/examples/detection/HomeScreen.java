package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.tensorflow.lite.examples.detection.data.DatabaseHandler;

public class HomeScreen extends AppCompatActivity {

    FusedLocationProviderClient mFusedLocationClient;
    String lat, long1;
    Button arbtn;
    Boolean isAllFabsVisible;
    private GpsTracker gpsTracker;
    // Initializing other items
    // from layout file
    FloatingActionButton b, savefab, signupfab;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;
    int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        DatabaseHandler db = new DatabaseHandler(this);
        // arbtn=findViewById(R.id.arButton);
        // method to get the location
        getLastLocation();
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        b=findViewById(R.id.editfab);
        savefab= findViewById(R.id.savefab);
        signupfab=findViewById(R.id.signupfab);
        savefab.setVisibility(View.GONE);
        signupfab.setVisibility(View.GONE);
        fabOpen = AnimationUtils.loadAnimation
                (this,R.anim.from_bottom_animation);
        fabClose = AnimationUtils.loadAnimation
                (this,R.anim.to_bottom_animation);
        rotateForward = AnimationUtils.loadAnimation
                (this,R.anim.anim2);
        rotateBackward = AnimationUtils.loadAnimation
                (this,R.anim.anim1);
        isAllFabsVisible = false;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
                if (!isAllFabsVisible) {

                    // when isAllFabsVisible becomes
                    // true make all the action name
                    // texts and FABs VISIBLE.
                    savefab.show();
                    signupfab.show();
                    signupfab.setClickable(true);
                    savefab.setClickable(true);
                    isAllFabsVisible = true;
                } else {

                    savefab.setVisibility(View.GONE);
                    signupfab.setVisibility(View.GONE);
                    isAllFabsVisible = false;
                }
            }

        });
        savefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(HomeScreen.this, DetectorActivity.class);
                startActivity(i);
            }
        });

        signupfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(HomeScreen.this, DetectorActivity.class);
                startActivity(i);
            }
        });
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent i = new Intent(SplashScreen.this, Logcat.class);
//                startActivity(i);
//                finish();
//            }
//        },2000);
    }
    private void animateFab(){
        if (isAllFabsVisible){
            b.startAnimation(rotateForward);
            savefab.startAnimation(fabClose);
            signupfab.startAnimation(fabClose);
            savefab.setClickable(false);
            signupfab.setClickable(false);
        }else {
            b.startAnimation(rotateBackward);
            savefab.startAnimation(fabOpen);
            signupfab.startAnimation(fabOpen);
            signupfab.setClickable(true);
            savefab.setClickable(true);

        }

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                          //  Toast.makeText(getApplicationContext(),location.getLatitude() + "",Toast.LENGTH_SHORT).show();
                          //  Toast.makeText(getApplicationContext(),location.getLongitude() + "",Toast.LENGTH_SHORT).show();
                            lat=String.valueOf(location.getLatitude());
                            long1=String.valueOf(location.getLongitude());

                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            Toast.makeText(getApplicationContext(),mLastLocation.getLatitude() + "",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),mLastLocation.getLongitude() + "",Toast.LENGTH_SHORT).show();
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;


    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

}