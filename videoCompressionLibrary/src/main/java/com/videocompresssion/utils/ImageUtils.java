package com.videocompresssion.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;

import com.adstringo.MainActivity;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;


public class ImageUtils {

    /*NKGSB Bank*/
 /*   private static final String FTP_IP = "180.179.24.236";
    private static final int PORT = 21;
    private static final String USERNAME = "cpcuser";
    private static final String PASSWORD = "admin@123";*/

    /*AdstringO Bank*/
    private static final String FTP_IP = "118.139.173.227";
    private static final String PORT = "22";
    private static final String USERNAME = "kycupload";
    private static final String PASSWORD = "Kyc@1234";

    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

    /**
     * utility to create an arbitrary directory hierarchy on the remote ftp server
     *
     * @param client
     * @param dirTree the directory tree only delimited with / chars.  No file name!
     * @throws Exception
     */
    private static void ftpCreateDirectoryTree(FTPClient client, String dirTree) throws IOException {

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


    public static boolean getCompressedImagePath(String orgImagePath,
                                                 String scanid, String resolution, String quality, String docType, String page_no) {
        boolean ftpUploadStatus = false;
        if (orgImagePath == null) {
            return ftpUploadStatus;
        }

        byte[] bmpPicByteArray = null;
        int compressQuality = 0;
        String fname = "";
        ByteArrayOutputStream bao = null;
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
                    ftpCreateDirectoryTree(client, "/" + MainActivity.USER_NAME_FTP_ROOT_FOLDER + "/" + scanid);

                    //client.changeWorkingDirectory("/" + scanid);

                    File root = new File(Environment.getExternalStorageDirectory() + File.separator + "sudesiCompressed" + File.separator);
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
                    ftpUploadStatus = client.storeFile(randomString + ".jpeg", in);
                    if (ftpUploadStatus) {
                        Log.v("upload result", "succeeded");
                        deleteRecursive(sdImageMainDirectory);
                        File sudesiMedium = new File(Environment.getExternalStorageDirectory() + File.separator + "sudesiOriginal" + File.separator);
                        File deleteSrcFile = new File(sudesiMedium, fname);
                        deleteRecursive(deleteSrcFile);

                    }
                    in.close();

                    showServerReply(client);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
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

    public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth,
                                     int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight,
                Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(),
                (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());

        // Bitmap result=waterMark(output, 90, 30, true);
        return output;
    }

    public static Bitmap decodeSampledBitmapFromResource(String orgImagePath,
                                                         int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(orgImagePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(orgImagePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
    }

    public static void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }
    //File sudesi = new File(Environment.getExternalStorageDirectory() + File.separator + "sudesi" + File.separator);
    //File sudesiMedium = new File(Environment.getExternalStorageDirectory() + File.separator + "sudesiMedium" + File.separator);

}
