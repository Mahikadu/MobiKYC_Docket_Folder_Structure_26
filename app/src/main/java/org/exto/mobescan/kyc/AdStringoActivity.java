package org.exto.mobescan.kyc;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.exto.dbConfig.Dbcon;
import org.exto.libs.ImageUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class AdStringoActivity extends Activity {

	Button btn_back, btn_save, btn_resolution, btn_quality;
	RelativeLayout ftp_layout, resolution_layout, quality_layout;

	int flgRESO = 0, flgQUA = 0;

	RadioGroup rdg_optn, rdg_optn1;

	RadioButton rb_reso, rb_qua;

	private SharedPreferences sharedpre = null;
	
	String appNo;
	
	 private Dbcon db=null;
	 
	 private ProgressDialog mProgress=null;
	 
	 SaveSubmit save;

	String strQua = "", strReso = "";
	private SharedPreferences.Editor saveuser = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_adstringo);

		sharedpre = getSharedPreferences("Sudesi", MODE_PRIVATE);

		saveuser = sharedpre.edit();
		
		appNo=sharedpre.getString("ApplicationNo", "");

		btn_resolution = (Button) findViewById(R.id.btn_resolution);
		btn_quality = (Button) findViewById(R.id.btn_quality);

		resolution_layout = (RelativeLayout) findViewById(R.id.resolution_layout);
		quality_layout = (RelativeLayout) findViewById(R.id.quality_layout);

		rdg_optn = (RadioGroup) findViewById(R.id.rdg_optn);
		rdg_optn1 = (RadioGroup) findViewById(R.id.rdg_optn1);
		
		 db = new Dbcon(this);
		 
		 db.open();
		 
		 mProgress = new ProgressDialog(AdStringoActivity.this);

		findViewById(R.id.Button01).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						finish();

						startActivity(new Intent(AdStringoActivity.this,
								PreviewActivity.class));
					}
				});
		
		findViewById(R.id.button_exit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				startActivity(new Intent(AdStringoActivity.this,LoginActivity.class));
			}
		});

		btn_resolution.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (flgRESO == 0) {
					resolution_layout.setVisibility(View.VISIBLE);
					flgRESO = 1;
				} else if (flgRESO == 1) {
					resolution_layout.setVisibility(View.GONE);
					flgRESO = 0;
				}
			}
		});

		btn_quality.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (flgQUA == 0) {
					quality_layout.setVisibility(View.VISIBLE);
					flgQUA = 1;
				} else if (flgQUA == 1) {
					quality_layout.setVisibility(View.GONE);
					flgQUA = 0;
				}
			}
		});

		rdg_optn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				rb_reso = (RadioButton) findViewById(checkedId);
				// strReso=rb_reso.getText().toString();

				String optn;

				switch (checkedId) {
				case R.id.rdo_org:
					optn = rb_reso.getText().toString();
					saveuser.putString("Reso", optn);
					saveuser.commit();
					break;
				case R.id.rdo_high:
					optn = rb_reso.getText().toString();
					saveuser.putString("Reso", optn);
					saveuser.commit();
					break;
				case R.id.rdo_medium:
					optn = rb_reso.getText().toString();
					saveuser.putString("Reso", optn);
					saveuser.commit();
					break;
				case R.id.rdo_low:
					optn = rb_reso.getText().toString();
					saveuser.putString("Reso", optn);
					saveuser.commit();
					break;
				}

			}
		});

		rdg_optn1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub

				rb_qua = (RadioButton) findViewById(checkedId);
				String quality;

				switch (checkedId) {
				case R.id.rdo_org1:
					quality = rb_qua.getText().toString();
					saveuser.putString("quality", quality);
					saveuser.commit();
					break;
				case R.id.rdo_high1:
					quality = rb_qua.getText().toString();
					saveuser.putString("quality", quality);
					saveuser.commit();
					break;
				case R.id.rdo_medium1:
					quality = rb_qua.getText().toString();
					saveuser.putString("quality", quality);
					saveuser.commit();
					break;
				case R.id.rdo_low1:
					quality = rb_qua.getText().toString();
					saveuser.putString("quality", quality);
					saveuser.commit();
					break;
				}

			}
		});

		findViewById(R.id.saveButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						
						if(rdg_optn.getCheckedRadioButtonId()!=-1 && rdg_optn1.getCheckedRadioButtonId()!=-1)
						{
							save=new SaveSubmit();
							save.execute();
						}						
						else
						{
							Toast.makeText(AdStringoActivity.this, "Select Resolution and Quality", Toast.LENGTH_LONG).show();
						}

						
					}
				});

	}
	
	public class SaveSubmit extends AsyncTask<Void, Void, String>
	{
		
		String fname;

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				int count=sharedpre.getInt("iCount", 0);
				
				fname="jdfjfj";
				
//				String resolution=sharedpre.getString("Reso", "");
//				String quality=sharedpre.getString("quality", "");
				
				String resolution="Original";
				String quality="Medium";
				
				String a[]=new String[]{"_scanId","resolution","quality","savedServer"};
				String b[]=new String[]{appNo,resolution,quality,"0"};
				
				db.insert(b, a, "scan");
				
				String imagePath[]=new String[count];
				String docType[]=new String[count];
				
				String a1[]=new String[]{"scannedId"};
				
				Cursor cur=db.fetchone(appNo, "scan", a1, "_scanId");
				int scannedId = 0;
				if(cur!=null && cur.getCount()>0)
				{
					cur.moveToFirst();
					scannedId=cur.getInt(0);
					
				}

				Calendar c2 = Calendar.getInstance();
				
				SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String formattedDate2 = null;
				
				
				String c[]=new String[]{"scannedId","imagePath","savedServer","saveTime"};

				formattedDate2 = df1.format(c2.getTime());
				for(int i=1;i<=count;i++)
				{
					imagePath[i-1]=sharedpre.getString("ImgPath_"+i , "");
					
					docType[i-1]=sharedpre.getString("docType_"+i, "");
				
					String d[]=new String[]{String.valueOf(scannedId),imagePath[i-1],"0",formattedDate2};
					
					db.insert(d, c, "image");
				
				}
				
				String b1[]=new String[]{"scannedId"};
				
				Cursor fetchCur=db.fetchallSpecify("scan", b1, "_scanId", appNo, null);
				
				if(fetchCur!=null&& fetchCur.getCount()>0)
				{
					fetchCur.moveToFirst();
					String names[]=new String[]{"imagePath","savedServer"};
					Cursor fetchimg=db.fetchallSpecify("image", names, "scannedId", String.valueOf(fetchCur.getInt(0)), null);
					
					if(fetchimg!=null && fetchimg.getCount()>0)
					{
						fetchimg.moveToFirst();
						int k=1;
						do
						{
							if(fetchimg.getInt(1)==0)
							{
								Log.e("scannedID", String.valueOf(fetchCur.getInt(0)));
								Log.e("ImagePath", String.valueOf(fetchimg.getString(0)));
//								fname=ImageUtils.getCompressedImagePath(fetchimg.getString(0), appNo, resolution, quality, docType[k-1]);
								if(fname!=null)
								{
									
									String v[]=new String[]{"1"};
									String n[]=new String[]{"savedServer"};
									
									boolean up=db.update("scannedId", v, n, "image",String.valueOf(fetchCur.getInt(0)) );
									Log.e("up", String.valueOf(up));
								}
								
							}
							k++;
						}while(fetchimg.moveToNext());
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return fname;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			mProgress.dismiss();
			if(fname!=null)
			{
				db.close();
				Toast.makeText(AdStringoActivity.this, "Images Saved Successfully", Toast.LENGTH_LONG).show();
				saveuser.clear();
				saveuser.commit();
				finish();
				startActivity(new Intent(AdStringoActivity.this,PreviewActivity.class));
			}
			
			else
			{
				Toast.makeText(AdStringoActivity.this, "Check Network Connectivity....", Toast.LENGTH_LONG).show();
			}
			
			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgress.setMessage("Processing...");
			mProgress.show();
			
			
			
		}
		
	}
	
	
	
	
	

}
