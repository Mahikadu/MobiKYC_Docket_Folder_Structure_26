package com.adstringo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.adstringo.video4androidRemoteServiceBridge.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;


public class MyCamera extends AppCompatActivity implements SurfaceHolder.Callback, Camera.ShutterCallback, Camera.PictureCallback {
    private byte[] bitmapData;
    private Camera mCamera = null;
    private LinearLayout click_cam;
    private SurfaceView mPreview;
    private String filePath;
    public static int CALL_MANAGER = 0;
    private FrameLayout preview;
    private FaceDetector.Face[] myFace;

    private int currentCameraId = 1;
    private int numberOfFaceDetected;
    private boolean safeToTakePicture = false;
    private long mLastClickTime = 0;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.pcvc_lib_camera_preview);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        mPreview = new SurfaceView(MyCamera.this);
        preview.addView(mPreview);
        mPreview.getHolder().addCallback(this);
        //  click_cam = (LinearLayout) findViewById(R.id.capture);
        //   click_cam.setEnabled(false);
        mPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //try {
        // This case can actually happen if the user opens and closes the camera too frequently.
        // The problem is that we cannot really prevent this from happening as the user can easily
        // get into a chain of activites and tries to escape using the back button.
        // The most sensible solution would be to quit the entire EPostcard flow once the picture is sent.
        mCamera = Camera.open(currentCameraId);
        /*} catch (Exception e) {
            finish();
            Toast.makeText(MyCamera.this, "Please capture photo again.", Toast.LENGTH_LONG).show();
            return;
        }*/
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Preventing multiple clicks, using threshold of 1 second
                    /////////////////////////////////////////////////////////
                    onSnapClick(view);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//        mCamera.setFaceDetectionListener(new Camera.FaceDetectionListener() {
//            @Override
//            public void onFaceDetection(Camera.Face[] faces, Camera camera) {
//
//Log.e("Face","detetcd");
//                       click_cam.setEnabled(true);
//
//
//            }
//        });
//
//        mCamera.startFaceDetection();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCamera != null)
            mCamera.stopPreview();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        Log.e("CAMERA", "Destroy");
    }


