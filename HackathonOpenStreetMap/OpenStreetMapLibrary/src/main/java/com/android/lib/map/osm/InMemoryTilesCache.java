package com.android.lib.map.osm;

import android.graphics.Bitmap;

public class InMemoryTilesCache {
	private LRUMap<String, Bitmap> mBitmapCache = new LRUMap<>(8, 8);
	private final Object mLock = new Object();

	public void add(String tileKey, Bitmap bitmap) {
		synchronized (mLock) {
			mBitmapCache.put(tileKey, bitmap);
		}
	}

	public boolean hasTile(String tileKey) {
		synchronized (mLock) {
			return mBitmapCache.containsKey(tileKey);
		}
	}

	public void clean() {
		synchronized (mLock) {
			mBitmapCache.clear();
		}
	}

	public Bitmap getTileBitmap(String tileKey) {
		synchronized (mLock) {
			return mBitmapCache.get(tileKey);
		}
	}
	public void setBitmapCacheSize(int size){
		mBitmapCache = new LRUMap<>(size, size+2);
	}
}
