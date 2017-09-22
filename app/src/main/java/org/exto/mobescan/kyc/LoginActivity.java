package org.exto.mobescan.kyc;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adstringo.MainActivity;
import com.adstringo.android_client.BaseWizard;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.exto.Login.ViewUtils;
import org.exto.libs.ConnectionDetector;
import org.exto.libs.SOAPWebservice;
import org.ksoap2.serialization.SoapPrimitive;

public class LoginActivity extends MainActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<LocationSettingsResult> {

    private EditText uname;
    private EditText password;
    private SOAPWebservice service = null;
    private LoginTask userlogin = null;

    private ProgressDialog mProgress = null;

    private String rdir = null;
    private static int ecolor;
    private static String namestring, fieldValue;
    private static ForegroundColorSpan fgcspan;
    private static SpannableStringBuilder ssbuilder;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;

    private ConnectionDetector cd;

    public static String USER_NAME_FTP_ROOT_FOLDER;
    /*    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
        <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
        <uses-permission android:name="android.permission.ACCESS_GPS" />
        <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
        <uses-permission android:name="android.permission.CAMERA" />
        <uses-permission android:name="android.permission.INTERNET" />
        <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />*/
    private String[] permissions = new String[]{
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.CAMERA,
            Manifest.permission.WAKE_LOCK,
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.MODIFY_AUDIO_SETTINGS,

    };

    private GoogleApiClient mGoogleApiClient;
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;

    private ViewUtils viewUtils;
    //private CompressedPathCallback compressedPathCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //compressedPathCallback = (CompressedPathCallback) this;

        viewUtils = new ViewUtils(LoginActivity.this);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .enableAutoManage(this, 34993, this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        gpsPermissionsCheck();

        cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            viewUtils.showDialog("Alert", "Check Your Internet Connection.");
            // stop executing code by return

        }

        SharedPreferences sharedpre = getSharedPreferences(
                "Sudesi", MODE_PRIVATE);

        rdir = sharedpre.getString("LoginSelectedName", "");

        /*if (sharedpre.getBoolean("Login_Status", false))//check login status
        {
            startActivity(new Intent(LoginActivity.this, ActivityPreview.class));
            finish();


        }*/

//		else
//		{

        setContentView(R.layout.activity_login);

        mEmailView = (EditText) findViewById(R.id.uname);

        mPasswordView = (EditText) findViewById(R.id.password);


        uname = (EditText) findViewById(R.id.uname);
        password = (EditText) findViewById(R.id.password);

        mProgress = new ProgressDialog(this);

        findViewById(R.id.button1).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Reset errors.
                        mEmailView.setError(null);
                        mPasswordView.setError(null);
                        ///

