package edu.wlan.deals;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import edu.wlan.deals.R;

//import org.apache.xmlrpc.XmlRpcException;
import org.xmlrpc.android.*;

import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class dealsActivity extends Activity {
    /** Called when the activity is first created. */
	
	
	public XMLRPCClient client;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        
        client = new XMLRPCClient("http://192.168.0.5:8080/xmlrpc");
        InetAddress addr=null;
        try {
			addr=InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        Log.i("IP address:",""+addr.toString());
        Toast.makeText(getBaseContext(), "IP: " + addr.toString(), 4).show();
        
        
        Object result=0;
        Object[] params = new Object[]{new Integer(33), new Integer(9)};
        try {
    		
    		result = (Object) client.callEx("Calculator.add", params);
    		Toast.makeText(getBaseContext(), "Try", 4).show();
    		
    	} catch (XMLRPCException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
        HashMap h1=(HashMap)result;
        
        System.out.print(""+h1.toString());
        
        Toast.makeText(getBaseContext(), "Result: " + result, 4).show();
        
        
        
    }
    
    public void getData(Context c)
    {

        	final Context toastContext=c;
        	   final Dialog dialog = new Dialog(c);
               dialog.setContentView(R.layout.alert_dialog_layout);
               dialog.setTitle("Select Preferneces");
               dialog.setCancelable(true);
               //there are a lot of settings, for dialog, check them all out!

               //set up text
               
             final RadioGroup mRadioGroup = (RadioGroup) dialog.findViewById(R.id.group1);
             mRadioGroup.check(R.id.food);
             final SeekBar dia = (SeekBar)dialog.findViewById(R.id.seekbar);
             
             Button button_get = (Button) dialog.findViewById(R.id.getData);
             Button button_cancel=(Button) dialog.findViewById(R.id.dismiss);
             button_cancel.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    			dialog.dismiss();	
    			}
    		});
             button_get.setOnClickListener(new OnClickListener() {
                  
            	   public void onClick(View v) {
    //CALL ADDTO DB HERE
            		   
                	   
                          
                          int selected_pref=R.id.food;
                          try{selected_pref=mRadioGroup.getCheckedRadioButtonId();}
                          catch(NullPointerException e){
                        	  selected_pref=R.id.food;
                        	  Toast.makeText(toastContext, "No Profile Selected ! Default Profile Set", 2);
                        	
                          };
                          int dia_location=dia.getProgress();
                          
                          RadioButton checkedRadioButton = (RadioButton) mRadioGroup.findViewById(selected_pref);
                          String selected_profile_String=checkedRadioButton.getText().toString();
                          //Toast.makeText(toastContext, "The Choice is: "+checkedRadioButton.getText().toString()+" with dia:" + dia_location, 5).show();
                     
                          Toast.makeText(getBaseContext(), "You selected: "+ selected_profile_String,	 4).show();
                          
                          dialog.dismiss();
                     }
               });   
               
                   
               dialog.show();

       
        
    }
    
}