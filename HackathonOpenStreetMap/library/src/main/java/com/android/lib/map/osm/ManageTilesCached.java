package com.android.lib.map.osm;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class ManageTilesCached {

	private static final int AVERAGE_TILE_SIZE = 35; // in Kb
	
	private final File mDirectory;
	
	public ManageTilesCached(File directory) {
		mDirectory = directory;
	}
	
  /**
   * Delete tiles above a certain limit (first in first out)
   * @param limit in Mb
   */
	public void deleteTilesAboveLimit(int limit) {
		
		final int limitKb = limit * 1024; //put limit in Kb 
		
		if (mDirectory == null)
			return;
		
		Thread t = new Thread() {
			@Override
			public void run() {
				
				List<File> files = getAllFilesInDirectories(mDirectory);
				
				Map<Long, File> filesMap = new HashMap<>();
				for (File file : files) {
					filesMap.put(file.lastModified(), file);
				}
				
				int filesSizeKb = AVERAGE_TILE_SIZE * files.size();
				if (filesSizeKb > limitKb) {
					
					int nbFilesToDeleted = (filesSizeKb - limitKb) / AVERAGE_TILE_SIZE;
					
					TreeSet<Long> keys = new TreeSet<>(filesMap.keySet());
					for (Long key : keys) {
						
						if (nbFilesToDeleted <= 0)
							break;
						
						File fileValue = filesMap.get(key);
						if (fileValue != null) {
							fileValue.delete();
						}

						nbFilesToDeleted--;
					}	
					
				}
			}
		};
		t.start();
	}
	
	public static List<File> getAllFilesInDirectories(File directory) {
		List<File> files = new ArrayList<>();

		for (File file : directory.listFiles()) {
			if (file.isFile()) {
				files.add(file);
			}
			if (file.isDirectory()) {
				files.addAll(getAllFilesInDirectories(file));
			}
		}
		return files;
	}
	
}
