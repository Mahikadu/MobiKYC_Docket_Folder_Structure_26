package com.adstringo;



import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adstringo.android_client.BaseWizard;
import com.adstringo.android_client.FileUtils;
import com.adstringo.android_client.Prefs;
import com.adstringo.video4androidRemoteServiceBridge.R;


public class DemoClient extends BaseWizard {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_client2);

		String lastCommandText = Prefs.getLastCommand(getApplicationContext());
		EditText commandText =  (EditText)findViewById(R.id.CommandText);
		if (lastCommandText != null) {
			Log.d(Prefs.TAG, "Setting command text as the last command: " + lastCommandText);
			commandText.setText(lastCommandText);
		}
		else {
			Log.d(Prefs.TAG, "No last command using default");
		}


		// if you want to change the default work location (/sdcard/videokit/) use the uncomment the below method.
		// It must be defined before calling the copyLicenseAndDemoFilesFromAssetsToSDIfNeeded method,
		// in order for this method to copy the assets to the correct location.
		//setWorkingFolder("/sdcard/videokit3/");

		// this will copy the license file and the demo video file 
		// to the videokit work folder location 
		copyLicenseAndDemoFilesFromAssetsToSDIfNeeded();

		if (Prefs.transcodingIsRunning) {
			Log.i(Prefs.TAG, "Currently transcoding is running, not running.");
			Toast.makeText(this, "Process is running in the background", Toast.LENGTH_LONG).show();
			finish();
			return;
		}


		Button invoke =  (Button)findViewById(R.id.invokeButton);
		invoke.setOnClickListener(new OnClickListener() {
			public void onClick(View v){

				EditText commandText = (EditText)findViewById(R.id.CommandText);
				String commandStr = commandText.getText().toString();

				setCommand(commandStr);
				String outputPath = FileUtils.getOutputFileFromCommandStr(commandStr);
				setOutputFilePath(outputPath);

				//setCommandComplex(complexCommand);

				///optional////
				setProgressDialogTitle("Transcoding...");
				setProgressDialogMessage("Depends on your video size, transcoding can take a few minutes");
				setNotificationIcon(android.R.drawable.alert_light_frame);
				setNotificationMessage("Demo is running...");
				setNotificationTitle("DemoClient");
				setNotificationfinishedMessageTitle("Transcoding finished");
				setNotificationfinishedMessageDesc("Click to play");
				setNotificationStoppedMessage("Transcoding stopped");
				///////////////

				runTranscoing();
				///////////////////////////////////////////////////////////////////////////////




			}
		});

		Button showLog =  (Button)findViewById(R.id.showLastRunLogButton);
		showLog.setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				startAct(com.adstringo.android_client.ShowFileAct.class);				
			}
		});


	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(Prefs.TAG, "DemoClient onDestroy()");
	}

	/*
	@Override
	public void onBackPressed() {
		Log.d(Prefs.TAG, "DemoClient onBackPressed");
		super.onDestroy();

	}
	 */





}
