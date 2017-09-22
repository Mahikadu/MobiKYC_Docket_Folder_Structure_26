package org.exto.Login;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.exto.ListView.LoginListAdapter;
import org.exto.Model.ImageModel;
import org.exto.dbConfig.Dbcon;
import org.exto.libs.ConnectionDetector;
import org.exto.libs.ImageUtils;
import org.exto.libs.SOAPWebservice;
import org.exto.mobescan.kyc.HomeActivity;
import org.exto.mobescan.kyc.LoginActivity;
import org.exto.mobescan.kyc.R;
import org.ksoap2.serialization.SoapPrimitive;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPreviewActivity extends ListActivity {
	
	private SharedPreferences sharedpre=null;
	 
	 private SharedPreferences.Editor saveuser=null;

	 private ListView listView;
	
	private String scannedId;
	
	 private Dbcon db;
	 
	 private TextView welcome,header;
	 
	 private SaveScan savescan=null;
 
	 private ArrayAdapter<ImageModel> adapter=null;
	 
	 private ProgressDialog mProgress=null;
	 
	 private String pathLocal;
	 
	 private SOAPWebservice service = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.loginactivity_preview);
		
		listView = (ListView) findViewById(android.R.id.list);
		
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
	    WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
	    wm.getDefaultDisplay().getMetrics(displayMetrics);

	    int screenHeight = displayMetrics.heightPixels-120;
	    
	    listView.setMinimumHeight(screenHeight);
	
		sharedpre = getSharedPreferences(
				"Sudesi",MODE_PRIVATE);
		
		mProgress = new ProgressDialog(this);
		
		welcome = (TextView) findViewById(R.id.welcome); 
		
		header = (TextView) findViewById(R.id.header);
		
		scannedId = sharedpre.getString("LoginscannedId", "");
		 		
		String SelectedName=sharedpre.getString("LoginSelectedName", "");
		
		String name=sharedpre.getString("LoginEmailid", "");
		
		welcome.setText("Welcome "+name);
		
		header.setText("Preview - "+SelectedName);
		
		db = new Dbcon(this);
			
			db.open();
			
			String a[]= new String[] {"imagePath","savedServer","scannedId","saveTime"};
			
			Cursor data=db.fetchallSpecify("image",a,"scannedId", scannedId,"imageId DESC");//"saveTime DESC"
			
			data.moveToFirst();
			
			saveuser = sharedpre.edit();

			saveuser.putInt("LoginCheckedBox",0);
            
			int count2=0;
				
		    while(data.isAfterLast() == false)
		    {
		    
		    	if(data.getInt(1)!=1)
		    	{
		     count2++;
             
		     saveuser.putString("LoginImgPath_"+String.valueOf(count2),data.getString(0));
		    	}
		    data.moveToNext();
		   }
		    saveuser.putInt("LoginiCount", count2);
		    
		    saveuser.commit();
		    
		    db.close();
			
		
		
		adapter = new LoginListAdapter(this,
		        getModel());
		    setListAdapter(adapter);
						
			
			findViewById(R.id.buttonNext).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							
							 int count = sharedpre.getInt("LoginiCount", 0);
							 
							 int select = sharedpre.getInt("LoginCheckedBox", 0);			
							 
							 int img=0;
					         
							 String pathLocal="";
							 
							    for(int j=1;j<=count;j++)
							    {								    
							    	pathLocal=sharedpre.getString("LoginImgPath_"+String.valueOf(j),"");

							    	if(!pathLocal.equalsIgnoreCase(""))
							    	{
							    		img++;
							    	}								    
							    }
							  
							  if(img==0)
							  {
								  Toast.makeText(LoginPreviewActivity.this, "No Pictures", Toast.LENGTH_LONG).show();
							  }
							  else
							  {
								  if(select==0)
								  {
									  Toast.makeText(LoginPreviewActivity.this, "Select One Picture", Toast.LENGTH_LONG).show();
								  }
								  else
								  {
										ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

										// Check if Internet present
										if (!cd.isConnectingToInternet()) {
											// Internet Connection is not present
											Toast.makeText(LoginPreviewActivity.this,"Check Your Internet Connection!!!", Toast.LENGTH_LONG).show();
											// stop executing code by return

										}
										else
										{	
									  savescan=new SaveScan();
									  
									  service = new SOAPWebservice(LoginPreviewActivity.this);
									  
									  savescan.execute();
										}
								  }
							  }
						}
					});
			
			findViewById(R.id.button_new).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							
							finish();
							startActivity(new Intent(LoginPreviewActivity.this,LoginMainActivity.class));
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
							
							startActivity(new Intent(LoginPreviewActivity.this,LoginActivity.class));
							//
						}
					});			


	}
	
	@Override
	public void onContentChanged() {
	    super.onContentChanged();

	    View empty = findViewById(R.id.empty);
	    ListView list = (ListView) findViewById(android.R.id.list);
	    list.setEmptyView(empty);
	}
	
	 private List<ImageModel> getModel() {
		 
		 List<ImageModel> list = new ArrayList<ImageModel>();
		 
		 try{

			 int count = sharedpre.getInt("LoginiCount", 0);
			 
			 int tCount=0;
		    
		    String pathLocal="";
		 
		    for(int j=1;j<=count;j++)
		    {
		    
		    	pathLocal=sharedpre.getString("LoginImgPath_"+String.valueOf(j),"");		   
		   	
		   
		    	  if(!pathLocal.trim().equalsIgnoreCase(""))
	                {

		    	File fTemp = new File(pathLocal);
    		
    		if(fTemp.exists())
    		{
    			
		    list.add(get(pathLocal,j,1));
		    
		    tCount++;
    		}
    		
	                }
		    
		    }
		    
		    if(tCount==0)
		    {
		    	finish();
		    	startActivity(new Intent(LoginPreviewActivity.this,HomeActivity.class));
		    }
		    
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
			 list=null;
		 }
	
		 return list;
		  }

		  private ImageModel get(String s,int i,int q) {
		    return new ImageModel(s,i,1);
		  }
		  
		  /**
			 * Represents an asynchronous login/registration task used to authenticate
			 * the user.
			 */
			public class SaveScan extends AsyncTask<Void, Void, SoapPrimitive> {		
					
				
				private SoapPrimitive soap_result=null;	
				
				@Override
				protected SoapPrimitive doInBackground(Void... params) {
					// TODO: attempt authentication against a network service.
					
					String result=null;
					
			            try{            
			            	
			            		
			            pathLocal=sharedpre.getString("LoginImgPath_"+String.valueOf(sharedpre.getInt("LoginCheckedBox", 0)),"");	
			           
//			            result = ImageUtils.getCompressedImagePath(pathLocal, sharedpre.getString("LoginScanId",""));
			            
			            String[] split=null;
			           
			           if(result!=null)
				       {
			        	   split = result.split("~");
				           
				            String split0 = split[1].substring(0, split[1].length()-5);
				           
			           soap_result=service.ImageSaveData(split0,sharedpre.getString("LoginScanId",""),sharedpre.getString("LoginEmpCode",""));
				       }
			            else
			            {
			            	soap_result=null;
			            }
			           
			           if(soap_result!=null&&soap_result.toString().trim().equals("true"))
			           {
			        	   ///
			        	   db.open();
							
							 String a[]= new String[] {"savedServer"};
							 
							 String b[]= new String[] {"1"};
							 
							 String id[]= new String[] {sharedpre.getString("LoginscannedId",""),pathLocal};
							
							db.updateMulti("scannedId = ? AND imagePath = ?", b,a,"image",id);
							 
							 db.close();
							
							File file = new File(split[0]);
							
							if(file.exists())
							{
								file.delete();
								
							}
							///
			           }
			           
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
					
				mProgress.setMessage("Uploading...");
				mProgress.show();
				
				}
				
				@Override
				protected void onPostExecute(SoapPrimitive result) {
					
					try
					{
											
					if(result!=null&&result.toString().trim().equals("true"))
					{
						Toast.makeText(LoginPreviewActivity.this,"Image Uploaded...", Toast.LENGTH_LONG).show();
												
						
							db.open();
							
							String a1[]= new String[] {"imagePath","savedServer","scannedId","saveTime"};
							
							Cursor data=db.fetchallSpecify("image",a1,"scannedId", scannedId,"imageId DESC");//"saveTime DESC"
							
							data.moveToFirst();
							
							saveuser = sharedpre.edit();

							saveuser.putInt("LoginCheckedBox",0);
				            
							int count2=0;
								
						    while(data.isAfterLast() == false)
						    {
						    
						    	if(data.getInt(1)!=1)
						    	{
						     count2++;
				             
						     saveuser.putString("LoginImgPath_"+String.valueOf(count2),data.getString(0));
						    	}
						    data.moveToNext();
						   }
						    saveuser.putInt("LoginiCount", count2);
						    
						    saveuser.commit();
						    
						    db.close();
						    
						    adapter = new LoginListAdapter(LoginPreviewActivity.this,
							        getModel());
							    setListAdapter(adapter);
											    
					
						}
						else
						{
							Toast.makeText(LoginPreviewActivity.this,"Error. Try Again!!!", Toast.LENGTH_LONG).show();
						}
								
					mProgress.dismiss();			
					savescan=null;
					service=null;
					}
					catch(Exception e)
					{
						Toast.makeText(LoginPreviewActivity.this, "Something went wrong!!!", Toast.LENGTH_LONG).show();
										
						mProgress.dismiss();
						
						savescan=null;
						
						service=null;
					
					}
				}

				@Override
				protected void onCancelled() {
					
					mProgress.dismiss();
					savescan=null;
					service=null;
				}	   
			}

}