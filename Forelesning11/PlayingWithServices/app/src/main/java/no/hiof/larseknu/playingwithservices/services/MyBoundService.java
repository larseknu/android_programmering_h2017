package no.hiof.larseknu.playingwithservices.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Date;

public class MyBoundService extends Service {
    private MyLocalBinder myLocalBinder = new MyLocalBinder();

    private Location currentLocation;

    HandlerThread gpsHandlerThread;
    LocationListener locationListener;
    LocationManager locationManager;

    public void monitorGpsInBackground(){
        gpsHandlerThread = new HandlerThread("GPSThread");
        gpsHandlerThread.start();

        locationListener = new MyLocationListener();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener, gpsHandlerThread.getLooper());
    }

    public void stopGpsMonitoring(){
        if(locationManager != null)
            locationManager.removeUpdates(locationListener);

        if(gpsHandlerThread != null)
            gpsHandlerThread.quit();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        monitorGpsInBackground();
        currentLocation = createLocationManually();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopGpsMonitoring();
    }

    public class MyLocalBinder extends Binder {

        public MyBoundService getService() {
            return MyBoundService.this;
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myLocalBinder;
    }


    public Location getCurrentLocation() {
        return currentLocation;
    }

    class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            currentLocation = location;
        }

        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        public void onProviderEnabled(String s) {
        }

        public void onProviderDisabled(String s) {
        }
    }

    private Location createLocationManually() {
        Location lastLocation = new Location("Hiof");
        Date now = new Date();
        lastLocation.setTime(now.getTime());
        lastLocation.setLatitude(59.128229);
        lastLocation.setLongitude(11.352860);

        return lastLocation;
    }
}
