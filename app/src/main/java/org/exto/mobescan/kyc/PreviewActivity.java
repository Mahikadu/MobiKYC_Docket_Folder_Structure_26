package org.exto.mobescan.kyc;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.exto.ListView.ListAdapter;
import org.exto.Model.ImageModel;
import org.exto.dbConfig.Dbcon;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PreviewActivity extends ListActivity {

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

    private Button b1,  b3;
    ImageView b2;

    EditText edt_appno;

    SaveSubmit save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_preview);

        sharedpre = getSharedPreferences(
                "Sudesi", MODE_PRIVATE);

        welcome = (TextView) findViewById(R.id.TextView01);

        listView = (ListView) findViewById(android.R.id.list);

        b1 = (Button) findViewById(R.id.button_new);

        b2 = (ImageView) findViewById(R.id.preview_button);

        b3 = (Button) findViewById(R.id.buttonNext);

        edt_appno = (EditText) findViewById(R.id.edt_appno);

        scannedId = sharedpre.getString("scannedId", "");

        String SelectedName = sharedpre.getString("SelectedName", "");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        int screenHeight = displayMetrics.heightPixels - 120;

        listView.setMinimumHeight(screenHeight);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        db = new Dbcon(this);
        db.open();

        String number = db.fetchVal();
        if (number != null) {
            db.insert(new String[]{String.valueOf(Integer.parseInt(number) + 1)}, new String[]{"claimId"}, "claim_auto");
            number = "Docket_Number" + db.fetchVal();
        } else {
            number = "1000";
            db.insert(new String[]{String.valueOf(Integer.parseInt(number) + 1)}, new String[]{"claimId"}, "claim_auto");
            number = "Docket_Number" + db.fetchVal();
        }

        Log.e("number", number);
        edt_appno.setText(number);

        if (!scannedId.equalsIgnoreCase("")) {
            welcome.setText("Preview - " + SelectedName);

            b1.setBackgroundResource(R.drawable.lo1);

            b2.setBackgroundResource(R.drawable.pluso1);

            b3.setBackgroundResource(R.drawable.ro1);
        }

        if (!scannedId.equalsIgnoreCase("") && sharedpre.getInt("iCount", 0) == 0) {

            db.open();

            String a[] = new String[]{"imagePath", "savedServer", "saveTime"};

            Cursor data = db.fetchallSpecify("image", a, "scannedId", scannedId, "imageId ASC");

            data.moveToFirst();

            saveuser = sharedpre.edit();

            int count2 = 0;

            while (data.isAfterLast() == false) {
                if (data.getInt(1) != 1) {
                    count2++;

                    saveuser.putString("ImgPath_" + String.valueOf(count2), data.getString(0));
                }

                data.moveToNext();
            }

            saveuser.putInt("iCount", count2);

            saveuser.commit();

            db.close();

        }


        adapter = new ListAdapter(this,
                getModel());
        setListAdapter(adapter);


        findViewById(R.id.button_exit).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(PreviewActivity.this, LoginActivity.class));
            }
        });


        findViewById(R.id.buttonNext).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int count = sharedpre.getInt("iCount", 0);

                        int img = 0, doc = 0;

                        String pathLocal = "", doctype = "";

                        for (int j = 1; j <= count; j++) {
                            pathLocal = sharedpre.getString("ImgPath_" + String.valueOf(j), "");
                            doctype = sharedpre.getString("docType_" + String.valueOf(j), "");
                            Log.e("doctype", doctype);
                            if (!pathLocal.equalsIgnoreCase("")) {
                                img++;
                            }
                            if (doctype.equalsIgnoreCase("--Select--")) {
                                doc++;
                            }
                        }
                        Log.e("doc", String.valueOf(doc));
                        if (img == 0) {
                            Toast.makeText(PreviewActivity.this, "Take Atleast One Picture", Toast.LENGTH_LONG).show();
                        } else if (doc > 0) {
                            Toast.makeText(PreviewActivity.this, "Select Document Type", Toast.LENGTH_LONG).show();
                        } else {
                            String applNo = edt_appno.getText().toString();
                            if (applNo.equals("") || applNo.contains(" ")) {
                                Toast.makeText(PreviewActivity.this, "Enter Application No.", Toast.LENGTH_LONG).show();
                            } else {
                                saveuser = sharedpre.edit();

                                saveuser.putString("ApplicationNo", applNo);
                                saveuser.commit();

                                save = new SaveSubmit();
                                save.execute();


//									  finish();
//									  startActivity(new Intent(PreviewActivity.this,AdStringoActivity.class));
                            }

                        }
                    }
                });
