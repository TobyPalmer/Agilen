package com.example.timemanagement;

import android.app.Activity;
import android.app.AlertDialog;
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
	        	builder.show();
	        }
	    });
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.backup, menu);
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

}