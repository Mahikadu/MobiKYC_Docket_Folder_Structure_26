package com.adstringo.android_client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.adstringo.video4androidRemoteServiceBridge.IFRemoteServiceBridge;
import com.adstringo.video4androidRemoteServiceBridge.R;
import com.videocompresssion.utils.Constants;
import com.videocompresssion.utils.Utils;

/**
 * This class use the Template Method design pattern,
 * the invokeService() virtual method is implemented at children level and called in this class template method onServiceConnected()
 *
 * @author ehasson
 */
public class BaseWizard extends Base {

    protected IFRemoteServiceBridge remoteServiceBridge;
    protected RemoteServiceConnection remoteServiceConn = null;//monitor remote service connection

    protected boolean started = false;
    protected boolean invokeFlag = false;
    protected ProgressDialog progressDialog;

    protected Button convertButton;
    protected Button playButton;
    protected Button shareButton;
    protected Button selectButton;
    protected int PICK_REQUEST_CODE = 0;

    protected static String workingFolder;
    protected String outputFile;
    protected String outputFilePath;
    protected String inputFilePath;
    protected Prefs _prefs = null;

    protected String commandStr;


    private String progressDialogMessage = "Please Wait.. \n\nDepending on your video size, it will take a time.";
    private String progressDialogTitle = "AdStringO Compression.";

    private int notificationIcon = android.R.drawable.ic_menu_info_details;
    private String notificationTitle = "AdStringO Compression";
    private String notificationMessage = "Process is running...";
    private String notificationfinishedMessageTitle = "Process Completed";
    private String notificationStoppedMessage = "Unfortunately process Failed";
    private String notificationfinishedMessageDesc = "Process finished successfully.";
    private String resolution;

    private Utils utils = new Utils();
    private ArrayList<String> ffmpegCommandList = new ArrayList<String>();

    //str_resolution = "470x320";//
    public void setNotificationfinishedMessageDesc(
            String notificationfinishedMessageDesc) {
        this.notificationfinishedMessageDesc = notificationfinishedMessageDesc;
    }


    String[] commandComplex;

