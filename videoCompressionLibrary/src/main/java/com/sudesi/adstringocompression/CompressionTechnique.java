package com.sudesi.adstringocompression;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Handler;

import com.adstringo.compressionreceiver.NetworkUtil;
import com.adstringo.imagecompression.countutils.UtilityClassVideo;
import com.adstringo.imagecompression.countutils.ValidateUserSoapVideo;
import com.sudesi.geolocation.MyLocation;

import java.io.File;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

//for timer

//http://stackoverflow.com/questions/16522815/creating-bitmaps-from-view-in-the-background
//http://stackoverflow.com/questions/2227292/how-to-get-latitude-and-longitude-of-the-mobile-device-in-android
public class CompressionTechnique {


    private File compressedfile;
    private Context context;
    // get input stream
    // private InputStream ims;
    private NetworkUtil networkUtils = new NetworkUtil();

    final static int KERNAL_WIDTH = 3;
    final static int KERNAL_HEIGHT = 3;

    final static int[][] kernalBlur = {{0, -1, 0}, {-1, 5, -1},
            {0, -1, 0}};
    private String compressionDetails;

    private UtilityClassVideo utilImage = new UtilityClassVideo();
    private UtilityClassVideo util = new UtilityClassVideo();
    private static final int NOTIFY_ME_ID = 1337;

    // private int increment = 4;
    private MyLocation myLocation = new MyLocation();
    //private String originalSize;
    //private String compressedSize;
    private static ImageCompresionApplication ica;
    private static int compressCount;
    int cmprsCount;
    //BufferedImage bufferedImage;

    //Timer timer;
    //TimerTask timerTask;

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();


    public CompressionTechnique(Context con) {
        context = con;
        // myLocation.getLocation(context, locationResult);
        // boolean r = myLocation.getLocation(context, locationResult);
        ica = new ImageCompresionApplication(context);

    }

