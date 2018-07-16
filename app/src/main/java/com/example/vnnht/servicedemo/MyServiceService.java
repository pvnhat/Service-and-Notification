package com.example.vnnht.servicedemo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

public class MyServiceService extends Service {
    public static final String ACTION_1 ="MY_ACTION_1";


    public MyServiceService() {
    }

    private Looper mLooper;
    private ServiceHandle mServiceHandle;

    // Receive messenge from thread
    private class ServiceHandle extends Handler {
        public ServiceHandle(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(MyServiceService.ACTION_1);

            try {
                for (int i = 1 ; i <= 5; i ++) {

                    Thread.sleep(1000);
                    System.out.println("service loading" + i);

                    //pass progress to Activity

                    broadcastIntent.putExtra("progress",i);
                    sendBroadcast(broadcastIntent);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // show luong hien tai dang bi interup
            }

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments");
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mLooper = thread.getLooper();
        mServiceHandle = new ServiceHandle(mLooper);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service is starting (onStartCommad)", Toast.LENGTH_SHORT).show();
        Message message = mServiceHandle.obtainMessage();
        message.arg1 = startId;
        mServiceHandle.sendMessage(message);

        return START_STICKY; // TU DONG KHOI TAO KHI BI KILL
    }



    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}
