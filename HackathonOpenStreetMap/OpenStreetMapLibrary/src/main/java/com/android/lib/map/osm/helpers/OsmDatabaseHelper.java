package com.android.lib.map.osm.helpers;

import java.io.File;

import android.content.Context;


public class OsmDatabaseHelper extends CustomDatabaseHelper {
	
	private File mDbFile;
	
	public boolean openOrCreateDatabase(Context context) {
		if (mDbFile != null)
			return super.openOrCreateDatabase(context, mDbFile);
	
		return false;
	}
	
	public boolean openDatabase(Context context) {
		if (mDbFile.exists())
			return super.openDatabase(context, mDbFile);
		
		return false;
	}
	
	public void setDatabaseFile(File dbFile) {
		mDbFile = dbFile;
	}
	
	public File getDatabaseFile() {
		return mDbFile;
	}
	
}
