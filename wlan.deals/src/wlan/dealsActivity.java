package wlan;

import java.net.InetAddress;
import java.net.UnknownHostException;

import edu.wlan.deals.R;

//import org.apache.xmlrpc.XmlRpcException;
import org.xmlrpc.android.*;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class dealsActivity extends Activity {
    /** Called when the activity is first created. */
	
	public XMLRPCClient client;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
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
        
        
        Integer result=0;
        Object[] params = new Object[]{new Integer(33), new Integer(9)};
        try {
    		//result = (Integer) client.execute("Calculator.add", params);
    		result = (Integer)client.callEx("Calculator.add", params);
    		Toast.makeText(getBaseContext(), "Try", 4).show();
    		
    	} catch (XMLRPCException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
        
        System.out.print(""+result);
        Log.d("REsult", ""+result);
        Toast.makeText(getBaseContext(), "Result: " + result, 4).show();
        
        
        
    }
}