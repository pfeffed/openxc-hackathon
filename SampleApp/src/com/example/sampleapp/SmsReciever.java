package com.example.sampleapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReciever extends BroadcastReceiver {


	@Override
	public void onReceive(Context context, Intent intent) {

	       Bundle extras = intent.getExtras();
	       if (extras == null)
	       return;
	      
	      // To display a Toast whenever there is an SMS.
	      //Toast.makeText(context,"Recieved",Toast.LENGTH_LONG).show();

	       Object[] pdus = (Object[]) extras.get("pdus");
	       for (int i = 0; i < pdus.length; i++) {
	          SmsMessage SMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
	          String sender = SMessage.getOriginatingAddress();
	          String body = SMessage.getMessageBody().toString();

	         Intent in = new Intent("SmsMessage.intent.MAIN").
	         putExtra("get_msg", sender+":"+body);

	         context.sendBroadcast(in);

	     
	        }
	     }

}
