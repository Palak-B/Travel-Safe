package com.microsoft.CognitiveServicesExample;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class Result extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationClient;
    double l1,l2;
    Button button;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        button=(Button)findViewById(R.id.button5);
        tv=(TextView)findViewById(R.id.textView3);
        Bundle bs=new Bundle();
        bs=getIntent().getExtras();
        final String a=bs.getString("lat");
        final String b=bs.getString("lon");
        final String p=bs.getString("pl");
        final double c=Double.parseDouble(a);
        final double d=Double.parseDouble(b);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double d1=l1-c;
                double d2=l2-d;
                if(d1<1.0 && d2<1.0)
                {
                    tv.setText("The person is near you please visit "+p+" for saving the person.");
                }
                else if(d1<3.0 && d2<3.0)
                    tv.setText("The person is at a fair distance, please visit "+p+" for saving the person.");
                else
                    tv.setText("The person is very far from you. If you can please visit "+p+" for saving the person.");
            }
        });
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                //Toast.makeText(MainActivity.this, "HelloWor", Toast.LENGTH_SHORT).show();
                if (location != null) {
                    //e1.setText(location.getLatitude()+"and"+location.getLongitude());
                    //Toast.makeText(MainActivity.this,location.getLatitude()+"and"+location.getLongitude(),Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this,location.getLatitude()+"",Toast.LENGTH_SHORT).show();
                    l1=location.getLatitude();
                    l2=location.getLongitude();
                    //Toast.makeText(MainActivity.this,location.getLongitude()+"",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this,loc+"",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                    // Logic to handle location object
                }
            }
        });

    }
}
