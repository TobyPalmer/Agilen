package com.example.timemanagement;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.example.timemanagement.customadapters.CustomListAdapter2;
import com.example.timemanagement.model.Notification;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemClickListener;

public class NotificationActivity extends MainActivity implements OnItemClickListener{
	
	private int nrOfNotifications=0;
	private Notification notification = new Notification();
	private ListView listView;
	private List<Notification> notificationList;
	private Boolean[] clicked;
	private Button delete, save;
	private CustomListAdapter2 mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifications);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
        listView = (ListView) findViewById(R.id.notificationListView);
        listView.setOnItemClickListener(this);
        
		Typeface font_neo = Typeface.createFromAsset(getAssets(), "neosanslight.ttf");
    	
        delete = (Button)findViewById(R.id.deleteNotification);
        save = (Button)findViewById(R.id.addNotification);
    	delete.setTypeface(font_neo);
    	save.setTypeface(font_neo);
    
        
		printList();
		
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
	
	public void printList(){
		
		notificationList = MainActivity.db.getAllNotifications();
		clicked = new Boolean[notificationList.size()];
		
    	for(int i=0;i<notificationList.size();i++)
    		clicked[i] = false;
    	
    	String[] orderList = new String[notificationList.size()];
    	
    	for(int i=0;i<orderList.length;i++)
    		orderList[i] = notificationList.get(i).toString();
		
		if(!notificationList.isEmpty()){
						
	        //ArrayAdapter<Notification> adapter = new ArrayAdapter<Notification>(this, android.R.layout.simple_list_item_1, android.R.id.text1,  notificationList);
	        mAdapter = new CustomListAdapter2(this,R.layout.listrow2, orderList , "neosanslight.ttf");
	        
	        listView.setAdapter(mAdapter);
			
		}
	  
	}

	public void setNotification(Calendar time){
		
		notification.setTime(time.getTimeInMillis());
		
		MainActivity.db.addNotification(notification);
		
		Intent mServiceIntent = new Intent(NotificationActivity.this, NotificationHandler.class);
		
		mServiceIntent.putExtra("title", "Chronox");
		mServiceIntent.putExtra("text", "Glöm inte att tidsrapportera!");
		
		PendingIntent pendingIntent = PendingIntent.getService(NotificationActivity.this, notification.getID(), mServiceIntent, 0);
		
		AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);

		
	}
	
	public void notificationPopup(View v){
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle("Notification Time");
		 
		 LayoutInflater inflater = getLayoutInflater();

		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		    builder.setView(inflater.inflate(R.layout.activity_notificationpopup, null));
 
		 
		 builder.setPositiveButton("Lägg till", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button
	        	Calendar cal = Calendar.getInstance();
	        	   
	        	Dialog d = (Dialog) dialog;

				DatePicker date = (DatePicker)d.findViewById(R.id.notificationDate);
	           	TimePicker time = (TimePicker)d.findViewById(R.id.notificationTime);
	           	CheckBox spareTime = (CheckBox)d.findViewById(R.id.spareTime);
	           	
	           	notification.setSpareTime(spareTime.isChecked());
	           		
	           	cal.set(date.getYear(), date.getMonth(), date.getDayOfMonth(), time.getCurrentHour(), time.getCurrentMinute());
	           	
	           	//Call to the function that creates the notification with the selected date/time
	           	setNotification(cal);
	           	
	           	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	           	Date da = new Date(cal.getTimeInMillis());
	           	String datum = sdf.format(da);
	           	/*
	           	TextView t = (TextView)findViewById(R.id.txtView);
	           	t.append(datum + "\n");
	           	*/
	     
	           	newPopUp("Notifikation tillagd på följande tid:", datum);
	           	
	           	printList();
	           	
	           }
	       });
		 
		 builder.setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User cancelled the dialog
	           }
	       });

		 AlertDialog dialog = builder.create();
		 
		 dialog.show(); 
	 }
	
   public void newPopUp(String title, String message){
  	 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle(title);
		 builder.setMessage(message);
		 
		 builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button
	           }
	     });
		
		 AlertDialog dialog = builder.create();
		 
		 dialog.show();
  	
  }
   
	@Override
	public void onItemClick(AdapterView<?> l, View v, int pos, long id) {
		// TODO Auto-generated method stub

		for(int i = 0; i<notificationList.size(); i++){
			if(pos==i){
				if(clicked[i]==false){
					v.setBackgroundColor(getResources().getColor(R.color.red));
					clicked[i]=true;
				}
				else{
					v.setBackgroundColor(Color.TRANSPARENT);
					clicked[i]=false;
				}
			}
		}
	}
   
   public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	    	case R.id.noRepeat:
	    		if(checked)
	    			notification.setNoRepeat();
	    		break;	
	        case R.id.everyDay:
	            if (checked){
	        		notification.setEveryMonth(false);
	        		notification.setEveryWeek(false);
	        		notification.setEveryDay(true);
	        	}
	            break;
	        case R.id.everyWeek:
	            if (checked){
	        		notification.setEveryMonth(false);
	        		notification.setEveryWeek(true);
	        		notification.setEveryDay(false);
	        	}
	            break;
	        case R.id.everyMonth:
	        	if(checked){
	        		notification.setEveryMonth(true);
	        		notification.setEveryWeek(false);
	        		notification.setEveryDay(false);
	        	}
	        	break;
	           
	    }
	}
   
   public void deleteNotifications(View v){
		
		if(nothingClicked()){
			
			newPopUp("Fel","Du måste markera de notifikationer du vill ta bort.");
			
			return;
		}
		
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);

		 builder.setTitle("Vill du verkligen ta bort?");

		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		 
		 builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button
		    		for(int index = 0; index<notificationList.size(); index++){
		    			if(clicked[index]==true){
		    				MainActivity.db.deleteNotification(notificationList.get(index));
		    			}
		    		}
		    		onCreate(null);

	           }	
	       });
		 
		 builder.setNegativeButton("Nej", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User cancelled the dialog

	           }
	       });

		 AlertDialog dialog = builder.create();
		 
		 dialog.show(); 
		
	}
   
	public boolean nothingClicked(){
		for(int i=0;i<clicked.length;i++){
			if(clicked[i]==true)
				return false;
		}
		return true;
	}
	
   



}