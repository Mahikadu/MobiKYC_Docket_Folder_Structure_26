package org.exto.mobescan.kyc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class HomeActivity extends Activity{

 private SharedPreferences sharedpre=null;
	 
	 private SharedPreferences.Editor saveuser=null;
	
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		sharedpre = getSharedPreferences(
				"Sudesi",MODE_PRIVATE);
					
		setContentView(R.layout.activity_home);
					
		/*	findViewById(R.id.buttonCreate).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							
							saveuser = sharedpre.edit();
							
							saveuser.putInt("iCount",0);
							
							saveuser.putInt("CheckedBox",0);
							
							saveuser.putString("SelectedName","");		
							
							saveuser.putString("scannedId", "");
							
							saveuser.putFloat("Lon",0.0f);
							
							saveuser.putFloat("Lat",0.0f);
							
							saveuser.commit();
						
								
							finish();					
							
							startActivity(new Intent(HomeActivity.this,MainActivity.class));
				}
					});			
		*/
			
			findViewById(R.id.buttonView).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View view) {
						

							saveuser = sharedpre.edit();
							
							saveuser.putInt("LoginiCount",0);
							
							saveuser.putInt("LoginCheckedBox",0);
							
							saveuser.putString("LoginSelectedName","");		
							
							saveuser.putString("LoginscannedId", "");
						
							saveuser.commit();
							
							finish();					
							
							startActivity(new Intent(HomeActivity.this,LoginActivity.class));

						}
					});
			
			findViewById(R.id.buttonEdit).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							
							saveuser = sharedpre.edit();
							
							saveuser.putInt("iCount",0);
							
							saveuser.putInt("CheckedBox",0);
							
							saveuser.putString("SelectedName","");		
							
							saveuser.putString("scannedId", "");
						
							saveuser.commit();
							
							finish();					
							
							startActivity(new Intent(HomeActivity.this,ViewActivity.class));

						}
					});		

		}
	}