package edu.wlan.deals;


import edu.wlan.deals.R;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.Toast;


public class mainUITabs extends TabActivity {
	public static TabHost mTabHost;
	

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
}
