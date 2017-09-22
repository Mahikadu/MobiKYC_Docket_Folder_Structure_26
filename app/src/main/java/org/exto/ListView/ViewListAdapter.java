package org.exto.ListView;

import java.util.List;

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

public class ViewListAdapter extends ArrayAdapter<ViewModel> {

  private final List<ViewModel> list;
  private final Activity context;
  
  public ViewListAdapter(Activity context, List<ViewModel> list) {
    super(context, R.layout.viewlayout, list);
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
      view = inflator.inflate(R.layout.viewlayout, null);
      final ViewHolder viewHolder = new ViewHolder();
      viewHolder.sName = (TextView) view.findViewById(R.id.textView2);
      viewHolder.sTime = (TextView) view.findViewById(R.id.textView3);
      viewHolder.but1 = (Button) view.findViewById(R.id.button1);
           
    /*  viewHolder.but1
          .setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			
				  SharedPreferences sharedpre=null;
		         	 
		         	 SharedPreferences.Editor saveuser=null;
		         	 
		         	 sharedpre = context.getSharedPreferences("Sudesi",0);
		         	 
		         	saveuser = sharedpre.edit();
		         	
		         	//
		         	saveuser.putInt("iCount",0);
					
					saveuser.putInt("CheckedBox",0);
	
					saveuser.putString("scannedId", "");
					///

		         	saveuser.putString("SelectedName", viewHolder.sName.getText().toString());
		         	
		         	saveuser.commit();
		      
		         	((Activity)context).finish();
		        
				getContext().startActivity(new Intent(getContext(),MainActivity.class));
				
			}
		});*/
		
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