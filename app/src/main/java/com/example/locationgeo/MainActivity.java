package com.example.locationgeo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView tv,addr;
    LocationManager lm;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.latlng);
        addr = findViewById(R.id.addr);
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                tv.setText("lat: " + lat + " " +"lon: " + lon);
                getAddress(lat,lon);
                lm.removeUpdates(locationListener);
            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},101);
        }else{
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if((requestCode == 101) && (grantResults.length > 0)){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                }

            }
        }
    }
    private void getAddress(double lat, double lon){
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(lat,lon,1);
            if(addressList.size() > 0){
                Address address = addressList.get(0);
                StringBuffer sb = new StringBuffer();
                if(address.getMaxAddressLineIndex() >0) {
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i) + "\n");
                    }
                    addr.append(sb.toString());

                }
                else {
                    addr.append(address.getAddressLine(0) );//+ "\n"+address.toString());
                }
                addr.append("\n" +address.getLocality()+ "\n" +address.getAdminArea() +"\n" +address.getSubAdminArea()+ "\n" +address.getCountryCode()
                +"\n" +address.getCountryName()+ "\n"+address.getPostalCode());
            }else {
                addr.append("No matching address  found");
            }
        }catch (Exception e){
            addr.append(e.toString());
        }
    }
}