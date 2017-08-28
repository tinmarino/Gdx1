package com.mygdx.gdx1.android;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.mygdx.gdx1.GpsInterface;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GpsAndroid implements GpsInterface, LocationListener {
	private LocationManager locationManager;
	private Location location;

	public GpsAndroid(AndroidApplication app){
		locationManager = (LocationManager) app.getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(this);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, false);
		try{
			location = locationManager.getLastKnownLocation(provider);
		}
		catch(Exception e){
			Log.i("Gdx1","I caught Exception: " + e);
		}
	}

	@Override
	public double getGPSAltitude() {
		if(location != null && location.hasAltitude())
			return location.getAltitude();
		return 0;
	}

	@Override
	public double getGPSLatitude() {
		if(location != null)
			return location.getLatitude();
		return 0;
	}

	@Override
	public double getGPSLongitude() {
		if(location != null)
			return location.getLongitude();
		return 0;
	}

	@Override
	public float getGPSBearing() {
		if(location != null && location.hasBearing())
			return location.getBearing();
		return 0;
	}

	@Override
	public float getGPSSpeed() {
		if(location != null && location.hasSpeed())
			return location.getSpeed();
		return 0;
	}

	/*
	 *	Methds from LocationListener
	*/

	@Override
	public void onLocationChanged(Location location) {
		Gdx.app.debug("Gdx1","Location changed: " + location);
		this.location = location;
	 }


	@Override
	public void onProviderEnabled(String provider) {
		Gdx.app.log("Gdx1", "Provider Enabled");

	}

	@Override
	public void onProviderDisabled(String provider) { }

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) { }

}
