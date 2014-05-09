package com.example.broadcast;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import LocationUtils.LocationUtils;
import android.R.layout;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.print.PrintAttributes.Resolution;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.*;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.utils.ProgressDialogUtils;
import com.example.utils.WebServiceUtils;
import com.example.utils.WebServiceUtils.WebServiceCallBack;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.platformtools.MMHandlerThread.ResetCallback;

public class MainActivity extends Activity {

	private String APP_ID = "wx31ee4a20e008915e";
	private IWXAPI api;

	private int mutex = 0 ;
	private Button tv;
	private Button updatelocationBtn;
	private Button calendarBtn;
	private Button shortcutBtn;
	private Button weixinBtn;
	private Button climateQueryBtn;
	private Resources resource;
	private Drawable drawable;
	private EditText cityEdt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	

		tv = (Button)findViewById(R.id.ShowText);
		updatelocationBtn = (Button)findViewById(R.id.locationBtn);
		calendarBtn = (Button)findViewById(R.id.calendarJumpBtn);
		shortcutBtn = (Button)findViewById(R.id.shortcutBtn);
		weixinBtn  =(Button)findViewById(R.id.weixinBtn);
		climateQueryBtn = (Button)findViewById(R.id.climateQueryBtn);
		cityEdt = (EditText)findViewById(R.id.editText1);
		resource = getResources();
		drawable = resource.getDrawable(R.drawable.sunny);
		this.getWindow().setBackgroundDrawable(drawable);
		
		api = WXAPIFactory.createWXAPI(getApplicationContext(),APP_ID,true);
		api.registerApp(APP_ID);
		
		
		
		weixinBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.e("Gotin","E!!!");;
				Toast.makeText(MainActivity.this, "" + api.openWXApp(), Toast.LENGTH_SHORT);

				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						String text = tv.getText().toString().trim(); 
						WXTextObject textObj = new WXTextObject();
						textObj.text = text;
						WXMediaMessage msg = new WXMediaMessage(textObj);
						msg.mediaObject = textObj; 
						msg.description = text;
						
						SendMessageToWX.Req req = new SendMessageToWX.Req();
						
						req.transaction = "BroadcastText" + String.valueOf(System.currentTimeMillis());
						req.message = msg;
						
