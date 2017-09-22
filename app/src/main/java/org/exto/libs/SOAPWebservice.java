package org.exto.libs;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.util.Log;

public class SOAPWebservice {

	Context context;

//	String url = "http://kycws.sudesi.in/kycws/Service1.svc";// webservice url
	String url = "http://licads.in/adsws/service1.svc";
//	String url = "http://licads.in/shrinkdocsws/service1.svc";

	// http://assetwebservice.sudesi.in/service.svc

	public SOAPWebservice(Context con) {
		context = con;
	}

	// soap service for login
	public SoapPrimitive loginWebservice(String id, String username,
										 String password) {
		SoapPrimitive result = null;

		try {
			SoapObject request = new SoapObject("http://tempuri.org/",
					"GetLogin");// soap object
			request.addProperty("Username", username);
			request.addProperty("Password", password);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);// soap envelop with version
			envelope.setOutputSoapObject(request); // set request object
			envelope.dotNet = true;
			HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
			// transport
			// call
			androidHttpTransport.call("http://tempuri.org/IService1/GetLogin",
					envelope);

			// response soap object
			result = (SoapPrimitive) envelope.getResponse();
			Log.e("result Login", result.toString());
			return result;

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}

		// soap service for change password
	public SoapObject CallSoapNoParameters(String method) {
		try {
			SoapObject request = new SoapObject("http://tempuri.org/", method);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;
			HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
			androidHttpTransport.call("http://tempuri.org/IService1/" + method,
					envelope);
			SoapObject result = (SoapObject) envelope.getResponse();
			return result;
		} catch (Exception e) {

			return null;
		}
	}

	// soap service for change password
	public SoapPrimitive CallSoapNoParametersPrimitive(String method) {
		try {
			SoapObject request = new SoapObject("http://tempuri.org/", method);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;
			HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
			androidHttpTransport.call("http://tempuri.org/IService1/" + method,
					envelope);
			SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
			return result;
		} catch (Exception e) {

			return null;
		}
	}

	// soap service for login
	public SoapPrimitive SaveScanDetails(/* String id, */String escanid,
			String serialno, String category, String empcode, Float lat,
			Float lon) {
		SoapPrimitive result = null;

		try {
			SoapObject request = new SoapObject("http://tempuri.org/",
					"SaveVerifiedAsset");// soap object

			// request.addProperty("compid",id);
			request.addProperty("EscanId", escanid);
			request.addProperty("Name", serialno);
			request.addProperty("category", category);
			request.addProperty("panno", empcode);
			request.addProperty("latitude", lat.toString());
			request.addProperty("longitude", lon.toString());

			// request.addProperty("model",model);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);// soap envelop with version
			envelope.setOutputSoapObject(request); // set request object
			envelope.dotNet = true;
			HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
																			// transport
																			// call
			androidHttpTransport.call(
					"http://tempuri.org/IService1/SaveVerifiedAsset", envelope);

			// response soap object
			result = (SoapPrimitive) envelope.getResponse();

			return result;

		} catch (Exception e) {

			return null;
		}

	}

	public SoapPrimitive ImageSaveData(String imageName, String escanid,
			String serialno) {
		SoapPrimitive result = null;

		try {
			SoapObject request = new SoapObject("http://tempuri.org/",
					"InsertImageData");// soap object

			request.addProperty("ImageName", imageName);
			request.addProperty("Format", ".jpeg");
			request.addProperty("EscanId", escanid);
			request.addProperty("panno", serialno);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);// soap envelop with version
			envelope.setOutputSoapObject(request); // set request object
			envelope.dotNet = true;
			HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
																			// transport
																			// call
			androidHttpTransport.call(
					"http://tempuri.org/IService1/InsertImageData", envelope);

			// response soap object
			result = (SoapPrimitive) envelope.getResponse();

			return result;

		} catch (Exception e) {

			return null;
		}

	}

}
