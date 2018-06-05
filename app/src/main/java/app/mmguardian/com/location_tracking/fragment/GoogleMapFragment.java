package app.mmguardian.com.location_tracking.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import app.mmguardian.com.location_tracking.R;

public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {

    public static final String EXTRA_LATITUDE = "EXTRA_LATITUDE";
    public static final String EXTRA_LONGITUDE = "EXTRA_LONGITUDE";

    MapView mMapView;
    GoogleMap mGoogleMap;
    double latitude, longitude;


    public static GoogleMapFragment newInstance(double latitude, double longitude){
        GoogleMapFragment instance = new GoogleMapFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble(EXTRA_LATITUDE , latitude);
        bundle.putDouble(EXTRA_LONGITUDE , longitude);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map_view, container, false);

        mMapView = v.findViewById(R.id.mapview);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        latitude = getArguments().getDouble(EXTRA_LATITUDE);
        longitude = getArguments().getDouble(EXTRA_LONGITUDE);

    }

        @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
        );
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
        googleMap.animateCamera(cameraUpdate);
    }


}