						api.sendReq(req);
					}
				});
				
			}
		});
		
		String defaultstr = "黑龙江\r\n哈尔滨\r\ntime\r\ntem1/tem2\r\nclimate\r\nwind\r\n";
		
		tv.setText(defaultstr);
		tv.setAlpha(50);

		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put("theCityName",GetLocationString());
		
		WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL, "getWeatherbyCityName", properties, new WebServiceCallBack() {
			
			@Override
			public void callBack(SoapObject result) {
				ProgressDialogUtils.dismissProgressDialog();
				if(result != null){
					SoapObject detail = (SoapObject) result.getProperty("getWeatherbyCityNameResult");
					StringBuilder sb = new StringBuilder();
					int index_out[] = {0,1,4,5,6,7};
					for(int i=0; i<index_out.length; i++){
						sb.append(detail.getProperty(index_out[i])).append("\r\n");
					}
					String str = detail.getProperty(6).toString();
					if(str.indexOf("雨") != -1)
					{
						drawable = resource.getDrawable(R.drawable.rainy);
						MainActivity.this.getWindow().setBackgroundDrawable(drawable);
					}
					else if(str.indexOf("云")!=-1)
					{
						drawable = resource.getDrawable(R.drawable.cloudy);
						MainActivity.this.getWindow().setBackgroundDrawable(drawable);
					}
					else
					{
						drawable = resource.getDrawable(R.drawable.sunny);
						MainActivity.this.getWindow().setBackgroundDrawable(drawable);
					}
					tv.setText(sb.toString());
				}else{
					Toast.makeText(MainActivity.this, "获取WebService数据错误", Toast.LENGTH_SHORT).show();
				}
				mutex += 1;
			}
		});
		
		climateQueryBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(mutex < 0) return ;

				Log.e("send","hehe");
				// TODO Auto-generated method stub
				if(!cityEdt.getText().equals("") && mutex >= 0)
				{

					mutex -= 1;
					HashMap<String, String> properties = new HashMap<String, String>();
					properties.put("theCityName",cityEdt.getText().toString().trim());
					
					WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL, "getWeatherbyCityName", properties, new WebServiceCallBack() {
						
						@Override
						public void callBack(SoapObject result) {
							ProgressDialogUtils.dismissProgressDialog();
							if(result != null){
								SoapObject detail = (SoapObject) result.getProperty("getWeatherbyCityNameResult");
								StringBuilder sb = new StringBuilder();
								int index_out[] = {0,1,4,5,6,7};
								for(int i=0; i<index_out.length; i++){
									sb.append(detail.getProperty(index_out[i])).append("\r\n");
								}
								String str = detail.getProperty(6).toString();
								if(str.indexOf("雨") != -1)
								{
									drawable = resource.getDrawable(R.drawable.rainy);
									MainActivity.this.getWindow().setBackgroundDrawable(drawable);
								}
								else if(str.indexOf("云")!=-1)
								{
									drawable = resource.getDrawable(R.drawable.cloudy);
									MainActivity.this.getWindow().setBackgroundDrawable(drawable);
								}
								else
								{
									drawable = resource.getDrawable(R.drawable.sunny);
									MainActivity.this.getWindow().setBackgroundDrawable(drawable);
								}
								tv.setText(sb.toString());
							}else{
								Toast.makeText(MainActivity.this, "获取WebService数据错误", Toast.LENGTH_SHORT).show();
							}
							mutex += 1;
						}
					});
				}
			}
		});
		
		shortcutBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				createDeskShortCut();
			}
		});
		
		calendarBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,CalendarPage.class);
				startActivity(intent);
			}
		});
		
		updatelocationBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new Thread()
				{
					public void run()
					{
						final String loc = GetLocationString();
						MainActivity.this.runOnUiThread(new Runnable() {					
							@Override
							public void run() {
								// TODO Auto-generated method stub
								tv.setText(loc);
							}
						});
					}
				}.start();
			}
		});
	}

	private String GetLocationString()
	{
		
		LocationManager locationmanager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		List<String>lp = locationmanager.getAllProviders();
		for(String _param : lp)
			Log.i("可用位置",_param);
		criteria.setCostAllowed(false);
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		String providername = locationmanager.getBestProvider(criteria, true);
		if(providername != null)
		{
			Log.i("所用定位",providername);
			Location location =  locationmanager.getLastKnownLocation(providername);
			if(location == null) return "error occured while getting location";
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			String locastr = "" + latitude + " " + longitude;
			locastr = "哈尔滨";
			return locastr;
		}
		return "error";
	}
	
	public void createDeskShortCut() {  
		   
        Log.i("coder","------createShortCut--------");  
        //创建快捷方式的Intent  
        Intent shortcutIntent = new Intent(  
                      "com.android.launcher.action.INSTALL_SHORTCUT");  
        //不允许重复创建  
        shortcutIntent.putExtra("duplicate",false);  
        //需要现实的名称  
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,  
                      getString(R.string.app_name));  

        //快捷图片  
        Parcelable icon = Intent.ShortcutIconResource.fromContext(  
                      getApplicationContext(),R.drawable.ic_launcher);  

        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,icon);  

        Intent intent = new Intent(getApplicationContext(),  
                      MainActivity.class);  
        //下面两个属性是为了当应用程序卸载时桌面 上的快捷方式会删除  
        intent.setAction("android.intent.action.MAIN");  
        intent.addCategory("android.intent.category.LAUNCHER");  
        //点击快捷图片，运行的程序主入口  
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,intent);  
        //发送广播。OK  
        sendBroadcast(shortcutIntent);  
	} 
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