    public void setCommandComplex(int type) {
        String logo = Environment.getExternalStorageDirectory().getAbsolutePath() + "/watermarkimage_selfi.jpeg";
        /*this.commandComplex = new String[]{utils.decodeProjectPath("ZmZtcGVn"), "-y", "-i",
				inputFilePath,"-i", logo,"-strict", "experimental","-vf", "transpose=1",
				"-s", Constants.InputFileSizeParams.RESOLUTION,"-r",Constants.InputFileSizeParams.FPS, "-aspect" ,Constants.InputFileSizeParams.ASPECT_RATIO,
				"-vcodec", Constants.InputFileSizeParams.CODEC, "-ac", "2", "-ar", "22050",
				"-b", Constants.InputFileSizeParams.BITRATE,
				"-vb", "20M", "-filter_complex", "overlay",
				outputFilePath };*/
        switch (type) {
            case 0:

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(inputFilePath);
                int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                retriever.release();

                this.commandComplex = new String[]{utils.decodeProjectPath("ZmZtcGVn"), "-y", "-i",
                        inputFilePath, "-i", logo, "-strict", "experimental",
                        "-s", Constants.InputFileSizeParams.RESOLUTION, "-r", Constants.InputFileSizeParams.FPS,
                        "-vcodec", Constants.InputFileSizeParams.CODEC, "-ac", "2", "-ar", "22050",
                        "-b", Constants.InputFileSizeParams.BITRATE,
                        "-vb", "20M", "-filter_complex", "overlay",
                        outputFilePath};
			
			/*this.commandComplex = new String[]{utils.decodeProjectPath("ZmZtcGVn"), "-y", "-i",
					inputFilePath, "-strict","experimental",
					"-s", Constants.InputFileSizeParams.RESOLUTION,"-r",Constants.InputFileSizeParams.FPS,
					"-vcodec", Constants.InputFileSizeParams.CODEC, "-ac", "2", "-ar", "22050",
					"-b", Constants.InputFileSizeParams.BITRATE,
					"-vb", "20M",
					outputFilePath };*/

                break;
            case 1:

                MediaPlayer mp = MediaPlayer.create(this, Uri.parse(inputFilePath));
                int videoDurarion = mp.getDuration();
                mp.release();
			/*convert millis to appropriate time*/
                long videoInMinutes = TimeUnit.MILLISECONDS.toMinutes(videoDurarion);
                //long videoInMinutes = 1;
                int startDurationMinute = 00;
                int startDurationSeconds = 00;

                int endDurationMinute = 00;
                int endDurationSeconds = 30;
                //String ffmepgCommand[] = ;
                ffmpegCommandList.add(utils.decodeProjectPath("ZmZtcGVn"));
                ffmpegCommandList.add("-v");
                ffmpegCommandList.add("quiet");
                ffmpegCommandList.add("-y");
                ffmpegCommandList.add("-i");
                ffmpegCommandList.add(inputFilePath);
			/*ffmpegCommandList.add("-i");
			ffmpegCommandList.add(logo);
			ffmpegCommandList.add("-filter_complex");
			ffmpegCommandList.add("overlay");*/


                for (int i = 0; i < videoInMinutes * 2; i++) {
                    ffmpegCommandList.add("-vcodec");
                    ffmpegCommandList.add("copy");
                    ffmpegCommandList.add("-acodec");
                    ffmpegCommandList.add("copy");
                    ffmpegCommandList.add("-ss");

                    if (startDurationMinute == 00 && startDurationSeconds == 00) {
                        ffmpegCommandList.add("00:" + String.format("%02d", startDurationMinute) + ":" + String.format("%02d", startDurationSeconds));//start time
                    } else {
                        if (startDurationSeconds == 31) {
                            ffmpegCommandList.add("00:" + String.format("%02d", startDurationMinute) + ":" + String.format("%02d", startDurationSeconds));//start time
                            endDurationSeconds = 59;
                        } else if (startDurationSeconds == 00) {
                            ffmpegCommandList.add("00:" + String.format("%02d", startDurationMinute) + ":" + String.format("%02d", startDurationSeconds));//start time
                            endDurationSeconds = 30;
                        }
                    }

                    ffmpegCommandList.add("-t");

                    if (endDurationMinute == 00 && endDurationSeconds == 30) {
                        ffmpegCommandList.add("00:" + String.format("%02d", endDurationMinute) + ":" + String.format("%02d", endDurationSeconds));//start time
                        startDurationSeconds = endDurationSeconds + 1;
                    } else {
                        if (endDurationSeconds == 30) {
                            ffmpegCommandList.add("00:" + String.format("%02d", endDurationMinute) + ":" + endDurationSeconds);//start time
                            startDurationSeconds = 31;
                        } else if (endDurationSeconds == 59) {
                            ffmpegCommandList.add("00:" + String.format("%02d", endDurationMinute) + ":" + endDurationSeconds);//start time
                            startDurationMinute = startDurationMinute + 1;
                            endDurationMinute = startDurationMinute;
                            startDurationSeconds = 00;
                        }
                    }
                    ffmpegCommandList.add("-sn");
                    ffmpegCommandList.add(Environment.getExternalStorageDirectory() + "/AdStringOVideos/Trim/" + i + ".mp4");
                }
			/*this.commandComplex = new String[]{"ffmpeg", "-v", "quiet", "-y", "-i", inputFilePath, 
					"-vcodec","copy", "-acodec" ,"copy", "-ss", "00:00:00", "-t", "00:00:30", "-sn", Environment.getExternalStorageDirectory()+"/AdStringOVideos/Output/"+"test1.mp4", 
					"-vcodec","copy" ,"-acodec", "copy", "-ss", "00:00:31", "-t", "00:00:59", "-sn", Environment.getExternalStorageDirectory()+"/AdStringOVideos/Output/"+"test2.mp4"
			};*/

                commandComplex = new String[ffmpegCommandList.size()];
                commandComplex = ffmpegCommandList.toArray(commandComplex);

                //this.commandComplex = new String[]{ffmpegCommandList.toArray(commandComplex)};
                break;
            default:
                break;
        }

		/*echo "Two commands" 
time ffmpeg -v quiet -y -i input.ts -vcodec copy -acodec copy -ss 00:00:00 -t 00:30:00 -sn test1.mkv
time ffmpeg -v quiet -y -i input.ts -vcodec copy -acodec copy -ss 00:30:00 -t 01:00:00 -sn test2.mkv
echo "One command" 
time ffmpeg -v quiet -y -i input.ts -vcodec copy -acodec copy -ss 00:00:00 -t 00:30:00 \
  -sn test3.mkv -vcodec copy -acodec copy -ss 00:30:00 -t 01:00:00 -sn test4.mkv
		 * */
    }

