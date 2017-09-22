package com.sudesi.adstringocompression;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;

import com.adstringo.compressionreceiver.NetworkUtil;
import com.adstringo.imagecompression.countutils.UtilityClassVideo;
import com.adstringo.imagecompression.countutils.ValidateUserSoapVideo;
import com.sudesi.geolocation.MyLocation;
import com.sudesi.geolocation.MyLocation.LocationResult;

import java.util.concurrent.ExecutionException;

/**
 * read api for Application class
 *
 * @author tushar
 */
public class ImageCompresionApplication {


    // username prefs
    public static SharedPreferences userDetailsPrefs;
    public static Editor userDetailsPrefEditor;

    public static SharedPreferences userProfilesPrefs;
    public static Editor userProfilesPrefEditor;

    public static SharedPreferences userProfilePhoLoadsPrefs;
    public static Editor userProfilePhoLoadsPrefsEditor;

    // stripe customer id prefs
    // public static SharedPreferences stripeCustomerPrefs;
    // public static SharedPreferences.Editor stripeCustomerPrefEditor;

    // public static boolean chatype = false;//false = active chat

    // public static Double post_spot_current_latitude;
    // public static Double post_spot_current_longitude;
    // public static Location post_spot_Location;
    // public static boolean connectionStatus=false;
    private UtilityClassVideo util = new UtilityClassVideo();
    private NetworkUtil networkUtils = new NetworkUtil();
    //private Timer t;
    // private int TimeCounter = 0;
    // private static final int FIVE_DAYES_MILIS = 432000000;
    /*
	 * public static OrganicParkingApplication init(){ return new
	 * OrganicParkingApplication(); }
	 */
    private MyLocation myLocation = new MyLocation();

    private Context context;

    public ImageCompresionApplication(Context con) {
        context = con;
        initialiseUserDetailsPrefs();
        inIt();
        turnGPSOn();
        myLocation.getLocation(context, locationResult);
    }

//	@Override
//	public void onCreate() {
//
//		// boolean r =
//		// myLocation.getLocation(getApplicationContext(),locationResult);
//		// fiveDaysTimer();
//		super.onCreate();
//	}

