package edu.wlan.deals;

import java.util.ArrayList;
import java.util.Map;

import org.xmlrpc.android.XMLRPCException;

import edu.wlan.deals.R;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyDeals extends Activity {
public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.tab3);
	    showCoupons();     
 }
public void onResume(){
	super.onResume();
	showCoupons();
}
public void showCoupons(){
	 DatabaseHelper db = new DatabaseHelper(this);
	 ArrayList list = new ArrayList();
	  try {
   		
		list = db.getCoupons();
   			
   	} catch (Exception e) {
   		// TODO Auto-generated catch block
   		e.printStackTrace();
   	}
	updateListView(list);
 }
public void updateListView(ArrayList list){
	 String[] from = { "Name", "Type", "Dist" };
		int[] to = {R.id.name, R.id.type ,R.id.dist};
		
		System.out.println(list.toString());
	    
	    SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.row, from, to);
	    
	   final ListView listView = (ListView) findViewById(R.id.listView);
	    
	    listView.setAdapter(adapter);
	    
	    listView.setOnItemClickListener(new  OnItemClickListener() {
	    	
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				Toast.makeText(getBaseContext(), ((TextView)arg1.findViewById(R.id.name)).getText(),4).show();
				popDialog((((TextView)arg1.findViewById(R.id.name)).getText()).toString(), (((TextView)arg1.findViewById(R.id.type)).getText()).toString(),(((TextView)arg1.findViewById(R.id.dist)).getText()).toString());
			}

	    });

  }
public void popDialog(String name,String type,String mile) {
	 final String n=name, t=type, m=mile;
	 final Dialog dialog = new Dialog(this);
    dialog.setContentView(R.layout.alert_dialog_layout_mydeals);
    dialog.setTitle("Coupon Details");
    dialog.setCancelable(true);
    Button btn = (Button) dialog.findViewById(R.id.Delete);
    Button btn1 = (Button) dialog.findViewById(R.id.dismiss);
    btn1.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			DatabaseHelper db = new DatabaseHelper(getBaseContext());
	
		dialog.dismiss();	
		}
	});
    
     btn.setOnClickListener(new OnClickListener(){
         public void onClick(View v){   
       	  DatabaseHelper db = new DatabaseHelper(getBaseContext());
       	  
         	  db.deleteCoupon(n, t, m);
         	  
         	Toast.makeText(getBaseContext(), "Coupon Deleted",4).show();
         	dialog.dismiss();
         	showCoupons();
       	  
        }
     });
  dialog.show();  
}
}

