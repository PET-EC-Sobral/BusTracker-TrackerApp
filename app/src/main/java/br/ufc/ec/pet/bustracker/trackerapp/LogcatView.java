package br.ufc.ec.pet.bustracker.trackerapp;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LogcatView extends AppCompatActivity {
    private Handler mUpdateLogHandler;
    private TextView mLogTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logcat_view);

        mLogTv = (TextView) findViewById(R.id.log_et);
        mUpdateLogHandler = new Handler();
        final Context context = getApplicationContext();
        mUpdateLogHandler.post(new Runnable() {
            @Override
            public void run() {
                mLogTv.setText(LogFile.read(context));
                mUpdateLogHandler.postDelayed(this, 1000);
            }
        });

    }
}
