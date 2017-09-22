package org.exto.ListView;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.exto.Model.ImageModel;
import org.exto.mobescan.kyc.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

public class LoginListAdapter extends ArrayAdapter<ImageModel> {

  private final List<ImageModel> list;
  private final Activity context;
  
  private ExecutorService service;
  
  public LoginListAdapter(Activity context, List<ImageModel> list) {
    super(context, R.layout.loginrowlayout, list);
    this.context = context;
    this.list = list;
    
    if (this.service != null) {
        this.service.shutdownNow();
    }

    this.service = Executors.newSingleThreadExecutor();
    this.notifyDataSetChanged();
  }

  static class ViewHolder {
    protected ImageView imgView;
    protected CheckBox checkbox;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = null;
    try
    {
    if (convertView == null) {
      LayoutInflater inflator = context.getLayoutInflater();
      view = inflator.inflate(R.layout.loginrowlayout, null);
      final ViewHolder viewHolder = new ViewHolder();
         
      
      viewHolder.imgView = (ImageView) view.findViewById(R.id.imgView);
      viewHolder.checkbox = (CheckBox) view.findViewById(R.id.chkBox);
      
      viewHolder.checkbox
          .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
            	ImageModel element = (ImageModel) viewHolder.checkbox
                  .getTag();
            	
            	
            	            	
           SharedPreferences sharedpre=null;
         	 
         	 SharedPreferences.Editor saveuser=null;
         	 
         	 sharedpre = context.getSharedPreferences("Sudesi",0);

         	int prev = sharedpre.getInt("LoginCheckedBox", 0);
         	 
         	saveuser = sharedpre.edit();   
         	
         	     	
         	        	
              if(isChecked)
              {
            	  if(prev==element.getImg()||prev==0)
            	  {	
            	  saveuser.putInt("LoginCheckedBox",element.getImg());
             	  }
            	  else
            	  {
            		  buttonView.setChecked(false);            		
            	  }
	       
              }
              else
              {
            	  if(prev==element.getImg())
            	  {
            	  saveuser.putInt("LoginCheckedBox",0);
            	}
              }
              
              saveuser.commit();
              
            }
          });
      view.setTag(viewHolder);
      viewHolder.checkbox.setTag(list.get(position));
    } else {
      view = convertView;
      ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
    }
    ViewHolder holder = (ViewHolder) view.getTag();

    holder.checkbox.setChecked(list.get(position).isSelected());
    holder.imgView.setTag(list.get(position).getPath());
    
   
    try {
        this.service.submit(new Job(list.get(position).getPath(),holder.imgView, list.get(position).getPath()));
    } catch (Throwable e) {
    	e.printStackTrace();
    }
    
    }
    catch(Exception e)
    {
    	view=null;    	
    }
    return view;
  }
  
  private final class Job
  implements Runnable
{

private final ImageView logo;
private final String logoTag;
private final String URL;

private Job(String url, ImageView logo, String logoTag)
{
  this.URL = url;
  this.logo = logo;
  this.logoTag = logoTag;
 }

@Override
public void run()
{
	try
	{
	
  Thread t = Thread.currentThread();
  
  t.setPriority(Thread.NORM_PRIORITY - 1); //not to load ui thread much
  
  if (logo.getTag().equals(logoTag)) {
      context.runOnUiThread(new Runnable()
      {
          @Override
          public void run()
          {  
        	    File f =new File(URL);
        	    
        	    Bitmap p = null;
          	    
    	        if(f.exists())
    	        {
    	        	p=BitmapFactory.decodeFile(URL); 
    	        	 
    	        	   // Get current dimensions AND the desired bounding box
    	        	    int width = p.getWidth();
    	        	    int height = p.getHeight();
    	        	    
    	        	    //
    	        	    float density = context.getResources().getDisplayMetrics().density;
    	        	    int bounding =   Math.round((float)150 * density);
    	        
    	        	    // Determine how much to scale: the dimension requiring less scaling is
    	        	    // closer to the its side. This way the image always stays inside your
    	        	    // bounding box AND either x/y axis touches it.  
    	        	    float xScale = ((float) bounding) / width;
    	        	    float yScale = ((float) bounding) / height;
    	        	    float scale = (xScale <= yScale) ? xScale : yScale;
    	        
    	        	    // Create a matrix for the scaling and add the scaling data
    	        	    Matrix matrix = new Matrix();
    	        	    matrix.postScale(scale, scale);

    	        	    // Create a new bitmap and convert it to a format understood by the ImageView 
    	        	    Bitmap scaledBitmap = Bitmap.createBitmap(p, 0, 0, width, height, matrix, true);
    	        	    width = scaledBitmap.getWidth(); // re-use
    	        	    height = scaledBitmap.getHeight(); // re-use
    	            
    	        	    logo.setImageBitmap(scaledBitmap);

    	            }
          }
      	});
}
}
catch(Exception e)
{
	e.printStackTrace();
}
}
}
} 