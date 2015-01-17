package ie.gmit.computing;

import ie.gmit.computing.model.Node;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class CameraActivity extends ActionBarActivity {

	private ImageView imageView;
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	String nodeName = null;
	Bitmap bitmap ;
	String parentNode = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		dispatchTakePictureIntent();
		imageView = (ImageView) findViewById(R.id.result);

		Button btnAdd = (Button) findViewById(R.id.add);
		btnAdd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				if (parentNode != null) {

					showDialogue1();

				} else {
					
					Node newNode = new Node(null, null);
					MyApp.helper.setNode(newNode);
					Intent dec = new Intent(CameraActivity.this,DecisionTreeActivity.class);
					startActivity(dec);
				}	
			}
		});

		Button btnDelete = (Button) findViewById(R.id.delete);
		btnDelete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				((ImageView) findViewById(R.id.result)).setImageResource(android.R.color.transparent);

			}
		});

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			imageView.setImageBitmap(imageBitmap);
		}

	}

	static final int REQUEST_TAKE_PHOTO = 1;

	
	public void showDialogue1() {
		AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
		builder.setTitle("Add Leaf Node To Tree");

		// Set up the input
		final EditText input = new EditText(CameraActivity.this);

		// Specify the type of input expected; this, for example, sets the input
		// as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				nodeName = input.getText().toString();
				Node newNode = new Node(nodeName, null);
				newNode.setLeaf(true);
				Bitmap b=bitmap;
				MyApp.helper.setBitmap(b);
				//MyApplication.helper.getMyMap().put(input.getText().toString(), b);
				MyApp.helper.setNode(newNode);
				Intent intent = new Intent(CameraActivity.this, MainActivity.class);
				startActivity(intent);
				

			}
		});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		builder.show();
	}

	public void showDialogue2() {

		Node newNode= new Node("new",null);
		MyApp.helper.setNode(newNode);
		Intent intent = new Intent(CameraActivity.this, DecisionTreeActivity.class);
		startActivity(intent);
	}

	
	 private void dispatchTakePictureIntent() { Intent takePictureIntent = new
	 Intent(MediaStore.ACTION_IMAGE_CAPTURE); if
	  (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	  startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE); } }
	 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
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

	String mCurrentPhotoPath;

	/*private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName,  prefix 
				".jpg",  suffix 
				storageDir  directory 
		);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}*/
}
