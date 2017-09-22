package com.adstringo.imagecompression.countutils;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.sudesi.adstringocompression.ImageCompresionApplication;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.concurrent.ExecutionException;

public class IncreaseCountSoapVideo {

    public final String SOAP_ACTION = "http://tempuri.org/SaveBulkImagesData";
    public final String OPERATION_NAME = "SaveBulkImagesData";
    public final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    private InsertCountTask insertCountTask = new InsertCountTask();
    private Context mContext;

    // public final String SOAP_ADDRESS =
    // "http://licads.in/Activation/Service.svc";
    // private static final String CALL_NAMESPACE =
    // "http://tempuri.org/IService/";
    // private UtilityClassVideo util = new UtilityClassVideo();

    public IncreaseCountSoapVideo() {

    }

    public void Call(Context context, String businessUnit, String imei,
                     String count) throws InterruptedException, ExecutionException {
        this.mContext = context;
        if (insertCountTask.getStatus() != AsyncTask.Status.RUNNING) {
            insertCountTask.execute(businessUnit, imei, String.valueOf(count));
        }
        // return new InsertCountTask().execute(businessUnit, imei,
        // count).get();
    }

    private class InsertCountTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,
                    OPERATION_NAME);
            PropertyInfo pi = new PropertyInfo();

            pi.setName("Username");
            pi.setValue(params[0]);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("IMEINO");
            pi.setValue(params[1]);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("ImageData");
            pi.setValue(params[2]);
            pi.setType(Integer.class);
            request.addProperty(pi);

            //For UAt comment Below Line
            /*pi = new PropertyInfo();
			pi.setName("Source");
			pi.setValue(UtilityClassVideo.SOURCE);
			pi.setType(String.class);
			request.addProperty(pi);*/


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransport = new HttpTransportSE(
                    UtilityClassVideo.SOAP_ADDRESS_UAT);
            Object response = null;
            try {
                httpTransport.call(UtilityClassVideo.CALL_NAMESPACE_UAT
                        + OPERATION_NAME, envelope);
                response = envelope.getResponse();


            } catch (Exception exception) {
                response = exception.toString();
            }
            return response.toString();

        }

        @Override
        protected void onPostExecute(String result) {
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
            Log.v("ImageParams", result);

            if (result.trim().equalsIgnoreCase("Success")) {
                //Toast.makeText(mContext, "Count : " + getImageCount().split(":").length, Toast.LENGTH_SHORT).show();
                Log.v("ImageParams", getImageCount());
                saveProfileDetails();
                Log.v("Count Log", "Count updated successfully.");

            }
        }
    }

    private void saveProfileDetails() {
        Editor profileDetailsPrefs = ImageCompresionApplication.userDetailsPrefs
                .edit();
        profileDetailsPrefs.putString("COUNT", "");
        profileDetailsPrefs.commit();
    }

    private String getImageCount() {
        return ImageCompresionApplication.userDetailsPrefs.getString("COUNT",
                "");
    }
}
