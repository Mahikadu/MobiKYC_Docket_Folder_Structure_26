package com.adstringo.imagecompression.countutils;

import android.os.AsyncTask;
import android.util.Log;

import com.sudesi.adstringocompression.CompressionTechnique;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.concurrent.ExecutionException;

public class ValidateUserSoapVideo {
    public final String OPERATION_NAME = "RegisterJarUser";
    // public final String SOAP_ADDRESS =
    // "http://licads.in/Activation/Service.svc";

    private static final String NAMESPACE = "http://tempuri.org/";

    // private String result =

    public ValidateUserSoapVideo() {

    }

    public String Call(String businessUnit, String imei)
            throws InterruptedException, ExecutionException {
        return new ValidateSoapTask().execute(businessUnit, imei).get();

    }

    private class ValidateSoapTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            SoapObject request = new SoapObject(NAMESPACE, OPERATION_NAME);
            PropertyInfo propInfo = new PropertyInfo();
            propInfo.type = PropertyInfo.STRING_CLASS;
            // adding parameters
            request.addProperty("Username", params[0]);
            request.addProperty("IMEINo", params[1]);
            //For UAT,Comment below line
            // request.addProperty("Source", UtilityClassVideo.SOURCE);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransport = new HttpTransportSE(
                    UtilityClassVideo.SOAP_ADDRESS_UAT);
            String result = null;
            try {
                httpTransport.call(UtilityClassVideo.CALL_NAMESPACE_UAT
                        + OPERATION_NAME, envelope);
                SoapPrimitive resultSoapPrimitive;
                resultSoapPrimitive = (SoapPrimitive) envelope.getResponse();
                if (resultSoapPrimitive != null) {
                    result = resultSoapPrimitive.toString();
                    CompressionTechnique.saveBusinessUnits(result);

                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("Video authenticate Log", result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

    }
}