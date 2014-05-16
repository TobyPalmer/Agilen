package com.example.timemanagement.customadapters;

import java.util.ArrayList;
import java.util.List;

import com.example.timemanagement.R;
import com.example.timemanagement.statistics.OrderTimeDetails;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



/**
 * List Adapter that is used for the Order List Statistics View. This one handles a more complex output in each
 * row as defined by resID, or in this case order_list_item
 * @author Marcus
 *
 */
public class CustomListAdapterStatistics extends ArrayAdapter<OrderTimeDetails>{
	
	private final Context context;
	private final ArrayList<OrderTimeDetails> itemsList;
	
	public CustomListAdapterStatistics(Context context, int resID, ArrayList<OrderTimeDetails> items){
		super(context, resID, items);				
		this.context = context;
		this.itemsList = items;
				
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		
		//1. Create inflater to access textviews
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		//2. Get rowView from inflater.
		
		View rowView = inflater.inflate(R.layout.order_list_item,  parent, false);
		
		//3. Get the textviews
		TextView orderNr = (TextView)rowView.findViewById(R.id.statistics_order_nr);
		TextView orderName = (TextView)rowView.findViewById(R.id.statistics_order_name);
		TextView orderTime = (TextView)rowView.findViewById(R.id.statistics_order_time);
		
		//4. Set the text for the textViews
		if(rowView != null){
			orderNr.setText(itemsList.get(position).getOrderNr());
			orderName.setText(itemsList.get(position).getOrderName());
			orderTime.setText(itemsList.get(position).getTotalTime());
		}
		
		//5. Return rowView
		
		return rowView;
		
		
	}
}