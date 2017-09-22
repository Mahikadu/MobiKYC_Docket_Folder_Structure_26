package org.exto.ListView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.exto.Login.ViewUtils;
import org.exto.mobescan.kyc.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ImageListAdapter extends ArrayAdapter {


    private ArrayList<HashMap<String, String>> dataoriginal;
    private ArrayList<String> animalList;
    HashMap<String, String> map;
    //private ArrayList<HashMap<String, ArrayList>> datacompress;
    private Context context1;
    private static LayoutInflater inflater = null;
    private ViewUtils viewUtils;

    public ImageListAdapter(Context context, int textViewResourceId, ArrayList<HashMap<String, String>> d) {
        super(context, textViewResourceId, d);
        dataoriginal = d;
        context1 = context;
    }
    @Override
    public int getCount() {
        return super.getCount();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.newrowlayout, null);
        ImageView imgView = (ImageView) v.findViewById(R.id.imgViewOriginal);
        ImageView imgViewcompress = (ImageView) v.findViewById(R.id.imgViewCompressed);


        map = dataoriginal.get(position);

        if (map != null) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            final Bitmap b = BitmapFactory.decodeFile(map.get("image_path"), options);
            imgView.setImageBitmap(b);
            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCustomDialog(b);
                }
            });


            BitmapFactory.Options options1 = new BitmapFactory.Options();
            options1.inSampleSize = 8;
            final Bitmap b1 = BitmapFactory.decodeFile(map.get("compimage_path"), options1);
            imgViewcompress.setImageBitmap(b1);
            imgViewcompress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCustomDialog(b1);
                }
            });

        }

        return v;
    }

    private void showCustomDialog(Bitmap bitmap) {

        final AlertDialog.Builder DialogMaster = new AlertDialog.Builder(context1);

        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogViewMaster = li.inflate(R.layout.image_dailog, null);
        DialogMaster.setView(dialogViewMaster);

        final AlertDialog showMaster = DialogMaster.show();

        Button btnDismissMaster = (Button) showMaster.findViewById(R.id.buttonClose);
        ImageView image = (ImageView) showMaster.findViewById(R.id.imageviewDialog);

        try {
            image.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnDismissMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMaster.dismiss();
            }
        });
        showMaster.setCancelable(false);

    }
    }