    public File sendVideoDetails(File inputvideopath, File outputVideoPath) {
        inIt();
        if (getUserDetails().trim().equalsIgnoreCase("Active")) {
            try {
                new CompressImageTask().execute(inputvideopath, outputVideoPath).get();


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            util.licenceInvalid(context);
        }

        return compressedfile;
    }

    public void inIt() {
        try {
            /*
             * if (getUserDetails().trim().equalsIgnoreCase("Active")) { // do
			 * nothing } else {
			 */
            if (networkUtils.getConnectivityStatusString(context)) {
                ValidateUserSoapVideo validate = new ValidateUserSoapVideo();
                if (validate.Call(UtilityClassVideo.BUSINESS_UNIT + "-"
                                + context.getApplicationInfo()
                                .loadLabel(context.getPackageManager()),
                        util.uniqueId(context)).equalsIgnoreCase("Active")) {
                    /*
                     * Toast.makeText(context, "Active", Toast.LENGTH_SHORT)
					 * .show();
					 */
                    // saveBusinessUnits("Active");
                } else {
					/*
					 * Toast.makeText(context, "Failure", Toast.LENGTH_SHORT)
					 * .show();
					 */
                    // saveBusinessUnits("Failure");
                    //util.licenceInvalid(context);
                    showNotification();
                }
				/*
				 * } else { util.internertErrorMsgDialog(context); }
				 */
            } else {
                showNotification1();
                //startTimer();
                //util.internertErrorMsgDialog(context);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class CompressImageTask extends AsyncTask<File, Integer, String> {


        public CompressImageTask() {
            super();
            //startTimer();
            //Timer  networkTimer = new Timer();
            //CompressImageTask networkTimerTask = new CompressImageTask()
            //		networkTimerTask.schedule(networkTimerTask, 0,40*1000);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(File... params) {
            try {
                String.valueOf(params[0].length());

                // if (UtilityClassVideo.isReplaceOriginalFile) {
                compressedfile = new File(params[0].getPath());
              /*  String filename = params[0]
                        .getAbsolutePath()
                        .toString()
                        .substring(
                                params[0].getAbsolutePath()
                                        .toString()
                                        .lastIndexOf("/") + 1);*/

               /* compressionDetails = filename + ";"
                        + originalSize + ";"
                        // + new
                        // File(compressedfile.getAbsolutePath()).length()
                        // + ";" + getLatLong();
                        + params[0].length() + ";" + "0:0";*/

                compressionDetails = params[0].getPath().toString() + ";"
                        + String.valueOf(params[0].length()) + ";"
                        // + new
                        // File(compressedfile.getAbsolutePath()).length()
                        // + ";" + getLatLong();
                        + String.valueOf(params[1].length()) + ";" + "0;0";


            } catch (Exception e) {
                e.printStackTrace();
            }
            /*catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
*/

            return compressionDetails;
        }

        @Override
        protected void onPostExecute(String count) {
            super.onPostExecute(count);

            if (compressionDetails != null) {
                saveProfileDetails(count);


            }

        }
    }

    private void saveProfileDetails(String count) {

        Editor profileDetailsPrefs = ica.userDetailsPrefs.edit();
        if (!getImageCount().equalsIgnoreCase("")) {
            profileDetailsPrefs.putString("COUNT", getImageCount() + ":"
                    + count);
        } else {
            profileDetailsPrefs.putString("COUNT", count);
        }
        profileDetailsPrefs.commit();
    }

    private String getImageCount() {

        return ica.userDetailsPrefs.getString("COUNT", "");
    }

    public static void saveBusinessUnits(String user) {
        Editor profileDetailsPrefs = ica.userDetailsPrefs.edit();
        profileDetailsPrefs.putString("USER", user);
        profileDetailsPrefs.commit();
    }

    private String getUserDetails() {
        return ImageCompresionApplication.userDetailsPrefs
                .getString("USER", "");
    }

    private void showNotification() {

        final NotificationManager mgr1 = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);

        builder.setAutoCancel(true);
        builder.setTicker("License Expired..!");
        builder.setContentTitle("License Expired..!");
        builder.setContentText("Please contact Adstringo for further details..");
        //builder.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0));
        builder.setSmallIcon(android.R.drawable.ic_menu_info_details);
        builder.setOngoing(false);
        //builder.setNumber(10098745);
        Notification myNotication1 = builder.build();
        mgr1.notify(NOTIFY_ME_ID, myNotication1);


    }

    private void showNotification1() {

        final NotificationManager mgr1 = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);

        builder.setAutoCancel(true);
        builder.setTicker("Adstringo Image Compression Alert..!");
        builder.setContentTitle("Adstringo Alert..!");
        builder.setContentText("Please Connect to internet..!");
        //builder.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0));
        builder.setSmallIcon(android.R.drawable.ic_menu_info_details);
        builder.setOngoing(false);
        //builder.setNumber(10098745);
        Notification myNotication1 = builder.build();
        mgr1.notify(NOTIFY_ME_ID, myNotication1);


    }



 /*   private String getLatLong() {
        return ImageCompresionApplication.userDetailsPrefs.getString(
                "LAT_LONG", "0;0");
    }*/

    public static String formatFileSize(long size) {
        String hrSize = null;

        double k = size / 1024.0;

        DecimalFormat dec = new DecimalFormat("0.00");

        hrSize = dec.format(k).concat(" KB");

        return hrSize;
    }

	/*public Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}*/

	/*	public static void main(File outputFile) {
		BufferedImage bufferedImage;
		try {
			// read image file
			bufferedImage = ImageIO.read(new File("c:\\javanullpointer.png"));
			// create a blank, RGB, same width and height, and a white
			// background
			BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),BufferedImage.TYPE_INT_RGB);
			// newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
			// Color.WHITE, null);
			// write to jpeg file
			ImageIO.write(newBufferedImage, "jpg", new File("c:\\javanullpointer.jpg"));
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();

		}
	}*/
}
