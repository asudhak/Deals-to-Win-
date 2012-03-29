package edu.wlan.deals;

import java.util.List;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class LocationRepresenter extends MapActivity {
    /** Called when the activity is first created. */
	static LocationManager locationManager;
	public static Location location;
	public static int profile;
	static MapController mapController;
	static LocPositionOverlay positionOverlay;
	public static Double geoLat, geoLong;
	
	
	
		 
	 @Override
	  protected boolean isRouteDisplayed() {
	    return false;
	  }
	
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab2);
     
        
        //MAP CODE=================================================
        final MapView myMapView = (MapView) findViewById(R.id.myMapView);
        mapController = myMapView.getController();

        // Configure the map display options
        myMapView.setSatellite(true);

        // Zoom in
        mapController.setZoom(17);

        // Add the AlertsPositionOverlay
        positionOverlay = new LocPositionOverlay(this);
        List<Overlay> overlays = myMapView.getOverlays();
        overlays.add(positionOverlay);
        
        
        
     	
        
    }
    
    public void refresh1(View v)
    {
    	TextView location = (TextView)findViewById(R.id.location);
    	location.setText("Lat: "+  LocationRepresenter.location.getLatitude()+ " \n" + "Long: "+ LocationRepresenter.location.getLongitude());
    	
    	
    }

       
    }


