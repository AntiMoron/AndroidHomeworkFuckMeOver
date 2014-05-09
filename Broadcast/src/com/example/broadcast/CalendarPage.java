package com.example.broadcast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CalendarPage extends Activity {

	private RelativeLayout layout;
	private Button [] calendarBtns;
	private Button nextMonth;
	private Button preMonth;
	private TextView topBar;
	private int nowweek,nowday,nowmonth,nowyear;
	private int stPos;
	private Calendar c;
	private int monthday[] = {0,31,28,31,30,31,30,31,31,30,31,30,31};
	private Drawable drawable;
	private Resources res;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenwidth = dm.widthPixels;
		int screenheight = dm.heightPixels;
		layout = new RelativeLayout(this);
		calendarBtns = new Button[49];
		topBar = new TextView(this);
		int btnHeight = (int)(screenheight * 0.7)/7;
		for(int i=0;i<7;i++)
		{
			for(int j=0;j<7;j++)
			{
				calendarBtns[i * 7 + j] = new Button(CalendarPage.this);
				calendarBtns[i * 7 + j].setId(2000 + i * 7 + j);
				calendarBtns[i * 7 + j].setText("");
				calendarBtns[i * 7 + j].setBackgroundColor(Color.parseColor("#FFFFFF"));
				calendarBtns[i * 7 + j].getBackground().setAlpha(60);
				RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams ((screenwidth-50)/7,btnHeight);
				btParams.leftMargin = (screenwidth/7) * j;
				btParams.topMargin = 50 + (btnHeight+2) * i;
				layout.addView(calendarBtns[i * 7 + j],btParams);
			}
		}
		
		RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams ((screenwidth-50)/2,btnHeight);
		RelativeLayout.LayoutParams btParams2 = new RelativeLayout.LayoutParams ((screenwidth-50)/2,btnHeight);
		RelativeLayout.LayoutParams btParams3 = new RelativeLayout.LayoutParams (screenwidth-50,btnHeight);
		preMonth = new Button(this);
		preMonth.setId(3000);
		preMonth.setText("上个月");
		btParams.leftMargin = 25;
		btParams.topMargin = (int)(screenheight * 0.75) + 20;
		layout.addView(preMonth,btParams);
		nextMonth = new Button(this);
		nextMonth.setId(3001);
		nextMonth.setText("下个月");
		btParams2.leftMargin = 25+(screenwidth/2);
		btParams2.topMargin = (int)(screenheight * 0.75) + 20;
		layout.addView(nextMonth,btParams2);
		topBar.setId(3002);
		layout.addView(topBar,btParams3);

		setContentView(layout);
		res = getResources();
		drawable = res.getDrawable(R.drawable.calendarback);
		this.getWindow().setBackgroundDrawable(drawable);
		
		calendarBtns[0].setText("日");
		calendarBtns[1].setText("一");
		calendarBtns[2].setText("二");
		calendarBtns[3].setText("三");
		calendarBtns[4].setText("四");
		calendarBtns[5].setText("五");
		calendarBtns[6].setText("六");

		TimeZone MyTimezone = TimeZone.getDefault();
		c = new GregorianCalendar(MyTimezone);
		nowyear = c.get(Calendar.YEAR);
		nowmonth = c.get(Calendar.MONTH);
		nowday = c.get(Calendar.DAY_OF_MONTH);
		nowweek = c.get(Calendar.DAY_OF_WEEK);
		c.set(nowyear, nowmonth, 1);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int week = c.get(Calendar.DAY_OF_WEEK) - 1;//当月1号周几
		int day = c.get(Calendar.DAY_OF_MONTH);
		topBar.setText("年:"+year +" 月:"+ (month+1));
		if(isLeapYear(nowyear))
			monthday[2] = 29;
		else monthday[2] = 28;
		for(int j = 0; j < monthday[nowmonth + 1];j++)
		{
			calendarBtns[week + j + 7].setText("" + (j + 1));
		}
		
		for(int j=7;j<49;j++)
		{
			String _vtext = calendarBtns[j].getText().toString().trim();
			if(_vtext.equals(""))
			{
				stPos = 0;
				calendarBtns[j].setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
				});
			}
			else
			{
				if(stPos == 0)
					stPos = j;
				final int _vmon = nowmonth + 1;
				final int _vday = j - stPos + 1;
				calendarBtns[j].setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(CalendarPage.this,DatePage.class);
						Bundle data = new Bundle();
						data.putString("date", ""+nowyear + "/"+_vmon + "/" + _vday);
						intent.putExtra("data1", data);
						startActivity(intent);
					}
				});
				
			}
		}
		preMonth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				nowmonth--;
				if(nowmonth<0)
				{
					nowmonth = 11;
					nowyear --;
				}
				if(isLeapYear(nowyear))
					monthday[2] = 29;
				else monthday[2] = 28;
				nowday = 1;
				c.set(nowyear,nowmonth,1);
				nowweek = c.get(Calendar.DAY_OF_WEEK) - 1;
				for(int j=7;j<49;j++)
					calendarBtns[j].setText("");
				for(int j = 0; j < monthday[nowmonth + 1];j++)
				{
					calendarBtns[nowweek + j + 7].setText("" + (j + 1));
				}
				for(int j=7;j<49;j++)
				{
					String _vtext = calendarBtns[j].getText().toString().trim();
					if(_vtext.equals(""))
					{
						stPos = 0;
						calendarBtns[j].setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								
							}
						});
					}
					else
					{
						if(stPos == 0)
							stPos = j;
						final int _vday = j - stPos + 1;
						final int _vmon = nowmonth + 1;
						calendarBtns[j].setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(CalendarPage.this,DatePage.class);
								Bundle data = new Bundle();
								data.putString("date", ""+nowyear + "/"+_vmon + "/" + _vday);
								intent.putExtra("data1", data);
								startActivity(intent);
							}
						});
						
					}
				}
				CalendarPage.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						topBar.setText("年:"+nowyear +" 月:"+ (nowmonth+1));						
					}
				});
			}
		});
		nextMonth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nowmonth ++;
				if(nowmonth > 11)
				{
					nowmonth = 1;
					nowyear ++;
				}
				if(isLeapYear(nowyear))
					monthday[2] = 29;
				else monthday[2] = 28;
				nowday = 1;
				c.set(nowyear,nowmonth,1);
				nowweek = c.get(Calendar.DAY_OF_WEEK) - 1;
				for(int j=7;j<49;j++)
					calendarBtns[j].setText("");
				for(int j = 0; j < monthday[nowmonth + 1];j++)
				{
					calendarBtns[nowweek + j + 7].setText("" + (j + 1));
				}
				for(int j=7;j<49;j++)
				{
					String _vtext = calendarBtns[j].getText().toString().trim();
					if(_vtext.equals(""))
					{
						stPos = 0;
						calendarBtns[j].setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								
							}
						});
					}
					else
					{
						if(stPos == 0)
							stPos = j;
						final int _vday = j - stPos + 1;
						final int _vmon = nowmonth + 1;
						calendarBtns[j].setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(CalendarPage.this,DatePage.class);
								Bundle data = new Bundle();
								data.putString("date", ""+nowyear + "/"+ _vmon + "/" + _vday);
								intent.putExtra("data1", data);
								startActivity(intent);
							}
						});
						
					}
				}
				CalendarPage.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						topBar.setText("年:"+nowyear +" 月:"+ (nowmonth+1));							
					}
				});
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calendar_page, menu);
		return true;
	}
	
	private boolean isLeapYear(int _y)
	{
		if(_y % 400 == 0) return true;
		if(_y % 100 == 0) return false;
		if(_y % 4 == 0) return true;
		return false;
	}
}
