package ie.gmit.computing;
import ie.gmit.computing.model.Node;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Bitmap;

public class HelperActivity  {

	private Node node;
	private String parent;
	private Node root;
	List<Pair> bitmapList = new ArrayList<Pair>();
	
	
	

	public Bitmap findPic(String nodeName){
		for (Pair s : bitmapList)
		    if (s.getPicName().equalsIgnoreCase(nodeName)){
		    	return s.getBitmap();
		    }
		return null;
		      
	}
	 public String findName(String nodeName) {
		 for (Pair s : bitmapList)
			    if (s.getPicName().equalsIgnoreCase(nodeName)){
			    	return s.getPicName();
			    }
			return "null";
	}

	private Bitmap bitmap;
	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	
    
}

