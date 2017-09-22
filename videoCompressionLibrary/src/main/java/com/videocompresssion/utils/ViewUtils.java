package com.videocompresssion.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class ViewUtils {
    private Activity _context;
    private Typeface MS_Reference_Sans_Serif;
    private Typeface MS_Reference_Sans_Serif_Bold;
    private static final int READ_PHONE_STATE = 0;

    //Typeface avenir_next_condensed_font;
    // constructor
    public ViewUtils(Activity context) {
        this._context = context;
        //font set
        MS_Reference_Sans_Serif = Typeface.createFromAsset(_context.getAssets(), "MS_Reference_Sans_Serif.ttf");
        //   MS_Reference_Sans_Serif_Bold = Typeface.createFromAsset(_context.getAssets(), "MS Reference Sans Serif Bold.ttf");
//        avenir_next_condensed_font = Typeface.createFromAsset(_context.getAssets(),	"Avenir Next Condensed.ttc");
    }
   /* public ViewUtils(Context context) {
        this._context = context;
        //font set
        droidSans = Typeface.createFromAsset(_context.getAssets(), "DroidSans.ttf");
        droidSans_bold = Typeface.createFromAsset(_context.getAssets(), "DroidSans-Bold.ttf");
        //avenir_next_condensed_font = Typeface.createFromAsset(_context.getAssets(),	"Avenir Next Condensed.ttc");

    }*/

/*    public String getUUID() {
        return Secure.getString(_context.getContentResolver(), Secure.ANDROID_ID);
    }*/

    public void set_Typeface_MS_Reference_Sans_Serif(View view) {

        if (view instanceof TextView) {
            ((TextView) view).setTypeface(MS_Reference_Sans_Serif);
        } else if (view instanceof Button) {
            ((TextView) view).setTypeface(MS_Reference_Sans_Serif);
        } else if (view instanceof EditText) {
            ((TextView) view).setTypeface(MS_Reference_Sans_Serif);
        }
    }

  /*  public void set_typeface_MS_Reference_Sans_Serif_Bold(View view) {

        if (view instanceof TextView) {
            ((TextView) view).setTypeface(MS_Reference_Sans_Serif_Bold);
        } else if (view instanceof Button) {
            ((TextView) view).setTypeface(MS_Reference_Sans_Serif_Bold);
        } else if (view instanceof EditText) {
            ((TextView) view).setTypeface(MS_Reference_Sans_Serif_Bold);
        }
    }*/



    /*
     * getting screen width
	 */
    /*	public int getScreenWidth() {
        int columnWidth;
		WindowManager wm = (WindowManager) _context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		final Point point = new Point();
		try {
			display.getSize(point);
		} catch (java.lang.NoSuchMethodError ignore) { // Older device
			point.x = display.getWidth();
			point.y = display.getHeight();
		}
		columnWidth = point.x;
		return columnWidth;
	}*/

    // Check supported file extensions
    private boolean IsSupportedFile(String filePath) {
        String ext = filePath.substring((filePath.lastIndexOf(".") + 1), filePath.length());
        /*if (Constants.Gallery.FILE_EXTN.contains(ext.toLowerCase(Locale.getDefault())))
            return true;
		else*/
        return false;

    }
/*	public void showResponseAlertDialog(String errorCode,String errorDesc){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(_context,AlertDialog.THEME_HOLO_LIGHT);
		builder1.setTitle("Error ");
		builder1.setMessage(errorDesc);
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

    public void showDialogCustom(String errorCode, String errorDesc) {


//        AlertDialog.Builder builder1 = new AlertDialog.Builder(_context,AlertDialog.THEME_HOLO_LIGHT);
//		//builder1.setTitle("Error code :"+errorCode);
//		builder1.setMessage(errorDesc);
//		builder1.setCancelable(true);
//		builder1.setNeutralButton(android.R.string.ok,
//				new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//				dialog.cancel();
//			}
//		});
//		AlertDialog alert11 = builder1.create();
//		alert11.show();
    }

	/*public void showResponseAlertDialogWithIntent(String errorCode,String errorDesc){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(_context,AlertDialog.THEME_HOLO_LIGHT);
		builder1.setTitle("Error code :"+errorCode);
		builder1.setMessage(errorDesc);
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


    /*public void showErrorAlertDialog(String errorMessage){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(_context,AlertDialog.THEME_HOLO_LIGHT);
        builder1.setTitle("Error :");
        builder1.setMessage(errorMessage);
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
    // Reading file paths from SDCard
//    public ArrayList<String> getFilePaths() {
//        ArrayList<String> filePaths = new ArrayList<String>();
//
//        File directory = new File(Constants.Folder.derrimut_gallery_folder);
//
//        // check for directory
//        if (directory.isDirectory()) {
//            // getting list of file paths
//            File[] listFiles = directory.listFiles();
//
//            // Check for count
//            if (listFiles.length > 0) {
//
//                // loop through all files
//                for (int i = 0; i < listFiles.length; i++) {
//
//                    // get file path
//                    String filePath = listFiles[i].getAbsolutePath();
//
//                    // check for supported file extension
//                    if (IsSupportedFile(filePath)) {
//                        // Add image path to array list
//                        filePaths.add(filePath);
//                    }
//                }
//            } else {
//                // image directory is empty
//                /*Toast.makeText(
//                        _context,"Image Gallery Empty!!",
//						Toast.LENGTH_LONG).show();*/
//            }
//
//        } else {
//            /*			AlertDialog.Builder alert = new AlertDialog.Builder(_context);
//            alert.setTitle("Error!");
//			alert.setMessage("Image Gallery Empty!!");
//			alert.setPositiveButton("OK", null);
//			alert.show();
//			 */
//			/*			AlertDialog.Builder builder = new AlertDialog.Builder(_context);
//			builder.setMessage("Oops !! Image Gallery Empty!! ")
//			       .setCancelable(false)
//			       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//			           public void onClick(DialogInterface dialog, int id) {
//			                dialog.dismiss();
//			                Intent intentB = new Intent(_context, GenerateScheduleActivity.class);
//			                _context.startActivity(intentB);
//
//			           }
//			       });
//			AlertDialog alert = builder.create();
//			alert.show();*/
//        }
//
//        return filePaths;
//    }

    /*
     * Resizing image size
     */
    public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT) {
        try {
            File f = new File(filePath);
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            final int REQUIRED_WIDTH = WIDTH;
            final int REQUIRED_HIGHT = HIGHT;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_WIDTH && o.outHeight / scale / 2 >= REQUIRED_HIGHT)
                scale *= 2;
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            //return BitmapFactory.decodeStream(new FileInputStream(f));decodeStream(new FileInputStream(f), null, o2);
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //public boolean isNetworkAvailable() {
    public void isNetworkAvailable(final Handler handler, final int timeout) {
        // ask fo message '0' (not connected) or '1' (connected) on 'handler'
        // the answer must be send before before within the 'timeout' (in milliseconds)

		/*new Thread() {
            private boolean responded = false;
			@Override
			public void run() {
				// set 'responded' to TRUE if is able to connect with google mobile (responds fast)
				new Thread() {
					@Override
					public void run() {
						HttpGet requestForTest = new HttpGet("http://m.google.com");
						try {
							new DefaultHttpClient().execute(requestForTest); // can last...
							responded = true;
						}
						catch (Exception e) {
						}
					}
				}.start();

				try {
					int waited = 0;
					while(!responded && (waited < timeout)) {
						sleep(100);
						if(!responded ) {
							waited += 100;
						}
					}
				}
				catch(InterruptedException e) {} // do nothing
				finally {
					if (!responded) { handler.sendEmptyMessage(0); }
					else { handler.sendEmptyMessage(1); }
				}
			}
		}.start();*/
        new Thread() {
            private boolean responded = false;

            @Override
            public void run() {
                // set 'responded' to TRUE if is able to connect with google mobile (responds fast)
                new Thread() {
                    @Override
                    public void run() {
                        //HttpGet requestForTest = new HttpGet("http://m.google.com");
                        /*try {
							boolean wifiDataAvailable = false;
							boolean mobileDataAvailable = false;
							ConnectivityManager conManager = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
							NetworkInfo[] networkInfo = conManager.getAllNetworkInfo();
							for (NetworkInfo netInfo : networkInfo) {
								if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))
									if (netInfo.isConnected())
										wifiDataAvailable = true;
								if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))
									if (netInfo.isConnected())
										mobileDataAvailable = true;
							}
							if(wifiDataAvailable || mobileDataAvailable){
								responded = true;
							}
						}
						catch (Exception e) {
							e.printStackTrace();
						}*/
                        // get Connectivity Manager object to check connection
                        ConnectivityManager connec = (ConnectivityManager) _context.getSystemService(_context.CONNECTIVITY_SERVICE);

                        // Check for network connections
                        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {

                            // if connected with internet

                            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
                            responded = true;

                        } else if (
                                connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                                        connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {

                            //Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
                            responded = false;
                        }
                        //return false;
                    }
                }.start();

                try {
                    int waited = 0;
                    while (!responded && (waited < timeout)) {
                        sleep(100);
                        if (!responded) {
                            waited += 100;
                        }
                    }
                } catch (InterruptedException e) {
                } // do nothing
                finally {
                    if (!responded) {
                        handler.sendEmptyMessage(0);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                }
            }
        }.start();

    }

//    public void showdialog(String error_code, String error_message) {
//
//        final Dialog dialog = new Dialog(_context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.popup_beneficiary);
//        Button button_yes = (Button) dialog.findViewById(R.id.button_close_popup);
//        TextView textview_popup_descripiton = (TextView) dialog.findViewById(R.id.disclamor_text);
//        setTypeFaceDroidSans(button_yes);
//        setTypeFaceDroidSans(textview_popup_descripiton);
//        textview_popup_descripiton.setText(error_message);
//        button_yes.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//
//            }
//        });
//        dialog.show();
//    }
//
//    public void showdialog(String error_code, int error_message) {
//
//        final Dialog dialog = new Dialog(_context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.popup_beneficiary);
//        Button button_yes = (Button) dialog.findViewById(R.id.button_close_popup);
//        TextView textview_popup_descripiton = (TextView) dialog.findViewById(R.id.disclamor_text);
//        textview_popup_descripiton.setText(error_message);
//        button_yes.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//
//            }
//        });
//        dialog.show();
//    }
//
//    public void killSessionDialog(String error_code, String error_message) {
//
//        final Dialog dialog = new Dialog(_context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.popup_beneficiary);
//        Button button_yes = (Button) dialog.findViewById(R.id.button_close_popup);
//        TextView textview_popup_descripiton = (TextView) dialog.findViewById(R.id.disclamor_text);
//        textview_popup_descripiton.setText(error_message);
//        button_yes.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                Intent i = new Intent(_context, LoginActivity.class);
//                _context.startActivity(i);
//                NSDLApplication.sessionId = "";
//            }
//        });
//        dialog.show();
//    }
/*    public void invalidSessionDialog(String errorCode, String errorDesc) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(_context, AlertDialog.THEME_HOLO_DARK);
        //builder1.setTitle("Error ");
        builder1.setMessage(errorDesc);
        builder1.setCancelable(true);
        builder1.setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent i = new Intent(_context, LoginActivity.class);
                        _context.startActivity(i);
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }*/

/*    public String getCurrentTimeString() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int amPm = calendar.get(Calendar.AM_PM);
        return String.format("%02d:%02d:%02d", hour, minute, second) + " " + amPm;
    }*/

    public void showDialog(String title, String message) {
        //AlertDialog.Builder dialog = new AlertDialog.Builder(_context);

        //final Dialog dialog = new Dialog(_context);
        /*dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        dialog.show();*/

        ///////////

        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                dialog.dismiss();
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


}
