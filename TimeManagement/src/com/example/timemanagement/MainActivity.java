package com.example.timemanagement;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.timemanagement.sqlite.SQLiteMethods;

public class MainActivity extends Activity {
	
	private Button login;
	private TextView user, pass;
	
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
    	
        user = (TextView)findViewById(R.id.userName);
    	user.setTypeface(font);
        pass = (TextView)findViewById(R.id.passWord);
    	pass.setTypeface(font);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	if(item.getItemId()==R.id.menu_checkview)
    	{
    		listActivity();
    		return true;
    	}else if(item.getItemId()==R.id.menu_statistics)
    	{
    		statisticsActivity();
    		return true;
    	}else if(item.getItemId()==R.id.menu_timestamp)
    	{
    		timestampActivity();
    		return true;
    	}else if(item.getItemId()==R.id.menu_schedule)
    	{
    		scheduleActivity();
    		return true;
    	}else if(item.getItemId()==R.id.menu_settings)
    	{
    		settingActivity();
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
    
    //Statistik view skapas?
    public void newStatisticsActivity(View view){
        Intent intent = new Intent(this, StatisticsActivity.class);
        
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

}
