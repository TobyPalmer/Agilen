package com.example.timemanagement;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.timemanagement.customadapters.CustomListAdapter1;
import com.example.timemanagement.customadapters.CustomListAdapter2;

public class SettingActivity extends MainActivity implements OnItemClickListener{
	
	private CustomListAdapter2 listAdapter;
	private  ListView listView;
	private ArrayList<String> settingsList;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		
        listView = (ListView) findViewById(R.id.listView1);
        listView.setOnItemClickListener(this);
        
        settingsList = new ArrayList<String>();
        
        
        //Add strings in the array to get a clickable listItem
        //If you want tomake something happen when you click the new Item
        //Make a new funktion with what should happen and make a call to it
        //in the onItemClick function in this class

        //String array[] = {"Hantera notifikationer", "Hantera ordrar"};
        String s1 = "Hantera notifikationer";
        String s2 = "Hantera ordrar"; 
        settingsList.add(s1);
        settingsList.add(s2);
    	listAdapter = new CustomListAdapter2(this,R.layout.listrow2, settingsList, "neosanslight.ttf");
    	listView.setAdapter(listAdapter);	
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(false);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> l, View v, int pos, long id) {
		// TODO Auto-generated method stub
		if(pos==0){
			//notificationPopup();
			notificationActivity();
		}
		
		if(pos==1){
			handleOrdersActivity();
		}
	}

}
