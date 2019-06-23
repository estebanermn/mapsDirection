package pe.edu.cibertec.mapdirections;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import pe.edu.cibertec.mapdirections.directionhelpers.FetchURL;
import pe.edu.cibertec.mapdirections.directionhelpers.TaskLoadedCallback;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    private Polyline polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng cibertecMiraflores = new LatLng(-12.1222432, -77.0282632);
        LatLng cibertecSanIsidro = new LatLng(-12.0859971, -77.0485066);



        mMap.addMarker(new MarkerOptions().position(cibertecSanIsidro).title("Cibertec").snippet("San Isidro"));
        mMap.addMarker(new MarkerOptions().position(cibertecMiraflores).title("Cibertec").snippet("Miraflores"));
        new FetchURL(this).execute(
                getUrl(cibertecSanIsidro, cibertecMiraflores, "driving"),
                "driving");


//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private String getUrl(LatLng origin, LatLng destination, String mode) {
        //origen
        String originString = "origin=" + origin.latitude + "," + origin.longitude;

        //destino
        String destinationString = "destination=" + destination.latitude + "," + destination.longitude;

        //modo

        String modoString = "mode=" + mode;
        //formato
        String outputFormat = "json";

        //parameters
        String parameters = originString + "&" + destinationString + "&" + mode;

        //url
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + outputFormat + "?"
                + parameters + "&key="
                + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if(polyline != null){
            polyline.remove();
        }
        polyline = mMap.addPolyline((PolylineOptions)values[0]);
    }
}