/*    public void onCancelClick(View v) {

        mCamera.stopPreview();
        mCamera.release();
        if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        mCamera = Camera.open(currentCameraId);
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_FRONT, info);
        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break; //Natural orientation
            case Surface.ROTATION_90:
                degrees = 90;
                break; //Landscape left
            case Surface.ROTATION_180:
                degrees = 180;
                break;//Upside down
            case Surface.ROTATION_270:
                degrees = 270;
                break;//Landscape right
        }
        int rotate = (info.orientation - degrees + 360) % 360;

        //STEP #2: Set the 'rotation' parameter
        Camera.Parameters params = mCamera.getParameters();
        params.setRotation(rotate);
        try {
            mCamera.setPreviewDisplay(mPreview.getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mCamera.setParameters(params);
        mCamera.setDisplayOrientation(90);
        mCamera.startPreview();
    }*/

    public void onSnapClick(View v) {

        if (safeToTakePicture) {

            mCamera.takePicture(this, null, null, this);
            safeToTakePicture = false;
        }
    }

    @Override
    public void onShutter() {
        //  Toast.makeText(getApplicationContext(), "Click!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        //Here, we chose internal storage
        try {

//            FileOutputStream fos = null;
            BitmapFactory.Options bfo = new BitmapFactory.Options();

            bfo.inPreferredConfig = Bitmap.Config.RGB_565;
            bfo.inDither = false;
            bfo.inScaled = false;
            Matrix matrix = new Matrix();
            matrix.postRotate(-180);
            Bitmap thumbnail = BitmapFactory.decodeByteArray(data, 0, data.length, bfo);
            Bitmap bitmap = Bitmap.createScaledBitmap(thumbnail, 1000, 1000, true);
            //Bitmap face = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            Bitmap face = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), getMatrixByDeviceModel(), true);
            int width = face.getWidth();
            int height = face.getHeight();
            myFace = new FaceDetector.Face[5];

            // filePath = "/sdcard/pcvc_image" + System.currentTimeMillis() + ".jpg";
            FaceDetector detector = new FaceDetector(width, height, 5);

            byte[] result = null;

            if (face != null) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                face.compress(Bitmap.CompressFormat.JPEG, 80, out);
                bitmapData = out.toByteArray();
                result = bitmapData;
            }
            String byteA = Base64.encodeToString(result, Base64.DEFAULT);
            numberOfFaceDetected = detector.findFaces(face, myFace);
            Paint ditherPaint = new Paint();
            //create a draw paint
            Paint drawPaint = new Paint();
            ditherPaint.setDither(true);
            drawPaint.setColor(Color.RED);
            drawPaint.setStyle(Paint.Style.STROKE);
            drawPaint.setStrokeWidth(2);
            Canvas canvas = new Canvas();
            //set bitmap to canvas
            canvas.setBitmap(face);
            int facesFound = detector.findFaces(face, myFace);
            PointF midPoint = new PointF();
            //eye distance variable
            float eyeDistance = 0.0f;
            //confidence variable
            float confidence = 0.0f;
            Log.e("Number of faces Detect", "" + facesFound);
            if (facesFound > 0) {
                if (facesFound > 1) {
                    Toast.makeText(MyCamera.this, "More than a single face detected.", Toast.LENGTH_LONG).show();
                    if (mCamera != null) {
                        preview.setEnabled(true);
                        mCamera.release();
                        mCamera = null;
                    }
                    mCamera = Camera.open(currentCameraId);
                    mCamera.setDisplayOrientation(90);

                    preview.removeAllViews();
                    preview.addView(mPreview);

                } else {
                    for (int index = 0; index < facesFound; index++) {
                        myFace[index].getMidPoint(midPoint);
                        eyeDistance = myFace[index].eyesDistance();
                        confidence = myFace[index].confidence();
                        //Toast.makeText(MyCamera.this, "Confidence :" + eyeDistance, Toast.LENGTH_LONG).show();

                        if (eyeDistance > 80 && eyeDistance < 300) {
                            saveSelfiRootSDCard(face);
                            //Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_LONG).show();
                            //Intent i = getIntent();
                            //String page = i.getStringExtra("Page");
                            // Intent intent = getIntent();
                            //   intent.putExtra("Path", filePath);
                            //setResult(RESULT_OK, intent);
                            //finish();

                            if (mCamera != null) {
                                mCamera.stopPreview();
                                mCamera.release();
                                mCamera = null;
                            }
                            Log.e("CAMERA", "Destroy");
                            //Intent launchIntent = pm.getLaunchIntentForPackage("com.adstringo.videocompression");
                            Intent launchIntent = new Intent(this, MainActivity.class);
                            launchIntent.putExtra("camera_intent", "camera_intent");
                            startActivity(launchIntent);
                            finish();

//                            PCVC_LIB_Communication_Details_Fragment.btn_confirm.setVisibility(View.VISIBLE);
//                            PCVC_LIB_Communication_Details_Fragment.btn_ok.setVisibility(View.GONE);
//                            PCVC_LIB_Communication_Details_Fragment.setBitMap(thumbnail);PCVC_LIB_Communication_Details_Fragment.Image_CLicked = true;
//                            PCVC_LIB_Communication_Details_Fragment.img_selfie.setVisibility(View.GONE);
//                            PCVC_LIB_Communication_Details_Fragment.cameraText.setVisibility(View.GONE);
                            //   PCVC_LIB_Communication_Details_Fragment.dialog_clickimage.dismiss();

                        } else {
                            Toast.makeText(MyCamera.this, "Please adjust camera properly.", Toast.LENGTH_LONG).show();
                            // CommonFunctions.ShowAlert(Activity, "Please Adjust Camera properly", null);
                            if (mCamera != null) {
                                preview.setEnabled(true);
                                mCamera.release();
                                mCamera = null;
                            }
                            mCamera = Camera.open(currentCameraId);
                            mCamera.setDisplayOrientation(90);

                            preview.removeAllViews();
                            preview.addView(mPreview);
                            safeToTakePicture = true;
                        }
                    }
                }
            } else {
                Toast.makeText(MyCamera.this, "Unable to detect face, please adjust face properly.", Toast.LENGTH_LONG).show();
                if (mCamera != null) {
                    preview.setEnabled(true);

                    mCamera.release();

                    mCamera = null;
                }
                mCamera = Camera.open(currentCameraId);
                mCamera.setDisplayOrientation(90);

                preview.removeAllViews();
                preview.addView(mPreview);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//            filePath = "/sdcard/test_"+System.currentTimeMillis() +".jpg";
//            fos = new FileOutputStream(
//                    filePath);
//            fos.write(data);
//            fos.close();
    //Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
//        } finally {
//            Intent i = getIntent();
//            i.putExtra("Path", filePath);
//            setResult(RESULT_OK, i);
//            finish();
//        }

    //  camera.startPreview();


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_FRONT, info);

        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break; //Natural orientation
            case Surface.ROTATION_90:
                degrees = 90;
                break; //Landscape left
            case Surface.ROTATION_180:
                degrees = 180;
                break;//Upside down
            case Surface.ROTATION_270:
                degrees = 270;
                break;//Landscape right
        }
        int rotate = (info.orientation - degrees + 360) % 360;

        //STEP #2: Set the 'rotation' parameter
        Camera.Parameters params = mCamera.getParameters();
        params.setRotation(rotate);
        mCamera.setParameters(params);
        mCamera.setDisplayOrientation(90);
        ////////////////////////////////////
        int result;
        result = (info.orientation + degrees) % 360;
        result = (360 - result) % 360;  // compensate the mirror

        mCamera.setDisplayOrientation(result);
        ////////////////////////////////////////////
        mCamera.startPreview();
        safeToTakePicture = true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(mPreview.getHolder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("PREVIEW", "surfaceDestroyed");
    }

    public static Matrix getMatrixByDeviceModel() {
        String manufacturer = Build.MANUFACTURER;
        Matrix matrix = new Matrix();
        Log.e("Device name is", "" + manufacturer);
      /*  if (manufacturer.equalsIgnoreCase("samsung") || manufacturer.equalsIgnoreCase("sony")) {
            matrix.postRotate(-90);
        } else {
            matrix.postRotate(-180);
        }*/

        // String model = Build.MODEL;

        return matrix;
    }

    private void saveSelfiRootSDCard(Bitmap face) {
        try {
            Bitmap mBitmap = Bitmap.createScaledBitmap(face, 50, 50, true);
     /* file_byte is yous json string*/
            //byte[] decodestring = Base64.decode(file_byte, Base64.DEFAULT);
            File file = Environment.getExternalStorageDirectory();
            //File dir= Environment.getExternalStorageDirectory() + "/watermarkimage.png";
            File dir = new File(file.getAbsolutePath());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File document = new File(dir, "/watermarkimage_selfi.jpeg");

            if (document.exists()) {
                document.delete();
            }

            try {
                FileOutputStream out = new FileOutputStream(document);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            /*FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            FileOutputStream fos = new FileOutputStream(document.getPath());
            fos.write(decodestring);
            fos.close();*/
        } catch (Exception e) {
            Log.e("MYCAMERA", "error: " + e.getMessage());

        }


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        startActivity(new Intent(MyCamera.this, MainActivity.class));

    }
}