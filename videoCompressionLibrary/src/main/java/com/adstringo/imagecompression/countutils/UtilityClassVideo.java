package com.adstringo.imagecompression.countutils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.widget.Toast;

public class UtilityClassVideo {

    //production
    //public static final String SOAP_ADDRESS_UAT = "http://adshdfcergo.adstringolicense.com/service.svc";
    //public static final String CALL_NAMESPACE_UAT = "http://tempuri.org/IService/";

    //UAT
    public static final String SOAP_ADDRESS_UAT = "http://licads.in/adsws/service1.svc";
    public static final String CALL_NAMESPACE_UAT = "http://tempuri.org/IService1/";

    public static final String BUSINESS_UNIT = "Religare Video UAT";

    //For UAT Comment below line
    //public static final String SOURCE = "ANDROID";
    public static final String WATERMARK = "";

    public static String SUCCESS = "NOT_CHECKED";
    public static boolean isReplaceOriginalFile = true;
    public static boolean isAngelBrokingTestMultpleFileCompress = true;
    public static final String NETWORK_CONNECTED = "NETWORK_CONNECTED";
    // quality
    // String waterMarkText = "ICICI UAT";
    public int IMAGE_MAX_SIZE = 1000;
    public int imgWidth = 800, imgHeight = 600;
    public int compressQuality = 20;

    private AlertDialog internetAlertDialog, invalidLicenceAlertDailog;
    private static boolean isLicenceInvalidAlertDialogOpen;
    private static boolean isInternetDialogOpen;

	/*
     * public boolean isConnectingToInternet(Context context) {
	 * ConnectivityManager connectivity = (ConnectivityManager)
	 * context.getSystemService(Context.CONNECTIVITY_SERVICE); if (connectivity
	 * != null) { NetworkInfo[] info = connectivity.getAllNetworkInfo(); if
	 * (info != null) for (int i = 0; i < info.length; i++) if
	 * (info[i].getState() == NetworkInfo.State.CONNECTED) { return true; } }
	 * return false; }
	 */

    public void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, "message", Toast.LENGTH_SHORT);
        toast.setText(message);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void internertErrorMsgDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(
                "It seems that you are not connected to the Internet. Kindly check your network settings.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        isInternetDialogOpen = false;
                    }
                });
        if (!isInternetDialogOpen) {
            isInternetDialogOpen = true;
            internetAlertDialog = builder.create();
            internetAlertDialog.show();
        }
    }

    public void licenceInvalid(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(
                "Licence invalid, please contact AdstringO for further assistance.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        isLicenceInvalidAlertDialogOpen = false;
                    }
                });
        if (!isLicenceInvalidAlertDialogOpen) {
            isLicenceInvalidAlertDialogOpen = true;
            invalidLicenceAlertDailog = builder.create();
            //invalidLicenceAlertDailog.show();
        }
    }

    public String uniqueId(Context context) {
        String identifier = null;
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null)
            identifier = tm.getDeviceId();
        if (identifier == null || identifier.length() == 0)
            identifier = Secure.getString(context.getContentResolver(),
                    Secure.ANDROID_ID);
        return identifier;
    }
}
