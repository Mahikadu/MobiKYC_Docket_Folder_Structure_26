package com.adstringo.compressionreceiver;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.adstringo.imagecompression.countutils.UtilityClassVideo;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class NetworkUtil {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public boolean hasActiveInternetConnection(Context context) {
        DownloadFilesTask task = new DownloadFilesTask();
        try {
            return task.execute("").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        }

    }
    /*private void exampleTwo() {
        new MyThread().start();
    }

    private static class MyThread extends Thread {
        @Override
        public void run() {
            while (true) {

            }
        }
    }*/
    public boolean getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkUtil.TYPE_WIFI) {
            status = UtilityClassVideo.NETWORK_CONNECTED;
            return hasActiveInternetConnection(context);
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = UtilityClassVideo.NETWORK_CONNECTED;
            return hasActiveInternetConnection(context);
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return false;
    }
    private class DownloadFilesTask extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... urls) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (Exception e) {
                return false;
            }
        }

        protected void onProgressUpdate(Integer... progress) {
        }

    }
}
