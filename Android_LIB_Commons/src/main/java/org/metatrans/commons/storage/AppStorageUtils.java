package org.metatrans.commons.storage;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.metatrans.commons.model.AppData;

import android.app.Activity;
import android.content.Context;


public class AppStorageUtils {
	
	
	private static final String FILE_NAME = "appstorage";
	
	
	public static Object[] readStorage(Context context) throws Exception {
		
		ObjectInputStream input = null;
		
		Object[] objs = new Object[1];
		
		try {
			
			File file = new File(context.getFilesDir(), FILE_NAME);
			if (file.exists()) {
				InputStream is = context.openFileInput(FILE_NAME);
				InputStream buffer = new BufferedInputStream(is);
				input = new ObjectInputStream(buffer);
				
				objs[0] = (AppData) input.readObject();
				
				//System.out.println("READ GAMEDATA: MOVES COUNT" + ((GameData_Base)objs[0]).getMoves().size());
			}
			
		} catch (Exception e) {
			//throw e;
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {}
			}
		}
		
		return objs;
	}
	
	
	public static void writeStore(Context context, AppData appdata) {
		ObjectOutput output = null;
		
		try {
			
			//System.out.println("WRITE GAMEDATA: MOVES COUNT" + gamedata.getMoves().size());
			
			OutputStream os = context.openFileOutput(FILE_NAME, Activity.MODE_PRIVATE);
			OutputStream buffer = new BufferedOutputStream(os);
			output = new ObjectOutputStream(buffer);
			
			output.writeObject(appdata);
						
			output.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {}
			}
		}
	}
	

	public static void clearStore(Context context) {
		ObjectOutput output = null;
		
		try {
			OutputStream os = context.openFileOutput(FILE_NAME, Activity.MODE_PRIVATE);
			OutputStream buffer = new BufferedOutputStream(os);
			output = new ObjectOutputStream(buffer);
			
			output.writeObject(null);
			
			output.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {}
			}
		}
	}
}
