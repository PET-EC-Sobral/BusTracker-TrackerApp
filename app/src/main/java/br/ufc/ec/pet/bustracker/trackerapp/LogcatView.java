package br.ufc.ec.pet.bustracker.trackerapp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class LogcatView extends AppCompatActivity implements ScrollViewListener {
    private Handler mUpdateLogHandler;
    private TextView mLogTv;
    public static final String LOG_TAG = "BUS_TRACKER_APP.LogView";
    public static final boolean DEBUG = true;
    private int mLoadLines;
    private int mLoadLinesIncrement;
    private ScrollViewExt mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logcat_view);

        mScrollView = (ScrollViewExt) findViewById(R.id.log_scroll_sv);
        mScrollView.setScrollViewListener(this);

        mLogTv = (TextView) findViewById(R.id.log_et);
        mUpdateLogHandler = new Handler();
        final Context context = getApplicationContext();
        mLoadLines = 35;
        mLoadLinesIncrement = 10;

        mUpdateLogHandler.post(new Runnable() {
            @Override
            public void run() {
                int size = LogFile.getSize(context);
                if(DEBUG) Log.d(LOG_TAG, "size: "+ size);
                mLogTv.setText(LogFile.read(context, size - mLoadLines, -1));
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

    @Override
    protected void onDestroy() {
        mUpdateLogHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void onScrollChanged(ScrollViewExt scrollView, int x, int y, int oldx, int oldy) {

    }

    @Override
    public void onEndScroll(ScrollViewExt scrollView) {
        mLoadLines += mLoadLinesIncrement;
    }
}
