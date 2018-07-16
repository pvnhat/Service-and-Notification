package com.example.vnnht.servicedemo;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class MyService extends IntentService {



    public MyService(String name) {
        super("Thread 1"); // tạo tên cho service
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Normally we would do some work here, like download a file.
        // do something

        try {
            for (int i = 0 ; i < 5; i ++) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // show luong hien tai dang bi interup
        }
    }


}
