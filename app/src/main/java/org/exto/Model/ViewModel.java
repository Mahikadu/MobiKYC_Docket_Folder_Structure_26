package org.exto.Model;

public class ViewModel {

		private String sName,sTime;
		private int saved;
	
		public ViewModel(String sName,String sTime,int saved) {
		    this.sName = sName;
		    this.sTime = sTime;
		    this.saved = saved;
		  }

		
		  public String getsName() {
			    return sName;
			  }
			  
			  public void setsName(String sName) {
				    this.sName = sName;
			 }
			  
			  public String getsTime() {
				    return sTime;
				  }
				  
				  public void setsTime(String sTime) {
					    this.sTime = sTime;
				 }
		  
				  public int getSaved() {
					    return saved;
					  }
					  
					  public void setSaved(int saved) {
						    this.saved = saved;
					 }
		
}