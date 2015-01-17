package ie.gmit.computing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import ie.gmit.computing.model.Node;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	
	int n = 10;
	// Node root = Root.getInstance();
	Node root = null;
	boolean deletePressed = false;
	Node parentNode = null;
	Node node = null;
	boolean leafLevel = false;
	Bitmap bitmap;
	String path;
	List<Node> tree1 = new ArrayList<Node>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (MyApp.helper.getNode() != null) {
			root = MyApp.helper.getRoot();
			Node newNode=MyApp.helper.getNode();			
			parentNode = root.recursiveSearch(MyApp.helper.getParent(),root);
			newNode.setParent(parentNode);
			bitmap= MyApp.helper.getBitmap();
			
			Pair pair = new Pair(newNode.getName()+".png",bitmap);
			MyApp.helper.bitmapList.add(pair);
			parentNode.addChild(MyApp.helper.getNode());
			MyApp.helper.setParent(null);
			MyApp.helper.setNode(null);
			showLevel(parentNode);

		} else {
			root = MyApp.helper.getRoot();
			parentNode=MyApp.helper.getRoot();
			showLevel(parentNode);
		}
        
        Button addButton = (Button) findViewById(R.id.btnAdd);
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (leafLevel == true && parentNode != root) {
					showDialogue();
				} else {
					addNode();
				}
			}
		});

		
		Button deleteButton = (Button) findViewById(R.id.btnDelete);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				Toast.makeText(MainActivity.this, "Select the Node to Delete",
						Toast.LENGTH_SHORT).show();

				deletePressed = true;
			}
		});
		
		Button saveButton = (Button) findViewById(R.id.btnSave);
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					serializeTree(MyApp.helper.getRoot(),
							"someFile.ser");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
    }
		
	public void deleteNode(String nodeName) {
		Node node = root.recursiveSearch(nodeName, root);
		Node parent = node.getParent();
		parent.removeChild(node);
		Toast.makeText(MainActivity.this, node.getName() + "was deleted",
				Toast.LENGTH_SHORT).show();
		deletePressed = false;

	}

	public void showDialogue() {
		final Dialog dialog = new Dialog(MainActivity.this);
		dialog.setContentView(R.layout.custom);
		dialog.setTitle("Title...");

		Button btnAdd1 = (Button) dialog.findViewById(R.id.btnAdd1);

		Button btnAdd2 = (Button) dialog.findViewById(R.id.btnAdd2);
		// if button is clicked, close the custom dialog
		btnAdd2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				MyApp.helper.setParent(parentNode.getName());
				Intent intent = new Intent(MainActivity.this, CameraActivity.class);
				// intent.putExtra("nodeName", userInput.getText().toString());
				startActivity(intent);
				dialog.dismiss();
			}
		});
		btnAdd1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				dialog.dismiss();
			}
		});

		dialog.show();
    }
		
		

	public void serializeTree(Node root, String fileName) throws IOException {
		// Base case
		if (MyApp.helper.getRoot() == null)
			return;

		// Else, store current node and recur for its children

		List<Pair> pics = MyApp.helper.bitmapList;
		if (pics != null) {
			for (Pair s : pics){
				path=saveToInternalStorage(s.getBitmap(), s.getPicName());
			}
				
		}

		FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
		ObjectOutputStream os = new ObjectOutputStream(fos);
		os.writeObject(MyApp.helper.getRoot());
		// Store marker at the end of children
		os.close();
		fos.close();

	}
		
		
	public void showLevel(final Node node) {

		final LinearLayout layout1 = (LinearLayout) findViewById(R.id.treeLayout);
		layout1.removeAllViews();
		if (node != null && node.children() != null) {
			for (final Node node1 : node.children()) {

				if (node1 != null) {
					final Button button1 = new Button(MainActivity.this);
					button1.setText(node1.getName());
					layout1.addView(button1);
					button1.setOnClickListener(new View.OnClickListener() {
						public void onClick(View view) {
							if (deletePressed == true) {
								deleteNode(node1.getName());
								ViewGroup layout = (ViewGroup) button1
										.getParent();
								if (null != layout) // for safety only as you
													// are doing onClick
									layout.removeView(button1);
							} else if (node1.isLeaf() == true) {
								layout1.removeAllViews();
								
								ImageView imageview = new ImageView(MainActivity.this);
								Bitmap bitmap=MyApp.helper.findPic(node1.getName()+".png");
								if(bitmap!=null){
									imageview.setImageBitmap(bitmap);
									int dimens = 420;
									float density = MainActivity.this.getResources().getDisplayMetrics().density;
									int finalDimens = (int)(dimens * density);

									LinearLayout.LayoutParams imgvwDimens = 
									        new LinearLayout.LayoutParams(finalDimens, finalDimens);
									imageview.setLayoutParams(imgvwDimens);
									layout1.addView(imageview);
									TextView t= new TextView(MainActivity.this);
									t.setTextSize(42);
									t.setGravity(Gravity.CENTER);
									t.setText(node1.getName());
									layout1.addView(t);						
								}
								;
							} else {

								parentNode = node1;
								showLevel(node1);

							}
						}
					});

				}
			}
		} else {
			leafLevel = true;
			parentNode = node;
			layout1.removeAllViews();

		}

	}
		
	private String saveToInternalStorage(Bitmap bitmapImage, String imageName) {
		ContextWrapper cw = new ContextWrapper(getApplicationContext());
		// path to /data/data/yourapp/app_data/imageDir
		File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
		// Create imageDir
		File mypath = new File(directory, imageName + ".png");

		FileOutputStream fos = null;
		try {

			fos = new FileOutputStream(mypath);

			// Use the compress method on the BitMap object to write image to
			// the OutputStream

			bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return directory.getAbsolutePath();
	}
	
	public void addNode() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("Add Node");

		// Set up the input
		final EditText input = new EditText(MainActivity.this);

		// Specify the type of input expected; this, for example, sets the input
		// as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				final LinearLayout layout1 = (LinearLayout) findViewById(R.id.treeLayout);
				String buttonText;
				final Button btn1 = new Button(MainActivity.this);
				String nodeName = input.getText().toString();
				if (parentNode == null){
					parentNode = root;
				}

				final Node newNode = new Node(nodeName, parentNode);
				parentNode.addChild(newNode);
				
				btn1.setText(nodeName);
				// button1.setId(newNode.hashCode());
				n += 1;
				btn1.setId(n);
				buttonText = btn1.getText().toString();
				btn1.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						Button b = (Button) view;
						String buttonText = b.getText().toString();
						if (deletePressed == true) {
							
							deleteNode(buttonText);
							ViewGroup layout = (ViewGroup) btn1.getParent();
							if (null != layout) // for safety only as you are
												// doing onClick
								layout.removeView(btn1);
						} else {
							
							String s1 = b.getText().toString();

							parentNode = root.recursiveSearch(s1, root);
							Toast.makeText(MainActivity.this,
									parentNode.getName(), Toast.LENGTH_SHORT)
									.show();

							showLevel(newNode);
						}
						// layout1.removeAllViews();

					}
				});
				layout1.addView(btn1);

			}
		});
		builder.setNegativeButton("Cancel",	new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		builder.show();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
