package bolona_pig.proj_imgapp.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import bolona_pig.proj_imgapp.R;

public class Maps2Activity extends FragmentActivity implements OnMapReadyCallback {

    LatLng latlng;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String latlngStr = getIntent().getExtras().getString("latlng");
        if (!latlngStr.isEmpty() && latlngStr.startsWith("[Lat/Lng] : ")) {
            latlngStr = latlngStr.substring(13, latlngStr.length() - 1);
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

        mMap.addMarker(new MarkerOptions().position(latlng).title("สถานที่หายตัว"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));
    }
}
