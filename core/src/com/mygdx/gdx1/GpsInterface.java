package com.mygdx.gdx1;

public interface GpsInterface{
	/** @return Last known location GPS altitude in meters */
	public double getGPSAltitude();

	/** @return Last known location GPS latitude in degrees */
	public double getGPSLatitude();

	/** @return Last known location GPS longitude in degrees */
	public double getGPSLongitude();

	/** @return Last known location GPS bearing in degrees East of true North */
	public float getGPSBearing();

	/** @return Last known location GPS speed in meters/second */
	public float getGPSSpeed();

	/** Ugly but well */
	public class Container{
		public static GpsInterface Instance;
	}
}

