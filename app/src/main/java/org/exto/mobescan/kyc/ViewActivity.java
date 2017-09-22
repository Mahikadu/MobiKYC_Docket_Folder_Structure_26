package org.exto.mobescan.kyc;

import java.util.ArrayList;
import java.util.List;

import org.exto.ListView.ViewListAdapter;
import org.exto.Model.ViewModel;
import org.exto.dbConfig.Dbcon;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ViewActivity extends ListActivity {
	
	private Dbcon db=null;
	
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_view);
		
		listView = (ListView) findViewById(android.R.id.list);
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
	    WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
	    wm.getDefaultDisplay().getMetrics(displayMetrics);

	    int screenHeight = displayMetrics.heightPixels-120;
	    
	    listView.setMinimumHeight(screenHeight);
		
		db = new Dbcon(this);
		
		ArrayAdapter<ViewModel> adapter = new ViewListAdapter(this, getModel());
				
		    setListAdapter(adapter);
		    
		    findViewById(R.id.button_new).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							
							finish();
							startActivity(new Intent(ViewActivity.this,HomeActivity.class));
							
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
	
 private List<ViewModel> getModel() {
		 
		 List<ViewModel> list = new ArrayList<ViewModel>();
		 
		 try{
			 
			 db.open();
						 
			 String a[]= new String[] {"saveName","saveTime","savedServer","scannedId"};
				
			Cursor data=db.fetchallOrder("scan", a,"saveTime DESC");
			
			data.moveToFirst();
				
		    while(data.isAfterLast() == false)
		    {
		    	String b[]= new String[] {"savedServer"};
				
		    	 Cursor data1=db.fetchone(String.valueOf(data.getInt(3)),"image",b,"scannedId");
		    	 
		    	 int total = data1.getCount();
		    	 
		    	 int saved=0;
		    	 
		    	 int flag=0;
		    	 
		    	 data1.moveToFirst();
		    	 
		    	 while(data1.isAfterLast() == false)
				    {		    		 
		    		 
		    		 if(String.valueOf(data1.getInt(0)).equalsIgnoreCase("1"))
		    		 {
		    			 saved++;
		    		 }
		    		 
		    		 data1.moveToNext();
		    		 
				    }
		    	 
		    	 if(saved==0)
		    	 {
		    		 flag=3;
		    	 }
		    	 
		    	 else if(saved==total)
		    	 {
		    		 flag=1;
		    	 }
		    	 
		    	 else if(saved<total&&saved>0)
		    	 {
		    		 flag=2;
		    	 }
		    	
		    list.add(get(data.getString(0),data.getString(1),flag));	    
		    
		    data.moveToNext();
		   }
		    
		    db.close();
		 }
		 catch(Exception e)
		 {
			 list=null;
			 e.printStackTrace();
		 }
		    // Initially select one of the items
		    //list.get(1).setSelected(true);
		    return list;
		  }

		  private ViewModel get(String n,String t,int s) {
		    return new ViewModel(n,t,s);
		  }
}
