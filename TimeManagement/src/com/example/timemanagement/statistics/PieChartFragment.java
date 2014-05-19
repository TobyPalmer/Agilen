package com.example.timemanagement.statistics;

import java.util.ArrayList;
import java.util.List;

import com.example.timemanagement.R;
import com.example.timemanagement.StatisticsActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PieChartFragment extends Fragment {
	
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_statistics_piechart_fragment, container, false);
		updateTextFields(rootView);
		updatePieChart(rootView);		
        return rootView;
    }
	/**
	 * Update text fields when fragment is created
	 * @param rootView
	 */
	private void updateTextFields(View rootView){
		
		Typeface font2 = Typeface.createFromAsset(((StatisticsActivity)getActivity()).getAssets(), "neosanslight.ttf");
    	Typeface font_for_icon = Typeface.createFromAsset(((StatisticsActivity)getActivity()).getAssets(), "fontawesome-webfont.ttf");    	
    	
    	TextView totalTimeSquare = (TextView)rootView.findViewById(R.id.totalTimeSquare);
    	TextView flexTimeSquare = (TextView)rootView.findViewById(R.id.flexTimeSquare);    	
    	
    	totalTimeSquare.setTypeface(font_for_icon);
    	flexTimeSquare.setTypeface(font_for_icon);
		
		TextView total, direct, indirect, flex;	
				
		total = (TextView)rootView.findViewById(R.id.totalTime);
		direct = (TextView)rootView.findViewById(R.id.directTime);
		indirect = (TextView)rootView.findViewById(R.id.indirectTime);
		flex = (TextView)rootView.findViewById(R.id.flexTime);
		
		total.setTypeface(font2);
		direct.setTypeface(font2);
		indirect.setTypeface(font2);
		flex.setTypeface(font2);
		
		long directTime, indirectTime, flexTime;
		directTime = ((StatisticsActivity)getActivity()).directTime;
		indirectTime = ((StatisticsActivity)getActivity()).indirectTime;
		flexTime = ((StatisticsActivity)getActivity()).flexTime;
		
		String t,d,i,f;
		t = ((StatisticsActivity)getActivity()).TOTAL_TIME + new timeHM(directTime+indirectTime).toString();
		d = ((StatisticsActivity)getActivity()).DIRECT_TIME + new timeHM(directTime).toString() + " " + ((StatisticsActivity)getActivity()).df.format(procent(directTime + indirectTime, directTime)) + " %";
		i = ((StatisticsActivity)getActivity()).INDIRECT_TIME + new timeHM(indirectTime).toString() + " " + ((StatisticsActivity)getActivity()).df.format(procent(directTime + indirectTime, indirectTime)) + " %";
		if(flexTime >= 0){
			f = ((StatisticsActivity)getActivity()).FLEX_TIME + new timeHM(Math.abs(flexTime)).toString() + ((StatisticsActivity)getActivity()).FLEX_TIME_PLUS;
		}
		else{
			f = ((StatisticsActivity)getActivity()).FLEX_TIME + new timeHM(Math.abs(flexTime)).toString() + ((StatisticsActivity)getActivity()).FLEX_TIME_MINUS;
		}
				
		total.setText(t);
		direct.setText(d);
		indirect.setText(i);
		flex.setText(f);
	}
	
	/**
	 * Update PieChart when fragment is created
	 * @param rootView
	 */
	private void updatePieChart(View rootView){
		List<PieDetailsItem> piedata = new ArrayList<PieDetailsItem>(0);		
		PieDetailsItem item;
		  int maxCount = 0;
		  int itemCount = 0;
		  timeHM dT,iT;
		  dT = new timeHM(((StatisticsActivity)getActivity()).directTime);
		  iT = new timeHM(((StatisticsActivity)getActivity()).indirectTime);
		  int items[] = { dT.getTotalMinutes(), iT.getTotalMinutes() };
		  int colors[] = { ((StatisticsActivity)getActivity()).DIRECT_TIME_PIE_CHART_COLOR, ((StatisticsActivity)getActivity()).INDIRECT_TIME_PIE_CHART_COLOR };		  
		  String itemslabel[] = { ((StatisticsActivity)getActivity()).DIRECT_TIME_PIE_LABEL, ((StatisticsActivity)getActivity()).INDIRECT_TIME_PIE_LABEL };
		  for (int i = 0; i < items.length; i++) {
			   itemCount = items[i];
			   item = new PieDetailsItem();
			   item.count = itemCount;
			   item.label = itemslabel[i];
			   item.color = colors[i];
			   piedata.add(item);
			   maxCount = maxCount + itemCount;
		  }
		  int size = ((StatisticsActivity)getActivity()).PIE_CHART_SIZE;
		  int BgColor = ((StatisticsActivity)getActivity()).PIE_CHART_COLOR;
		  Bitmap mBaggroundImage = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		  View_PieChart piechart = new View_PieChart(getActivity().getApplicationContext());
		  piechart.setLayoutParams(new LayoutParams(size, size));
		  piechart.setGeometry(size, size, 2, 2, 2, 2, 2130837504);
		  piechart.setSkinparams(BgColor);
		  piechart.setData(piedata, maxCount);
		  piechart.invalidate();
		  piechart.draw(new Canvas(mBaggroundImage));
		  piechart = null;
		  ImageView mImageView = new ImageView(getActivity().getApplicationContext());
		  mImageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
		    LayoutParams.WRAP_CONTENT));
		  mImageView.setBackgroundColor(BgColor);
		  mImageView.setImageBitmap(mBaggroundImage);
		  LinearLayout finalLayout = (LinearLayout) rootView.findViewById(R.id.pie_container);
		  finalLayout.removeAllViews();
		  finalLayout.addView(mImageView);
}
	
	double procent(long total, long part){
		double t = total;
		double p = part;
		
		if(total != 0){
			Log.e("RÄknade ", Long.toString(total));
			return ((p/t) * 100);
		}
		return 0;		
	}
}
