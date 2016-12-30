package br.ufc.ec.pet.bustracker.trackerapp;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;

import br.ufc.ec.pet.bustracker.trackerapp.types.Bus;
import br.ufc.ec.pet.bustracker.trackerapp.types.Message;
import br.ufc.ec.pet.bustracker.trackerapp.types.Route;

public class TrackerService extends Service {
    Route mRoute;
    Bus mBus;
    String mHost;
    long mTimeInterval = 1*1000;
    float mDistanceMin = 10;
    private static TrackerService mInstance;
    private boolean sendRequest;
    LocationManager mLocationManager;
    LocationListenerTracker mLocationListener;
    ConnectionManager mConnectionManager;
    public static final String PREFS_SERVICE = "service_prefs";
    private SharedPreferences pref;

    /*public TrackerService(Route route, Bus bus) {
        mRoute = route;
        mBus = bus;
    }*/

    @Override
    public void onCreate() {
        mInstance = this;
        mConnectionManager = new ConnectionManager(getBaseContext(), "");
        Log.d("Bus", "create service");

        pref = getSharedPreferences(PREFS_SERVICE, 0);

        mLocationListener = new LocationListenerTracker();
        mLocationManager = (LocationManager) getSystemService(this.getApplicationContext().LOCATION_SERVICE);
        setSendLocation(true);

        createRoute();
        creatBus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setSendLocation(false);
        mInstance = null;
        LogFile.writeln(getApplicationContext(), "Serviço finalizado...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            boolean stopSend = intent.getBooleanExtra("STOP_SEND", false);
            setSendLocation(!stopSend);

            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(SharedPreferencesAttributes.STARTUP, !stopSend);

            if(intent.hasExtra("BUS")) {
                mBus = intent.getParcelableExtra("BUS");
                editor.putString(SharedPreferencesAttributes.BUS, new Gson().toJson(mBus));
            }

            if(intent.hasExtra("ROUTE")) {
                mRoute = intent.getParcelableExtra("ROUTE");
                editor.putString(SharedPreferencesAttributes.ROUTE, new Gson().toJson(mRoute));
            }

            if(intent.hasExtra("HOST")) {
                String host = intent.getStringExtra("HOST");
                mConnectionManager.setServerPrefix(host);
                editor.putString(SharedPreferencesAttributes.HOST, host);
            }

            mTimeInterval = intent.getLongExtra("TIME_INTERVAL", mTimeInterval);
            editor.putLong(SharedPreferencesAttributes.TIME_INTERVAL, mTimeInterval);

            mDistanceMin = intent.getFloatExtra("DISTANCE_MIN", mDistanceMin);
            editor.putFloat(SharedPreferencesAttributes.DISTANCE_MIN, mDistanceMin);

            if(intent.hasExtra("TOKEN")) {
                String token = intent.getStringExtra("TOKEN");
                mConnectionManager.setToken(token);
                Log.d("Bus", intent.getStringExtra("TOKEN"));
                Log.d("Bus", mConnectionManager.getToken());

                editor.putString(SharedPreferencesAttributes.TOKEN, token);
            }
            if(intent.hasExtra("MESSAGE")){
                Message message = intent.getParcelableExtra("MESSAGE");
                sendMessage(message);
            }
            editor.commit();
        }
        LogFile.writeln(getApplicationContext(), "Serviço iniciado...");


        return super.onStartCommand(intent, flags, startId);
    }
    private void sendMessage(Message message){
        mConnectionManager.postMessage(mBus, mRoute, message);
    }
    private void setSendLocation(boolean value){
        if(value) {
            if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, mTimeInterval, mDistanceMin, mLocationListener);

            if(mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, mTimeInterval, mDistanceMin, mLocationListener);
        }
        else
            mLocationManager.removeUpdates(mLocationListener);
    }
    public static boolean isRunning(){
        return mInstance != null;
    }
    private void createRoute(){

    }
    private void creatBus(){

    }
    public static TrackerService getInstance(){
        return mInstance;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    private class LocationListenerTracker implements LocationListener{
        Location mLocation = new Location(LocationManager.GPS_PROVIDER);
        @Override
        public void onLocationChanged(Location location) {
            Log.d("Bus", "Location is changed");
            Log.d("Bus", "Accuracy: "+ location.getAccuracy());
            Log.d("Bus", "provider: "+ location.getProvider());
            mLocation = location;
            Log.d("BusTracker", "lan:"+mLocation.getLatitude()+" lon:"+mLocation.getLongitude());
            mBus.setLatitude(mLocation.getLatitude());
            mBus.setLongitude(mLocation.getLongitude());
            mConnectionManager.postLocation(mBus, mRoute);
        }
        public Location getLocation(){
            return mLocation;
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
    class Extra{
        private Extra(){}
        public static final String TOKEN = "TrackerService.Extra.TOKEN";//string value
        public static final String BUS = "TrackerService.Extra.BUS";//json from Bus object
        public static final String ROUTE = "TrackerService.Extra.ROUTE";
        public static final String HOST = "TrackerService.Extra.HOST";
        public static final String TIME_INTERVAL = "TrackerService.Extra.TIME_INTERVAL";
        public static final String DISTANCE_MIN = "TrackerService.Extra.DISTANCE_MIN";
        public static final String MESSAGE = "TrackerService.Extra.MESSAGE";

    }
    class SharedPreferencesAttributes extends TrackerService.Extra{
        private SharedPreferencesAttributes(){}
        public static final String STARTUP = "TrackerService.SharedPreferencesAttributes.STARTUP";//boolean value
        public static final String MESSAGE = "";
    }
}

