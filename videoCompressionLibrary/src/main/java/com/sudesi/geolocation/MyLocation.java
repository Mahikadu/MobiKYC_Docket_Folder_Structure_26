package com.sudesi.geolocation;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.sudesi.adstringocompression.ImageCompresionApplication;

import java.util.Timer;
import java.util.TimerTask;

public class MyLocation {
    private Timer timer;
    private LocationManager mLocationManager;
    private LocationResult locationResult;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;

    public boolean getLocation(Context context, LocationResult result) {
        // I use LocationResult callback class to pass location value from
        // MyLocation to user code.
        locationResult = result;
        if (mLocationManager == null)
            mLocationManager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);

        // exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = mLocationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            network_enabled = mLocationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Toast.makeText(context, gps_enabled+" "+network_enabled,
        // Toast.LENGTH_LONG).show();

        // don't start listeners if no provider is enabled
        if (!gps_enabled && !network_enabled)
            return false;

        if (gps_enabled)
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
        if (network_enabled)
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0,
                    locationListenerNetwork);
        timer = new Timer();

        // timer.schedule(new GetLastLocation(), 3600000);
        // timer.schedule(new GetLastLocation(), 10000);
        return true;
    }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer.cancel();
            locationResult.gotLocation(location);
            if (!String.valueOf(location.getLatitude()).equalsIgnoreCase("")) {
                saveLatLong(location.getLatitude() + ";"
                        + location.getLongitude());
            }
            mLocationManager.removeUpdates(this);
            mLocationManager.removeUpdates(locationListenerNetwork);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer.cancel();
            locationResult.gotLocation(location);
            if (!String.valueOf(location.getLatitude()).equalsIgnoreCase("")) {
                saveLatLong(location.getLatitude() + ";"
                        + location.getLongitude());
            }
            mLocationManager.removeUpdates(this);
            mLocationManager.removeUpdates(locationListenerGps);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    class GetLastLocation extends TimerTask {
        @Override
        public void run() {
            // Context context = getClass().getgetApplicationContext();
            Location net_loc = null, gps_loc = null;
            if (gps_enabled)
                gps_loc = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (network_enabled)
                net_loc = mLocationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            // if there are both values use the latest one
            if (gps_loc != null && net_loc != null) {
                if (gps_loc.getTime() > net_loc.getTime())
                    locationResult.gotLocation(gps_loc);
                else
                    locationResult.gotLocation(net_loc);
                return;
            }

            if (gps_loc != null) {
                locationResult.gotLocation(gps_loc);
                return;
            }
            if (net_loc != null) {
                locationResult.gotLocation(net_loc);
                return;
            }
            locationResult.gotLocation(null);
        }
    }

    public static abstract class LocationResult {
        public abstract void gotLocation(Location location);
    }

    public void saveLatLong(String latlong) {
        Editor profileDetailsPrefs = ImageCompresionApplication.userDetailsPrefs
                .edit();
        profileDetailsPrefs.putString("LAT_LONG", latlong);
        profileDetailsPrefs.commit();
    }
}
