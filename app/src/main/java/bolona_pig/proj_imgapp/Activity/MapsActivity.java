package bolona_pig.proj_imgapp.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import bolona_pig.proj_imgapp.CallBack.GetBooleanCallBack;
import bolona_pig.proj_imgapp.ObjectClass.PermissionUtils;
import bolona_pig.proj_imgapp.R;

public class MapsActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, PlaceSelectionListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    final String TAG = "custom_check";
    Context context = this;
    TextView addressText, setLocation;
    LatLng latlng, clatlng;
    MapFragment mapFragment;
    PlaceAutocompleteFragment autocompleteFragment;
    boolean firstChange = true, latlngParameter = false;
    FrameLayout frameLayout;
    private boolean mPermissionDenied = false;
    private GoogleApiClient googleApiClient;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Locale.setDefault(new Locale("th_TH"));

        setContentView(R.layout.activity_maps);

        addressText = (TextView) findViewById(R.id.addressText);
        setLocation = (TextView) findViewById(R.id.locationMarkertext);

        mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);

        View mapView = mapFragment.getView();
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 20, 20);
        }

        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);

        frameLayout = (FrameLayout) findViewById(R.id.fm);
        frameLayout.bringToFront();

        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("custom_check", latlng.toString());
                //Toast.makeText(MapsActivity.this, latlng.toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("lat", latlng.latitude);
                intent.putExtra("lng", latlng.longitude);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
        String latlngStr = getIntent().getExtras().getString("latlng");
        if (!latlngStr.isEmpty() && latlngStr.startsWith("[พิกัด] : ")) {
            latlngStr = latlngStr.substring(11, latlngStr.length() - 1);
            String[] latlong = latlngStr.split(",");
            double latitude = Double.parseDouble(latlong[0]);
            double longitude = Double.parseDouble(latlong[1]);
            latlng = new LatLng(latitude, longitude);
            Toast.makeText(this, latlng.toString(), Toast.LENGTH_SHORT).show();
            latlngParameter = true;
            firstChange = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            PermissionUtils.PermissionDeniedDialog.newInstance(true).show(getSupportFragmentManager(), "dialog");
            mPermissionDenied = false;
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {

        this.mMap = map;

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        enableMyLocation();

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.setOnCameraChangeListener(new OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition position) {

                addressText.setText("กำลังระบุตำแหน่ง");

                latlng = position.target;

                //Log.i("custom_check", "[" + position.target.latitude + "," + position.target.longitude + "]");

                new ReverseGeoCodingTask(position.target, new GetBooleanCallBack() {
                    @Override
                    public void done(Boolean flag, String resultStr) {
                        addressText.setText(resultStr);
                    }
                }).execute();
            }
        });

        if (latlngParameter) {
            Log.i("custom_check", "Move To " + latlng.toString());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 14));
        }

    }

    private void enableMyLocation() {
        boolean isCheckFineLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        boolean isCheckCoarseLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        if (isCheckFineLocation || isCheckCoarseLocation) {
            if (isCheckFineLocation)
                PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION, true);
            if (isCheckCoarseLocation)
                PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_COARSE_LOCATION, true);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setFastestInterval(16).setInterval(5000);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (firstChange) {
            firstChange = false;
            LatLng temp = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(temp, 14));
        }
    }

    @Override
    public void onPlaceSelected(Place place) {
        Log.i(TAG, "Place: " + place.getName());
        latlng = place.getLatLng();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16));
    }

    @Override
    public void onError(Status status) {
        Log.i(TAG, "An error occurred: " + status);
    }

    private class ReverseGeoCodingTask extends AsyncTask<Void, Void, Boolean> {
        LatLng latlng;
        GetBooleanCallBack booleanCallback;
        String str;

        public ReverseGeoCodingTask(LatLng latlng, GetBooleanCallBack booleanCallback) {
            this.latlng = latlng;
            this.booleanCallback = booleanCallback;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1);
            } catch (IOException e) {
                Log.e("custom_check", e.toString());
            }

            if (addresses != null && addresses.size() > 0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    if (returnedAddress.getMaxAddressLineIndex() == (i - 1)) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i));
                    } else {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(",");
                    }
                }
                str = strReturnedAddress.toString();
                //Log.e("custom_check", str);
            }

            return Boolean.TRUE;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            booleanCallback.done(bool, str);
            super.onPostExecute(bool);
        }
    }

}