package bolona_pig.proj_imgapp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import bolona_pig.proj_imgapp.R;

public class Maps2Activity extends AppCompatActivity implements OnMapReadyCallback {

    LatLng latlng;
    String mode;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Locale.setDefault(new Locale("th_TH"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        String latlngStr = getIntent().getExtras().getString("latlng");
        mode = getIntent().getExtras().getString("mode");
        if (!latlngStr.isEmpty() && latlngStr.startsWith("[พิกัด] : ")) {
            latlngStr = latlngStr.substring(11, latlngStr.length() - 1);
            String[] latlong = latlngStr.split(",");
            double latitude = Double.parseDouble(latlong[0]);
            double longitude = Double.parseDouble(latlong[1]);
            latlng = new LatLng(latitude, longitude);
        }
        if (latlng == null) {
            Toast.makeText(this, "ไม่พบพิกัดของสถามที่", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Marker locationMarker;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));
        if (mode.equals("notice"))
            locationMarker = mMap.addMarker(new MarkerOptions().position(latlng).title("สถานที่หายตัว"));
        else
            locationMarker = mMap.addMarker(new MarkerOptions().position(latlng).title("สถานที่พบเห็น"));
        locationMarker.showInfoWindow();
    }

}
