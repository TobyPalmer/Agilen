package com.example.timemanagement;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.example.timemanagement.model.Notification;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationHandler extends IntentService
{

	public NotificationHandler() {
		super("NotificationHandler");
		// TODO Auto-generated constructor stub

	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		
		List<Notification> list = MainActivity.db.getAllNotifications();
		Notification notification = new Notification();
		Boolean bool=false;
	
		
		for(int i=0;i<list.size();i++){
			long diff = Math.abs(list.get(i).getTime() - System.currentTimeMillis());
			
			if( diff<1000 ){
				notification = list.get(i);
				
				if(notification.spareTime())
					bool = true;
				else if(!weekend(System.currentTimeMillis())){
					bool = true;
				}
					
						
			}
			
		}
		
		if(bool){
			
			Log.e("BOOL","BRAA");
			
			// TODO Auto-generated method stub
			NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(this);
			mBuilder.setSmallIcon(R.drawable.ic_launcher);
			mBuilder.setContentTitle(arg0.getStringExtra("title"));
			mBuilder.setContentText(arg0.getStringExtra("text"));

			Intent notificationIntent = new Intent(this, ListActivity.class);
			mBuilder.setContentIntent(PendingIntent.getActivity(this, notification.getID(), notificationIntent, 0));
			mBuilder.setAutoCancel(true);
	
			
	
			NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			nm.notify(0, mBuilder.build());
			
			Calendar cal = Calendar.getInstance();
			
			if(notification.everyDay()){
				cal.add(Calendar.HOUR, 24);
				setNotification(cal,notification.getID());
				notification.setTime(cal.getTimeInMillis());
				MainActivity.db.putNotification(notification);
			}
			else if(notification.everyWeek()){
				cal.add(Calendar.HOUR, 24*7);
				setNotification(cal,notification.getID());
				notification.setTime(cal.getTimeInMillis());
				MainActivity.db.putNotification(notification);
			}
			else if(notification.everyMonth()){
				cal.add(Calendar.MONTH, 1);
				setNotification(cal,notification.getID());
				notification.setTime(cal.getTimeInMillis());
				MainActivity.db.putNotification(notification);
			}
			
		}
		else{
			Calendar cal = Calendar.getInstance();
			notification.setTime(modifyTimeWeekend(cal.getTimeInMillis()));
			setNotification(cal,notification.getID());
		}
		
	


	}
	
	private boolean weekend(long t) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(t);
		
		SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
		String weekDay = dayFormat.format(cal.getTime());
		
		if(weekDay.equals("Sunday"))
			return true;
		
		if(weekDay.equals("Saturday"))
			return true;
		
		return false;
	}
	
	private long modifyTimeWeekend(long t) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(t);
		
		SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
		String weekDay = dayFormat.format(cal.getTime());
		
		if(weekDay.equals("Sunday"))
			cal.add(Calendar.HOUR, 24);
		
		if(weekDay.equals("Saturday"))
			cal.add(Calendar.HOUR, 48);

		return cal.getTimeInMillis();
	}

	public void setNotification(Calendar time, int id){
		
		Intent mServiceIntent = new Intent(NotificationHandler.this, NotificationHandler.class);
		
		mServiceIntent.putExtra("title", "Chronox");
		mServiceIntent.putExtra("text", "Du har glömt att tidsrapportera!");
		
		PendingIntent pendingIntent = PendingIntent.getService(NotificationHandler.this, id, mServiceIntent, 0);
		
		AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
		//nrOfNotifications++;
		
	}

}


