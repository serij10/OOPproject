package ie.gmit.computing;
import android.graphics.Bitmap;

public class Pair {
	public Pair( String picName,Bitmap bitmap) {
		super();
		this.bitmap = bitmap;
		this.picName = picName;
	}
	private Bitmap bitmap;
	private String picName;
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getPicName() {
		return picName;
	}
	public void setPicName(String picName) {
		this.picName = picName;
	}
	
	
	

}
