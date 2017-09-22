package com.adstringo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.adstringo.video4androidRemoteServiceBridge.R;
import com.videocompresssion.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;



/**
 * http://android-er.blogspot.in/2011/10/simple-exercise-of-video-capture-using.html
 * http://downloads.ziddu.com/downloadfile/16983569/AndroidVideoCapture_111024a.zip.html
 *
 * @author tushar
 */
public class VideoRecordSurfaceViewActivity extends Activity implements SurfaceHolder.Callback {

    private Camera myCamera;
    private MediaRecorder mrec = new MediaRecorder();

    ImageView myButton;
    boolean isRecording = false;
    private SurfaceHolder holder;
    final Handler handler = new Handler();
    public static String recordedVideoPath;
    public ImageView selfiView;
    //public static String inputVideoPath;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_surface);
        myButton = (ImageView) findViewById(R.id.mybutton);
        selfiView = (ImageView) findViewById(R.id.selfiView);

        myButton.setOnClickListener(myButtonOnClickListener);
        initCamera();

		/*inIt();
        setWorkingFolder("/sdcard/Android/data/qwerty/sd23/asdf/");
		copyLicenseAndDemoFilesFromAssetsToSDIfNeeded();*/

        String pathName = Environment.getExternalStorageDirectory() + "/watermarkimage_selfi.jpeg";
        //String pathName = "/sdcard/gif001.gif";
        Resources res = getResources();
        Bitmap bitmap = BitmapFactory.decodeFile(pathName);
        BitmapDrawable bd = new BitmapDrawable(res, bitmap);
        selfiView.setBackgroundDrawable(bd);
    }

    Button.OnClickListener myButtonOnClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (isRecording) {
                // stop recording and release camera
                mrec.stop();  // stop the recording
                releaseMediaRecorder(); // release the MediaRecorder object
                releaseCamera();
                //inputVideoPath = recordedVideoPath;
                handler.removeCallbacks(null);
                //Exit after saved
                Intent intent = new Intent(VideoRecordSurfaceViewActivity.this, MainActivity.class);
                intent.putExtra(Constants.VideoCompressionRecordedPath.path, recordedVideoPath);
                intent.putExtra("camera_intent", "recorded_intent");
                startActivity(intent);
               // VideoRecordSurfaceViewActivity.this.
                        finish();
                /*	setInputFilePath(recordedVideoPath);
                setCommandComplex(1);
				runTranscoing();*/

            } else {
                if (!prepareMediaRecorder()) {
                    Toast.makeText(VideoRecordSurfaceViewActivity.this,
                            "Media controller failed.",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
                myButton.setImageResource(R.drawable.camera_stop_button);
                isRecording = true;
                //auto finish after 1 second
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // stop recording and release camera
                        if (mrec != null) {
                            mrec.stop();  // stop the recording
                            releaseMediaRecorder(); // release the MediaRecorder object
                            releaseCamera();
                            Intent intent = new Intent(VideoRecordSurfaceViewActivity.this, MainActivity.class);
                            intent.putExtra(Constants.VideoCompressionRecordedPath.path, recordedVideoPath);
                            intent.putExtra("camera_intent", "recorded_intent");
                            startActivity(intent);
                            //VideoRecordSurfaceViewActivity.this.
                             finish();

							/*	setInputFilePath(recordedVideoPath);
							setCommandComplex(1);
							runTranscoing();*/
                        }
                    }
                }, Constants.VideoRecordingParams.RECORDING_DURATION);
            }
        }
    };

    private boolean prepareMediaRecorder() {
        mrec = new MediaRecorder();  // Works well
        myCamera.unlock();
        mrec.setCamera(myCamera);

        mrec.setPreviewDisplay(holder.getSurface());
        mrec.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mrec.setAudioSource(MediaRecorder.AudioSource.MIC);
        mrec.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mrec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mrec.setVideoEncoder(MediaRecorder.VideoEncoder.H263);

        mrec.setOrientationHint(Constants.VideoRecordingParams.ORIENTATION);//working
        //setRecorderOrientation(90);
        //mrec.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        mrec.setPreviewDisplay(holder.getSurface());
        recordedVideoPath = getOutputMediaFile().getPath();
        mrec.setOutputFile(recordedVideoPath);

        mrec.setMaxDuration(Constants.VideoRecordingParams.RECORDING_DURATION);
        mrec.setVideoFrameRate(Constants.VideoRecordingParams.FRAME_RATE);
        mrec.setVideoSize(Constants.VideoRecordingParams.VIDEO_WIDTH, Constants.VideoRecordingParams.VIDEO_HEIGHT);
        mrec.setAudioChannels(Constants.VideoRecordingParams.AUDIO_CHANNELS);
        mrec.setVideoEncodingBitRate(Constants.VideoRecordingParams.VIDEOENCODINGBITRATE);//800k
        //mrec.setAudioEncodingBitRate(8000);
        mrec.setMaxFileSize(Constants.VideoRecordingParams.MAXFILESIZE); // Set max file size 5000000  5M
        try {
            mrec.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        mrec.start();
        return true;

    }

    private void releaseMediaRecorder() {
        if (mrec != null) {
            mrec.reset();   // clear recorder configuration
            mrec.release(); // release the recorder object
            mrec = null;
            myCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera() {
        if (myCamera != null) {
            myCamera.stopPreview();
            myCamera.release();       // release the camera for other applications
            myCamera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (myCamera != null) {
            Parameters params = myCamera.getParameters();
            myCamera.setParameters(params);
        } else {
            Toast.makeText(getApplicationContext(), "Camera not available!", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Parameters parameters = myCamera.getParameters();
        myCamera.setParameters(parameters);
        try {
            myCamera.setPreviewDisplay(holder);
            myCamera.startPreview();
        } catch (IOException _le) {
            _le.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();

    }

    @Override
    protected void onPause() {
        super.onPause();
		/*if (mrec != null) {

			mrec.stop();
			mrec.reset();   // You can reuse the object by going back to setAudioSource() step
			mrec.release(); // Now the object cannot be reused
		}*/
        releaseMediaRecorder(); // release the MediaRecorder object
        releaseCamera();

    }

    public void setRecorderOrientation(int orientation) {
        // For back camera only
        if (orientation != -1) {
            Log.d("VideoActivity", "set orientationHint:" + (orientation + 135) % 360 / 90 * 90);
            mrec.setOrientationHint((orientation + 135) % 360 / 90 * 90);
        } else {
            Log.d("VideoActivity", "not set orientationHint to mediaRecorder");
        }
    }

    private void initCamera() {
        //outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.mp4";

        myCamera = Camera.open();
        SurfaceView surface = (SurfaceView) findViewById(R.id.surface_view);
        holder = surface.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    /**
     * Create a File for saving an image or video
     */
/*	public File getOutputMediaFile() {
		// Check that the SDCard is mounted
		// File mediaStorageDir = new
		// File(Environment.getExternalStoragePublicDirectory(
		// Environment.DIRECTORY_PICTURES), "MyCameraVideo");
		//File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/AdStringOVideos/Input/");
		// Create the storage directory(MyCameraVideo) if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Toast.makeText(VideoRecordSurfaceViewActivity.this,	"Failed to create directory AdStringOVideos/Input.",Toast.LENGTH_LONG).show();
				return null;
			}
		}
		// Create a media file name
		// For unique file name appending current timeStamp with file name
		java.util.Date date = new java.util.Date();
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator+ "VID_" + timeStamp + ".mp4");
		return mediaFile;
	}*/
    public File getOutputMediaFile() {

        // Check that the SDCard is mounted
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraVideo");


        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {

            if (!mediaStorageDir.mkdirs()) {


                Toast.makeText(VideoRecordSurfaceViewActivity.this, "Failed to create directory MyCameraVideo.",
                        Toast.LENGTH_LONG).show();

                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }


        // Create a media file name

        // For unique file name appending current timeStamp with file name
        java.util.Date date = new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.getTime());

        File mediaFile;

        //if(type == MEDIA_TYPE_VIDEO) {

        // For unique video file name appending current timeStamp with file name
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "VID_" + timeStamp + ".mp4");

		/* } else {
            return null;
        }*/

        return mediaFile;
    }
}