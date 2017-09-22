package com.adstringo.compressionreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.adstringo.imagecompression.countutils.IncreaseCountSoapVideo;
import com.adstringo.imagecompression.countutils.UtilityClassVideo;
import com.sudesi.adstringocompression.CompressionTechnique;
import com.sudesi.adstringocompression.ImageCompresionApplication;

import java.util.concurrent.ExecutionException;
//import com.sudesi.adstringocompression.R;

public class CompressionReceiver extends BroadcastReceiver {

    private NetworkUtil networkUtils = new NetworkUtil();
    private UtilityClassVideo util = new UtilityClassVideo();
    private IncreaseCountSoapVideo countAsyncTask = new IncreaseCountSoapVideo();
    private CompressionTechnique comp;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (networkUtils.getConnectivityStatusString(context)) {
            if (!getImageCount().equalsIgnoreCase("")) {
                try {
                    //appended business unit to get the application name
                    countAsyncTask.Call(context, UtilityClassVideo.BUSINESS_UNIT+ "-"
                                    + context.getApplicationInfo()
                                    .loadLabel(context.getPackageManager()), util.uniqueId(context),
                            getImageCount());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                // util.showToast(context, "Count updated successfully.");
            } else {
                // util.showToast(context, "Count already updated.");
            }
            //comp = new CompressionTechnique(context);
            //comp.inIt();
        } else {
            // Toast.makeText(context, "Unable to upload",
            // Toast.LENGTH_LONG).show();
        }
    }

    private String getImageCount() {
        if (ImageCompresionApplication.userDetailsPrefs != null) {
            return ImageCompresionApplication.userDetailsPrefs.getString("COUNT", "");
        } else {
            return "";
        }
    }
}
