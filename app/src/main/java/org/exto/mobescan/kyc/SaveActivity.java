package org.exto.mobescan.kyc;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.exto.dbConfig.Dbcon;
import org.exto.libs.ConnectionDetector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SaveActivity extends Activity {
		
	 
	 private SharedPreferences sharedpre=null;
	 
	 private SharedPreferences.Editor saveuser=null;
	 
	 private SaveScan savescan=null;
	 
	 private Button button1=null;
	 
	 private ProgressDialog mProgress=null;
	 
	 private EditText et=null;
	 
	 private Dbcon db=null;
	 
	 private int count;
	 
	 private TextView welcome,txtview;
	 
	 private Button b1;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		db = new Dbcon(this);
		
			
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_save);
		
		button1 = (Button) findViewById(R.id.saveButton);
		
		b1=(Button) findViewById(R.id.Button01);;
		
		
		welcome = (TextView) findViewById(R.id.welcome);
		
		txtview = (TextView) findViewById(R.id.textView2);
		
		et = (EditText) findViewById(R.id.saveNameET);
		
		//saveButton = (Button) findViewById(R.id.saveButton);
				
		mProgress = new ProgressDialog(this);
		
		sharedpre = getSharedPreferences(
				"Sudesi",MODE_PRIVATE);
	
		count = sharedpre.getInt("iCount", 0);
		
		String SelectedName = sharedpre.getString("SelectedName", "");
		
		if(!SelectedName.equalsIgnoreCase(""))
		{
		//saveButton.setBackgroundDrawable();
			
			button1.setBackgroundResource(R.drawable.round_red);//(Color.parseColor("#FF9900"));
			
			b1.setBackgroundResource(R.drawable.lo1);
			
			et.setText(SelectedName);
			
			et.setEnabled(false);
			
			et.setFocusable(false);
			
			welcome.setText("Save - "+SelectedName);
		}
		else
		{
			//saveButton.setBackgroundColor(R.drawable.round);
		}
		
		findViewById(R.id.saveButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						
						 if(!et.getText().toString().trim().equalsIgnoreCase(""))
						 {	
						
							if(button1.getText().toString().trim().equals("Save"))
							{
							savescan=new SaveScan();
							
							savescan.execute();
							
						}
					else if (button1.getText().toString().trim().equals("Go Home"))
					{
						finish();
						
						startActivity(new Intent(SaveActivity.this,HomeActivity.class));
						
					}
							
						 }
						 else
						 {
							 Toast.makeText(SaveActivity.this,"enter a Name", Toast.LENGTH_LONG).show();
						 }
					
					}
				});
		
		findViewById(R.id.Button01).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						
						finish();
						
						startActivity(new Intent(SaveActivity.this,PreviewActivity.class));
										}
				});

	}

		/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */

	public class SaveScan extends AsyncTask<Void, Void, String> {		
		
		private String result=null;
		
		private String name=null;
			
		@SuppressLint("SimpleDateFormat")
		@Override
		protected String doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
		        try{  
		        	db.open();
					
	            	String Category=sharedpre.getString("Category","");
	            	String SerialNo=sharedpre.getString("SerialNo","");
	            	String EmpCode=sharedpre.getString("EmpCode","");
	            	String Model=sharedpre.getString("Model","");
	            	
	            	String latitude=String.valueOf(sharedpre.getFloat("Lat",0.0f));
	            	String longitude=String.valueOf(sharedpre.getFloat("Lon",0.0f));
	            	
	            	name = et.getText().toString().trim();
	            	
	            	String names[]={"saveName"};
	            	
	            	String SelectedName = sharedpre.getString("SelectedName", "");
	            	
	            	 Calendar c2 = Calendar.getInstance();
		                
		                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		                String formattedDate2 = null;
	            	
	            	if(!SelectedName.equalsIgnoreCase(""))
	        		{
	            	     String a[]= new String[] {"category","serialNo","empCode","modelNo","saveTime"};
						 
						
			                formattedDate2 = df1.format(c2.getTime());
						 
						 String b[]= new String[] {Category,SerialNo,EmpCode,Model,formattedDate2};
					 
						 db.update(sharedpre.getString("scannedId",""), b,a,"scan","scannedId");
						 
						 String e[]= new String[] {"savedServer","imageId"};
						 
						 Cursor data3=db.fetchallSpecify("image",e,"scannedId", sharedpre.getString("scannedId",""),null);
					
						 data3.moveToFirst();
																						
						    while(data3.isAfterLast() == false)
						    {

						    	if(data3.getInt(0)!=1)
						     {
						    	db.delete("image","imageId",String.valueOf(data3.getInt(1)));
						    	
						     }
							
						    data3.moveToNext();
						   }

						    String a2[]= new String[] {"scannedId","imagePath","saveTime"};
						 
						 		        		    
		        		    for(int k=1;k<=count;k++)
		        		    {
		        		    
		        		    	String pathLocal=sharedpre.getString("ImgPath_"+String.valueOf(k),"");
		        		    	
		        	    	        		    	
		    	                String b2[]= null;
		    	               
		    	                if(!pathLocal.trim().equalsIgnoreCase(""))
		    	                {
		    	                File fTemp = new File(pathLocal);
		    	        		
		    	        		if(fTemp.exists())
		    	        		{
		    	        			
		 			                formattedDate2 = df2.format(c2.getTime());
		 			                
		 			               b2 = new String[] {sharedpre.getString("scannedId",""),pathLocal,formattedDate2};
		 			                
		    	        		
		    	                db.insert(b2, a2, "image");
		    	        		}
		    	               }
		    	           	
		        		    }
		        		    result="1";
	            		
	        		}
	            	else
	            	{	            	
	            	Cursor data=db.fetchone(name,"scan",names,"saveName");
	            	
	            	if(data.getCount()==0)
	            	{	            	
	            
	            	Calendar c = Calendar.getInstance();
	                
	                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                String formattedDate = df.format(c.getTime());
	            	
	    	 	
	                String a[]= new String[] {"category","serialNo","empCode","modelNo","latitude","longitude","saveName","saveTime"};
	                String b1[]= new String[] {Category,SerialNo
						  ,EmpCode,Model,
						  latitude,longitude,name,formattedDate};
	                
	                db.insert(b1, a, "scan");
	                
	                String names1[]={"scannedId"};
	                
	                Cursor data1=db.fetchone(name,"scan",names1,"saveName");
	                
	                String saveId = String.valueOf(data1.getInt(0));
	                
	                if(saveId!=null&&!saveId.equalsIgnoreCase("0"))
	                {   
	        		    
	        		    String pathLocal="";
	                 
	        		    String a2[]= new String[] {"scannedId","imagePath","saveTime"};
	        		    
	        		    for(int j=1;j<=count;j++)
	        		    {
	        		    
	        		    	pathLocal=sharedpre.getString("ImgPath_"+String.valueOf(j),"");
	        		    	        		    	
	    	                String b2[]= null;
	    	                
	    	                if(!pathLocal.trim().equalsIgnoreCase(""))
	    	                {
	    	                
	    	                File fTemp = new File(pathLocal);
	    	        		
	    	        		if(fTemp.exists())
	    	        		{
	    	        			formattedDate2 = df2.format(c2.getTime());
	    	        	            
	    	        			b2 = new String[] {saveId,pathLocal,formattedDate2};
	    	        			
	    	                db.insert(b2, a2, "image");
	    	        		}
	    	        		
	    	                }
	    	            }

	                }
	                	                
	                
	                result="1";
	            	}
	            	else
	            	{
	            		result="2";
	            	}
	            
	            	}
	            	
	            	db.close();
			     }
	            catch(Exception e)
	            {	
	            	db.close();
	            	
	          //  	e.printStackTrace();
	            	
	            	result=null;
	    	     }

			
			return result;
		}
		
		@Override
		protected void onPreExecute() {
			
		mProgress.setMessage("Processing...");
		mProgress.show();
		}
		
		@Override
		protected void onPostExecute(String result) {
			
			try
			{		    
				
							
			if(result!=null)
			{	
				
				if(result.equalsIgnoreCase("1"))
				{
							    
			    button1.setText("Saved");
			    
			    saveuser = sharedpre.edit();
				saveuser.putString("Category","");
				saveuser.putString("SerialNo","");
				saveuser.putString("EmpCode","");
				saveuser.putString("Model","");
				
				saveuser.putFloat("Lon",0.0f);
				
				  for(int j=1;j<=count;j++)
      		    {
      		    
					  saveuser.putString("ImgPath_"+String.valueOf(count),"");
      		    }
				  
				  saveuser.putInt("CheckedBox",0);
				
				saveuser.putInt("iCount",0);
				
				saveuser.putString("scannedId","");
	    		saveuser.putString("SavedServer","");
	    		
	    		saveuser.putString("SelectedName","");
	    						
				saveuser.commit();	
				
				welcome.setText("");
				
				txtview.setText("");
				
				et.setVisibility(View.GONE);
				
				b1.setVisibility(View.GONE);
			 
				Toast.makeText(SaveActivity.this, "Saved Successfully", Toast.LENGTH_LONG).show();
		
				
				
				ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

				// Check if Internet present
				if (!cd.isConnectingToInternet()) {
					// Internet Connection is not present
					finish();
					startActivity(new Intent(SaveActivity.this,HomeActivity.class));
					// stop executing code by return

				}
				else
				{	
				
					new AlertDialog.Builder(SaveActivity.this)
			        .setIcon(android.R.drawable.ic_dialog_alert)
			        .setTitle("Save To Server")
			        .setMessage("Do you want save the details to server")
			        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			            @Override
			            public void onClick(DialogInterface dialog, int which) {

			                //Stop the activity
			            	SharedPreferences sharedpre=null;
			           	 
			            	SharedPreferences.Editor saveuser=null;
			            	 
			            	sharedpre = getSharedPreferences("Sudesi",0);
			            	
			            	  saveuser = sharedpre.edit();					            
				  	    		saveuser.putString("LoginSelectedName",name);
				  	    		
				  	         	saveuser.commit();
			            	
				  	         	finish();
			            	startActivity(new Intent(SaveActivity.this,LoginActivity.class));
			    			
			            }

			        })
			        .setNegativeButton("No", new DialogInterface.OnClickListener() {

			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	
			               	SharedPreferences sharedpre=null;
				           	 
			            	SharedPreferences.Editor saveuser=null;
			            	 
			            	sharedpre = getSharedPreferences("Sudesi",0);
			            	
			            	  saveuser = sharedpre.edit();					            
				  	    		saveuser.putString("LoginSelectedName","");
				  	    		
				  	         	saveuser.commit();
			    
			           
				  	         	finish();
			            	startActivity(new Intent(SaveActivity.this,HomeActivity.class));
			    			
			            }

			        })
			        .show();

					
				}
				
				}
				else
				{
					Toast.makeText(SaveActivity.this,"Error In Saving!!! Name Exists", Toast.LENGTH_LONG).show();
				}
				
				}
				else
				{
					Toast.makeText(SaveActivity.this,"Something Went Wrong!!! Try Later", Toast.LENGTH_LONG).show();
				}
						
			mProgress.dismiss();			
			savescan=null;
			
			}
			catch(Exception e)
			{
				Toast.makeText(SaveActivity.this, "Something Went Wrong!!! " , Toast.LENGTH_LONG).show();
			
				mProgress.dismiss();
				
			//	e.printStackTrace();
				
				savescan=null;
			
				
			}
		}

		@Override
		protected void onCancelled() {
			
			mProgress.dismiss();
			savescan=null;
			
		}	   
	}
}