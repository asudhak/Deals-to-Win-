package edu.wlan.deals;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
    	(new ReverseGeocodingTask(this)).execute(new Location[] {LocationRepresenter.location});
    	
    	mHandler=new Handler()
    	{
    		public void HandleMessage(Message msg)
    		{
    			Log.d("MESSAGE FROM HANDLER", msg.toString());
    		}
    	};
    	
    }
    
    Handler mHandler;
    
    
    private class ReverseGeocodingTask extends AsyncTask<Location, Void, Void> {
        private static final int UPDATE_ADDRESS = 0;
		Context mContext;

        public ReverseGeocodingTask(Context context) {
            super();
            mContext = context;
        }

    
    @Override
    protected Void doInBackground(Location... params) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

        Location loc = params[0];
        List<Address> addresses = null;
        try {
            // Call the synchronous getFromLocation() method by passing in the lat/long values.
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
            // Update UI field with the exception.
            Message.obtain(mHandler, UPDATE_ADDRESS, e.toString()).sendToTarget();
            Log.d("ADDRESS: ", "ERROR");
        }
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            // Format the first line of address (if available), city, and country name.
            String addressText = String.format("%s, %s, %s",
                    address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                    address.getLocality(),
                    address.getCountryName());
            // Update the UI via a message handler.
            Message.obtain(mHandler, UPDATE_ADDRESS, addressText).sendToTarget();
            Log.d("ADDRESS: ", addressText);
        }
        return null;
    }

       
    }
}