    public String getNotificationfinishedMessageTitle() {
        return notificationfinishedMessageTitle;
    }

    public void setNotificationfinishedMessageTitle(String notificationfinishedMessage) {
        this.notificationfinishedMessageTitle = notificationfinishedMessage;
    }

    public String getNotificationStoppedMessage() {
        return notificationStoppedMessage;
    }

    public void setNotificationStoppedMessage(String notificationStoppedMessage) {
        this.notificationStoppedMessage = notificationStoppedMessage;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public void setNotificationIcon(int notificationIcon) {
        this.notificationIcon = notificationIcon;
    }

    public String getProgressDialogMessage() {
        return progressDialogMessage;
    }

    public void setProgressDialogMessage(String progressDialogMessage) {
        this.progressDialogMessage = progressDialogMessage;
    }

    public String getProgressDialogTitle() {
        return progressDialogTitle;
    }

    public void setProgressDialogTitle(String progressDialogTitle) {
        this.progressDialogTitle = progressDialogTitle;
    }

    public String getCommand() {
        return commandStr;
    }

    public void setCommand(String commandStr) {
        Log.i(Prefs.TAG, "Command is set");
        this.commandStr = commandStr;
    }


    public static String getWorkingFolder() {
        return workingFolder;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    private void setRemoteNotificaitonIcon() {
        if (notificationIcon != -1)
            Prefs.setRemoteNotificationIconId(getApplicationContext(), notificationIcon);
    }

    public String[] getCommandComplex() {
        return commandComplex;

    }

    public String getVideoResolution() {
        return resolution;
    }

    public void setVideoResolution(String resolution) {
        this.resolution = resolution;
    }

    private void setRemoteNotificationInfo() {
        try {
            if (remoteServiceBridge != null) {
                Log.i(Prefs.TAG, "setting remote notification info");
                if (notificationTitle != null)
                    remoteServiceBridge.setNotificationTitle(notificationTitle);
                if (notificationMessage != null)
                    remoteServiceBridge.setNotificationMessage(notificationMessage);
            } else {
                Log.w(Prefs.TAG, "remoteService is null, can't set remote notification info");
            }
        } catch (RemoteException e1) {
            Log.w(Prefs.TAG, e1.getMessage(), e1);
        }
    }

    // called from onServiceConnected
    public void invokeService() {
        Log.i(Prefs.TAG, "invokeService called");

        // needed for demo client for grasfull fail
		/*int rc = isLicenseValid(getApplicationContext());

		if (rc < 0) {
			String message = "";
			if (rc == -1) 
				message = "Trial expired, please contact tushar@adstringo.in";
			else
				message = "License Validation failed, please contact tushar@adstringo.in";


			new AlertDialog.Builder(this)
			.setTitle("License Validation failed")
			.setMessage(message)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { 

				}
			})
			.show();

			return;
		}*/

        setRemoteNotificationInfo();

        // this call with handle license gracefully.
        // If it will be removed, the fail will be in the native code, causing the progress dialog to start.
        //if (! isLicenseValid()) return;

        if (invokeFlag) {
            if (remoteServiceConn == null) {
                Toast.makeText(this, "Cannot invoke - service not bound", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    String command = getCommand();

                    if (remoteServiceBridge != null) {
                        if (command != null)
                            remoteServiceBridge.setSimpleCommand(command);
                        else {
                            remoteServiceBridge.setComplexCommand(commandComplex);
                        }
                        remoteServiceBridge.setWorkingFolder(Prefs.getWorkFolder());
                        runWithCommand(command);

                    } else {
                        Log.w(Prefs.TAG, "Invoke failed, remoteService is null.");
                    }

                } catch (android.os.DeadObjectException e) {
                    Log.d(Prefs.TAG, "ignoring DeadObjectException (Project process exit)");
                } catch (RemoteException re) {
                    Log.e(Prefs.TAG, re.getMessage(), re);
                }
            }
            invokeFlag = false;
        } else {
            Log.d(Prefs.TAG, "Not invoking");

        }
    }

    protected boolean invokeFileInfoServiceFlag = false;
    private TranscodeBackground _transcodeBackground;

    public void getInputFileAndOutputFileFromCommand(String workingFolder, String inputFileName) {

    }

    public IFRemoteServiceBridge getRemoteService() {
        return remoteServiceBridge;
    }

    public void setWorkingFolder(String workingFolder) {
        Prefs.setWorkFolder(workingFolder);
    }


	/*	public int isLicenseValid(Context ctx) {
		LicenseCheckJNI lm = new LicenseCheckJNI();
		int rc = lm.licenseCheck(Prefs.getWorkingFolderForNative(), ctx);
		if (rc < 0) {
			if (rc == -1)
				Toast.makeText(this, "Trail Expired. contact support.", Toast.LENGTH_SHORT).show();
			else if (rc == -2) 
				Toast.makeText(this, "License invalid contact support", Toast.LENGTH_SHORT).show();
			else 
				Toast.makeText(this, "License check failed. contact support." + rc, Toast.LENGTH_SHORT).show();
		}
		return rc;
	}*/


    public void runTranscoing() {
        try {
			/*File inputFilePathFile = new File(inputFilePath);
			Log.v("++++++++++++++++++++++InputFilePath+++++++++++++++++", inputFilePathFile.getPath());
			int inputFileDurationMiliSeconds = MediaPlayer.create(this, Uri.fromFile(inputFilePathFile)).getDuration();
			int secs = (int) (inputFileDurationMiliSeconds / 1000.0);
			if (secs <= Constants.InputFileSizeParams.FILE_DURATION_SEC) {
				if (inputFilePathFile.length() >= 0 && inputFilePathFile.length() <= Constants.InputFileSizeParams.FILE_SIZE_BYTES) {*/
            setRemoteNotificaitonIcon();
            releaseService();
            stopService();
            startService();
            invokeFlag = true;
            bindService();
			/*	}else {
					AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
					builder1.setTitle("Size error");
					builder1.setMessage("Video size should be more than 0 MB and less than 150 MB");
					builder1.setCancelable(true);
					builder1.setNeutralButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
					AlertDialog alert11 = builder1.create();
					alert11.show();
				}
			}
			else {
				AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
				builder1.setTitle("Length error");
				builder1.setMessage("Video duration should not exceeds than 60 seconds.");
				builder1.setCancelable(true);
				builder1.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

				AlertDialog alert11 = builder1.create();
				alert11.show();
			}*/
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startAct(Class act) {
        Intent intent = new Intent(this, act);
        Log.d(Prefs.TAG, "Starting act:" + act);
        this.startActivity(intent);
    }

    public void runWithCommand(String command) {
        Prefs p = new Prefs();
        p.setContext(getApplicationContext());

        deleteLogs();
        FileUtils.writeToLocalLog("command: " + command);
        FileUtils.writeToLocalLog("Input file size: " + Prefs.inputFileSize);
        Log.d(Prefs.TAG, "Client invokeService()");
        Random rand = new Random();
        int randInt = rand.nextInt(1000);
        _transcodeBackground = new TranscodeBackground(this, remoteServiceBridge, randInt);
        _transcodeBackground.setProgressDialogTitle(progressDialogTitle);
        _transcodeBackground.setProgressDialogMessage(progressDialogMessage);
        _transcodeBackground.setNotificationIcon(notificationIcon);
        _transcodeBackground.setNotificationfinishedMessageTitle(notificationfinishedMessageTitle);
        _transcodeBackground.setNotificationfinishedMessageDesc(notificationfinishedMessageDesc);
        _transcodeBackground.setNotificationStoppedMessage(notificationStoppedMessage);
        _transcodeBackground.execute();
    }


    public void copyLicenseAndDemoFilesFromAssetsToSDIfNeeded() {
        _prefs = new Prefs();
        _prefs.setContext(this);
        File destVid = null;
        File destLic = null;
        //String workingFolderPath = Environment.getExternalStorageDirectory() + Prefs.WORKING_DIRECTORY;
        String workingFolderPath = Prefs.getWorkFolder();
        Log.i(Prefs.TAG, "workingFolderPath: " + workingFolderPath);
        try {
            if (!FileUtils.checkIfFolderExists(workingFolderPath)) {

                boolean isFolderCreated = FileUtils.createFolder(workingFolderPath);
                Log.i(Prefs.TAG, workingFolderPath + " created? " + isFolderCreated);
                if (isFolderCreated) {
                    destVid = new File(workingFolderPath + "in.mp4");
                    Log.i(Prefs.TAG, "Adding vid file at " + destVid.getAbsolutePath());
                    InputStream is = getApplication().getAssets().open("in.mp4");
                    BufferedOutputStream o = null;
                    try {
                        byte[] buff = new byte[10000];
                        int read = -1;
                        o = new BufferedOutputStream(new FileOutputStream(destVid), 10000);
                        while ((read = is.read(buff)) > -1) {
                            o.write(buff, 0, read);
                        }
                        Log.i(Prefs.TAG, "Copy " + destVid.getAbsolutePath() + " from assets to SDCARD finished succesfully");
                    } catch (Exception e) {
                        Log.w(Prefs.TAG, "Failed copying: " + destVid.getAbsolutePath());
                    } finally {
                        is.close();
                        if (o != null) o.close();

                    }


                    //Toast.makeText(this, "Demo video created at: " + workingFolderPath , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Working folder was not created, You need SDCARD to use this app!", Toast.LENGTH_LONG).show();
                }

            } else {
                Log.d(Prefs.TAG, "Working directory exists, not coping assests (license file and demo videos)");
                //				Toast.makeText(this, "Sample videos located at: " + workingFolderPath , Toast.LENGTH_SHORT).show();
            }

            // Creation of output directory is removed, since when using custom working folder
            // it created the _prefs.getOutFolder() which is by default /sdcard/videokit
			/*
			if (!FileUtils.checkIfFolderExists(_prefs.getOutFolder())) {
				boolean isFolderCreated = FileUtils.createFolder(_prefs.getOutFolder());
				Log.i(Prefs.TAG, _prefs.getOutFolder() + " created? " + isFolderCreated);
			}
			else {
				Log.d(Prefs.TAG, "output directory exists.");

			}
			 */
        } catch (FileNotFoundException e) {
            Log.e(Prefs.TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(Prefs.TAG, e.getMessage());
        }

        //========= License Copy ===================================================================
        InputStream is = null;
        BufferedOutputStream o = null;
        boolean copyLic = true;
        try {
            is = getApplication().getAssets().open("license.lic");
        } catch (Exception e) {
            Log.i(Prefs.TAG, "License file does not exist in the assets.");
            copyLic = false;
        }

        if (copyLic) {
            destLic = new File(workingFolderPath + "license.lic");
            Log.i(Prefs.TAG, "Adding lic file at " + destLic.getAbsolutePath());

            o = null;
            try {
                byte[] buff = new byte[10000];
                int read = -1;
                o = new BufferedOutputStream(new FileOutputStream(destLic), 10000);
                while ((read = is.read(buff)) > -1) {
                    o.write(buff, 0, read);
                }
                Log.i(Prefs.TAG, "Copy " + destLic.getAbsolutePath() + " from assets to SDCARD finished succesfully");
            } catch (Exception e) {
                Log.e(Prefs.TAG, "Error when coping license file from assets to working folder: " + e.getMessage());
            } finally {
                try {
                    is.close();
                    if (o != null) o.close();
                } catch (IOException e) {
                    Log.w(Prefs.TAG, "Error when closing license file io: " + e.getMessage());
                }

            }

        } else {
            Log.i(Prefs.TAG, "Not coping license");
        }

        //========License Copy ======================================================================

    }


    protected void startService() {
        if (started) {
            Toast.makeText(this, "Service already started", Toast.LENGTH_SHORT).show();
        } else {

            Intent i = new Intent(Constants.IntentFilterService.INTENT_FILTER);
            PackageManager packageManager = getPackageManager();
            List<ResolveInfo> services = packageManager.queryIntentServices(i, 0);
            Log.i(Prefs.TAG, "!!!!!!!!!!!!!!!!!!services.size(): " + services.size());


            if (services.size() > 0) {
                ResolveInfo service = services.get(0);
                i.setClassName(service.serviceInfo.packageName, service.serviceInfo.name);
                i.setAction(Constants.IntentFilterService.INTENT_FILTER);

                if (!invokeFileInfoServiceFlag) {
                    i.addCategory("Base");
                    Log.i(Prefs.TAG, "putting Base categoty");
                } else {
                    i.addCategory("Info");
                    Log.i(Prefs.TAG, "putting Info categoty");
                }

                ComponentName cn = startService(i);
                Log.d(Prefs.TAG, "started: " + cn.getClassName());
            }

            started = true;
            Log.d(Prefs.TAG, "Client startService()");
        }

    }

    // this is not working, not stopping the remote service.
    protected void stopService() {
        Log.d(Prefs.TAG, "Client stopService()");
        //Intent i = new Intent();
        Intent i = new Intent(Constants.IntentFilterService.INTENT_FILTER);
        stopService(i);
		/*Intent serviceIntent = new Intent(context,RemoteServiceBridgeService.class);
		context.startService(serviceIntent);*/
        started = false;
    }

    protected void bindService() {
        Log.d(Prefs.TAG, " bindService() called");
        if (remoteServiceConn == null) {
            remoteServiceConn = new RemoteServiceConnection();
            //Intent i = new Intent();
            Intent i = new Intent(Constants.IntentFilterService.INTENT_FILTER);
            bindService(i, remoteServiceConn, Context.BIND_AUTO_CREATE);
            Log.d(Prefs.TAG, "Client bindService()");
        } else {
            Log.d(Prefs.TAG, " Client Cannot bind - service already bound");
            //Toast.makeText(this, "Client Cannot bind - service already bound", Toast.LENGTH_SHORT).show();
        }
    }

    protected void releaseService() {
        if (remoteServiceConn != null) {
            unbindService(remoteServiceConn);
            remoteServiceConn = null;
            Log.d(Prefs.TAG, "releaseService()");
        } else {
            //Toast.makeText(this, "Client Cannot unbind - service not bound", Toast.LENGTH_SHORT).show();
            Log.d(Prefs.TAG, "Client Cannot unbind - service not bound");
        }
    }


    /**
     * android.content.ServiceConnection
     * <p>
     * <p>
     * Interface for monitoring the state of an application service. See android.app.Service and
     * Context.bindService() for more information.
     * Like many callbacks from the system, the methods on this class are called from the main thread of your
     * process.
     *
     * @author tushar
     */
    public class RemoteServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName className, IBinder boundService) {
            Log.d(Prefs.TAG, "Client onServiceConnected()");
            remoteServiceBridge = IFRemoteServiceBridge.Stub.asInterface((IBinder) boundService);

            if (invokeFileInfoServiceFlag)
                invokeFileInfoService(inputFilePath);
            else
                invokeService();

        }

        public void onServiceDisconnected(ComponentName className) {
            remoteServiceBridge = null;
            Log.d(Prefs.TAG, "onServiceDisconnected");
        }
    }

    ;


    public void handleServiceFinished() {
        Log.i(Prefs.TAG, "Project finished.");
        Toast.makeText(this, "Process Finished.", Toast.LENGTH_LONG).show();

        //remove the sticky notification
        // fix 4.4.2 bug, should not effect other versions.
        releaseService();
        stopService();

    }

    protected void handleInfoServiceFinished() {
        Log.i(Prefs.TAG, "Project finished (info).");
        removeDialog(FILE_INFO_DIALOG);
        showDialog(FILE_INFO_DIALOG);
        invokeFileInfoServiceFlag = false;


    }


    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void invokeFileInfoService(String inputFilePath) {
        Log.i(Prefs.TAG, "invokeFileInfoService called");

        if (invokeFlag) {

            if (remoteServiceConn == null) {
                Toast.makeText(this, "Cannot invoke - service not bound", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    //FileUtils.deleteFile(workingFolder + outputFile);
                    String command = utils.decodeProjectPath("ZmZtcGVn") + "-i " + inputFilePath;
                    if (remoteServiceBridge != null) {
                        deleteLogs();
                        FileUtils.writeToLocalLog("command: " + command);
                        Log.i(Prefs.TAG, "command: " + command);
                        remoteServiceBridge.setSimpleCommand(command);
                        Log.d(Prefs.TAG, "Client invokeService()");
                        remoteServiceBridge.runTranscoding();
                    } else {
                        Log.w(Prefs.TAG, "Invoke failed, remoteService is null.");
                    }

                } catch (android.os.DeadObjectException e) {

                    Log.d(Prefs.TAG, "ignoring DeadObjectException (Project process exit)");

                } catch (RemoteException re) {
                    Log.e(Prefs.TAG, re.getMessage(), re);
                }
            }
            handleInfoServiceFinished();
            invokeFlag = false;
        } else {
            Log.d(Prefs.TAG, "Not invoking");

        }
    }

    public void deleteLogs() {
        FileUtils.deleteFile(Prefs.getVkLogFilePath());
        FileUtils.deleteFile(Prefs.get4androidLogFilePath());
        FileUtils.deleteFile(Prefs.getVideoKitLogFilePath());
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
        this.outputFile = FileUtils.getFileNameFromFilePath(outputFilePath);
    }

    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }


    public void stopTranscoding() {
        Log.d(Prefs.TAG, "stopTranscoding called");
        if (_transcodeBackground != null) {
            _transcodeBackground.forceCancel();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(Prefs.TAG, "BaseWizard onDestroy");
        stopTranscoding();
    }

    public void inIt() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = true;
            options.outHeight = 150;
            options.outWidth = 150;
            Bitmap myBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.watermarkimage);
            try {
                File file = new File(Environment.getExternalStorageDirectory() + java.io.File.separator + "watermarkimage.png");
                if (!file.exists()) {
                    file.delete();
                    file.createNewFile();
                }
                Bitmap scaledBitmap = myBitmap.createScaledBitmap(myBitmap, 100, 32, false);
                if (myBitmap != null) {
                    myBitmap.recycle();
                    myBitmap = null;
                }
                FileOutputStream out = new FileOutputStream(file);
                Bitmap scalesBitmap = Bitmap.createScaledBitmap(myBitmap, 150, 150, false);
                scalesBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                if (myBitmap != null) {
                    myBitmap.recycle();
                    myBitmap = null;
                }
                if (scalesBitmap != null) {
                    scalesBitmap.recycle();
                    scalesBitmap = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            AssetManager assetManager = getAssets();
            String[] files = null;
            files = assetManager.list("");

            AssetManager am = BaseWizard.this.getAssets();
            AssetFileDescriptor afd = null;
            afd = am.openFd("watermarkimage.jpeg");
            // Create new file to copy into.
            File file = new File(Environment.getExternalStorageDirectory() + java.io.File.separator + "watermarkimage.jpeg");
            if (!file.exists()) {
                file.createNewFile();
                copyFdToFile(afd.getFileDescriptor(), file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyFdToFile(FileDescriptor src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }
    public interface IDateCallback {
        void compressedFilePath(String compressedFilePath);
    }

    //private IDateCallback callerActivity;

    //public void BaseWizard(Activity activity) {
        //callerActivity = (IDateCallback) activity;
    //}

}
