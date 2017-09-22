package org.exto.Login;

import org.exto.dbConfig.Dbcon;
import org.exto.libs.ConnectionDetector;
import org.exto.libs.SOAPWebservice;
import org.exto.mobescan.kyc.LoginActivity;
import org.exto.mobescan.kyc.R;
import org.ksoap2.serialization.SoapPrimitive;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class LoginMainActivity extends Activity {
	

	 private TextView spinner1,header;
	 
	 private TextView sno;
	 private TextView ecode;
	 private TextView model;
	 
	 private SharedPreferences sharedpre=null;
	 
	 private SharedPreferences.Editor saveuser=null;
	 
	 private String sName="",savedServer;
	 
	 private TextView welcome;
	 
	 private SOAPWebservice service=null;
	 
	 private ProgressDialog mProgress=null;
	 
	 private GetScanId getscanid=null;
	 
	 private SaveDetails savedetails=null;
	 
	 
	 private Dbcon db;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.loginactivity_main);
		
	
		sharedpre = getSharedPreferences(
				"Sudesi",MODE_PRIVATE);
		
		
		sno = (TextView) findViewById(R.id.sno);

		ecode = (TextView) findViewById(R.id.ecode);
		
		model = (TextView) findViewById(R.id.model);		
		
		
		welcome = (TextView) findViewById(R.id.welcome);
		
		header = (TextView) findViewById(R.id.header);
	
		spinner1 = (TextView) findViewById(R.id.spinner1);
				
		sName = sharedpre.getString("LoginSelectedName", "");
		
		String name = sharedpre.getString("LoginEmailid", "");
		
		welcome.setText("Welcome "+name);
		
		header.setText("View - "+sName);
		
		mProgress = new ProgressDialog(this);
		
			
		db = new Dbcon(this);
		
		db.open();
			
		 String a[]= new String[] {"category","serialNo","empCode","modelNo","latitude","longitude","saveName","saveTime","scannedId","savedServer","_scanId"};
			
			Cursor data=db.fetchone(sName,"scan",a,"saveName");
		
			
			saveuser = sharedpre.edit();
			saveuser.putString("LoginCategory",data.getString(0));
			saveuser.putString("LoginSerialNo",data.getString(1));
			saveuser.putString("LoginEmpCode",data.getString(2));
			saveuser.putString("LoginModel",data.getString(3));
			
			saveuser.putFloat("LoginLat",Float.valueOf(data.getString(4)));
    		saveuser.putFloat("LoginLon",Float.valueOf(data.getString(5)));
    		
    		saveuser.putString("LoginscannedId",String.valueOf(data.getInt(8)));
    		
    		saveuser.putString("LoginSavedServer",String.valueOf(data.getInt(9)));
    		
    		saveuser.putString("LoginScanId",data.getString(10));
							    		
			saveuser.commit();
		
			spinner1.setText(data.getString(0));
			
			sno.setText(data.getString(1));
			ecode.setText(data.getString(2));
			model.setText(data.getString(3));
			
			db.close();
			
			savedServer=sharedpre.getString("LoginSavedServer","");
			
			String LoginscannedId=sharedpre.getString("LoginScanId","");
			
			
			
			ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

			// Check if Internet present
			if (!cd.isConnectingToInternet()) {
				// Internet Connection is not present
				Toast.makeText(LoginMainActivity.this,"Check Your Internet Connection!!!", Toast.LENGTH_LONG).show();
				// stop executing code by return

			}
			else
			{	
				if(LoginscannedId.trim().equalsIgnoreCase("")){
					
		getscanid=new GetScanId();
		
		service = new SOAPWebservice(LoginMainActivity.this);
   	       
		getscanid.execute();
				}
			}
		
		
		findViewById(R.id.buttonHome).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						
						finish();
				
							startActivity(new Intent(LoginMainActivity.this,LoginViewActivity.class));
					}
				});
		
		findViewById(R.id.button_exit).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						saveuser = sharedpre.edit();
						
						saveuser.putBoolean("Login_Status", false);
					
						saveuser.putInt("LoginiCount",0);
						
						saveuser.putInt("LoginCheckedBox",0);
						
						saveuser.putString("LoginSelectedName","");		
						
						saveuser.putString("LoginscannedId", "");
						
						saveuser.commit();
						
						finish();					
						
						startActivity(new Intent(LoginMainActivity.this,LoginActivity.class));
						//
					}
				});			
		findViewById(R.id.button2).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						
						savedServer=sharedpre.getString("LoginSavedServer","");
					
						if(savedServer.trim().equalsIgnoreCase("1")){
						
							finish();
							startActivity(new Intent(LoginMainActivity.this,LoginPreviewActivity.class));
						}
						else
						{
							ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
							
							if (!cd.isConnectingToInternet()) {
								// Internet Connection is not present
								Toast.makeText(LoginMainActivity.this,"Check Your Internet Connection!!!", Toast.LENGTH_LONG).show();
								// stop executing code by return

							}
							else
							{	
							savedetails=new SaveDetails();
							
							service = new SOAPWebservice(LoginMainActivity.this);
					   	       
							savedetails.execute();
							}
						}
							
						}
						
					
				});
	}
	
	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class GetScanId extends AsyncTask<Void, Void, SoapPrimitive> {		
		
		private SoapPrimitive soap_result=null;		
		
		@Override
		protected SoapPrimitive doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			
			try{
							
				soap_result = service.CallSoapNoParametersPrimitive("GetScanId");
				
			   		    
			}
			catch(Exception e)
			{
				
				soap_result=null;
				service=null;
			}
						
			return soap_result;
		}
		
		@Override
		protected void onPreExecute() {
			
		mProgress.setMessage("Preparing..");
		mProgress.show();
		}
		
		@Override
		protected void onPostExecute(SoapPrimitive result) {
			
			try
			{
				
			if(result!=null)
			{
					
				if(!result.toString().trim().equals("")&&result!=null){
					
					db.open();
					
					 String a[]= new String[] {"_scanId"};
					 
					 String b[]= new String[] {result.toString().trim()};				
					
					db.update(sharedpre.getString("LoginscannedId",""), b, a, "scan", "scannedId");
					
					db.close();
										
					saveuser = sharedpre.edit();
					saveuser.putString("LoginScanId",result.toString().trim());
					saveuser.commit();
					
					//finish();
					//startActivity(new Intent(LoginMainActivity.this,LoginPreviewActivity.class));			
				
				}
				else
				{
					Toast.makeText(LoginMainActivity.this,"Problem in Getting ScanId. Try Again!!!", Toast.LENGTH_LONG).show();
				}
				
			}
			service=null;
			mProgress.dismiss();
			}
			catch(Exception e)
			{
				Toast.makeText(LoginMainActivity.this, "Something Went Wrong!!! Try Again", Toast.LENGTH_LONG).show();
			
				mProgress.dismiss();
				
				service = null;
				getscanid=null;
			}
		}

		@Override
		protected void onCancelled() {
			mProgress.dismiss();
			service = null;
			getscanid=null;
			
		}	   
	}
	
	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class SaveDetails extends AsyncTask<Void, Void, SoapPrimitive> {		
		
		private SoapPrimitive soap_result=null;		
			
		@Override
		protected SoapPrimitive doInBackground(Void... params) {
				            
	            try{
	            	
	            	
	    soap_result =service.SaveScanDetails(/*sharedpre.getString("LoginCompId",""),*/sharedpre.getString("LoginScanId",""),
		sharedpre.getString("LoginSerialNo",""),sharedpre.getString("LoginCategory",""),sharedpre.getString("LoginEmpCode",""),
		sharedpre.getFloat("LoginLat",0.0f),sharedpre.getFloat("LoginLon",0.0f));
	           	
	        	            }
	            catch(Exception e)
	            {
	            	soap_result=null;
	            }

			
			return soap_result;
		}
		
		@Override
		protected void onPreExecute() {
			
		mProgress.setMessage("Sending...");
		mProgress.show();
		}
		
		@Override
		protected void onPostExecute(SoapPrimitive result) {
			
			try
			{
				mProgress.dismiss();		

			if(result!=null&&!result.toString().trim().equals("false"))
			{
				db.open();
				
				 String a[]= new String[] {"savedServer"};
				 
				 String b[]= new String[] {"1"};				
				
				db.update(sharedpre.getString("LoginscannedId",""), b, a, "scan", "scannedId");
				
				db.close();
							
			
				
			    Toast.makeText(LoginMainActivity.this,"Transfer Success", Toast.LENGTH_LONG).show();
			    
			    finish();
				startActivity(new Intent(LoginMainActivity.this,LoginPreviewActivity.class));
				}
				else
				{
					Toast.makeText(LoginMainActivity.this,"Error. Try Again!!!", Toast.LENGTH_LONG).show();					
				}
						
						
			savedetails=null;
			service = null;
			}
			catch(Exception e)
			{
				Toast.makeText(LoginMainActivity.this, "Something went wrong!!!", Toast.LENGTH_LONG).show();
								
				mProgress.dismiss();
				
				savedetails=null;
				
				service = null;
				
			}
		}

		@Override
		protected void onCancelled() {
			
			service = null;
			mProgress.dismiss();
			savedetails=null;
			
		}	   
	}
}