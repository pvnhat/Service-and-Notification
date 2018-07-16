package com.example.vnnht.servicedemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String KEY_TEXT_REPLY = "key_text_reply";

    ResponseReceiver mReceiver = new ResponseReceiver();

    private int mRequestCode = 10;

    NotificationCompat.Builder mBuilder;
    Button mButtonShowNotify;
    Button mButtonReplyNotify;
    Button mButtonProgressNoti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButtonShowNotify = findViewById(R.id.button_showNotify);
        mButtonReplyNotify = findViewById(R.id.button_notifiReply);
        mButtonProgressNoti = findViewById(R.id.button_progressNoti);




        mButtonShowNotify.setOnClickListener(this);
        mButtonReplyNotify.setOnClickListener(this);
        mButtonProgressNoti.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter(
                MyServiceService.ACTION_1));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mReceiver);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_showNotify:
                makeNotification();
                break;
            case R.id.button_notifiReply:
                Toast.makeText(this, "as", Toast.LENGTH_SHORT).show();
                replyNotification();
                break;
            case R.id.button_progressNoti:
                makeProgressNotification();
                break;
        }
    }


    private void makeNotification() {

        // build action
        // Build a PendingIntent for the reply action to trigger.

        // Create the reply action and add the remote input.

        Intent notifiIntent = new Intent(this, MainActivity.class);
        notifiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifiIntent, 0);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(this, 0, notifiIntent, 0);
        mBuilder = new NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.drawable.ic_notifications_none_black_24dp)
                .setContentTitle("Thu cuối")
                .setContentText("Có lẽ nào mua thi chẳng còn lại gì trong tâm trí em")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Cuốn theo cơn gió lặng lẽ vào trong một buổi chiều vô vọng"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // gui intent khi click ve app
                .setAutoCancel(true) // tu dong cancel
                .addAction(R.drawable.ic_reply_black_24dp, "Reply" ,snoozePendingIntent );

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, mBuilder.build());

    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private void replyNotification() {
        Intent resultIntent = new Intent(this, NotificationActivity.class);
        String replyLabel = getResources().getString(R.string.reply_label);
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .build();

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // them stack de xac dinh noi data return
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        mRequestCode,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        NotificationCompat.Action replyAction =
                new NotificationCompat.Action.Builder(R.drawable.ic_reply_black_24dp, replyLabel, resultPendingIntent)
                        .addRemoteInput(remoteInput)
                        .setAllowGeneratedReplies(true)
                        .build();


        mBuilder = new NotificationCompat.Builder(this, "default")
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_notifications_none_black_24dp)
                        .setContentTitle("DevDeeds Says")
                        .setContentText("Do you like my tutorials ?")
                        .addAction(replyAction);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Show it
        mNotificationManager.notify(mRequestCode, mBuilder.build());
    }

    private void makeProgressNotification() {

        Intent intent = new Intent(this, MyServiceService.class);

        //calls the service's onStartCommand() method.
        // If the service isn't already running, the system first calls onCreate(), and then it calls onStartCommand().
        startService(intent);



    }



    public class ResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyServiceService.ACTION_1)) {
                int value = intent.getIntExtra("progress", -1);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                int MAX_PROGRESS = 5;
                int CURRENT_PROGRESS = 0;
                mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_notifications_none_black_24dp)
                        .setContentTitle("Dowload Song")
                        .setContentText("Dowloading ... ?")
                        .setProgress(MAX_PROGRESS, CURRENT_PROGRESS, false);;

                // Them progress bar , do some thing

                notificationManager.notify(1, mBuilder.build());



                System.out.println("best :  " + value);



                mBuilder.setProgress(MAX_PROGRESS, value, false);
                notificationManager.notify(1, mBuilder.build());

                if (value == 5) {
                    mBuilder.setContentText("Download complete")
                            .setProgress(0,0,false);
                    notificationManager.notify(1, mBuilder.build());
                }
            }
        }
    }


}
