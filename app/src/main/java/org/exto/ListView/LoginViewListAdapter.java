package org.exto.ListView;

import java.util.List;

import org.exto.Login.LoginMainActivity;
import org.exto.Model.ViewModel;
import org.exto.mobescan.kyc.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class LoginViewListAdapter extends ArrayAdapter<ViewModel> {

  private final List<ViewModel> list;
  private final Activity context;
  
  public LoginViewListAdapter(Activity context, List<ViewModel> list) {
    super(context, R.layout.loginviewlayout, list);
    this.context = context;
    this.list = list;
  }

static class ViewHolder {
    protected TextView sName;
    protected TextView sTime;
    protected Button but1;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = null;
    if (convertView == null) {
      LayoutInflater inflator = context.getLayoutInflater();
      view = inflator.inflate(R.layout.loginviewlayout, null);
      final ViewHolder viewHolder = new ViewHolder();
      viewHolder.sName = (TextView) view.findViewById(R.id.textView2);
      viewHolder.sTime = (TextView) view.findViewById(R.id.textView3);
      viewHolder.but1 = (Button) view.findViewById(R.id.button1);
     
      viewHolder.but1
          .setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				  SharedPreferences sharedpre=null;
		         	 
		         	 SharedPreferences.Editor saveuser=null;
		         	 
		         	 sharedpre = context.getSharedPreferences("Sudesi",0);
		         	 
		         	saveuser = sharedpre.edit();
		         	
					saveuser.putInt("LoginCheckedBox",0);
	
					saveuser.putString("LoginscannedId", "");
					
						saveuser.putString("LoginCategory","");
						saveuser.putString("LoginSerialNo","");
						saveuser.putString("LoginEmpCode","");
						saveuser.putString("LoginModel","");
						
						saveuser.putFloat("LoginLon",0.0f);
						saveuser.putFloat("LoginLat",0.0f);
	
						int lcount = sharedpre.getInt("LoginiCount",0);
						
						  for(int j=1;j<=lcount;j++)
		      		    {		      		    
							  saveuser.putString("LoginImgPath_"+String.valueOf(lcount),"");
		      		    }
						  
						saveuser.putInt("LoginiCount",0);
						
						saveuser.putString("LoginSavedServer","");
					
		         	saveuser.putString("LoginSelectedName", viewHolder.sName.getText().toString());
		         	
		         	saveuser.commit();
		      
		         	((Activity)context).finish();	
		         	
				getContext().startActivity(new Intent(getContext(),LoginMainActivity.class));
				
			}
		});
		
      view.setTag(viewHolder);
      viewHolder.but1.setTag(list.get(position));
      
    } else {
      view = convertView;
      ((ViewHolder) view.getTag()).but1.setTag(list.get(position));
    }
    ViewHolder holder = (ViewHolder) view.getTag();
    holder.sName.setText(list.get(position).getsName());
    holder.sTime.setText(list.get(position).getsTime());
    
    if(list.get(position).getSaved()==1)
    {
    	holder.sName.setTextColor(Color.GREEN);
    	holder.sTime.setTextColor(Color.GREEN);
    }
    
    if(list.get(position).getSaved()==2)
    {
    	holder.sName.setTextColor(Color.parseColor("#FF9900"));
    	holder.sTime.setTextColor(Color.parseColor("#FF9900"));
    }
    
    if(list.get(position).getSaved()==3)
    {
    	holder.sName.setTextColor(Color.RED);
    	holder.sTime.setTextColor(Color.RED);
    }
      
    return view;
  } 
} 