package org.exto.mobescan.kyc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adstringo.MainActivity;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import org.exto.ListView.ImageListAdapter;
import org.exto.Login.ViewUtils;
import org.exto.Model.ImageModel;
import org.exto.dbConfig.Dbcon;
import org.exto.libs.ConnectionDetector;
import org.exto.libs.ImageUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.R.attr.key;
import static android.R.attr.value;

public class ActivityPreview extends Activity implements
        AdapterView.OnItemLongClickListener {

    private Uri outputFileUri;

    private String pathCamera = "";

    private SharedPreferences sharedpre = null;

    private SharedPreferences.Editor saveuser = null;

    private File root = null;

    private String fname = "";

    private String scannedId;

    private Dbcon db;

    private TextView welcome;

    private ListView listView;

    private ArrayAdapter<ImageModel> adapter;

    private Button b1, b2, b3;

    EditText edt_appno;

    SaveSubmit save;

    private ArrayList<String> combineArraylist;
    private ArrayList<HashMap<String, String>> listOfImagesPath;
    private HashMap<String, String> map;
    private ArrayList<HashMap<String, String>> compresslistofImagepath;
    public String[] filepath;
    HashMap<String, String> mapcompress;
    //private ArrayList<HashMap<String, String>> listofcompressimage;

    Spinner spin_docType;

    ArrayList<File> filesArray;

    UploadDocument upload;
    private ConnectionDetector cd;
    View emptyTextView;
    int doc1, doc2, doc3, doc4, doc5, doc6, doc7, doc8;
    private ViewUtils viewUtils;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_activity);
        viewUtils = new ViewUtils(ActivityPreview.this);

        cd = new ConnectionDetector(getApplicationContext());
        emptyTextView = (View) findViewById(R.id.list_empty);
        listOfImagesPath = new ArrayList<HashMap<String, String>>();

        compresslistofImagepath = new ArrayList<HashMap<String, String>>();
        combineArraylist = new ArrayList<>();
        //listofcompressimage = new ArrayList<HashMap<String, String>>();
        filesArray = new ArrayList<File>();

        sharedpre = getSharedPreferences("Sudesi", MODE_PRIVATE);

        saveuser = sharedpre.edit();

        welcome = (TextView) findViewById(R.id.TextView01);

        listView = (ListView) findViewById(R.id.lv_thumbnail);

        b1 = (Button) findViewById(R.id.button_new);

        b2 = (Button) findViewById(R.id.preview_button);

        b3 = (Button) findViewById(R.id.buttonNext);

        edt_appno = (EditText) findViewById(R.id.edt_appno);

        spin_docType = (Spinner) findViewById(R.id.spin_doctype);

        listView.setOnItemLongClickListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                ActivityPreview.this, R.array.docTypeArary,
                R.layout.layout_spinner_item);

        spin_docType.setAdapter(adapter);

        db = new Dbcon(this);
        db.open();

        String number = db.fetchVal();
        if (number != null) {
            db.insert(
                    new String[]{String.valueOf(Integer.parseInt(number) + 1)},
                    new String[]{"claimId"}, "claim_auto");
            number = "Docket_Number" + db.fetchVal();
        } else {
            number = "1000";
            db.insert(
                    new String[]{String.valueOf(Integer.parseInt(number) + 1)},
                    new String[]{"claimId"}, "claim_auto");
            number = "Docket_Number" + db.fetchVal();
        }

        Log.e("number", number);
        //edt_appno.setText(number);
        //edt_appno.setSelection(edt_appno.getText().length());

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        findViewById(R.id.button_exit).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityPreview.this);

                        builder.setTitle("Exit");
                        builder.setMessage("Are you sure ?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing but close the dialog
                                startActivity(new Intent(ActivityPreview.this, LoginActivity.class));
                                finish();
                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();


                    }
                });

        findViewById(R.id.buttonNext).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Check if Internet present
                        if (listOfImagesPath.size() > 0) {
                        /*    upload = new UploadDocument();
                            upload.execute();*/
                            if (!cd.isConnectingToInternet()) {
                                // Internet Connection is not present
                                viewUtils.showDialog("Alert", "Please check Your Internet Connection.");
                                // stop executing code by return
                            } else {
                                new SaveSubmit().execute();// FTP upload

                            }
                        } else {
                            viewUtils.showDialog("Alert", "Please capture image(s) to proceed further.");
                        }


                    }
                });

        findViewById(R.id.preview_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkPermission()) {
                            String number = edt_appno.getText().toString();
                            if (number != null) {
                                db.insert(
                                        new String[]{number},
                                        new String[]{"claimId"}, "claim_auto");
                                //number = "CLAIM" + db.fetchVal();
                            }
                        /*else if (edt_password.getText().toString().contains(" ")) {
            viewUtils.showOneButtonAlertDialog("Alert!", "Password should not contain spaces");
            edt_password.setText("");
            edt_confirm_password.setText("");
            return false;
        } */
                            if (!edt_appno.getText().toString().equals("") && !spin_docType.getSelectedItem().toString().equalsIgnoreCase("--Select document category--")) {
                                //if (!edt_appno.getText().toString().equalsIgnoreCase("")) {
                                if (edt_appno.getText().toString().contains(" ")) {
                                    viewUtils.showDialog("Alert",
                                            "Docket number should not contain spaces.");
                                } else {
                                    openImageIntent();

                                }
                            } else if (spin_docType.getSelectedItem().toString().equalsIgnoreCase("--Select document category--")) {
                                Toast.makeText(ActivityPreview.this,
                                        "Please select document category below.", Toast.LENGTH_LONG)
                                        .show();
                            } else {
                                viewUtils.showDialog("Alert",
                                        "Please enter docket number.");
                            }

                        } else {
                            requestPermission();
                        }
                    }
                });
        listView.setEmptyView(emptyTextView);
        viewUtils.set_Typeface_MS_Reference_Sans_Serif(findViewById(R.id.TextView01));
        viewUtils.set_Typeface_MS_Reference_Sans_Serif(findViewById(R.id.preview_button));//password
        viewUtils.set_Typeface_MS_Reference_Sans_Serif(findViewById(R.id.buttonNext));//password
        viewUtils.set_Typeface_MS_Reference_Sans_Serif(findViewById(R.id.textView1));//textView24
        viewUtils.set_Typeface_MS_Reference_Sans_Serif(findViewById(R.id.edt_appno));//textView24
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED &&
                result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && read && write) {
                        Toast.makeText(ActivityPreview.this,
                                "Permission Granted, Now you can access camera.", Toast.LENGTH_LONG)
                                .show();
                        String number = edt_appno.getText().toString();
                        if (number != null) {
                            db.insert(
                                    new String[]{number},
                                    new String[]{"claimId"}, "claim_auto");
                            //number = "CLAIM" + db.fetchVal();
                        }
                        /*else if (edt_password.getText().toString().contains(" ")) {
            viewUtils.showOneButtonAlertDialog("Alert!", "Password should not contain spaces");
            edt_password.setText("");
            edt_confirm_password.setText("");
            return false;
        } */
                        if (!edt_appno.getText().toString().equals("") && !spin_docType.getSelectedItem().toString().equalsIgnoreCase("--Select document category--")) {
                            //if (!edt_appno.getText().toString().equalsIgnoreCase("")) {
                            if (edt_appno.getText().toString().contains(" ")) {
                                viewUtils.showDialog("Alert",
                                        "Docket number should not contain spaces.");
                            } else {
                                openImageIntent();

                            }
                        } else if (spin_docType.getSelectedItem().toString().equalsIgnoreCase("--Select document category--")) {
                            Toast.makeText(ActivityPreview.this,
                                    "Please select document category below.", Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            viewUtils.showDialog("Alert",
                                    "Please enter docket number.");
                        }
                    }
                } else {
                    Toast.makeText(ActivityPreview.this,
                            "Permission Denied, You cannot access camera.", Toast.LENGTH_LONG)
                            .show();

                }


                break;
        }
    }


    private void showCustomDialog(int positions, ArrayList<HashMap<String, String>> ImagelistArray) {

        HashMap<String, String> map = ImagelistArray.get(positions);

        final AlertDialog.Builder DialogMaster = new AlertDialog.Builder(this);

        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogViewMaster = li.inflate(R.layout.custom_dialog, null);
        DialogMaster.setView(dialogViewMaster);

        final AlertDialog showMaster = DialogMaster.show();

        BitmapFactory.Options bitMapOption = new BitmapFactory.Options();
        bitMapOption.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(map.get("image_path"), bitMapOption);
        int imageWidth = bitMapOption.outWidth;
        int imageHeight = bitMapOption.outHeight;
        String resolution = imageWidth + "*" + imageHeight;

        BitmapFactory.Options bitMapOption1 = new BitmapFactory.Options();
        bitMapOption1.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(map.get("compimage_path"), bitMapOption1);
        int imageWidth1 = bitMapOption1.outWidth;
        int imageHeight1 = bitMapOption1.outHeight;
        String resolution1 = imageWidth1 + "*" + imageHeight1;

        File orifile = new File(map.get("image_path"));
        int file_size_ori = Integer.parseInt(String.valueOf(orifile.length() / 1024));


        File comfile = new File(map.get("compimage_path"));
        int file_size_com = Integer.parseInt(String.valueOf(comfile.length() / 1024));

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

// will either be DENSITY_LOW, DENSITY_MEDIUM or DENSITY_HIGH
        int dpiClassification = dm.densityDpi;

/*// these will return the actual dpi horizontally and vertically
        float xDpi = dm.xdpi;
        float yDpi = dm.ydpi;*/


        Button btnDismissMaster = (Button) showMaster.findViewById(R.id.buttonClose);
        TextView textviewOriginPath = (TextView) showMaster.findViewById(R.id.textviewOriginPath);
        TextView textviewOriginSize = (TextView) showMaster.findViewById(R.id.textviewOriginSize);
        TextView textviewOriginResolution = (TextView) showMaster.findViewById(R.id.textviewOriginResolution);
        TextView textviewOriginDpi = (TextView) showMaster.findViewById(R.id.textviewOriginDpi);

        TextView textviewCompressPath = (TextView) showMaster.findViewById(R.id.textviewCompressPath);
        TextView textviewCompressSize = (TextView) showMaster.findViewById(R.id.textviewCompressSize);
        TextView textviewCompressResolution = (TextView) showMaster.findViewById(R.id.textviewCompressResolution);
        TextView textviewCompressDpi = (TextView) showMaster.findViewById(R.id.textviewCompressDpi);

        textviewOriginPath.setText(map.get("image_path").toString());
        if (file_size_ori > 1024) {
            double fileSizeInMB = file_size_ori / 1024;
            textviewOriginSize.setText("Size :" + String.valueOf(fileSizeInMB) + "Mb");
        } else {
            textviewOriginSize.setText("Size :" + String.valueOf(file_size_ori) + "Kb");
        }
        textviewOriginResolution.setText("Resolution :" + resolution);
        textviewOriginDpi.setText("Dpi :" + String.valueOf(dpiClassification));

        textviewCompressPath.setText(map.get("compimage_path").toString());
        if (file_size_com > 1024) {
            double fileSizeInMB = file_size_com / 1024;
            textviewCompressSize.setText("Size :" + String.valueOf(fileSizeInMB) + "Mb");
        } else {
            textviewCompressSize.setText("Size :" + String.valueOf(file_size_com) + "Kb");
        }

        textviewCompressResolution.setText("Resolution :" + resolution1);
        textviewCompressDpi.setText("Dpi :" + String.valueOf(dpiClassification));

        btnDismissMaster.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showMaster.dismiss();
            }
        });
        showMaster.setCancelable(false);

    }

    @Override
    public void onBackPressed() {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(ActivityPreview.this);

        builder.setTitle("Exit");
        builder.setMessage("Are you sure ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                startActivity(new Intent(ActivityPreview.this, LoginActivity.class));
                finish();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();*/
        backDialog("", "");
    }

    @SuppressLint("NewApi")
    private void openImageIntent() {

        try {
            String app = edt_appno.getText().toString();

            String doc_Code = "";

            if (spin_docType.getSelectedItem().toString()
                    .equalsIgnoreCase("Discrepancy Reply Docs")) {
                doc_Code = "Q";
                doc1 = sharedpre.getInt("doc1", 0);

                saveuser.putInt("doc1", doc1 + 1);
                saveuser.commit();

                fname = app + "_" + doc_Code + "_" + doc1 + "_" + System.currentTimeMillis()
                        + ".jpeg";
            } else if (spin_docType.getSelectedItem().toString()
                    .equalsIgnoreCase("NEFT Details / Canceled Cheque")) {
                doc_Code = "H";
                doc2 = sharedpre.getInt("doc2", 0);

                saveuser.putInt("doc2", doc2 + 1);
                saveuser.commit();

                fname = app + "_" + doc_Code + "_" + doc2 + "_" + System.currentTimeMillis()
                        + ".jpeg";
            } else if (spin_docType.getSelectedItem().toString()
                    .equalsIgnoreCase("Additional Documents")) {
                doc_Code = "A";
                doc3 = sharedpre.getInt("doc3", 0);

                saveuser.putInt("doc3", doc3 + 1);
                saveuser.commit();

                fname = app + "_" + doc_Code + "_" + doc3 + "_" + System.currentTimeMillis()
                        + ".jpeg";
            } else if (spin_docType.getSelectedItem().toString()
                    .equalsIgnoreCase("Bills")) {
                doc_Code = "B";
                doc4 = sharedpre.getInt("doc4", 0);

                saveuser.putInt("doc4", doc4 + 1);
                saveuser.commit();

                fname = app + "_" + doc_Code + "_" + doc4 + "_" + System.currentTimeMillis()
                        + ".jpeg";
            } else if (spin_docType.getSelectedItem().toString()
                    .equalsIgnoreCase("Claim Form")) {
                doc_Code = "C";
                doc5 = sharedpre.getInt("doc5", 0);

                saveuser.putInt("doc5", doc5 + 1);
                saveuser.commit();

                fname = app + "_" + doc_Code + "_" + doc5 + "_" + System.currentTimeMillis()
                        + ".jpeg";
            } else if (spin_docType.getSelectedItem().toString()
                    .equalsIgnoreCase("Discharg Summary")) {
                doc_Code = "D";
                doc6 = sharedpre.getInt("doc6", 0);

                saveuser.putInt("doc6", doc6 + 1);
                saveuser.commit();

                fname = app + "_" + doc_Code + "_" + doc6 + "_" + System.currentTimeMillis()
                        + ".jpeg";
            } else if (spin_docType.getSelectedItem().toString()
                    .equalsIgnoreCase("Reports")) {
                doc_Code = "R";
                doc7 = sharedpre.getInt("doc7", 0);

                saveuser.putInt("doc7", doc7 + 1);
                saveuser.commit();

                fname = app + "_" + doc_Code + "_" + doc7 + "_" + System.currentTimeMillis()
                        + ".jpeg";
            } else if (spin_docType.getSelectedItem().toString()
                    .equalsIgnoreCase("Proposal Form")) {
                doc_Code = "P";
                doc8 = sharedpre.getInt("doc8", 0);

                saveuser.putInt("doc8", doc8 + 1);
                saveuser.commit();

                fname = app + "_" + doc_Code + "_" + doc8 + "_" + System.currentTimeMillis()
                        + ".jpeg";
            }

            fname = app + "_" + doc_Code + "_" + doc8 + "_" + System.currentTimeMillis()
                    + ".jpeg";

            FileOutputStream fos = openFileOutput(fname, Context.MODE_PRIVATE);//Context.MODE_PRIVATE
            fos.close();

            root = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "sudesiMedium" + File.separator);
            root.mkdirs();

            root.setWritable(true);

            File f = new File(root, fname);

            outputFileUri = Uri.fromFile(f);

            pathCamera = Environment.getExternalStorageDirectory()
                    + "/sudesiMedium/" + fname;

            final Intent captureIntent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri());
            } else {
                //File file = new File(getPhotoFileUri().getPath());
                outputFileUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", f);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            captureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivityForResult(captureIntent, 202);
        } catch (Exception e) {
            Toast.makeText(ActivityPreview.this,
                    "Something Wrong with The Camera. Try Again!!!",
                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)

    {

        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RESULT_OK) {

                if (requestCode == 202) {

                    compresslistofImagepath.clear();

                    String selectedImageUri = pathCamera;

                    saveuser = sharedpre.edit();

                    int count = sharedpre.getInt("iCount", 0);

                    count++;

                    saveuser.putString("ImgPath_" + String.valueOf(count), selectedImageUri.toString());

                    saveuser.putInt("iCount", count);

                    saveuser.commit();


                    // originalArray
                    //Log.e("image_path", selectedImageUri);
                    //Log.e("image_name", selectedImageUri.substring(selectedImageUri.lastIndexOf("/") + 1, selectedImageUri.length()));

                    map = new HashMap<String, String>();
                    map.put("image_path", selectedImageUri);
                    // map.put("image_name", selectedImageUri.substring(selectedImageUri.lastIndexOf("/") + 1, selectedImageUri.length()));

                    // listOfImagesPath.add(map);

                    compresslistofImagepath.add(map);

                    upload = new UploadDocument();
                    upload.execute();


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            viewUtils.showDialog("Error",
                    "Something Wrong with The Camera. Please try Again");

        }
    }

    public File createPDF(String imagepath) {
        File file = null;
        try {
            Document document = new Document(PageSize.A4);

            String filename = imagepath.substring(imagepath.lastIndexOf("/") + 1, imagepath.length());

            String fil[] = filename.split("\\.");

            Log.e("fil", fil[0]);

            String PDFpath = Environment.getExternalStorageDirectory() + "/" + fil[0] + ".pdf";

            file = new File(PDFpath);

            file.createNewFile();

            PdfWriter.getInstance(document, new FileOutputStream(PDFpath));

            document.open();

            Image image = Image.getInstance(imagepath);
            //image.scaleAbsolute(PageSize.A4);

            document.setPageSize(image);
            image.setAbsolutePosition(0, 0);
            document.newPage();
            document.add(image);


            document.close();

            File f = new File(imagepath);
            if (f.exists()) {
                f.delete();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }


    public String getCompressImage(String imgPath) {

        String path = null;
        ByteArrayOutputStream bao = null;
        byte[] bmpPicByteArray = null;
        try {

            File f = new File(imgPath);

            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            int orgWidth = bitmap.getWidth();
            int orgHeight = bitmap.getHeight();

            int compressedWidth, compressedHeight, compressQuality;

            //compressedWidth = 1600;
            //compressedHeight = 1200;
            compressQuality = 20;

            bao = new ByteArrayOutputStream();
            bao.reset();

            ///float ratioX = (float) compressedWidth / (float) orgWidth;
            //float ratioY = (float) compressedHeight / (float) orgHeight;
            //float ratio = Math.min(ratioX, ratioY);

            // New width and height based on aspect ratio
            //int newWidth = (int) (orgWidth * ratio);
            //int newHeight = (int) (orgHeight * ratio);
            //Bitmap bitmap2 = scaleBitmap(bitmap, newWidth, newHeight);

            Bitmap waterMarkedBit = waterMark(bitmap, 90, true, f.length());
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }
            //bitmap2.recycle();

            if (waterMarkedBit != null) {
                waterMarkedBit.compress(Bitmap.CompressFormat.JPEG, compressQuality, bao);
                bmpPicByteArray = bao.toByteArray();
                if (waterMarkedBit != null) {
                    waterMarkedBit.recycle();
                    waterMarkedBit = null;

                }
            }

            File root = new File(
                    Environment.getExternalStorageDirectory()
                            + File.separator + "sudesiCompressed"
                            + File.separator);
            root.mkdirs();


            fname = imgPath.substring(imgPath.lastIndexOf("/") + 1, imgPath.length());


            File sdImageMainDirectory = new File(root, fname);

            path = sdImageMainDirectory.getPath();

            if (!sdImageMainDirectory.exists()) {
                sdImageMainDirectory.createNewFile();
            }

            Bitmap bi = BitmapFactory.decodeByteArray(bmpPicByteArray, 0, bmpPicByteArray.length);
            FileOutputStream fos = new FileOutputStream(sdImageMainDirectory);
            bi.compress(Bitmap.CompressFormat.JPEG, compressQuality,
                    fos);
            fos.flush();
            fos.close();

            if (bi != null) {
                bi.recycle();
                bi = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;


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


    public static Bitmap waterMark(Bitmap src, int alpha, boolean underline, long imgSize) {
        // get source <span class="ncz69s84" id="ncz69s84_1">image width</span>
        // and height
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        // create canvas object
        Canvas canvas = new Canvas(result);
        // draw bitmap on canvas
        canvas.drawBitmap(src, 0, 0, null);
        // create paint object
        Paint paint = new Paint();
        Paint paint1 = new Paint();

        float x, x1, y, y1;
        if (imgSize <= 1048576) {
            paint.setTextSize(100);
            paint1.setTextSize(60);
        } else if (imgSize <= 2097152) {
            paint.setTextSize(230);
            paint1.setTextSize(100);
        } else if (imgSize > 2097152) {
            paint.setTextSize(300);
            paint1.setTextSize(155);
        }

        if (h > w) {
            x = w / 3.2f;
            x1 = w / 8f;
//			paint.setTextSize(150);
//			paint1.setTextSize(70);
        } else {
            x = w / 3.1f;
            x1 = w / 8f;
//			paint.setTextSize(120);

        }
        y = h / 2;

        y1 = h / 1.1f;


        // apply color
        paint.setColor(Color.RED);
        paint1.setColor(Color.RED);
        // set transparency
        paint.setAlpha(alpha);
        paint1.setAlpha(alpha);
        // set text size

        paint.setAntiAlias(true);


        paint1.setAntiAlias(true);
        // set should be underlined or not
        paint.setUnderlineText(underline);
        // paint.setShadowLayer(5, x, y, Color.BLUE);
        paint.setFakeBoldText(true);

        paint1.setUnderlineText(underline);
        // paint1.setShadowLayer(5, x, y, Color.BLUE);
        paint1.setFakeBoldText(true);

        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String dateee = formatter.format(calendar.getTime());

        // draw text on given location
//		canvas.drawText("AdStringO", x, y, paint);
        canvas.drawText("Compressed by AdStringO  " + dateee, x1, y1, paint1);

        return result;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        showCustomDialog(position, listOfImagesPath);
        return false;
    }

    public class UploadDocument extends AsyncTask<Void, Void, String> {

        ProgressDialog progress = new ProgressDialog(ActivityPreview.this);

        String uploaded;

        @Override
        protected String doInBackground(Void... params) {
            if (compresslistofImagepath.size() > 0) {
                uploaded = "true";


                for (int i = 0; i < compresslistofImagepath.size(); i++) {
                    String compressImagePath = getCompressImage(compresslistofImagepath.get(i).get("image_path"));
                    String compressImagename = compressImagePath.substring(compressImagePath.lastIndexOf("/") + 1, compressImagePath.length());
//					File f = createPDF(p);
//					UploadPDF.uploadpdf(f);
//					filesArray.add(f);
                    // mapcompress = new HashMap<String, String>();
                    map.put("compimage_path", compressImagePath);
                    // map.put("compimage_name", compressImagename);

                    listOfImagesPath.add(map);


                }


            }


            return uploaded;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle("Compressing image.");
            progress.setMessage("Please Wait..");
            progress.setCancelable(false);
            progress.show();


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (uploaded != null) {
                if (uploaded.equalsIgnoreCase("true")) {
                    Toast.makeText(ActivityPreview.this, "File compressed and saved locally.", Toast.LENGTH_LONG).show();
                    //startActivity(new Intent(ActivityPreview.this, ActivityPreview.class));
                    //finish();
                    if (listOfImagesPath != null) {
                        ImageListAdapter imageListAdapter = new ImageListAdapter(ActivityPreview.this, R.layout.newrowlayout, listOfImagesPath);
                        listView.setAdapter(imageListAdapter);
                        //imageListAdapter.notifyDataSetChanged();
                    }
                    if (!isFinishing() && progress != null && progress.isShowing())
                        progress.dismiss();

                } else {
                    viewUtils.showDialog("Error",
                            "Unable to compress image. Please try Again");
                }
            } else {
                viewUtils.showDialog("Error",
                        "Unable to compress image. Please try Again");
            }


        }


    }

    public class SaveSubmit extends AsyncTask<Void, Void, Boolean> {

        String fname;
        boolean ftpUploadStatus = false;

        private ProgressDialog mProgress = null;

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                int count = sharedpre.getInt("iCount", 0);

                fname = "jdfjfj";

//				String resolution=sharedpre.getString("Reso", "");
//				String quality=sharedpre.getString("quality", "");

                String resolution = "Original";
                String quality = "Original";

                String a[] = new String[]{"_scanId", "resolution", "quality", "savedServer"};
                String b[] = new String[]{edt_appno.getText().toString(), resolution, quality, "0"};

                db.insert(b, a, "scan");

                String imagePath[] = new String[count];
                String docType[] = new String[count];

                String a1[] = new String[]{"scannedId"};

                Cursor cur = db.fetchone(edt_appno.getText().toString(), "scan", a1, "_scanId");
                int scannedId = 0;
                if (cur != null && cur.getCount() > 0) {
                    cur.moveToFirst();
                    scannedId = cur.getInt(0);
                }

                Calendar c2 = Calendar.getInstance();

                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate2 = null;


                String c[] = new String[]{"scannedId", "imagePath", "savedServer", "saveTime"};

                formattedDate2 = df1.format(c2.getTime());
                for (int i = 1; i <= count; i++) {
                    imagePath[i - 1] = sharedpre.getString("ImgPath_" + i, "");

                    docType[i - 1] = sharedpre.getString("docType_" + i, "");

                    String d[] = new String[]{String.valueOf(scannedId), imagePath[i - 1], "0", formattedDate2};

                    db.insert(d, c, "image");

                }

                String b1[] = new String[]{"scannedId"};

                Cursor fetchCur = db.fetchallSpecify("scan", b1, "_scanId", edt_appno.getText().toString(), null);

                if (fetchCur != null && fetchCur.getCount() > 0) {
                    fetchCur.moveToFirst();
                    String names[] = new String[]{"imagePath", "savedServer"};
                    Cursor fetchimg = db.fetchallSpecify("image", names, "scannedId", String.valueOf(fetchCur.getInt(0)), null);

                    if (fetchimg != null && fetchimg.getCount() > 0) {
                        fetchimg.moveToFirst();
                        int k = 1;
                        do {
                            // if (fetchimg.getInt(1) == 0) {
                            Log.e("scannedID", String.valueOf(fetchCur.getInt(0)));
                            Log.e("ImagePath", String.valueOf(fetchimg.getString(0)));
                            ftpUploadStatus = ImageUtils.getCompressedImagePath(fetchimg.getString(0), edt_appno.getText().toString(), resolution, quality, docType[k - 1], "0");
                            if (fname != null) {

                                String v[] = new String[]{"1"};
                                String n[] = new String[]{"savedServer"};

                                boolean up = db.update("scannedId", v, n, "image", String.valueOf(fetchCur.getInt(0)));
                                Log.e("up", String.valueOf(up));
                            }
                            //}
                            k++;
                        } while (fetchimg.moveToNext());
                    }


                }
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
                db.close();
                Toast.makeText(ActivityPreview.this, "Images Uploaded Successfully.", Toast.LENGTH_LONG).show();
                saveuser.clear();
                saveuser.commit();
                //startActivity(new Intent(ActivityPreview.this, PreviewActivity.class));
                //finish();

                startActivity(new Intent(ActivityPreview.this, ActivityPreview.class));
                finish();

                //File sudesi = new File(Environment.getExternalStorageDirectory() + File.separator + "sudesi" + File.separator);
                //File sudesiMedium = new File(Environment.getExternalStorageDirectory() + File.separator + "sudesiMedium" + File.separator);

                //deleteRecursive(sudesi);
                //deleteRecursive(sudesiMedium);
                dropTables();
            } else {
                viewUtils.showDialog("Error.", "Unable to upload images, please try again..");
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress = new ProgressDialog(ActivityPreview.this);
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

        void dropTables() {
            db.open();

            db.delete("claim_auto");
            db.delete("scan");
            db.delete("image");

            db.close();

        }
    }

    public void kycDialog() {
        //AlertDialog.Builder dialog = new AlertDialog.Builder(_context);

        //final Dialog dialog = new Dialog(this);
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.menu);
        dialog.setCancelable(false);
        //TextView dialog_title = (TextView) dialog.findViewById(R.id.dialog_title);
        //TextView dialog_message = (TextView) dialog.findViewById(R.id.dialog_message);
        Button mobiKYCButton = (Button) dialog.findViewById(R.id.mobiKYCButton);
        Button sycButton = (Button) dialog.findViewById(R.id.sycButton);

        //dialog_title.setText("");

        mobiKYCButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Preventing multiple clicks, using threshold of 1 second
                startActivity(new Intent(ActivityPreview.this, ActivityPreview.class));
                finish();
                dialog.dismiss();

            }
        });
        sycButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Preventing multiple clicks, using threshold of 1 second
                startActivity(new Intent(ActivityPreview.this, MainActivity.class));
                finish();
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    public void backDialog(String title, String message) {
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Go to SYC ?");
        //builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                startActivity(new Intent(ActivityPreview.this, MainActivity.class));
                finish();
                dialog.dismiss();
            }
        });


        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(ActivityPreview.this, LoginActivity.class));
                finish();
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

}
