package com.example.timemanagement;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class BackupActivity extends MainActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backup);
		// Show the Up button in the action bar.
		setupActionBar();

		Button exportAllAsJSONButton = (Button)findViewById(R.id.exportAllAsJSONButton);
		exportAllAsJSONButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	String message = MainActivity.db.exportJSON();
	        	AlertDialog.Builder builder = new AlertDialog.Builder(BackupActivity.this);
	        	builder.setCancelable(true);
	        	builder.setTitle("Exportera som JSON");
	        	builder.setMessage(message);
	        	
	        	
	        	// User clicked OK button
	   		 builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	   	           public void onClick(DialogInterface dialog, int id) {
	   	        	   
	   	           }
	   	     });
	        	
	        	
	        	builder.show();
	        }
	    });
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(false);
		}
	}

}