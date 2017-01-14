package br.ufc.ec.pet.bustracker.trackerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StartTrackerService extends BroadcastReceiver {
    public static final String LOG_TAG = "BUS_TRACKER_APP";
    public static final boolean DEBUG = true;

    public StartTrackerService() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(DEBUG) Log.d(LOG_TAG, "Startup service receiver");
        if(DEBUG) Log.d(LOG_TAG, "Action is "+intent.getAction());

        // check if this has started by boot complented event
        if(intent.getAction() == Intent.ACTION_BOOT_COMPLETED) {
            //check if tracker service has safe closed...
            boolean startService = !TrackerService.safeClosed(context);
            if(startService){//... start service if true
                if(DEBUG) Log.d(LOG_TAG, "Service will start");
                Intent it = new Intent(context, TrackerService.class);
                it.putExtra(TrackerService.SharedPreferencesAttributes.LOAD_PREFS, true);
                context.startService(it);
            }
        }
    }
}
