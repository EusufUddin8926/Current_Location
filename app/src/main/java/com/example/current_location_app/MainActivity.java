package com.example.current_location_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    TextView addressTxt,cityTxt,countryTxt,latitudeTxt,longitudeTxt;
    Button currentLocBtnId;
    FusedLocationProviderClient providerClient;
    private final static int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addressTxt = findViewById(R.id.addressTxt);
        cityTxt = findViewById(R.id.cityTxt);
        countryTxt = findViewById(R.id.countryTxt);
        latitudeTxt = findViewById(R.id.latitudeTxt);
        longitudeTxt = findViewById(R.id.longitudeTxt);
        currentLocBtnId = findViewById(R.id.CurrentLocBtnId);

        providerClient = LocationServices.getFusedLocationProviderClient(this);

        currentLocBtnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });



    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {

            providerClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null){
                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                try {
                                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    latitudeTxt.setText("Lagitude : "+ addressList.get(0).getLatitude());
                                    longitudeTxt.setText("Longitude : "+ addressList.get(0).getLongitude());
                                    countryTxt.setText("Longitude : "+ addressList.get(0).getCountryName());
                                    addressTxt.setText("Longitude : "+ addressList.get(0).getAddressLine(0));
                                    cityTxt.setText("Longitude : "+ addressList.get(0).getLocality());
                                    
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    });

        }else {
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}