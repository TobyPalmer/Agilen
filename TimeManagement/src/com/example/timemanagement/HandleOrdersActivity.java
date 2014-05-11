
package com.example.timemanagement;

import java.util.ArrayList;
import java.util.List;

import com.example.timemanagement.model.Order;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class HandleOrdersActivity extends MainActivity implements OnItemClickListener{
	
	int nrOfNotifications=0;
	private List<Order> list = new ArrayList<Order>();
	private String orderList[];
	private Boolean[] clicked;
			
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_removeorderpopup);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		list = MainActivity.db.getAllOrders();
		
		orderList = new String[list.size()];
		clicked = new Boolean[list.size()];
		
		
		for(int i=0; i<list.size(); i++){
			clicked[i]=false;
			orderList[i] = list.get(i).toString();
		}
		
        ListView lv = (ListView) findViewById(R.id.orderListView);
        lv.setOnItemClickListener(this);
        
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,  orderList);
        lv.setAdapter(adapter);
		
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void deleteOrders(View v){
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);

		 builder.setTitle("Vill du verkligen ta bort?");

		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		 
		 builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
	           @Override
			public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button
		    		for(int index = 0; index<list.size(); index++){
		    			if(clicked[index]==true){
		    				MainActivity.db.deleteOrder(list.get(index));
		    			}
		    		}
		    		onCreate(null);

	           }	
	       });
		 
		 builder.setNegativeButton("Nej", new DialogInterface.OnClickListener() {
	           @Override
			public void onClick(DialogInterface dialog, int id) {
	               // User cancelled the dialog

	           }
	       });

		 AlertDialog dialog = builder.create();
		 
		 dialog.show(); 
		
	}

	@Override
	public void onItemClick(AdapterView<?> l, View v, int pos, long id) {
		// TODO Auto-generated method stub
		for(int i = 0; i<orderList.length; i++){
			if(id==i){
				if(clicked[i]==false){
					v.setBackgroundColor(Color.RED);
					clicked[i]=true;
				}
				else{
					v.setBackgroundColor(Color.TRANSPARENT);
					clicked[i]=false;
				}
			}
		}
	}
}