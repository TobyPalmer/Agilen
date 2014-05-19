package com.example.timemanagement.statistics;

import com.example.timemanagement.R;
import com.example.timemanagement.StatisticsActivity;
import com.example.timemanagement.customadapters.CustomListAdapterStatistics;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.Fragment;

/**
 * Order List Statistics View. 
 * @author Marcus
 *
 */
public class OrderListStatisticsFragment extends Fragment {
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_statistics_orderlist_fragment, container, false);
        updateOrderLists(rootView);        
        return rootView;
    }
	
	
	/**
	 * Initializes the Lists when the fragment is first created. Afterwards the updates are made in the parent Activity
	 * @param rootView
	 */
	private void updateOrderLists(View rootView){
		
		Typeface font2 = Typeface.createFromAsset(((StatisticsActivity)getActivity()).getAssets(), "neosanslight.ttf");
    	
    	TextView directText = (TextView)rootView.findViewById(R.id.statsListDirectText);
    	TextView indirectText = (TextView)rootView.findViewById(R.id.statsListIndirectText);
    	
    	directText.setTypeface(font2);
		indirectText.setTypeface(font2);
        
		ListView directList = (ListView)rootView.findViewById(R.id.statistics_direct_time_list);
		ListView indirectList = (ListView)rootView.findViewById(R.id.statistics_indirect_time_list);
		
		CustomListAdapterStatistics directAdapter = new CustomListAdapterStatistics(getActivity().getApplicationContext(), R.layout.order_list_item, ((StatisticsActivity)getActivity()).orderTimeDetailsDirect);        
        directList.setAdapter(directAdapter);
        
        CustomListAdapterStatistics indirectAdapter = new CustomListAdapterStatistics(getActivity().getApplicationContext(), R.layout.order_list_item, ((StatisticsActivity)getActivity()).orderTimeDetailsIndirect);        
        indirectList.setAdapter(indirectAdapter);
	}
}
