package com.mi.sta5.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
public class LocationService {
	private double longitude; //经度
	 private double latitude;  //纬度
	 private StringBuffer locations;
	 private Context context;
	 private String provider;
	 private static LocationManager locationManager;
	 private static final int MIN_TIME_REQUEST = 60000;
	 private static final int MINDISTANCE=1;
	private LocationService(){}
	private static LocationService locationService=null;
	public synchronized final static LocationService getLocationService()
	{
	
		if (null==locationService) {
			 locationService=new LocationService();
		}
		return locationService;
		
	}
	
	@SuppressWarnings("static-access")
	public void requestLocationUpdates()
	{
		locationManager=(LocationManager) context.getSystemService(context.LOCATION_SERVICE);
		provider=locationManager.getBestProvider(new Criteria(), true);
		locationManager.requestLocationUpdates(provider,MIN_TIME_REQUEST , MINDISTANCE, locationListener);
		
	}
	public  LocationListener locationListener=new  LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			getLocation(location);
		}
		@Override
		public void onProviderDisabled(String provider) {
		}
		@Override
		public void onProviderEnabled(String provider) {
		}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};
  private void getLocation(Location location)
  {
	  if (null!=location) {
			longitude=location.getLongitude();
			latitude=location.getLatitude();
			locations=new StringBuffer();
			locations.append(longitude).append(",").append(latitude);
		   new GetLocation().execute();
		}
  }
	class GetLocation extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params) {
			List<Address> addresses=new ArrayList<Address>();
			Geocoder geocoder=new Geocoder(context);
			try {
				
					addresses=geocoder.getFromLocation(latitude, longitude, 1);
					if (addresses.size()>0) {
						Log.d("hck", "GetLocation"+addresses.get(0).toString());
					}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}
	@SuppressWarnings("static-access")
	public  boolean isServiceEnabled(Context context) {
		this.context=context;
		locationManager=(LocationManager) context.getSystemService(context.LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
		locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

}
