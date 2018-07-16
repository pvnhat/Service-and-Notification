package com.example.vnnht.servicedemo;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {
    private static final String KEY_TEXT_REPLY = "key_text_reply";

    int mRequestCode = 10;

    TextView mTextReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mTextReturn = findViewById(R.id.text_return);
        mTextReturn.setText(getMessageText(getIntent()));

        String returnMessage = "Thank you";
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, "default")
                        .setSmallIcon(R.drawable.ic_notifications_none_black_24dp);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //update notification
        mNotificationManager.notify(mRequestCode, mBuilder.build());



    }

    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(KEY_TEXT_REPLY);
        }
        return null;
    }
}
