/* OLD
package com.example.timemanagement;

import android.app.Activity;
import android.os.Bundle;

public class ListActivity extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
	}

}
*/


package com.example.timemanagement;

import com.example.timemanagement.R;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ListActivity extends MainActivity
{
	ListView myList;
	
	String[] listContent = 
		{
			"Anna",
			"vill",
			"att",
			"det", 
			"här",
			"ska",
			"fungera"
		};
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		myList = (ListView)findViewById(R.id.list);
		
		ArrayAdapter<String> adapter = 
				new ArrayAdapter<String>
					(this, 
					android.R.layout.simple_list_item_1,
					listContent);
		
		myList.setAdapter(adapter);
		
		/*
		TextView arrow = (TextView)findViewById(R.id.arrow);
		Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
		arrow.setTypeface(font);
		*/	
	}
}


