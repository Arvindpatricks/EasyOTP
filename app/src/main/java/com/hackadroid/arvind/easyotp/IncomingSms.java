package com.hackadroid.arvind.easyotp;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by arvind on 09/01/16.
 */
public  class IncomingSms extends BroadcastReceiver {

    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                 //   Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);


                   // HomePage.smsotp.setText(message);
                    if(message.contains("OTP")||message.contains("otp")||message.contains("Otp")||message.contains("One Time Password")){


                        if(senderNum.equals("VM-SBIACS")||senderNum.equals("BX-SBIACS")){
                            Toast.makeText(context,"OTP Received From SBI..",Toast.LENGTH_SHORT).show();

                            int index=message.indexOf("is");
                            String otp = message.substring(index + 3);
                            otp=otp.substring(0,6);
                            System.out.println("OTP IS:" + otp);
                            Intent i2 = new Intent(context,LoginActivity.class);
                            i2.putExtra("otp",otp);
                            i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //HomePage.smsotp.setText(otp);

                            context.startActivity(i2);

                        }
                        else if(senderNum.equals("RM-IndBnk")){
                            Toast.makeText(context,"OTP Received From INdian Bank..",Toast.LENGTH_SHORT).show();

                            int index=message.indexOf("is");
                            String otp = message.substring(index + 3);
                            otp=otp.substring(0,6);
                            System.out.println("OTP IS:" + otp);
                            Intent i2 = new Intent(context,LoginActivity.class);
                            i2.putExtra("otp",otp);
                            i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //HomePage.smsotp.setText(otp);

                            context.startActivity(i2);

                        }

                        else{
                            Toast.makeText(context,"Testing OTP Received..",Toast.LENGTH_SHORT).show();
                            int index=message.indexOf("is");
                            String otp = message.substring(index + 3);
                            otp=otp.substring(0,6);
                            System.out.println("OTP IS:" + otp);
                            Intent i2 = new Intent(context,LoginActivity.class);
                            i2.putExtra("otp",otp);
                            i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                           // HomePage.smsotp.setText(otp);

                            context.startActivity(i2);

                        }


                    }


                    // Show Alert


                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }

    }


}
