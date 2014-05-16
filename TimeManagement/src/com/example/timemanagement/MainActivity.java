package com.example.timemanagement;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.timemanagement.sqlite.SQLiteMethods;
import com.example.timemanagement.model.*;

public class MainActivity extends Activity {
	
	public static final long DEFAULT_WORKDAY = 8 * 60 * 60 * 1000; 
	public static final String DEFAULT_USER_NAME = "TEST";
	private Button login;
	private TextView user, pass;
	private List<Order> orderList = new ArrayList<Order>();
	
	public static UserDetails currentUser;	
	public static SQLiteMethods db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
   
        db = new SQLiteMethods(this);
        
        //add neo sans font to button and text fields
        Typeface font = Typeface.createFromAsset(getAssets(), "neosanslight.ttf");
    	
        login = (Button)findViewById(R.id.timestampButton);
    	login.setTypeface(font);
    	
		login.setOnClickListener(new View.OnClickListener() {					
					/**
					 * Click listener for the login button that creates a user if needed and starts TimeStampActivity
					 * If nothing was entered a standard user is created
					 */
				@Override
					public void onClick(View v) {
				        if(user.getText().length() > 0){
				        	currentUser = db.getUserDetails(user.getText().toString());
				            if(currentUser == null){
				            	currentUser = new UserDetails(user.getText().toString(), DEFAULT_WORKDAY);
				            	db.addUser(currentUser);
				            }
				        }
				        else{
				        	currentUser = new UserDetails(DEFAULT_USER_NAME, DEFAULT_WORKDAY);
				        }				
				        Log.e("USERNAME: ", currentUser.getUsername());
				        Log.e("WORKDAY: ", Long.toString(currentUser.getWorkday()));
				        
				        Intent intent = new Intent(getApplicationContext(), TimestampActivity.class);				        
				        startActivity(intent);	
					}
				});
    	
        user = (TextView)findViewById(R.id.userName);
    	user.setTypeface(font);
        pass = (TextView)findViewById(R.id.passWord);

    	pass.setTypeface(font);
    	
    	Order standardOrder = new Order("0", "Saknar ordernr.", 0);

       	// Get all orders
    	orderList = db.getAllOrders();
    	
    	if(!orderList.contains(standardOrder)){
          	orderList.add(standardOrder);
          	db.addOrder(standardOrder);
      	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getItemId() == R.id.menu_checkview) {
    		listActivity();
    		return true;
    	}
    	else if(item.getItemId() == R.id.menu_statistics) {
    		statisticsActivity();
    		return true;
    	}
    	else if(item.getItemId() == R.id.menu_timestamp) {
    		timestampActivity();
    		return true;
    	}
    	/*else if(item.getItemId() == R.id.menu_schedule) {
    		scheduleActivity();
    		return true;
    	}*/
    	else if(item.getItemId() == R.id.menu_settings) {
    		settingActivity();
    		return true;
    	}
    	else if(item.getItemId() == R.id.menu_backup) {
    		backupActivity();
    		return true;
    	}
    	return false;
    }
    
    public void timestampActivity(View view){    	       
        Intent intent = new Intent(this, TimestampActivity.class);
        
        startActivity(intent);
    }
    
    public void newOrderActivity(View view){
        Intent intent = new Intent(this, NewOrderActivity.class);
        
        startActivity(intent);
    }

    // Making ListView
    public void listActivity(){
        Intent intent = new Intent(this, ListActivity.class);

        startActivity(intent);
    }
    
    // Making Statistics view
    public void statisticsActivity(){
        Intent intent = new Intent(this, StatisticsActivity.class);
        
        startActivity(intent);
    }
    
    // Making Schedule view
    public void scheduleActivity(){
        Intent intent = new Intent(this, ScheduleActivity.class);
        
        startActivity(intent);
    }
    
    // Making Timestamp view
    public void timestampActivity(){
    	Intent intent = new Intent(this, TimestampActivity.class); 
        
        startActivity(intent);
    }
    
    // Making Settings view
    public void settingActivity(){
    	
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
    
    public void handleOrdersActivity(){
    	
        Intent intent = new Intent(this, HandleOrdersActivity.class);
        startActivity(intent);
    }
    
    public void userDetailActivity(){
    	
        Intent intent = new Intent(this, UserDetailActivity.class);
        startActivity(intent);
    }
    
        // Making Settings view
    public void backupActivity(){
        Intent intent = new Intent(this, BackupActivity.class);
        startActivity(intent);
    }
    
    public void notificationActivity(){
    	
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }
    
}