//			
//			findViewById(R.id.button_new).setOnClickListener(
//					new View.OnClickListener() {
//						@Override
//						public void onClick(View view) {
//							
//							finish();
//							startActivity(new Intent(PreviewActivity.this,MainActivity.class));
//						}
//					});

        findViewById(R.id.preview_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (!edt_appno.getText().toString().equals("")) {
                            openImageIntent();
                        } else {
                            Toast.makeText(PreviewActivity.this, "Enter Application No.", Toast.LENGTH_LONG).show();
                        }


                    }
                });

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        View empty = findViewById(R.id.empty);
        ListView list = (ListView) findViewById(android.R.id.list);
        list.setEmptyView(empty);
    }

    private List<ImageModel> getModel() {

        List<ImageModel> list = new ArrayList<ImageModel>();

        try {

            int count = sharedpre.getInt("iCount", 0);

            File fTemp = null;

            String pathLocal = "";

            int spinCount = 0;

            int localCount = 0;

            for (int j = 0; j <= count; j++) {
                pathLocal = sharedpre.getString("ImgPath_" + String.valueOf(j), "");
                Log.e("pathLocal", pathLocal);

                if (!pathLocal.trim().equalsIgnoreCase("")) {
                    fTemp = new File(pathLocal);
                    if (sharedpre.getString("docType_" + String.valueOf(j), "").equalsIgnoreCase("--Select--")) {
                        spinCount = 0;
                    } else if (sharedpre.getString("docType_" + String.valueOf(j), "").equalsIgnoreCase("Discrepancy Reply Docs")) {
                        spinCount = 1;
                    } else if (sharedpre.getString("docType_" + String.valueOf(j), "").equalsIgnoreCase("NEFT Details / Canceled Cheque")) {
                        spinCount = 2;
                    } else if (sharedpre.getString("docType_" + String.valueOf(j), "").equalsIgnoreCase("Additional Documents")) {
                        spinCount = 3;
                    } else if (sharedpre.getString("docType_" + String.valueOf(j), "").equalsIgnoreCase("Bills")) {
                        spinCount = 4;
                    } else if (sharedpre.getString("docType_" + String.valueOf(j), "").equalsIgnoreCase("Claim Form")) {
                        spinCount = 5;
                    } else if (sharedpre.getString("docType_" + String.valueOf(j), "").equalsIgnoreCase("Discharg Summary")) {
                        spinCount = 6;
                    } else if (sharedpre.getString("docType_" + String.valueOf(j), "").equalsIgnoreCase("Reports")) {
                        spinCount = 7;
                    } else if (sharedpre.getString("docType_" + String.valueOf(j), "").equalsIgnoreCase("Proposal Form")) {
                        spinCount = 8;
                    } else {
                        spinCount = 0;
                    }

                    if (fTemp.exists()) {
                        list.add(get(pathLocal, j, spinCount));

                        localCount++;
                    }
                }

            }

            if (localCount == 0) {
                //list.add(get("No Images",0));
            }

        } catch (Exception e) {
            list = null;
        }

        return list;
    }

    private ImageModel get(String s, int i, int spin) {
        return new ImageModel(s, i, spin);
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    private void openImageIntent() {

        try {
            String app = edt_appno.getText().toString();
            fname = app + "_" + System.currentTimeMillis() + ".jpeg";

            FileOutputStream fos = openFileOutput(fname, Context.MODE_WORLD_WRITEABLE);
            fos.close();

            root = new File(Environment.getExternalStorageDirectory() + File.separator + "sudesiOriginal" + File.separator);
            root.mkdirs();

            root.setWritable(true);

            File f = new File(root, fname);

            outputFileUri = Uri.fromFile(f);

            pathCamera = Environment.getExternalStorageDirectory() + "/sudesiOriginal/" + fname;

            final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

            startActivityForResult(captureIntent, 202);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(PreviewActivity.this, "Something Wrong with The Camera. Try Again!!!", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)

    {

        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RESULT_OK) {

                if (requestCode == 202) {

                    String selectedImageUri = pathCamera;

                    saveuser = sharedpre.edit();

                    int count = sharedpre.getInt("iCount", 0);

                    count++;

                    saveuser.putString("ImgPath_" + String.valueOf(count), selectedImageUri.toString());

                    saveuser.putInt("iCount", count);

                    saveuser.commit();

                    adapter = null;

                    Log.e("selectedImageUri", selectedImageUri);

                    adapter = new ListAdapter(this,getModel());
                    setListAdapter(adapter);

                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(PreviewActivity.this, "Something Wrong with The Camera. Try Again!!!", Toast.LENGTH_LONG).show();
        }
    }

    public class SaveSubmit extends AsyncTask<Void, Void, String> {

        String fname;
        String appNo;
        private ProgressDialog mProgress = new ProgressDialog(PreviewActivity.this);
        ;

        @Override
        protected String doInBackground(Void... params) {
            try {
                int count = sharedpre.getInt("iCount", 0);

                fname = "jdfjfj";
                appNo = edt_appno.getText().toString();
//					String resolution=sharedpre.getString("Reso", "");
//					String quality=sharedpre.getString("quality", "");

                String resolution = "Medium";
                String quality = "Medium";

                String a[] = new String[]{"_scanId", "resolution", "quality", "savedServer"};
                String b[] = new String[]{edt_appno.getText().toString(), resolution, quality, "0"};
                db.open();
                db.insert(b, a, "scan");

                String imagePath[] = new String[count];
                String docType[] = new String[count];

                Log.e("docType.length", String.valueOf(docType.length));
                String a1[] = new String[]{"scannedId"};

                Cursor cur = db.fetchone(appNo, "scan", a1, "_scanId");
                int scannedId = 0;
                if (cur != null && cur.getCount() > 0) {
                    cur.moveToFirst();
                    scannedId = cur.getInt(0);

                }

                Calendar c2 = Calendar.getInstance();

                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate2 = null;


                String c[] = new String[]{"scannedId", "imagePath", "savedServer", "saveTime", "docType"};

                formattedDate2 = df1.format(c2.getTime());

                for (int i = 0; i < count; i++) {
                    imagePath[i] = sharedpre.getString("ImgPath_" + String.valueOf((i + 1)), "");

                    Log.e("imagePath", imagePath[i]);

                    docType[i] = sharedpre.getString("docType_" + String.valueOf((i)), "");

                    Log.e("docType", docType[i]);

                }

                for (int i = 0; i < count; i++) {
                    String d[] = new String[]{String.valueOf(scannedId), imagePath[i], "0", formattedDate2, docType[i]};
                    db.insert(d, c, "image");
                }

//					for(int i=0;i<count;i++)
//					{
//						imagePath[i]=sharedpre.getString("ImgPath_"+(i+1) , "");
//						
//						docType[i]=sharedpre.getString("docType_"+(i+1), "");
//					
//						String d[]=new String[]{String.valueOf(scannedId),imagePath[i],"0",formattedDate2};
//						
//						db.insert(d, c, "image");
//					
//					}

                String b1[] = new String[]{"scannedId"};

                Cursor fetchCur = db.fetchallSpecify("scan", b1, "_scanId", appNo, null);

//					if(fetchCur!=null&& fetchCur.getCount()>0)
//					{
//						fetchCur.moveToFirst();
//						String names[]=new String[]{"imagePath","savedServer","docType"};
//						Cursor fetchimg=db.fetchallSpecify("image", names, "scannedId", String.valueOf(fetchCur.getInt(0)), null);
//						
//						if(fetchimg!=null && fetchimg.getCount()>0)
//						{
//							fetchimg.moveToFirst();
//							int k=0;
//							do
//							{
//								if(fetchimg.getInt(1)==0)
//								{
//									Log.e("scannedID", String.valueOf(fetchCur.getInt(0)));
//									Log.e("ImagePath", String.valueOf(fetchimg.getString(0)));
//									
//									int doc1_count,doc2_count,doc3_count,doc4_count,doc5_count,doc6_count,doc7_count,doc8_count;
//									if(fetchimg.getString(2).equalsIgnoreCase("Discrepancy Reply Docs"))
//									{
//										doc1_count = sharedpre.getInt("doc1_count", 0);
//										doc1_count = doc1_count+1;
//										saveuser.putInt("doc1_count", doc1_count);
//										saveuser.commit();
//										String doc_Code = "Q";
//										fname=ImageUtils.getCompressedImagePath(fetchimg.getString(0), appNo, resolution, quality, doc_Code,doc1_count);
//									}
//									else if(fetchimg.getString(2).equalsIgnoreCase("NEFT Details / Canceled Cheque"))
//									{
//										doc2_count=sharedpre.getInt("doc2_count", 0);
//										doc2_count = doc2_count+1;
//										saveuser.putInt("doc2_count", doc2_count);
//										saveuser.commit();
//										String doc_Code = "H";
//										fname=ImageUtils.getCompressedImagePath(fetchimg.getString(0), appNo, resolution, quality, doc_Code,doc2_count);
//									}
//									else if(fetchimg.getString(2).equalsIgnoreCase("Additional Documents"))
//									{
//										doc3_count=sharedpre.getInt("doc3_count", 0);
//										doc3_count = doc3_count+1;
//										saveuser.putInt("doc3_count", doc3_count);
//										saveuser.commit();
//										String doc_Code = "A";
//										fname=ImageUtils.getCompressedImagePath(fetchimg.getString(0), appNo, resolution, quality, doc_Code,doc3_count);
//									}
//									else if(fetchimg.getString(2).equalsIgnoreCase("Bills"))
//									{
//										doc4_count=sharedpre.getInt("doc4_count", 0);
//										doc4_count = doc4_count+1;
//										saveuser.putInt("doc4_count", doc4_count);
//										saveuser.commit();
//										String doc_Code = "B";
//										fname=ImageUtils.getCompressedImagePath(fetchimg.getString(0), appNo, resolution, quality, doc_Code,doc4_count);
//									}
//									else if(fetchimg.getString(2).equalsIgnoreCase("Claim Form"))
//									{
//										doc5_count=sharedpre.getInt("doc5_count", 0);
//										doc5_count = doc5_count+1;
//										saveuser.putInt("doc5_count", doc5_count);
//										saveuser.commit();
//										String doc_Code = "C";
//										fname=ImageUtils.getCompressedImagePath(fetchimg.getString(0), appNo, resolution, quality, doc_Code,doc5_count);
//									}
//									else if(fetchimg.getString(2).equalsIgnoreCase("Discharg Summary"))
//									{
//										doc6_count=sharedpre.getInt("doc6_count", 0);
//										doc6_count = doc6_count+1;
//										saveuser.putInt("doc6_count", doc6_count);
//										saveuser.commit();
//										String doc_Code = "D";
//										fname=ImageUtils.getCompressedImagePath(fetchimg.getString(0), appNo, resolution, quality, doc_Code,doc6_count);
//									}
//									else if(fetchimg.getString(2).equalsIgnoreCase("Reports"))
//									{
//										doc7_count=sharedpre.getInt("doc7_count", 0);
//										doc7_count = doc7_count+1;
//										saveuser.putInt("doc7_count", doc7_count);
//										saveuser.commit();
//										String doc_Code = "R";
//										fname=ImageUtils.getCompressedImagePath(fetchimg.getString(0), appNo, resolution, quality, doc_Code,doc7_count);
//									}
//									else if(fetchimg.getString(2).equalsIgnoreCase("Proposal Form"))
//									{
//										doc8_count=sharedpre.getInt("doc8_count", 0);
//										doc8_count = doc8_count+1;
//										saveuser.putInt("doc8_count", doc8_count);
//										saveuser.commit();
//										String doc_Code = "P";
//										fname=ImageUtils.getCompressedImagePath(fetchimg.getString(0), appNo, resolution, quality, doc_Code,doc8_count);
//									}
//									
//									
//									
//									if(fname!=null)
//									{
//										
//										String v[]=new String[]{"1"};
//										String n[]=new String[]{"savedServer"};
//										
//										boolean up=db.update("scannedId", v, n, "image",String.valueOf(fetchCur.getInt(0)) );
//										Log.e("up", String.valueOf(up));
//										if(up == true)
//										{
//											File f = new File(fname);
//											if(f.exists())
//											{
//												f.delete();
//											}
//										}
//										
//									}
//									
//								}
//								k++;
//							}while(fetchimg.moveToNext());
//						}
//					}
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return fname;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            mProgress.dismiss();
            if (fname != null) {
//					db.close();
                Toast.makeText(PreviewActivity.this, "Images Saved Successfully", Toast.LENGTH_LONG).show();
                saveuser.clear();
                saveuser.commit();
                finish();
                startActivity(new Intent(PreviewActivity.this, PreviewActivity.class));
            } else {
                Toast.makeText(PreviewActivity.this, "Check Network Connectivity....", Toast.LENGTH_LONG).show();
            }


        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgress.setMessage("Processing...");
            mProgress.show();


        }

    }

}