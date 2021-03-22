package de.cyzetlc.utils;

import java.text.SimpleDateFormat;

public class Date {
	
	public static String getDateFormat(long timeMillis)
	{
		
		java.util.Date date = new java.util.Date(timeMillis);
		String mm_dd_yyyy = new SimpleDateFormat("dd.MM.yyyy").format(date);
		String hour_min = new SimpleDateFormat("HH:mm").format(date);
		String datum = "§e" + mm_dd_yyyy +  " §7um §e" + hour_min;
		
		return datum;
	}
	
	public static String getDate() 
	{
		
		java.util.Date date = new java.util.Date(System.currentTimeMillis());
		String mm_dd_yyyy = new SimpleDateFormat("dd.MM.yyyy").format(date);
		String hour_min = new SimpleDateFormat("HH:mm:ss").format(date);
		String datum = mm_dd_yyyy +  " " + hour_min;
		
		return datum;
	}

}