                        if (uname.getText().toString().trim().equals("") || uname.getText().toString().trim().contains(" ")) {//email id validation &&!email.getText().contains("@")
                            //mEmailView.setError(getString(R.string.error_field_required));

                            ecolor = Color.BLACK; // whatever color you want
                            namestring = "Enter Username";
                            fgcspan = new ForegroundColorSpan(ecolor);
                            ssbuilder = new SpannableStringBuilder(namestring);
                            ssbuilder.setSpan(fgcspan, 0, namestring.length(), 0);
                            mEmailView.setError(ssbuilder);

                        } else if (password.getText().toString().trim().equals("")) //password validation
                        {
//								mPasswordView.setError(getString(R.string.error_field_required));
                            ecolor = Color.BLACK; // whatever color you want
                            namestring = "Enter Password";
                            fgcspan = new ForegroundColorSpan(ecolor);
                            ssbuilder = new SpannableStringBuilder(namestring);
                            ssbuilder.setSpan(fgcspan, 0, namestring.length(), 0);
                            password.setError(ssbuilder);

                        } else {

                            cd = new ConnectionDetector(getApplicationContext());

                            // Check if Internet present
                            if (!cd.isConnectingToInternet()) {
                                // Internet Connection is not present
                                viewUtils.showDialog("Alert", "Check Your Internet Connection.");
                                // stop executing code by return

                            } else {
                                mProgress.setMessage("Signing In...");
                                mProgress.setCancelable(false);
                                mProgress.show();

                                service = new SOAPWebservice(LoginActivity.this);

                                userlogin = new LoginTask();

                                userlogin.execute();
                            }
                        }
                    }
                });

			
			/*findViewById(R.id.button_exit).setOnClickListener(
                    new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							
							finish();					
							
							startActivity(new Intent(LoginActivity.this,HomeActivity.class));
							//
						}
					});*/


        findViewById(R.id.button_new).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();

                        //
                    }
                });
        //}
        viewUtils.set_Typeface_MS_Reference_Sans_Serif(findViewById(R.id.textView2));
        viewUtils.set_Typeface_MS_Reference_Sans_Serif(findViewById(R.id.uname));//password
        viewUtils.set_Typeface_MS_Reference_Sans_Serif(findViewById(R.id.password));//password
        viewUtils.set_Typeface_MS_Reference_Sans_Serif(findViewById(R.id.button1));//textView24
        viewUtils.set_Typeface_MS_Reference_Sans_Serif(findViewById(R.id.textView24));//textView24
    }

    //

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class LoginTask extends AsyncTask<Void, Void, SoapPrimitive> {

        private SoapPrimitive soap_result = null;

        @Override
        protected SoapPrimitive doInBackground(Void... params) {
            try {
                soap_result = service.loginWebservice("1", uname.getText().toString().trim(), password.getText().toString().trim());
                //intializing the mobexchange soap webservice
            } catch (Exception e) {

                soap_result = null;
            }

            return soap_result;
        }

        @Override
        protected void onPostExecute(SoapPrimitive result) {

            mProgress.dismiss();

            try {

                if (result != null && result.toString().equalsIgnoreCase("true")) {
                    //process the soap response  for login
                    //SoapObject userdetailObject=(SoapObject)result.getProperty("UserDetails");

                    //String CompId=String.valueOf(userdetailObject.getProperty("CompId"));
//				if(CompId.equals("false"))//failure
//				{
//					Toast.makeText(LoginActivity.this, "Invalid Login!!! Try Again...", Toast.LENGTH_LONG).show();					
//				}
//				else//success
//				{
                    //store the necessary values in shared preference for future access
//					SharedPreferences sharedpre = getSharedPreferences(
//							"Sudesi", MODE_PRIVATE);
//					SharedPreferences.Editor saveuser = sharedpre.edit();
//
//					saveuser.putBoolean("Login_Status", true);//loginstatus
//					saveuser.putString("LoginEmailid",uname.getText().toString().trim());//emailid
//					saveuser.putString("LoginCompId",CompId);
//					saveuser.putString("LoginRoleName",String.valueOf(userdetailObject.getProperty("RoleName")));
//					
//					saveuser.commit();
//				
//					finish();
//										
//					


                    SharedPreferences sharedpre = getSharedPreferences(
                            "Sudesi", MODE_PRIVATE);
                    SharedPreferences.Editor saveuser = sharedpre.edit();
                    saveuser.putBoolean("Login_Status", true);
                    saveuser.clear();
                    saveuser.commit();

//					if(rdir.equalsIgnoreCase(""))
//					{

                    USER_NAME_FTP_ROOT_FOLDER = uname.getText().toString().trim();
                    //USER_NAME_FTP_ROOT_FOLDER = "CrispDox";
                    kycDialog();

//					}
//					else
//					{
//						startActivity(new Intent(LoginActivity.this,LoginMainActivity.class));
//					}

                    //}
                    service = null;
                    userlogin = null;
                } else {
                    viewUtils.showDialog("Alert", "Something went wrong, please try Again.");
                    service = null;
                    userlogin = null;
                }


            } catch (Exception e) {
                viewUtils.showDialog("Alert", "Something went wrong, please try Again.");
                service = null;
                userlogin = null;

            }
        }

        @Override
        protected void onCancelled() {
            mProgress.dismiss();

            service = null;
            userlogin = null;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                mGoogleApiClient.stopAutoManage(this);
                mGoogleApiClient.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkAndRequestPermissions() {
        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE_LOCATION);
            return false;
        }
        return true;

    }

    public boolean hasPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && LoginActivity.this != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(LoginActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        showSettingsAlert();
                    }
                }
                break;
        }
    }

    public void showSettingsAlert() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(false);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    LoginActivity.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    private void gpsPermissionsCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            if (checkAndRequestPermissions()) {
                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    showSettingsAlert();
                }
            } /*else {
                checkAndRequestPermissions();
            }*/
        } else {
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showSettingsAlert();
            }
        }
    }

    public void kycDialog() {
        //AlertDialog.Builder dialog = new AlertDialog.Builder(_context);

        //final Dialog dialog = new Dialog(this);
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.menu);
        dialog.setCancelable(false);
        //TextView dialog_title = (TextView) dialog.findViewById(R.id.dialog_title);
        //TextView dialog_message = (TextView) dialog.findViewById(R.id.dialog_message);
        Button mobiKYCButton = (Button) dialog.findViewById(R.id.mobiKYCButton);
        Button sycButton = (Button) dialog.findViewById(R.id.sycButton);

        //dialog_title.setText("");

        mobiKYCButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Preventing multiple clicks, using threshold of 1 second
                startActivity(new Intent(LoginActivity.this, ActivityPreview.class));
                finish();
                dialog.dismiss();

            }
        });
        sycButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Preventing multiple clicks, using threshold of 1 second
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                dialog.dismiss();

            }
        });
        dialog.show();
    }

  /*  @Override
    public void getCompressedFilePath(String compressedFilePath) {
        Toast.makeText(this, compressedFilePath, Toast.LENGTH_LONG).show();
        //super.compressedFilePath(compressedFilePath); // Performs the save logic for A
    }*/

}