    private void inIt() {
        try {
            if (getUserDetails().trim().equalsIgnoreCase("Active")) {
                // do nothing
            } else {
                if (networkUtils
                        .getConnectivityStatusString(context)) {
                    ValidateUserSoapVideo validate = new ValidateUserSoapVideo();
                    //appended business unit here for application name

                    if (validate.Call(
                            UtilityClassVideo.BUSINESS_UNIT + "-"
                                    + context.getApplicationInfo()
                                    .loadLabel(context.getPackageManager()),
                            util.uniqueId(context))
                            .equalsIgnoreCase("Active")) {
                        saveBusinessUnits("Active");
                    } else {
                        saveBusinessUnits("Failure");
                    }
                } else {
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/*
	 * private void initialiseChatMaps() { oldRoomHashMap = new HashMap<String,
	 * String>(); availableRoomsHashMap = new HashMap<String, String>(); }
	 */

    /**
     * store logged in user details
     */
    private void initialiseUserDetailsPrefs() {
        userDetailsPrefs = context.getSharedPreferences("USER", context.MODE_PRIVATE);
        userDetailsPrefEditor = userDetailsPrefs.edit();
    }

    /**
     * store logged in user details
     */
	/*
	 * private void initialiseProfilesPrefs() { userProfilesPrefs =
	 * getSharedPreferences("PROFILE", MODE_PRIVATE); userProfilesPrefEditor =
	 * userProfilesPrefs.edit(); }
	 */

    /**
     * maintain states of profile photo
     */
	/*
	 * private void initialiseProfilesPhotoLoadPrefs() {
	 * userProfilePhoLoadsPrefs = getSharedPreferences("PROFILE", MODE_PRIVATE);
	 * userProfilePhoLoadsPrefsEditor = userProfilePhoLoadsPrefs.edit(); }
	 */
	/*
	 * private void initStripeCustomerIdPrefs() { stripeCustomerPrefs =
	 * getSharedPreferences(Constants.UserDetailsKeys.USER, MODE_PRIVATE);
	 * stripeCustomerPrefEditor = stripeCustomerPrefs.edit();
	 *
	 * }
	 */
    private void saveBusinessUnits(String user) {
        Editor profileDetailsPrefs = ImageCompresionApplication.userDetailsPrefs
                .edit();
        profileDetailsPrefs.putString("USER", user);
        profileDetailsPrefs.commit();
    }

    private String getUserDetails() {
        return ImageCompresionApplication.userDetailsPrefs
                .getString("USER", "");
    }

    /*
     * private void fiveDaysTimer(){ t = new Timer(); t.scheduleAtFixedRate(new
     * TimerTask() {
     *
     * @Override public void run() { runOnUiThread(new Runnable() { public void
     * run() { tvTimer.setText(String.valueOf(TimeCounter)); // you can set it
     * to a textView to show it to the user to see the time passing while he is
     * writing. TimeCounter++; } }); //rest shared prefs tag to failure
     * saveBusinessUnits("Failure"); } }, 1000, FIVE_DAYES_MILIS); // 1000 means
     * start from 1 sec, and the second 1000 is do the loop each 1 sec. }
     */
    public LocationResult locationResult = new LocationResult() {

        @Override
        public void gotLocation(Location location) {
            double Longitude = location.getLongitude();
            double Latitude = location.getLatitude();
            try {
                // SharedPreferences locationpref =
                // context.getSharedPreferences("location",
                // Context.MODE_WORLD_READABLE);
                // SharedPreferences.Editor prefsEditor = locationpref.edit();
                // prefsEditor.putString("Longitude", Longitude + "");
                // prefsEditor.putString("Latitude", Latitude + "");
                // prefsEditor.commit();
                saveLatLongDetails(Latitude + ";" + Longitude);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void saveLatLongDetails(String latlong) {
        Editor profileDetailsPrefs = ImageCompresionApplication.userDetailsPrefs
                .edit();
        profileDetailsPrefs.putString("LAT_LONG", "");
        profileDetailsPrefs.commit();
    }

	/*
	 * private void enableAuto() { final Intent intent = new
	 * Intent("android.location.GPS_ENABLED_CHANGE"); intent.putExtra("enabled",
	 * true); getApplicationContext().sendBroadcast(intent); }
	 */

    private void turnGPSOn() {
        String provider = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { // if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings",
                    "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }
		/*
		 * String provider = android.provider.Settings.Secure.getString(
		 * getContentResolver(),
		 * android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED); if
		 * (!provider.contains("gps")) { // if gps is disabled final Intent poke
		 * = new Intent(); poke.setClassName("com.android.settings",
		 * "com.android.settings.widget.SettingsAppWidgetProvider");
		 * poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
		 * poke.setData(Uri.parse("3")); sendBroadcast(poke);
		 */

		/*
		 * LocationManager lm = (LocationManager)
		 * getSystemService(LOCATION_SERVICE); if
		 * (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
		 * !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) { // Build
		 * the alert dialog AlertDialog.Builder builder = new
		 * AlertDialog.Builder(this);
		 * builder.setTitle("Location Services Not Active");
		 * builder.setMessage("Please enable Location Services and GPS");
		 * builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
		 * { public void onClick(DialogInterface dialogInterface, int i) { //
		 * Show location settings when the user acknowledges // the alert dialog
		 * Intent intent = new Intent(
		 * Settings.ACTION_LOCATION_SOURCE_SETTINGS); startActivity(intent); }
		 * }); Dialog alertDialog = builder.create();
		 * alertDialog.setCanceledOnTouchOutside(false); alertDialog.show();
		 */

    }
}
