package edu.wlan.deals;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;



public class updateRingerService extends Service {
	static String globalPlace="";
	static int globalAllowemail=0;
	
	LocationListener locationListener;
	LocationListener GPSListener;
	
	
	
	
//	LocationManager locationManager;
//	public static Location location;
	
		private static final String TAG = "LocationService";
		public AudioManager myAM;
		
		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}
	
		@Override
		public void onCreate() {
//			Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
			Log.d(TAG, "onCreate");
			}

		@Override
		public void onDestroy() {
			Toast.makeText(this, "Location Services Turned off. Profiles will not be updated", Toast.LENGTH_LONG).show();
			Log.d(TAG, "onDestroy");
			
			LocationRepresenter.locationManager.removeUpdates(GPSListener);
			LocationRepresenter.locationManager.removeUpdates(locationListener);
			
			stopSelf();
			super.onDestroy();
			stopSelf();
			
		}
		
		
		
		@Override
		public void onStart(Intent intent, int startid) {
//			Toast.makeText(this, "Now Listening", Toast.LENGTH_LONG).show();
			Log.d(TAG, "onStart");
//			locationClick();
			
			final class MyLocationListener implements LocationListener {

			      @Override
			      public void onLocationChanged(Location location1) {
//			    	  locationClick();
//			    	  updateProvider(this, "best");
			    	  try{updateWithNewLocation(location1);}catch(Exception NullPointerException){Log.d("NULL","Exception");};
			    	  		          
			      }

			      @Override
			      public void onProviderDisabled(String provider) {
			         //GPSstatus("GPS DISABLED");//Added for Debugging purposes
			      }

			      @Override
			      public void onProviderEnabled(String provider) {
			         //UNUSED
			    	  updateProvider(this, provider);
			      }

			      @Override
			      public void onStatusChanged(String provider, int status, Bundle extras) {
			        //UNUSED
			      }
			}
//			updateProvider(locationListener ,"best");
						
			locationListener=new MyLocationListener();	
			GPSListener=new MyLocationListener();
			
			
			
			
			updateProvider(locationListener, "network");
			updateProvider(GPSListener, "gps");
			
		}
		
		public static void refresh()	
		{
			try{
			LocationRepresenter.location = LocationRepresenter.locationManager.getLastKnownLocation("network");}
			catch(Exception NullPointerException)
			{
				
			}
		}
		
		
		public void updateProvider(LocationListener locationListener, String provider)
		{
			
	        LocationRepresenter.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
	        /*
	        Criteria criteria = new Criteria();
	        criteria.setAccuracy(Criteria.ACCURACY_FINE);
	        criteria.setAltitudeRequired(false);
	        criteria.setBearingRequired(false);
	        criteria.setCostAllowed(true);
//	        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
	        */
	        int interval=0;
	        
	        if(provider.compareTo("gps")==0)
	        	interval=60000;
	        
	        
	        LocationRepresenter.location = LocationRepresenter.locationManager.getLastKnownLocation(provider);
	        
	        
	        
	        LocationRepresenter.locationManager.requestLocationUpdates(provider, interval, 0, locationListener); 
//	        Toast.makeText(getBaseContext(), "Provider: " + provider, 3).show();
		}
		

		public void updateWithNewLocation(Location location1) {   
	    	
			@SuppressWarnings("unused")
			String latLongString = null;
		    if (location1 != null) {
		      
		    	LocationRepresenter.location=location1;
		    	
		      // Update the map location.
		    	LocationRepresenter.geoLat = LocationRepresenter.location.getLatitude() * 1E6;
		        LocationRepresenter.geoLong = LocationRepresenter.location.getLongitude() * 1E6;
		        GeoPoint point = new GeoPoint(LocationRepresenter.geoLat.intValue(), LocationRepresenter.geoLong.intValue());

		        LocationRepresenter.mapController.animateTo(point);

		        // update my position marker
		        LocationRepresenter.positionOverlay.setLocation(LocationRepresenter.location);

		      double lat = LocationRepresenter.location.getLatitude();
		      double lng = LocationRepresenter.location.getLongitude();
		      
		      latLongString=lat+" "+lng;
		      Log.d("Lat","lat"+LocationRepresenter.geoLat);
		      Log.d("Lng","lng"+LocationRepresenter.geoLong);
		      
		      setLoc(lat,lng);
		      
		    
		           
		      
//		      Toast.makeText(getBaseContext(),"Your Location is: "+ latLongString, 3).show();
		      
		      		      
		      }
		    }

		  public static void setLoc(double lat, double lng) {
		  		// TODO Auto-generated method stub
		  		
		  	}

}
	
	
	
	
	
	
	
	
	
	
	
	

