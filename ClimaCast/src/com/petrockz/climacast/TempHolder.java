package com.petrockz.climacast;

public class TempHolder {

	public static String _temp;
	public static String _zip;
	
	
	public  static String setTemp(String temp){
		
		_temp = temp;
		
		return _temp;
		
	}
	
	public static String setZip(String zip) {
		
		_zip = zip;
		
		return _zip;
	}
}
