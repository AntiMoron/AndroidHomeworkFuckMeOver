package com.example.broadcast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.example.utils.Lunar;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

public class DatePage extends Activity {

	private Calendar c;
	private Lunar lunar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_date_page);
		Intent intent = this.getIntent();
		Bundle data = intent.getBundleExtra("data1");

		TextView tv = (TextView)findViewById(R.id.textView1);
		TextView tv2 = (TextView)findViewById(R.id.textView2);
		TimeZone MyTimezone = TimeZone.getDefault();
		GregorianCalendar tcal = new GregorianCalendar(); 
		c = new GregorianCalendar(MyTimezone);
		Date dt = c.getTime();
		tcal.setTime(dt);
		tv.setText(data.getString("date"));
		String[] date = data.getString("date").split("/");
		c.set(Integer.parseInt(date[0]),Integer.parseInt(date[1]) - 1,Integer.parseInt(date[2]));
		double daycount = (c.getTimeInMillis()-tcal.getTimeInMillis())/(1000*3600*24);
		TextView tv3 = (TextView)findViewById(R.id.textView3);
		tv3.setText(String.format("距离今天相距%d天",(int)daycount));
		lunar = new Lunar(c);

		tv.setText(data.getString("date"));
		
		String lunarStr = "";  
		
		lunarStr=lunar.animalsYear()+"年(";  
		lunarStr +=lunar.cyclical()+"年)\r\n";  
		lunarStr +=lunar.toString();
		tv2.setText(lunarStr);	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.date_page, menu);
		return true;
	}

}
