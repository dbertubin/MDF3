/*
 * project	ClimaCast
 * 
 * package 	com.petrockz.climacast
 * 
 * @author 	${author}
 * 
 * date 	Oct 10, 2013
 */
package com.petrockz.climacast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class ReadWrite.
 */
public class ReadWrite {
	// this might also be a the JAR 
	/**
	 * Store string file.
	 *
	 * @param context the context
	 * @param fileName the file name
	 * @param content the content
	 * @param external the external
	 * @return the boolean
	 */
	@SuppressWarnings("resource")
	public static Boolean storeStringFile(Context context, String fileName, String content, Boolean external){
		try {
			File file;
			FileOutputStream fos;
			if(external){
				file = new File(context.getExternalFilesDir(null),fileName);
				fos = new FileOutputStream(file);
			} else {
				fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			}
			Log.i("WRITING", "FOS OPENED");
			fos.write(content.getBytes());			
		} catch (IOException e) {
			Log.e("WRITE ERROR", e.toString());
		}
		
		return true;
		
	}
	
	/**
	 * Store object file.
	 *
	 * @param context the context
	 * @param fileName the file name
	 * @param content the content
	 * @param external the external
	 * @return the boolean
	 */
	@SuppressWarnings("resource")
	public static Boolean storeObjectFile(Context context, String fileName, Object content, Boolean external){
		
		try {
			File file;
			FileOutputStream fos;
			ObjectOutputStream oos;
			if(external){
				file = new File(context.getExternalFilesDir(null),fileName);
				fos = new FileOutputStream(file);
			} else {
				fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);	
			}
			oos = new ObjectOutputStream(fos);
			oos.writeObject(content);
			Log.i("CONTENT WRITTEN", content.toString());
			oos.close();
			fos.close();

		} catch (IOException e) {
			// TODO: handle exception
			Log.e("WRITE ERROR", fileName);
		}
		
		return true;
		
	}
	
	/**
	 * Read string file.
	 *
	 * @param context the context
	 * @param fileName the file name
	 * @param external the external
	 * @return the string
	 */
	@SuppressWarnings("resource")
	public static String readStringFile(Context context,String fileName, Boolean external){
		String content ="";
		try {
			File file;
			FileInputStream fis;
			if(external)
			{
				file = new File(context.getExternalFilesDir(null), fileName);
				fis = new FileInputStream(file);
			} else {
				file = new File(fileName);
				fis = context.openFileInput(fileName);
			}
			
			BufferedInputStream bin = new BufferedInputStream(fis);
			byte[] contentByte = new byte[1024];
			int bytesRead = 0;
			StringBuffer contentBuffer = new StringBuffer();
			
			while ((bytesRead = bin.read(contentByte)) != -1) {
				content = new String(contentByte,0,bytesRead);
				contentBuffer.append(content);
			}
			content = contentBuffer.toString();
			fis.close();
		} catch (FileNotFoundException e) {
			Log.e("READ ERROR", "FILE NOT FOUND");
			
		} catch (IOException e){
			Log.e("READ ERROR", "I/O Error");
		}
		
		return content;
	}
	
	
	/**
	 * Read string object.
	 *
	 * @param context the context
	 * @param fileName the file name
	 * @param external the external
	 * @return the object
	 */
	@SuppressWarnings("resource")
	public static Object readStringObject(Context context,String fileName, Boolean external){
		Object content = new Object();
		try {
			File file;
			FileInputStream fis;
			if(external)
			{
				file = new File(context.getExternalFilesDir(null), fileName);
				fis = new FileInputStream(file);
			} else {
				file = new File(fileName);
				fis = context.openFileInput(fileName);
			}
			
			ObjectInputStream ois = new ObjectInputStream(fis);
			try{
				content = ois.readObject();
			} catch (ClassNotFoundException e){
				Log.e("READ ERROR", "INVALID JAVA OBJECT FILE");
			}
			
			fis.close();
			ois.close();
		} catch (FileNotFoundException e) {
			Log.e("READ ERROR", "FILE NOT FOUND");
			return null;
		} catch (IOException e){
			Log.e("READ ERROR", "I/O Error");
		}
		
		return content;
	}
	
}
