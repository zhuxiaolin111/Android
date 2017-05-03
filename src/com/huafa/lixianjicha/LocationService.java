/**
 * Copyright (C) 2014-2015 Imtoy Technologies. All rights reserved.
 * @charset UTF-8
 * @author xiong_it
 */
package com.huafa.lixianjicha;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * @description
 * @charset UTF-8
 * @author xiong_it
 * @date 2015-7-20上午10:31:39
 * @version
 */
public class LocationService extends IntentService implements LocationListener {
	private static final String TAG = "LocationService";
	private static final String SERVICE_NAME = "LocationService";

	private static final long MIN_TIME = 1000l;
	private static final float MIN_DISTANCE = 10f;

	private LocationManager locationManager;

	public LocationService() {
		super(SERVICE_NAME);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(TAG, "onHandleIntent");
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null || locationManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
			Log.i(TAG, "正在定位");
			/*
			 * 进行定位
			 * provider:用于定位的locationProvider字符串
			 * minTime:时间更新间隔，单位：ms
			 * minDistance:位置刷新距离，单位：m
			 * listener:用于定位更新的监听者locationListener
			 */
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
		} else {
			Log.i(TAG, "无法定位");
			//无法定位：1、提示用户打开定位服务；2、跳转到设置界面
			Toast.makeText(this, "无法定位，请打开定位服务", Toast.LENGTH_SHORT).show();
			Intent i = new Intent();
		//	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(i);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, String.valueOf(location.getLatitude()));
		Log.d(TAG, String.valueOf(location.getLongitude()));
		Common.latitude=location.getLatitude();
		Common.longitude=location.getLongitude();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

}
