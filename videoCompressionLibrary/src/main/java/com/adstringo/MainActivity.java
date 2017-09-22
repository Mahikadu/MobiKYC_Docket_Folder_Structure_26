package com.adstringo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adstringo.android_client.BaseWizard;
import com.adstringo.video4androidRemoteServiceBridge.R;
import com.sudesi.adstringocompression.ImageCompresionApplication;
import com.sudesi.adstringocompression.SimpleFTP;
import com.videocompresssion.utils.ConnectionDetector;
import com.videocompresssion.utils.Constants;
import com.videocompresssion.utils.ViewUtils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends BaseWizard implements BaseWizard.IDateCallback {

    // private EditText Dob;
    private Calendar myCalendar;
    private Button buttonRecording, submitButton, buttonCompress, imageCapture;
    // private DeleteFileBroadcastReceiver deleteFilesAlarm;
    //private DeleteFileBroadcastReceiver deleteFilesAlarm;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private Uri fileUri;
    public static final int MEDIA_TYPE_VIDEO = 2;
    String inputpath = "", trimvidpath = "", OriginalSize, CompressedSize, original_str_resolution;
    static String outvidpath = "";
    ConnectionDetector connect;
    private String camera_intent;

    private ViewUtils viewUtils;
    EditText edt_appno;

    public static String USER_NAME_FTP_ROOT_FOLDER = "ABC";
    private static String FTP_IP = "118.139.173.227";
    private static String PORT = "22";
    private static String USERNAME = "kycupload";
    private static String PASSWORD = "Kyc@1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.syc_menu);
        viewUtils = new ViewUtils(MainActivity.this);

        // camera_intent = getIntent().getStringExtra("camera_intent");
        try {
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if (extras == null) {
                    camera_intent = null;
                    //1st time
                } else {
                    camera_intent = extras.getString("camera_intent");
                    if (camera_intent.toString().equalsIgnoreCase("camera_intent")) {
                        //startCamera();
                        //next time
                        Intent surfaceIntent = new
                                Intent(MainActivity.this, VideoRecordSurfaceViewActivity.class
                        );
                        surfaceIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(surfaceIntent);
                        //MainActivity.this.
                                finish();
                    }
                    if (camera_intent.toString().equalsIgnoreCase("recorded_intent")) {
                        //startCamera();
                        //next time
                        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/AdStringOVideos/Output/");

                        if (!mediaStorageDir.exists()) {
                            mediaStorageDir.mkdirs();
                        }
                        java.util.Date date = new java.util.Date();
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());
                        outvidpath = Environment.getExternalStorageDirectory() + "/AdStringOVideos/Output/" + "OUT_VID"
                                + timeStamp + ".mp4";

                        setInputFilePath(VideoRecordSurfaceViewActivity.recordedVideoPath);
                        setOutputFilePath(outvidpath);
                        setCommandComplex(0);
                        runTranscoing();
                    }
                }

            } else {
                camera_intent = (String) savedInstanceState.getSerializable("camera_intent");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/AdStringOVideos/Trim");

        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        //Dob = (EditText) findViewById(R.id.et_dateOfBirth);
        buttonRecording = (Button) findViewById(R.id.buttonRecording);
        submitButton = (Button) findViewById(R.id.submitButton);
        buttonCompress = (Button) findViewById(R.id.buttonCompress);
        imageCapture = (Button) findViewById(R.id.imageCapture);
        edt_appno = (EditText) findViewById(R.id.edt_appno);

        // Dob.setFocusable(false);
        myCalendar = Calendar.getInstance();
        myCalendar.set(1980, Calendar.JANUARY, 1);

        //deleteFilesAlarm = new DeleteFileBroadcastReceiver();
        //deleteFilesAlarm.SetAlarm(MainActivity.this);
        //inIt();
        setWorkingFolder("/sdcard/Android/data/qwerty/sd23/asdf/");
        copyLicenseAndDemoFilesFromAssetsToSDIfNeeded();

        connect = new ConnectionDetector(MainActivity.this);


        buttonRecording.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {
                    if (getUserDetails().trim().equalsIgnoreCase("Active")) {
                        // do nothing
                        Intent surfaceIntent = new
                                Intent(MainActivity.this, VideoRecordSurfaceViewActivity.class
                        );
                        surfaceIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(surfaceIntent);
                        MainActivity.this.finish();

                    } else {
                        showDialog("AdstringO licence invalid!", "Please activate your device with server.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //
               /* File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/AdStringOVideos/Output/");

                if (!mediaStorageDir.exists()) {
                    mediaStorageDir.mkdirs();
                }
                java.util.Date date = new java.util.Date();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());
                outvidpath = Environment.getExternalStorageDirectory() + "/AdStringOVideos/Output/" + "OUT_VID"
                        + timeStamp + ".mp4";

                setInputFilePath(VideoRecordSurfaceViewActivity.recordedVideoPath);
                setOutputFilePath(outvidpath);*/
                //startCamera();

            }
        });

        buttonCompress.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/AdStringOVideos/Output/");

                if (!mediaStorageDir.exists()) {
                    mediaStorageDir.mkdirs();
                }
                java.util.Date date = new java.util.Date();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());
                outvidpath = Environment.getExternalStorageDirectory() + "/AdStringOVideos/Output/" + "OUT_VID"
                        + timeStamp + ".mp4";

                setInputFilePath(VideoRecordSurfaceViewActivity.recordedVideoPath);
                setOutputFilePath(outvidpath);
                setCommandComplex(0);
                runTranscoing();
            }
        });

        submitButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {

				/*
                 * trim = new trimVideo(); trim.execute();
				 */

				/*
                 * inIt(); setInputFilePath(VideoRecordSurfaceViewActivity.
				 * inputVideoPath); setCommandComplex(1); runTranscoing();
				 */
                /*
                 * Cursor cursor =
				 * MediaStore.Video.query(getContentResolver(),Uri.parse(
				 * videoSurface.getOutputMediaFile().toString()), new String[] {
				 * MediaStore.Video.VideoColumns.DURATION });
				 * System.out.println(">>>>>>>>>>"+cursor.getCount());
				 * cursor.moveToFirst();
				 *
				 * String duration =
				 * cursor.getString(cursor.getColumnIndex("duration"));
				 */

                if (connect.isConnectingToInternet()) {

                    if (!edt_appno.getText().toString().equals("")) {
                        //if (!edt_appno.getText().toString().equalsIgnoreCase("")) {
                        if (edt_appno.getText().toString().contains(" ")) {
                            viewUtils.showDialog("Alert",
                                    "Docket number should not contain spaces.");
                        } else {
                            if (!outvidpath.equalsIgnoreCase(""))
                                new SaveSubmit().execute();
                                // FTP upload
                            else {
                                viewUtils.showDialog("Alert",
                                        "Please record video to upload.");
                            }

                        }
                    } /*else if (spin_docType.getSelectedItem().toString().equalsIgnoreCase("--Select document category--")) {
                        Toast.makeText(ActivityPreview.this,
                                "Please select document category below.", Toast.LENGTH_LONG)
                                .show();
                    }*/ else {
                        viewUtils.showDialog("Alert",
                                "Please enter docket number.");
                    }

                    /*uploadvdo = new UploadVideo();
                    uploadvdo.execute();*/

                } else {
                    Toast.makeText(MainActivity.this, "No Internet Connection !! Try Again Later!!", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });
        imageCapture.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                openImageIntent();
            }
        });

    }

    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, Constants.InputFileSizeParams.FILE_DURATION_SEC);

        // create a file to save the video
        try {
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // set the image file name
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        //intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, 90);
        // set the video image quality to high
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        // start the Video Capture Intent
        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; // In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        //Dob.setText(sdf.format(myCalendar.getTime()));

    }

    /**
     * Create a file Uri for saving an image or video
     */
    private Uri getOutputMediaFileUri(int type) {

        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    @SuppressLint("SimpleDateFormat")
    private File getOutputMediaFile(int type) {

        // Check that the SDCard is mounted
        // File mediaStorageDir = new
        // File(Environment.getExternalStoragePublicDirectory(
        // Environment.DIRECTORY_PICTURES), "MyCameraVideo");

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/AdStringOVideos/Input/");

        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {

            if (!mediaStorageDir.mkdirs()) {

                // output.setText("Failed to create directory
                // AdStringOVideos/Input.");
                Toast.makeText(MainActivity.this, "Failed to create directory AdStringOVideos/Input.",
                        Toast.LENGTH_LONG).show();

                Log.d("SudesiVideoClips", "Failed to create directory AdStringOVideos/Input.");
                return null;
            }
        }

        // Create a media file name

        // For unique file name appending current timeStamp with file name
        java.util.Date date = new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());

        File mediaFile;

        if (type == MEDIA_TYPE_VIDEO) {

            // For unique video file name appending current timeStamp with file
            // name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");

        } else {
            return null;
        }

        return mediaFile;
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                java.util.Date date = new java.util.Date();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());

                MediaMetadataRetriever metadata = new MediaMetadataRetriever();

                Bitmap bmpMeta = null;

                inputpath = fileUri.getPath();
                Log.e("inputpath", inputpath);

                metadata.setDataSource(inputpath);

                bmpMeta = metadata.getFrameAtTime();

                int w = bmpMeta.getWidth();
                int h = bmpMeta.getHeight();

                String videoResolution = w + "x" + h;
                original_str_resolution = videoResolution;
                File f = new File(inputpath);

                long fileSiz = f.length();

                double fileSizeKB = fileSiz / 1024;

                double fileSizeMB = fileSizeKB / 1024;

                double roundOff = Math.round(fileSizeMB * 100.0) / 100.0;

                Log.e("fileSiz", String.valueOf(roundOff));

                System.out.println("fameWidth-----" + w);
                System.out.println("fameHeight----" + h);

				/*
                 * int msec =
				 * MediaPlayer.create(VideocameraActivity.this,Uri.fromFile(new
				 * File(inputpath))).getDuration();
				 *
				 * secs = (int) (msec / 1000.0);
				 *
				 * Log.e("Duration", String.valueOf(secs));
				 *
				 * tvReso.setText(String.valueOf(videoResolution));
				 *
				 * tvSize.setText(String.valueOf(roundOff) + " MB");
				 *
				 * tvDuration.setText(String.valueOf(secs) + " secs");
				 */

                File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/AdStringOVideos/Output/");

                if (!mediaStorageDir.exists()) {
                    mediaStorageDir.mkdirs();
                }

                outvidpath = Environment.getExternalStorageDirectory() + "/AdStringOVideos/Output/" + "OUT_VID"
                        + timeStamp + ".mp4";
                trimvidpath = outvidpath;
                // System.out.println("str_bitrate" + str_bitrate);
                /// System.out.println("str_resolution" + str_resolution);

                // String commandStr = "ffmpeg -y -i "
                // + inputpath
                // + " -strict experimental -s "
                // +str_resolution
                // +
                // " -r 20 -aspect 4:3 -ab 48000 -ac 2 -ar 22050 -b
                // "+str_bitrate+" "
                // + outvidpath;

                // System.out.println("str_bitrate" + str_bitrate);
                // System.out.println("str_resolution" + str_resolution);

                // OriginalSize = String.valueOf(fileSiz);

                // String
                // commandStr="ffmpeg -y -i "+data.getData().getPath()+" -strict
                // experimental -s 320x240 -r 25 -aspect 16:9 -ab 48000 -ac 2
                // -ar 22050 -b 128k "+outvid;

                // String
                // commandStr="ffmpeg -y -i "+data.getData().getPath()+" -strict
                // experimental -s 160x120 -r 30 -aspect 4:3 -ab 48000 -ac 2 -ar
                // 22050 -b 2097k /sdcard/videokit/out.mp4";

                // output.setText(commandStr);
                // Bitmap bmThumbnail;

				/*
                 * bmThumbnail = ThumbnailUtils.createVideoThumbnail(inputpath,
				 * Thumbnails.MICRO_KIND);
				 */

                // vidThumbnail.setImageBitmap(bmThumbnail);

                // Video captured and saved to fileUri specified in the Intent
                // Toast.makeText(this, "Video saved to:\n" +
                // data.getData(), Toast.LENGTH_LONG).show();

                setInputFilePath(inputpath);
                setOutputFilePath(outvidpath);
                setCommandComplex(0);
                runTranscoing();
            } else if (resultCode == RESULT_CANCELED) {

                // output.setText("User cancelled the video capture.");

                // User cancelled the video capture
                Toast.makeText(this, "User cancelled the video capture.", Toast.LENGTH_LONG).show();

            } else {

                // output.setText("Video capture failed.");

                // Video capture failed, advise user
                Toast.makeText(this, "Video capture failed.", Toast.LENGTH_LONG).show();
            }
        }
    }

 /*   public class UploadVideo extends AsyncTask<Void, Void, SoapPrimitive> {

        BufferedInputStream in = null;
        ByteArrayOutputStream out = null;
        String base64encode;
        String filename;
        SoapPrimitive soap_result;
        File file;
        ProgressDialog progress = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setTitle("Please Wait..");
            progress.setMessage("Compressed video is uploading..");
            progress.setCancelable(false);
            progress.show();

        }

        @Override
        protected void onPostExecute(SoapPrimitive result) {
            super.onPostExecute(result);
            progress.dismiss();
            if (result != null) {
                if (result.toString().equalsIgnoreCase("true")) {
                    Toast.makeText(MainActivity.this, "Data Uploaded Successfully.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Server busy, Data uploading failed.", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(MainActivity.this, "Data Uploading Failed!!!", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected SoapPrimitive doInBackground(Void... params) {

            String path = Environment.getExternalStorageDirectory().toString() + "/AdStringOVideos/Trim/";

            // AssetManager mgr = getAssets();

            try {
                File f = new File(path);
                File folderFiles[] = f.listFiles();
                // String list[] = mgr.list(path);
                // Log.e("FILES", String.valueOf(list.length));

                if (folderFiles != null)
                    for (int i = 0; i < folderFiles.length; i++) {

                        // -----------new code--------------------

                        // filename = path.substring(path.lastIndexOf("/") +
                        // 1,path.length());
                        filename = folderFiles[i].getName();
                        File file = new File(path + filename);

                        try {

                            long lenght = file.length();
                            int ChunkSize = 65536;
                            byte[] Buffer = new byte[ChunkSize];
                            int Offset = 0;
                            int BytesRead = 0;

                            in = new BufferedInputStream(new FileInputStream(file));
                            while (Offset != lenght) {
                                BytesRead = in.read(Buffer, 0, ChunkSize);
                                if (BytesRead != Buffer.length) {
                                    ChunkSize = BytesRead;
                                    byte[] TrimmedBuffer = new byte[BytesRead];
                                    TrimmedBuffer = Arrays.copyOf(Buffer, BytesRead);
                                    Buffer = TrimmedBuffer;

                                }

                                base64encode = Base64.encodeToString(Buffer, Base64.DEFAULT);

                                soap_result = service.testsplit(filename, base64encode, Offset);

                                Offset += BytesRead;

                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();

                            base64encode = null;

                        }

                    }

            } catch (Exception e) {
                Log.v("List error:", "can't list" + path);
            }

            return soap_result;
        }

    }

    public class trimVideo extends AsyncTask<Void, Void, SoapPrimitive> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(SoapPrimitive result) {
            super.onPostExecute(result);

            uploadvdo = new UploadVideo();
            uploadvdo.execute();
        }

        @Override
        protected SoapPrimitive doInBackground(Void... params) {

            setInputFilePath(outvidpath);
            // setOutputFilePath(outvidpath);
            setCommandComplex(1);
            runTranscoing();

            return null;

        }

    }*/

    public class SaveSubmit extends AsyncTask<Void, Void, Boolean> {

        String fname;
        boolean ftpUploadStatus = false;

        private ProgressDialog mProgress = null;

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                ftpUploadStatus = getCompressedImagePath(outvidpath, edt_appno.getText().toString(), "Original", "Original");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ftpUploadStatus;
        }

        @Override
        protected void onPostExecute(Boolean ftpUploadStatus) {
            super.onPostExecute(ftpUploadStatus);
            if (!isFinishing() && mProgress != null && mProgress.isShowing())
                mProgress.dismiss();
            if (ftpUploadStatus) {
                Toast.makeText(MainActivity.this, "Video Uploaded Successfully.", Toast.LENGTH_LONG).show();
               // MainActivity.this.finish();


                //startActivity(new Intent(ActivityPreview.this, PreviewActivity.class));
                //finish();


                //File sudesi = new File(Environment.getExternalStorageDirectory() + File.separator + "sudesi" + File.separator);
                //File sudesiMedium = new File(Environment.getExternalStorageDirectory() + File.separator + "sudesiMedium" + File.separator);

                //deleteRecursive(sudesi);
                //deleteRecursive(sudesiMedium);
            } else {
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress = new ProgressDialog(MainActivity.this);
            mProgress.setCancelable(false);
            mProgress.setTitle("Uploading.");
            mProgress.setMessage("Please Wait..");
            mProgress.show();

        }

        /*void deleteRecursive(File fileOrDirectory) {

            if (fileOrDirectory.isDirectory())
                for (File child : fileOrDirectory.listFiles())
                    deleteRecursive(child);

            fileOrDirectory.delete();
        }*/


    }

    public boolean getCompressedImagePath(String orgImagePath,
                                          String scanid, String resolution, String quality) {
        boolean ftpUploadStatus = false;
        if (orgImagePath == null) {
            return ftpUploadStatus;
        }

        byte[] bmpPicByteArray = null;
        int compressQuality = 0;
        String fname = "";
        ByteArrayOutputStream bao = null;
        SimpleFTP ftp = null;
        if (resolution.equalsIgnoreCase("Original") && quality.equalsIgnoreCase("Original")) {
            try {
                FTPClient client = new FTPClient();
                //FileInputStream fis = null;
                try {
                    //client.setConnectTimeout(50000); // 5000 Milliseconds (5 Seconds)
                    client.connect(FTP_IP);
                    showServerReply(client);
                    client.login(USERNAME, PASSWORD);
                    showServerReply(client);
                    System.out.println("Current working directory is: " + client.printWorkingDirectory());
                    //String someDirectory = "nonexistentDir";

                    //client.makeDirectory("/" + scanid);
                    showServerReply(client);

                    client.enterLocalPassiveMode(); // important!
                    client.setFileType(FTP.BINARY_FILE_TYPE);

                    //ftpCreateDirectoryTree(client, "/" + scanid);
                    //String USER_NAME_FTP_ROOT_FOLDER = "CrispDox";

                    ftpCreateDirectoryTree(client, "/" + USER_NAME_FTP_ROOT_FOLDER + "/" + scanid);

                    //client.changeWorkingDirectory("/" + scanid);
                    File root = new File(Environment.getExternalStorageDirectory() + "/AdStringOVideos/Output/");
                    //File root = new File(Environment.getExternalStorageDirectory() + File.separator + "sudesiCompressed" + File.separator);
                    root.mkdirs();

                    fname = orgImagePath.substring(orgImagePath.lastIndexOf("/") + 1);
                    File sdImageMainDirectory = new File(root, fname);

                    /*if (!sdImageMainDirectory.exists()) {

                        sdImageMainDirectory.createNewFile();

                    }*/
                    //if (client.changeWorkingDirectory("/" + scanid)) {
                    // Create an InputStream of the file to be uploaded\
                    Random generator = new Random();
                    String randomString = String.valueOf(generator.nextInt(9676678) + 3256678);

                    FileInputStream in = new FileInputStream(sdImageMainDirectory);
                    ftpUploadStatus = client.storeFile(randomString + ".mp4", in);
                    if (ftpUploadStatus) {
                        Log.v("upload result", "succeeded");
                        //deleteRecursive(sdImageMainDirectory);
                        //File sudesiMedium = new File(Environment.getExternalStorageDirectory() + File.separator + "sudesiOriginal" + File.separator);
                        //File deleteSrcFile = new File(sudesiMedium, fname);
                        //deleteRecursive(deleteSrcFile);

                    }
                    in.close();

                    showServerReply(client);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (client.isConnected()) {
                            client.logout();
                            client.disconnect();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                //////////////////////////

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ftpUploadStatus;
    }

    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

    private void ftpCreateDirectoryTree(FTPClient client, String dirTree) throws IOException {

        boolean dirExists = true;

        //tokenize the string and attempt to change into each directory level.  If you cannot, then start creating.
        String[] directories = dirTree.split("/");
        for (String dir : directories) {
            if (!dir.isEmpty()) {
                if (dirExists) {
                    dirExists = client.changeWorkingDirectory(dir);
                }
                if (!dirExists) {
                    if (!client.makeDirectory(dir)) {
                        throw new IOException("Unable to create remote directory '" + dir + "'.  error='" + client.getReplyString() + "'");
                    }
                    if (!client.changeWorkingDirectory(dir)) {
                        throw new IOException("Unable to change into newly created remote directory '" + dir + "'.  error='" + client.getReplyString() + "'");
                    }
                }
            }
        }
    }

    private void openImageIntent() {
//        Image_CLicked = true;
        Intent i = new Intent(MainActivity.this, MyCamera.class);
        startActivity(i);
        MainActivity.this.finish();
    }

 /*   public void backDialog(String title, String message) {
        //AlertDialog.Builder dialog = new AlertDialog.Builder(_context);

        //final Dialog dialog = new Dialog(_context);
        *//*dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.message_dialog);
        dialog.setCancelable(false);
        TextView dialog_title = (TextView) dialog.findViewById(R.id.dialog_title);
        TextView dialog_message = (TextView) dialog.findViewById(R.id.dialog_message);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);

        dialog_title.setText(title);
        dialog_message.setText(message);

        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Preventing multiple clicks, using threshold of 1 second
                dialog.dismiss();

            }
        });
        dialog.show();*//*

        ///////////

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Go to mobiKYC ?");
        //builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                startActivity(new Intent(MainActivity.this, ActivityPreview.class));
                finish();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }*/

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //backDialog("", "");
        MainActivity.this.finish();

    }

    private String getUserDetails() {
        return ImageCompresionApplication.userDetailsPrefs
                .getString("USER", "");
    }

    private void showDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                dialog.dismiss();
                //VideoRecordLibActivity.this.finish();

            }
        });

       /* builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });*/

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void compressedFilePath(String compressedFilePath) {
        MainActivity.this.finish();
        Constants.COMPRESSED_VIDEO_FILE_PATH = compressedFilePath;
        Toast.makeText(this,
                "Path :" + Constants.COMPRESSED_VIDEO_FILE_PATH, Toast.LENGTH_LONG)
                .show();
        //compressedPathCallback.getCompressedFilePath(compressedFilePath);
    }
/*    public interface CompressedPathCallback {
        void getCompressedFilePath(String compressedFilePath);
    }*/

}

