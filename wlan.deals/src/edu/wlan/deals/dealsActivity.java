package edu.wlan.deals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import edu.wlan.deals.R;

//import org.apache.xmlrpc.XmlRpcException;
import org.apache.ws.commons.util.Base64;
import org.apache.ws.commons.util.Base64.DecodingException;
import org.xmlrpc.android.*;

import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.ws.commons.util.Base64;
public class dealsActivity extends Activity {
    /** Called when the activity is first created. */
	
	
	public XMLRPCClient client;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1);
        
        client = new XMLRPCClient("http://192.168.3.1:8080/xmlrpc");
        InetAddress addr=null;
        try {
			addr=InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        Log.i("IP address:",""+addr.toString());
         
    }
    
    public void getData(View V)
    {

        	final Context toastContext=getBaseContext();
//        	Context c=this.getBaseContext();
        	   final Dialog dialog = new Dialog(this);
               dialog.setContentView(R.layout.alert_dialog_layout);
               dialog.setTitle("Select Preferneces");
               dialog.setCancelable(true);
              
               
             final RadioGroup mRadioGroup = (RadioGroup) dialog.findViewById(R.id.group1);
             mRadioGroup.check(R.id.food);
             final SeekBar dia = (SeekBar)dialog.findViewById(R.id.seekbar);
             
             Button button_get = (Button) dialog.findViewById(R.id.getDeals);
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
                          String selected_pref_str=checkedRadioButton.getText().toString();
                          //Toast.makeText(toastContext, "The Choice is: "+checkedRadioButton.getText().toString()+" with dia:" + dia_location, 5).show();
                     
                          //STORE PARAMS
                          Object[] params=new Object[5];
//                      			params[0]=LocationRepresenter.location.getLatitude();
//                      			params[1]= LocationRepresenter.location.getLongitude();
                      			params[2]=dia_location;
                      			params[3]=selected_pref_str;
                      			
                          //END OF STORE PARAMS
                          
                          
                          //RPC CALL
                                                   makeRPCcall(params);
                          //END OF RPC CALL
                          
                          dialog.dismiss();
                     }
               });   
               
                   
               dialog.show();

              
    }
    
    public void makeRPCcall(Object[] params)
    {
//    	 Object result=0;
//    	ArrayList<Map<String, String>> result=new ArrayList<Map<String, String>>();
//    	HashMap result= new HashMap();4
//    	Object[] result= new Object[3];
    	
    	//NEED TO CHANGE HERE, need to add location
    	
    	
    	
         Object[] params1 = new Object[]{new Integer(33), new Integer(9)};
       ArrayList list=new ArrayList();
//         params=null;
         try {
     		
     		Object[] result = (Object[])client.callEx("queryDataBase.queryDB", params1);
     		Toast.makeText(getBaseContext(), "Try", 4).show();
     		 for(int i=0;i<result.length;i++)
             {
            	 list.add(result[i]);
             }
     		
     	}
         
         catch (XMLRPCException e) {
     		// TODO Auto-generated catch block
     		Toast.makeText(getBaseContext(), e.getMessage(), 4).show();
     		e.printStackTrace();
     	}
         /*
      
       try {
		Object result = (Object)client.callEx("Calculator.add", params);
	} catch (XMLRPCException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
        
     	
//         HashMap h1=(HashMap)result;
//         Toast.makeText(getBaseContext(), "Result: " + result, 4).show();
         
//         ArrayList list=(ArrayList)result;
         
         
         updateListView(list);
         
         //System.out.print(""+h1.toString());
         
//         Toast.makeText(getBaseContext(), "Result: " + result, 4).show();
         
    }
    
    
    public void updateListView(ArrayList list)
    {
    	   
   	    
   	    String[] from = { "Name", "Type", "Dist" , "Image"};
   		int[] to = {R.id.name, R.id.type ,R.id.dist , R.id.image};
   		
   		/*
   		ArrayList list=new ArrayList();
   		list.add(h);
   		list.add(h);
   		list.add(h);
  */
   		
//   		System.out.println(list.toString());
   	    
   	    SimpleAdapter adapter = new SimpleAdapter(this, list,
   				R.layout.row, from, to);
   	    
   	    final ListView listView = (ListView) findViewById(R.id.listView);
   	    
   	    listView.setAdapter(adapter);
   	      listView.setTextFilterEnabled(true);
   	      
   	   listView.setOnItemClickListener(new  OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                   long arg3) {
               
               Toast.makeText(getBaseContext(), ((TextView)arg1.findViewById(R.id.name)).getText(),4).show();
               popDialog((((TextView)arg1.findViewById(R.id.name)).getText()).toString(),
            		   (((TextView)arg1.findViewById(R.id.type)).getText()).toString(),
            		   (((TextView)arg1.findViewById(R.id.dist)).getText()).toString(),
            		   (((TextView)arg1.findViewById(R.id.image)).getText()).toString());
           }

          });

   	     
   }
    public void popDialog(String name,String type,String mile, String base64img) {
        final String n=name, t=type, m=mile;
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_dialog_layout_add);
        dialog.setTitle("Coupon Details");
        dialog.show();  
        
        //RPC CALL FOR IMAGE ( ASYNC TASK)
        
        base64img=new getImage().doInBackground(1);
        
        
        //GET IMAGE
        
        //IMAGE CODE
       
        byte[] imgBytes = null;
		try {
			imgBytes = getImage(base64img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
        BitmapFactory Bm= new BitmapFactory();
        Bitmap pic=Bm.decodeByteArray(imgBytes,0,imgBytes.length);
        
        
        
        
        	ImageView image = (ImageView) dialog.findViewById(R.id.imageView1);
        	image.setImageBitmap(pic);
        
        
        
        
        //END OF IMAGE CODE
        
        
        
        dialog.setCancelable(true);
        Button btn = (Button) dialog.findViewById(R.id.Add);
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
                 
                   db.insert(n, t, Double.parseDouble(m));
                 Toast.makeText(getBaseContext(), "Coupon Bought",4).show();
                 dialog.dismiss();
                 
            }
         });
      
   }

   	      private byte[] getImage(String str) throws IOException
   	      {
   	    	Base64 decoder = new Base64();   
   	     byte[] imgBytes = decoder.decode(str); 
   	     return imgBytes;
   	      }
    
   	   public class getImage extends AsyncTask<Integer, Void, String>{
   		
   		@Override
   		protected String doInBackground(Integer... deal_id) {
			
   			String base64img = "";
			try {
				base64img = (String)client.callEx("queryDataBase.getImage", deal_id);
			} catch (XMLRPCException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			        
			
			
   			return base64img;
   			

   		}   		
   		  	   }
 
    
    
}