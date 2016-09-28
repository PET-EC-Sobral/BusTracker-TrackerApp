package br.ufc.ec.pet.bustracker.trackerapp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear_log:
                LogFile.clear(this);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.log_action, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
