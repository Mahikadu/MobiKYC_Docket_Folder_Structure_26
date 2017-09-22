package org.exto.Model;

public class ImageModel {

		private String path;
		private int selectedImg,spinSelect;
		  private boolean selected;

		  public ImageModel(String path,int selectedImg,int spinSelect) {
		    this.path = path;
		    this.selectedImg = selectedImg;
		    this.spinSelect=spinSelect;
		    selected = false;
		  }

		  public String getPath() {
		    return path;
		  }

		  public void setPath(String path) {
		    this.path = path;
		  }
		  
		  public int getImg() {
			    return selectedImg;
			  }

			  public void setImg(int selectedImg) {
			    this.selectedImg = selectedImg;
			  }


		  public boolean isSelected() {
		    return selected;
		  }

		  public void setSelected(boolean selected) {
		    this.selected = selected;
		  }
		  
		  public int getSelected() {
		        return spinSelect;
		    }

		    public void setSelected(int spinSelect) {
		        this.spinSelect = spinSelect;
		    }


}