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
			"08:00 - 09:37, SAAB ",
			"09:37 - 10:00, Frukost",
			"10:00 - 10:17, Scrummöte",
			"10:17 - 11:50, Arbete", 
			"11:50 - 13:17, Lunch",
			"13:17 - 14:02, Internt möte", 
			"14:02 - 16:23, SAAB",
			"16:23 - 17:05, Arbete"
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


