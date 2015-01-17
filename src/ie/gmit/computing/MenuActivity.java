package ie.gmit.computing;

import ie.gmit.computing.model.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends ActionBarActivity implements LocationListener {
	
	protected TextView userInfo, shipInfo;
	protected TextView mLatitudeText, mLongitudeText, mLastLocation;
	LocationManager mLocationManager;
	private static final int RESULT_SETTINGS = 1;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		try {

			Node root = deSerializeTree("someFile.ser");

			if (root == null) {
				root = new Node("root", null);
				MyApp.helper.setRoot(root);
				root = MyApp.helper.getRoot();
				

			} else if (root != null) {
				//Toast.makeText(context, "root exist", Toast.LENGTH_SHORT).show();
				MyApp.helper.setRoot(root);
				root = MyApp.helper.getRoot();
				

			}

		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

		}
		
		Button takePhoto = (Button) findViewById(R.id.takePhoto);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent cam = new Intent(MenuActivity.this, CameraActivity.class);
            	startActivity(cam);
            	
            	
             //   Toast.makeText(MainActivity.this, "Button clicked index = " + buttonID, Toast.LENGTH_SHORT).show();
            }
        });
        
        Button tree = (Button) findViewById(R.id.editTree);
        tree.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent main = new Intent(MenuActivity.this, MainActivity.class);
            	startActivity(main);
            	
             //   Toast.makeText(MainActivity.this, "Button clicked index = " + buttonID, Toast.LENGTH_SHORT).show();
            }
        });
        
        //imageView = (ImageView) findViewById(R.id.result);
        userInfo = (TextView) findViewById(R.id.userInfo);
        
        mLatitudeText = (TextView) findViewById((R.id.latitude_text));
        mLongitudeText = (TextView) findViewById((R.id.longitude_text));
        
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
            // Do something with the recent location fix
            //  otherwise wait for the update below
        	mLatitudeText.setText(String.valueOf("Latitude: "+location.getLatitude()));
        	mLongitudeText.setText(String.valueOf("Longitude: "+location.getLongitude()));
        }
        else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        
        showUserSettings();
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_SETTINGS) {
        	showUserSettings();
        }
        
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	private List<File> getListFiles(File parentDir) {
		ArrayList<File> inFiles = new ArrayList<File>();
		File[] files = parentDir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				inFiles.add(file);
			} 
		}
		return inFiles;
	}
	public boolean fileExistance(String fname) {
		File file = getBaseContext().getFileStreamPath(fname);
		return file.exists();
	}
	public Node deSerializeTree(String fileName) throws IOException,ClassNotFoundException {
		if (fileExistance(fileName) == true) {
			FileInputStream fis = openFileInput(fileName);
			ObjectInputStream is = new ObjectInputStream(fis);

			Node root = (Node) is.readObject();
			is.close();
			fis.close();
			//Toast.makeText(context, "root exist", Toast.LENGTH_SHORT).show();
			MyApp.helper.setRoot(root);
			// showLevel(root);

			List<Pair> pics = new ArrayList<Pair>() ;
			ContextWrapper cw = new ContextWrapper(getApplicationContext());
			// path to /data/data/yourapp/app_data/imageDir
			File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
			/*if(directory!=null){
				Toast.makeText(MainActivity.this, directory.list().toString(),Toast.LENGTH_SHORT).show();
				
			}*/
			List<File> picFiles = getListFiles(directory);		
			
			for (File f : picFiles) {
				Pair newPair= new Pair(f.getName(),loadImageFromStorage(directory.getAbsolutePath(), f.getName()));
				//Toast.makeText(MainActivity.this, f.getName(),Toast.LENGTH_SHORT).show();
				MyApp.helper.bitmapList.add(newPair);
			}
			/*// lists all the files into an array
			if(pics!=null){
				MyApplication.helper.bitmapList=pics;
			}*/
			//Toast.makeText(this,String.valueOf(MyApplication.helper.bitmapList.size()),Toast.LENGTH_SHORT).show();

			return root;
		}
		return null;

		/*
		 * while(is.readObject()!=null) { root.addChild((Node) is.readObject());
		 * 
		 * }
		 */

	}
	private Bitmap loadImageFromStorage(String path, String picName) {

		try {
			File f = new File(path, picName);
			Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

			return b;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}

	private void showUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
 
        StringBuilder builder = new StringBuilder();
 
        builder.append("Ship : " + sharedPrefs.getString("ship_name", "NULL"));
        builder.append("\nName : " + sharedPrefs.getString("your_name", "NULL"));
 
        //shipInfo.setText(builder.toString());
        userInfo.setText(builder.toString());
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
        if (id == R.id.action_prefs) {
        	Intent pref = new Intent(MenuActivity.this, Preferences.class);
        	startActivity(pref);
            return true;
        }
        if (id == R.id.exit) {
        	finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onLocationChanged(Location location) {
        /*if (location != null) {
        Log.d("LOCATION CHANGED", location.getLatitude() + "");
        Log.d("LOCATION CHANGED", location.getLongitude() + "");
        Toast.makeText(MainActivity.this,
            location.getLatitude() + "" + location.getLongitude(),
            Toast.LENGTH_LONG).show();
        }*/
    	if (location != null) {
            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
            mLocationManager.removeUpdates(this);
            mLatitudeText.setText(String.valueOf("Latitude: "+location.getLatitude()));
        	mLongitudeText.setText(String.valueOf("Longitude: "+location.getLongitude())); 
        }
    	
    	
    }

    @Override
    public void onProviderDisabled(String provider) {
    	
    	Toast.makeText( getApplicationContext(),"Gps Disabled", Toast.LENGTH_SHORT ).show();
    }
    
    @Override
    public void onProviderEnabled(String provider) {
    	
    	Toast.makeText( getApplicationContext(),"Gps Enabled", Toast.LENGTH_SHORT ).show();   
    }
    
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
