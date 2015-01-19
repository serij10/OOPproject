package ie.gmit.computing;

import ie.gmit.computing.model.Node;

import java.io.IOException;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DecisionTreeActivity extends ActionBarActivity {
	Node currentNode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_decision_tree);
		
		Node root = MyApp.helper.getRoot();		
		nextLevel(root);		
	}
	
public void nextLevel(Node node){
		
		final LinearLayout btnLayout = (LinearLayout) findViewById(R.id.btnLayout);
		if(node!=null && node.children()!=null){
			for(final Node node1:node.children()){
				
				if(node1!=null){
					Button btnAdd = new Button(DecisionTreeActivity.this); 
					btnAdd.setText(node1.getName());
					btnLayout.addView(btnAdd);
					btnAdd.setOnClickListener(new View.OnClickListener() {
	     	            public void onClick(View view) {
	     	            	if (node1.isLeaf() == true) {
	     	            		btnLayout.removeAllViews();

								ImageView imageview = new ImageView(DecisionTreeActivity.this);
								imageview.setImageBitmap(MyApp.helper.findPic(node1.getName()+".png"));
								int dimens = 420;
								float density = DecisionTreeActivity.this.getResources().getDisplayMetrics().density;
								int finalDimens = (int)(dimens * density);

								LinearLayout.LayoutParams imgvwDimens = 
								        new LinearLayout.LayoutParams(finalDimens, finalDimens);
								imageview.setLayoutParams(imgvwDimens);
								btnLayout.addView(imageview);
								Button b1 =new Button(DecisionTreeActivity.this);
								b1.setText("Select");
								b1.setOnClickListener(new View.OnClickListener() {
				     	            public void onClick(View view) {
				     	            	 
				     	            	
				     	           }
				     	        });
								btnLayout.addView(b1);

								
								
								//layout1.setContentView(R.layout.custom);
								currentNode=node1;
								;
							} else {

								btnLayout.removeAllViews();
								nextLevel(node1);

							}
	     	            	
	     	            }
	     	        });
						
						
				}
			}
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.decision_tree, menu);
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
