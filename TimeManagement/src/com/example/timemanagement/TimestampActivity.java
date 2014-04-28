package com.example.timemanagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.timemanagement.R.color;
import com.example.timemanagement.model.Block;
import com.example.timemanagement.model.Order;

public class TimestampActivity extends MainActivity {

	private List<Block> l = new ArrayList<Block>();
	private int listIndex = 0;
	boolean started = false;
	boolean stopped = true;
	Block b;
	private int row=0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timestamp);
		// Show the Up button in the action bar.
		setupActionBar();
		
		printBlocks();

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
		getMenuInflater().inflate(R.menu.timestamp, menu);
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
	
	public void startTime(View view){
		//String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTimeInMillis());
		//String startTime = mydate.substring(mydate.length()-8, mydate.length()-3);
		
		if(stopped){
			
			long startTime = System.currentTimeMillis();
			
			b = new Block(startTime);
			
			MainActivity.db.addBlock(b);
			
			stopped = false;
			started = true;
			
			printBlocks();
		}
	}
	
	/*
	@SuppressLint("NewApi") public void changeOrder(View view){
		//String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTimeInMillis());
		//String changeTime = mydate.substring(mydate.length()-8, mydate.length()-3);
		
		if(started){
		
			long changeTime = System.currentTimeMillis();
	
			TextView current = (TextView)findViewById(R.id.timestampText);
			
			l.get(listIndex).setStop(changeTime);
			listIndex++;
			Block b = new Block(changeTime);
			l.add(b);
			
			current.setText("");
			for(int i=0; i<l.size();i++){
				current.append(l.get(i).toString());
			}
			
			started = true;
			stopped = false;
		}		
	}
*/
	
	public void stopTime(View view){
		//String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTimeInMillis());
		//String stopTime = mydate.substring(mydate.length()-8, mydate.length()-3);
		
		if(started){
		
			long stopTime = System.currentTimeMillis();		
			
			b.setStop(stopTime);
			
			MainActivity.db.putBlock(b);			
			
			started = false;
			stopped = true;
			
			printBlocks();
		}
	}
	
	@SuppressLint("NewApi") public void printBlocks(){
		
		l = MainActivity.db.getAllBlocks();
		Collections.sort(l);
		
		Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
		
		//TextView current = (TextView)findViewById(R.id.timestampText);
		
	    Display display = getWindowManager().getDefaultDisplay();
	    Point size = new Point();
	    display.getSize(size);
	    int width = size.x;
	    int height = size.y;
		
		/* Find Tablelayout defined in main.xml */
		TableLayout tl = (TableLayout) findViewById(R.id.TableLayout);		
		tl.removeAllViews();
		tl.setPadding(0, 0, 0, 350);
		
		for(int i=0; i<l.size();i++){
			
			String s = "";
			
			int orderId = l.get(i).getOrderID();
			final Block block = l.get(i);
			Order order = MainActivity.db.getOrder(orderId);
			
			if(MainActivity.db.getOrder(orderId)!=null)
				s = block.toStringPublic() + " - <b>" + order.getOrderName() + "</b> - <i>" + block.getComment() + "</i>";
			else
				s = block.toStringPublic() + " - <i>" + block.getComment() + "</i>";
				//s = block.toStringPublic() + " " + MainActivity.db.getOrder(orderId).toString();
			
			
				
			
			/* Create a new row to be added. */
			TableRow tr = new TableRow(this);
			
			tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
			/*Set text*/
			TextView t = new TextView(this);
			t.setText(Html.fromHtml(s));
			
			/*Set time*/
			TextView t2 = new TextView(this);
			t2.setText(block.printTime());
			
			/* Create a Button to be the row-content. */
			Button b = new Button(this);
			b.setTypeface(font);
			b.setBackgroundColor(getResources().getColor(color.editColor));
			b.setText(R.string.edit);
			b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));		
		    b.setOnClickListener(new View.OnClickListener() {

		        public void onClick(View v) {
		            // TODO Auto-generated method stub
		        	Log.w("AgilTag", block.toString());
		        	
		        	Intent i = new Intent(getApplicationContext(), NewOrderActivity.class);
		        	
		        	i.putExtra("Block", block);
		        	i.putExtra("String", "editBlock");
		        	        	
		        	startActivity(i);	
		        }
		    });
		    
			Button c = new Button(this);
			c.setTypeface(font);
			c.setText(R.string.comment);
			c.setBackgroundColor(getResources().getColor(color.commentColor));
			c.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
		    c.setOnClickListener(new View.OnClickListener() {

		        public void onClick(View v) {
		            // TODO Auto-generated method stub
		        	//Log.w("AgilTag", block.toString());
		        	addComment(v, block);
		        	
		        }
		    });
			
			Button d = new Button(this);
	    	d.setTypeface(font);
			d.setText(R.string.trash);
			d.setBackgroundColor(getResources().getColor(color.deleteColor));
			d.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
		    d.setOnClickListener(new View.OnClickListener() {

		        public void onClick(View v) {
		            // TODO Auto-generated method stub
		        	Log.w("AgilTag", block.toString());
		        	deleteBlock(v, block);
		        	
		        }
		    });
			
			/* Add Button to row. */
		    
			tr.addView(t, (int)(width*0.6), 70);
			tr.addView(t2,(int)(width*0.1), 70);
			tr.addView(b, (int)(width*0.1), 70);
			tr.addView(c, (int)(width*0.1), 70);
			tr.addView(d, (int)(width*0.1), 70);
			if(i%2==0)
				tr.setBackgroundColor(getResources().getColor(color.lightGrey));

			/* Add row to TableLayout. */
			//tr.setBackgroundResource(R.drawable.sf_gradient_03);
			tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT));
			
		}	
	}
	
	
	public void addComment(View view, final Block block){
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle("Lägg till kommentar");
		 
		 String s = "";
		 if(block.getOrderID()!=0){
			 Order o = MainActivity.db.getOrder(block.getOrderID());
			 s="Kommentera '<i>" + block.toStringPublic() + ": <b>" + o.getOrderName() + "</b> </i>' :"; 	 
		 } 
		 else
			 s="Kommentera '<i>" + block.toStringPublic() + "</i>' :";
			 
		 builder.setMessage(Html.fromHtml(s));

		 
		 LayoutInflater inflater = getLayoutInflater();

		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		    builder.setView(inflater.inflate(R.layout.activity_addcommentpopup, null));
		    
		   
		 
		 builder.setPositiveButton("Lägg till", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button
	        	
	        	Dialog d = (Dialog) dialog;

	           	EditText comment = (EditText)d.findViewById(R.id.addCommentPop);
	        	String commentText = comment.getText().toString();
	        	block.setComment(commentText);
	        	MainActivity.db.putBlock(block);
	        	printBlocks();
	        	
	        	
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
	
	public void deleteBlock(View view, final Block block){
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);


		 builder.setTitle("Ta bort");
		 
		 String s = "";
		 if(block.getOrderID()!=0){
			 Order o = MainActivity.db.getOrder(block.getOrderID());
			 s="Vill du ta bort '<i>" + block.toStringPublic() + ": <b>" + o.getOrderName() + "</b> </i> '?"; 	 
		 } 
		 else
			 s="Vill du ta bort '<i>" + block.toStringPublic() + "</i>' ?";
			 
		 builder.setMessage(Html.fromHtml(s));

	
		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		 
		 builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button

		        	MainActivity.db.deleteBlock(block);
		        	printBlocks();
	        	
	        	
	           }	
	       });
		 
		 builder.setNegativeButton("Nej", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User cancelled the dialog
	        	   getWindow().setSoftInputMode(
	        			      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	           }
	       });

		 AlertDialog dialog = builder.create();
		 
		 dialog.show(); 
	 }
	
	/*public void printBlocks(){
		l = MainActivity.db.getAllBlocks();
		
		TextView current = (TextView)findViewById(R.id.timestampText);
		
		current.setText(" ");
		
		for(int i=0; i<l.size();i++){
			
			int orderId = l.get(i).getOrderID();
			
			if(MainActivity.db.getOrder(orderId)==null)
				current.append(l.get(i).toStringPublic() + "\n " );
			else
				current.append(l.get(i).toStringPublic() + " " +MainActivity.db.getOrder(orderId).toString() + "\n ");
			
			createButton();
		}	
	}*/
	
	
}
