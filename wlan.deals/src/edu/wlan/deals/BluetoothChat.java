/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//
//
package edu.wlan.deals;
//Added comment
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the main Activity that displays the current chat session.
 */
public class BluetoothChat extends Activity {
    // Debugging
    private static final String TAG = "BluetoothChat";
    private static final boolean D = true;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int IMAGE_READ = 6;
    public String  n;
    public String d;
    public String t;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Layout Views
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;

    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothChatService mChatService = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE +++");

        // Set up the window layout
        if(getIntent().getExtras().getString("sender").equals("sender")){
           setContentView(R.layout.bluetoothsender);
        }          
        else {
        	setContentView(R.layout.bluetooth_recv);
        }

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        } else {
            if (mChatService == null) setupChat();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
              // Start the Bluetooth chat services
              mChatService.start();
            }
        }
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");

       // Initialize the array adapter for the conversation thread
        //     mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        //       mConversationView = (ListView) findViewById(R.id.in);
        //          mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
       // mOutEditText = (EditText) findViewById(R.id.edit_text_out);
       //  mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
       
        
        //mSendButton = (Button) findViewById(R.id.button_send);
        if(getIntent().getExtras().getString("sender").equals("sender")){
        	 TextView tv1= (TextView)findViewById(R.id.dealname);
        	 tv1.setText(getIntent().getExtras().getString("name"));
        	 TextView tv2 = (TextView)findViewById(R.id.desc);
        	 tv2.setText(getIntent().getExtras().getString("desc"));
        	 byte[] i =  getIntent().getExtras().getByteArray("image");
        	 ImageView ii = (ImageView)findViewById(R.id.imageView1);
        	 ii.setImageBitmap(BitmapFactory.decodeByteArray(i, 0, i.length));
        	 
        	 Button btn = (Button) findViewById(R.id.Cancel);
        	 Button btn1 = (Button) findViewById(R.id.Send);
        	 
        	 
        
        
        final byte[] value= getIntent().getExtras().getByteArray("rest");
        final byte[] img = getIntent().getExtras().getByteArray("image");
        final String s= new String(value);
       
        btn1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
             //   TextView view = (TextView) findViewById(R.id.edit_text_out);
               // String message = view.getText().toString();
                
                sendMessage(s);
               
                Log.i("Size",""+img.length);
                sendImage(img);
                
                int existing_coupons=0;
         		try{
         			existing_coupons=Integer.parseInt(dealsActivity.stats.getString("coupons_b_send", "0"));	
         		}
         		catch(NullPointerException e)
         		{
         			existing_coupons=0;
         		}
         		 
         		 
         		dealsActivity.stats.edit().putString("coupons_b_send", existing_coupons+1 +"").commit();
    			
                
            }
        
        });
       }
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
       
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }

    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
           // mOutEditText.setText(mOutStringBuffer);
        }
    }
    
    private void sendImage(byte[] message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            mChatService.write(message);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
         
        }
    }

    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
        new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            if(D) Log.i(TAG, "END onEditorAction");
            return true;
        }
    };

    /*
    private final void setStatus(int resId) {
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(resId);
    }

    private final void setStatus(CharSequence subTitle) {
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(subTitle);
    }*/

    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothChatService.STATE_CONNECTED:
//                    setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                 //   mConversationArrayAdapter.clear();
                    break;
                case BluetoothChatService.STATE_CONNECTING:
//                    setStatus(R.string.title_connecting);
                    break;
                case BluetoothChatService.STATE_LISTEN:
                case BluetoothChatService.STATE_NONE:
//                    setStatus(R.string.title_not_connected);
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                       // construct a string from the buffer
                String writeMessage = new String(writeBuf);
             //   mConversationArrayAdapter.add("Me:  " + writeMessage);
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                String items[]=readMessage.split(",");
                
                TextView dealsText = (TextView) findViewById(R.id.blue_recv_dealname);
                dealsText.setText(items[0]);
                n=items[0];
                TextView dealsdesc = (TextView) findViewById(R.id.blue_recv_desc);
                dealsdesc.setText(items[3]);
                t=items[1];
                
                
//                mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                //ADD to DB readMessage
                break;
            case IMAGE_READ:
                byte[] imageBuf = (byte[]) msg.obj;
                //IMAGE
                ImageView imgBuf= (ImageView) findViewById(R.id.blue_recv_imageView1);
                imgBuf.setImageBitmap(BitmapFactory.decodeByteArray(imageBuf, 0, imageBuf.length));
                
                
                BitmapFactory Bm= new BitmapFactory();
		        final Bitmap pic=Bm.decodeByteArray(imageBuf,0,imageBuf.length);
		        
		        int existing_coupons=0;
	     		try{
	     			existing_coupons=Integer.parseInt(dealsActivity.stats.getString("coupons_r_b", "0"));	
	     		}
	     		catch(NullPointerException e)
	     		{
	     			existing_coupons=0;
	     		}
	     		 
	     		 
	     		dealsActivity.stats.edit().putString("coupons_r_b", existing_coupons+1 +"").commit();
				
		        
		        Button btn = new Button(getBaseContext());
		        btn.setText("Add");
		        LinearLayout layout;
		        layout = (LinearLayout) findViewById(R.id.blue_linear); 
		        layout.addView(btn);
		        btn.setOnClickListener(new OnClickListener() {
		        	 public void onClick(View v) {
                       store(n,t,"abc",d, pic);
                       finish();
		        	}
		        });
                
                //ADD to DB readMessage
                break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE_SECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                connectDevice(data, true);
            }
            break;
        case REQUEST_CONNECT_DEVICE_INSECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                connectDevice(data, false);
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
                setupChat();
            } else {
                // User did not enable Bluetooth or an error occurred
                Log.d(TAG, "BT not enabled");
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    public void store(String n,String t,String m,String d,Bitmap b){
    	DatabaseHelper db = new DatabaseHelper(getBaseContext());
    	db.insert(n, t, 10.0, d, b);
    	
    }
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent serverIntent = null;
        switch (item.getItemId()) {
        case R.id.secure_connect_scan:
            // Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
            return true;
        case R.id.insecure_connect_scan:
            // Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
            return true;
        case R.id.discoverable:
            // Ensure this device is discoverable by others
            ensureDiscoverable();
            return true;
        }
        return false;
    }

}

//END