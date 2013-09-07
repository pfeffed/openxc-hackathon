package com.example.sampleapp;

import com.openxc.VehicleManager;
import com.openxc.measurements.BeepRequest;
import com.openxc.measurements.DoorLocks;
import com.openxc.measurements.Measurement;
import com.openxc.measurements.UnrecognizedMeasurementTypeException;
import com.openxc.measurements.VehicleSpeed;
import com.openxc.remote.VehicleServiceException;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private VehicleManager mVehicleManager;
	private TextView mVehicleSpeedView;
	private BroadcastReceiver mIntentReceiver;
	private int task = 0;
	private int CheckSpeed = 0;
	private String body = new String("");
    private String pNumber = new String("");
    
	private ServiceConnection mConnection = new ServiceConnection() {
	    // Called when the connection with the service is established
	    public void onServiceConnected(ComponentName className,
	            IBinder service) {
	        Log.i("openxc", "Bound to VehicleManager");
	        mVehicleManager = ((VehicleManager.VehicleBinder)service).getService();
	       
	        try {
				mVehicleManager.addListener(VehicleSpeed.class, mSpeedListener);
			} catch (VehicleServiceException e) {
				e.printStackTrace();
			} catch (UnrecognizedMeasurementTypeException e) {
				e.printStackTrace();
			}
	    }
 
	    // Called when the connection with the service disconnects unexpectedly
	    public void onServiceDisconnected(ComponentName className) {
	        Log.w("openxc", "VehicleService disconnected unexpectedly");
	        mVehicleManager = null;
	    }
	};
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mVehicleSpeedView = (TextView) findViewById(R.id.vehicle_speed);
		
		Intent intent = new Intent(this, VehicleManager.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);   
	}
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		
		return true;
	}
	
	public void onPause() {
	    super.onPause();
	    Log.i("openxc", "Unbinding from vehicle service");
	    unbindService(mConnection);
	}
 
	VehicleSpeed.Listener mSpeedListener = new VehicleSpeed.Listener() {
	    public void receive(Measurement measurement) {
	    	final VehicleSpeed speed = (VehicleSpeed) measurement;
	    	final DoorLocks lock = new DoorLocks(DoorLocks.LockCommands.LOCK_ALL);
	    	final DoorLocks unlock = new DoorLocks(DoorLocks.LockCommands.UNLOCK_ALL);
	    	final BeepRequest beep = new BeepRequest(true);
	        MainActivity.this.runOnUiThread(new Runnable() {
	            public void run() {
	            	
	            		try {
	            	if(pNumber.equals("4154244347")){		
	            	if(body.equals("lock")){
	            		    mVehicleManager.send(lock);
	            	}	
	            	if(body.equals("unlock")){
		            		mVehicleManager.send(unlock);
	            	}
	            	if(body.equals("beep")){
		            		mVehicleManager.send(beep);
		            	} 
	            	}
	            	else {
	            		Log.e("DRIVEN", " Wrong Phone Number");
	            	}
	            		}
	            catch(UnrecognizedMeasurementTypeException e) {
            		Log.w("Driven Computing", "Unable to send command", e);
            	}	
	            	
	            /*	if(speed.getValue().intValue() > CheckSpeed)
	            	{
	                mVehicleSpeedView.setText(
	                    "Warning You are over " + CheckSpeed + "you are at: "+ speed.getValue().doubleValue());
	            	}*/
	            }
	        });
	    }
	};
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	@Override
	protected void onResume() {
	super.onResume();

	IntentFilter intentFilter = new IntentFilter("SmsMessage.intent.MAIN");
	mIntentReceiver = new BroadcastReceiver() {
	@Override
	public void onReceive(Context context, Intent intent) {
	String msg = intent.getStringExtra("get_msg");

	//Process the sms format and extract body &amp; phoneNumber
	msg = msg.replace("\n", "");
	body = msg.substring(msg.lastIndexOf(":")+1, msg.length());
	String pNumber = msg.substring(0,msg.lastIndexOf(":"));
	char[] messageArray = body.toCharArray();
	String CSpeed = new String(messageArray, 1, 2);
	Log.e("DRIVEN COMPUTING", body + pNumber + CSpeed);
//  CheckSpeed = Integer.parseInt(CSpeed);
	mVehicleSpeedView = (TextView) findViewById(R.id.vehicle_speed);
	 
	//Add it to the list or do whatever you wish to

	}
	};
	this.registerReceiver(mIntentReceiver, intentFilter);
	}

    
}