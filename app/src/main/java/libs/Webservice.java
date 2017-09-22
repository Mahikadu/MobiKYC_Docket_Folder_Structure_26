package libs;

import android.content.Context;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Webservice {

	Context context;
	// String url="http://shrinkdocsws.licads.in/service1.svc";

	String url = "http://23.229.229.20/adsws/service1.svc";

	SoapSerializationEnvelope envelope;

	public Webservice(Context context) {
		this.context = context;
	}

	// GetLogin(string Username ,string Password);

	public SoapPrimitive LoginWebServer(String username, String password) {
		SoapPrimitive result = null;

		try {
			SoapObject request = new SoapObject("http://tempuri.org/",
					"GetLogin");// soap object
			request.addProperty("Username", username);
			request.addProperty("Password", password);
			// request.addProperty("Type", Type);
			Log.e("Log", "1");
			envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);// soap
			Log.e("Log", "2"); // version
			envelope.setOutputSoapObject(request); // set request object
			Log.e("Log", "3");
			envelope.dotNet = true;
			Log.e("Log", "4");
			HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
			Log.e("Log", "5"); // call
			androidHttpTransport.call("http://tempuri.org/IService1/GetLogin",
					envelope);
			Log.e("Log", "6");
			// response soap object
			result = (SoapPrimitive) envelope.getResponse();
			Log.e("Log", "7");
			Log.e("GetLogin", result.toString());
		} catch (Exception e) {
			e.printStackTrace();

		}
		return result;
	}

	public SoapPrimitive SaveVideoMetaData(String Username, String VideoName,
                                           String VideoFormat, String originalSize, String compressedSize,
                                           String storageSaved) {
		SoapPrimitive result = null;
		try {
			SoapObject request = new SoapObject("http://tempuri.org/",
					"SaveVideoMetaData");
			request.addProperty("Username", Username);
			request.addProperty("VideoName", VideoName);
			request.addProperty("VideoFormat", VideoFormat);
			request.addProperty("originalSize", originalSize);
			request.addProperty("compressedSize", compressedSize);
			request.addProperty("storageSaved", storageSaved);

			Log.e("Request", request.toString());

			envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);// soap
			// envelop
			// with

			envelope.setOutputSoapObject(request); // set request object

			envelope.dotNet = true;

			HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
			// transport

			androidHttpTransport.call(
					"http://tempuri.org/IService1/SaveVideoMetaData", envelope);

			// response soap object
			result = (SoapPrimitive) envelope.getResponse();

			Log.e("SaveVideoMetaData", result.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}
//	String namespace = "http://tempuri.org/";
//    private String url1 = "http://www.webservicex.net/ConvertWeight.asmx";
	// / http://licads.in/vuws/service.svc
	// UploadFile(byte[] f, string fileName);
	public SoapPrimitive UploadFile(String base64, String fileName) {
		SoapPrimitive result = null;
		try {
			SoapObject request = new SoapObject("http://tempuri.org/",
					"UploadFile");

			request.addProperty("base64", base64);
			request.addProperty("fileName", fileName);

//			Log.e("Request", request.toString());

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);// soap
			// envelop
			// with
			
//			new MarshalBase64().register(envelope);
			
			envelope.setOutputSoapObject(request); // set request object

			envelope.dotNet = true;

			HttpTransportSE androidHttpTransport = new HttpTransportSE("http://licads.in/vuws/FileUploader.asmx");// http
			// transport

			androidHttpTransport.call(
					"http://tempuri.org/UploadFile", envelope);

			// response soap object
			result = (SoapPrimitive) envelope.getResponse();

			Log.e("UploadFile", result.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	
//	public SoapPrimitive Base64ToImage(String base64String, String imgName) {
//		SoapPrimitive result = null;
//		try {
//			SoapObject request = new SoapObject("http://tempuri.org/",
//					"Base64ToImage");
//
//			request.addProperty("base64String", base64String);
//			request.addProperty("imgName", imgName);
//
////			Log.e("Request", request.toString());
//
//			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);// soap
//			// envelop
//			// with
//			
//			new MarshalBase64().register(envelope);
//			
//			envelope.setOutputSoapObject(request); // set request object
//
//			envelope.dotNet = true;
//
//			HttpTransportSE androidHttpTransport = new HttpTransportSE("http://emersonsws.sudesi.in/emersonsws/service1.svc");// http
//			// transport
//
//			androidHttpTransport.call(
//					"http://tempuri.org/IService1/Base64ToImage", envelope);
//
//			// response soap object
//			result = (SoapPrimitive) envelope.getResponse();
//
//			Log.e("Base64ToImage", result.toString());
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//
//	}
	
	//"testsplit(string FileName,string base64 , int Offset)"
	// http://licads.in/vuws/Uploader.svc
	public SoapPrimitive testsplit(String FileName, String base64 , int Offset) {
		SoapPrimitive result = null;
		try {
			SoapObject request = new SoapObject("http://tempuri.org/", "testsplit");

			request.addProperty("FileName", FileName);
			request.addProperty("base64", base64);
			request.addProperty("Offset", Offset);
			

//			Log.e("Request", request.toString());

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);// soap envelop with
			
//			new MarshalBase64().register(envelope);
			
			envelope.setOutputSoapObject(request); // set request object

			envelope.dotNet = true;

			HttpTransportSE androidHttpTransport = new HttpTransportSE("http://licads.in/vuws_tsplit/Uploader.svc");// http transport

			androidHttpTransport.call("http://tempuri.org/IUploader/testsplit", envelope);// response soap object
			result = (SoapPrimitive) envelope.getResponse();

			Log.e("testsplit", result.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}
	
}
