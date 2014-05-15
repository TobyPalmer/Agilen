
package com.example.timemanagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.timemanagement.customadapters.CustomListAdapter2;
import com.example.timemanagement.model.Order;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

public class HandleOrdersActivity extends MainActivity implements OnItemClickListener{
	
	int nrOfNotifications=0;
	private List<Order> list = new ArrayList<Order>();
	private String orderList[];
	private Boolean[] clicked;
	private Button delete, save;
	private CustomListAdapter2 mAdapter;
			
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_removeorderpopup);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		Typeface font_neo = Typeface.createFromAsset(getAssets(), "neosanslight.ttf");
    	
        delete = (Button)findViewById(R.id.deleteButton);
        save = (Button)findViewById(R.id.addNewOrderButton);
    	delete.setTypeface(font_neo);
    	save.setTypeface(font_neo);
		
		printList();
		
	}
	
	public void printList(){
		list = MainActivity.db.getAllOrders();
		
		orderList = new String[list.size()];
		clicked = new Boolean[list.size()];
		
		
		for(int i=0; i<list.size(); i++){
			clicked[i]=false;
			orderList[i] = list.get(i).toString();
		}
		
        ListView lv = (ListView) findViewById(R.id.orderListView);
        lv.setOnItemClickListener(this);
        
        
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,  orderList);
        mAdapter = new CustomListAdapter2(this,R.layout.listrow2, orderList, "neosanslight.ttf");
        lv.setAdapter(mAdapter);
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
	
	public void addNewOrder(View v){
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle("Ny order");
		 
		 LayoutInflater inflater = getLayoutInflater();

		 // Inflate and set the layout for the addNewOrderdialog
		 // Pass null as the parent view because its going in the dialog layout
		 builder.setView(inflater.inflate(R.layout.activity_neworderpopup, null));
		 
		 builder.setPositiveButton("Lägg till", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int id) {
				 // User clicked OK button
	        	 Dialog d = (Dialog) dialog;

	        	 EditText orderName = (EditText)d.findViewById(R.id.orderNamePop);
	        	 EditText orderNumber = (EditText)d.findViewById(R.id.orderNumberPop);
	        	 CheckBox orderDirectWork = (CheckBox)d.findViewById(R.id.orderDirectWorkPop);
	        	 String stringOrderName = orderName.getText().toString();
	        	 String stringOrderNumber = orderNumber.getText().toString();
	        	 int integerOrderDirectWork = 0; 
	        	 if(orderDirectWork.isChecked()){
	        		integerOrderDirectWork = 1;
	        	 }	        	
	        	 if(isInteger(stringOrderNumber)) {
	        		 Order order = new Order(stringOrderNumber, stringOrderName, integerOrderDirectWork);
	        		 if(!list.contains(order)){
	        			 list.add(order);
			        	
	        			 String message = "Du har lagt till en order!";
	        			 newPopUp("Order tillagd", message);
	        			
	        			 //Save to dB
	        			 MainActivity.db.addOrder(order); 
	        			 
	        			 printList();
	        			 
	        		 }
	        		 else{
	        			 newPopUp("Error!","'" + order + "' finns redan!");
	        		 }
	        	 }
	        	 else{
	        		 newPopUp("Error!","'" + stringOrderNumber + "' är inte ett giltigt nummer!");
	        	 }
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
	
	public boolean nothingClicked(){
		for(int i=0;i<clicked.length;i++){
			if(clicked[i]==true)
				return false;
		}
		return true;
	}
	
	public void deleteOrders(View v){
		
		if(nothingClicked()){
			
			newPopUp("Fel","Du måste markera de ordrar du vill ta bort.");
			
			return;
		}
		
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);

		 builder.setTitle("Vill du verkligen ta bort?");

		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		 
		 builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
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
	
	public static boolean isInteger(String s) {
	        try { 
	            Integer.parseInt(s); 
	        } catch(NumberFormatException e) { 
	            return false; 
	        }
	        // only got here if we didn't return false
	        return true;
	    }
}