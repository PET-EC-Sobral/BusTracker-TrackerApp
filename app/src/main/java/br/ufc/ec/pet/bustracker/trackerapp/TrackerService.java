package br.ufc.ec.pet.bustracker.trackerapp;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import br.ufc.ec.pet.bustracker.trackerapp.types.Bus;
import br.ufc.ec.pet.bustracker.trackerapp.types.Message;
import br.ufc.ec.pet.bustracker.trackerapp.types.Route;

public class TrackerService extends Service {
    Route mRoute;
    Bus mBus;
    String mHost;
    long mTimeInterval = 1*1000;
    private static TrackerService mInstance;
    private boolean sendRequest;
    LocationManager mLocationManager;
    LocationListenerTracker mLocationListener;
    ConnectionManager mConnectionManager;

    /*public TrackerService(Route route, Bus bus) {
        mRoute = route;
        mBus = bus;
    }*/

    @Override
    public void onCreate() {
        mInstance = this;
        mConnectionManager = new ConnectionManager(getBaseContext(), "");
        Log.d("Bus", "create service");


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
            setSendLocation(!intent.getBooleanExtra("STOP_SEND", false));

            if(intent.hasExtra("BUS"))
                mBus = intent.getParcelableExtra("BUS");

            if(intent.hasExtra("ROUTE"))
                mRoute = intent.getParcelableExtra("ROUTE");

            if(intent.hasExtra("HOST"))
                mConnectionManager.setServerPrefix(intent.getStringExtra("HOST"));

            mTimeInterval = intent.getLongExtra("TIME_INTERVAL", mTimeInterval);

            if(intent.hasExtra("TOKEN")) {
                mConnectionManager.setToken(intent.getStringExtra("TOKEN"));
                Log.d("Bus", intent.getStringExtra("TOKEN"));
                Log.d("Bus", mConnectionManager.getToken());
            }
            if(intent.hasExtra("MESSAGE")){
                Message message = intent.getParcelableExtra("MESSAGE");
                sendMessage(message);
            }
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
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, mTimeInterval, 10, mLocationListener);

            if(mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, mTimeInterval, 10, mLocationListener);
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
}

