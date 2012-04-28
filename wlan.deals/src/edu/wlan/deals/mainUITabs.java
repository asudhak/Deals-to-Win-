package edu.wlan.deals;



import edu.wlan.deals.R;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;



public class mainUITabs extends TabActivity {
	public static TabHost mTabHost;

	public int thisUID = android.os.Process.myUid();

	WifiManager wifiMan;
	WifiInfo wifiInfo;
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    
	    ConnectivityManager cm =
                (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
	
		
	    
	   try{ if( cm.getActiveNetworkInfo().isConnectedOrConnecting()==false)
	    {
	    	Toast.makeText(getBaseContext(), "No Internet ConnectioN Found", 4).show();
	    }}
	   catch(NullPointerException e)
    	  {
    		Toast.makeText(getBaseContext(), "No Internet ConnectioN Found", 4).show();
    		  
    	  }
	    
	    
	    startService(new Intent(this, updateRingerService.class));
	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Reusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, dealsActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("Deals Now").setIndicator("Deals Now",
	                      res.getDrawable(R.drawable.tab1))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, LocationRepresenter.class);
	    spec = tabHost.newTabSpec("Location").setIndicator("Location",
	                      res.getDrawable(R.drawable.tab2)).setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, MyDeals.class);
	    spec = tabHost.newTabSpec("My Deals").setIndicator("My deals",
	                      res.getDrawable(R.drawable.tab3)).setContent(intent);
	    tabHost.addTab(spec);
	 			
	}
	// Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.mainmenu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
 
        switch (item.getItemId())
        {
        case R.id.menu_usage:

    	    wifiMan = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    		wifiInfo = wifiMan.getConnectionInfo();
        	AlertDialog.Builder builder_usage = new AlertDialog.Builder(this);
            
            builder_usage.setTitle("Usage Statistics");
            builder_usage.setMessage("Number of packets sent:" + android.net.TrafficStats.getUidTxBytes(thisUID) + "\n"
            + "Number of packets received:" + android.net.TrafficStats.getUidRxBytes(thisUID)
            +"\n========="
//            +"\n Wifi Signal Level: " +  android.net.wifi.WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 101)
            +"\n Wifi SSID: " +  wifiInfo.getSSID()
            +"\n Wifi LinkSpeed: " +  wifiInfo.getLinkSpeed()
            +"\n Coupons recieved:" + dealsActivity.stats.getString("coupons_r", ""));
            
            AlertDialog alert_usage = builder_usage.create();
            alert_usage.show();
            return true;
 
        case R.id.menu_about:
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setMessage("WLAN DEALS")
        	       .setCancelable(false)
        	             	       .setNegativeButton("OK", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	                dialog.cancel();
        	           }
        	       });
        	AlertDialog alert = builder.create();
        	alert.show();
            return true;
 
        case R.id.menu_help:
        	AlertDialog.Builder builder_help = new AlertDialog.Builder(this);
        	builder_help.setMessage("=====HELP=====\n")
        	       .setCancelable(false)
        	             	       .setNegativeButton("OK", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	                dialog.cancel();
        	           }
        	       });
        	AlertDialog alert_help = builder_help.create();
        	alert_help.show();
            return true;
  
        default:
            return super.onOptionsItemSelected(item);
        }
    }

}
