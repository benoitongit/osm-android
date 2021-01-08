package com.android.lib.map.osm;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import com.android.lib.map.osm.InDbTileLoader.IDbTileLoaderListener;
import java.util.List;
import java.util.Map;

public class TilesProvider implements IDbTileLoaderListener {
		
	private final InMemoryTilesCache mInMemoryTilesCache;
	private final ResizedTilesCache mResizedTilesCache;
	private final RemoteAsyncTileLoader mRemoteTileLoader;
	private final InDbTileLoader mInDbTileLoader;
	private final Handler mHandler;
	private final boolean mAllowRequestTilesViaInternet;

	private Bitmap mMapTileUnavailableBitmap;
	
	
	public TilesProvider(Handler handler, boolean allowRequestTilesViaInternet) {
		mInMemoryTilesCache = new InMemoryTilesCache();
		mRemoteTileLoader = new RemoteAsyncTileLoader(handler);
		mResizedTilesCache = new ResizedTilesCache(handler);
		mInDbTileLoader = new InDbTileLoader(this);
		mHandler = handler;
		mAllowRequestTilesViaInternet = allowRequestTilesViaInternet;
	}
	
	public void setResizeBitmapCacheSize(int size){
		mResizedTilesCache.setBitmapCacheSize(size);
	}
	
	public void setBitmapMemoryCacheSize(int size){
		mInMemoryTilesCache.setBitmapCacheSize(size);
	}
	
	public void setMapTileUnavailableBitmap(Bitmap bitmap){
		mMapTileUnavailableBitmap = bitmap;
		mResizedTilesCache.setMapTileUnavailableBitmap(mMapTileUnavailableBitmap);
	}
	
	public Bitmap getTileBitmap(Tile tile) {
		
		if (mInMemoryTilesCache.hasTile(tile.key)) {
			return mInMemoryTilesCache.getTileBitmap(tile.key);
		}
		
		if (mAllowRequestTilesViaInternet) {
			
			mInDbTileLoader.queue(new Tile(tile));
			if (mResizedTilesCache.hasTile(tile)) {
				return mResizedTilesCache.getTileBitmap(tile);
			}
			
		} else {
			
			if (mResizedTilesCache.hasTile(tile)) {
				return mResizedTilesCache.getTileBitmap(tile);
			}
			mInDbTileLoader.queue(new Tile(tile));	
		
		}
		
		return null;
	}

	@Override
	public void onTilesLoadedFromDb(Map<Tile, Bitmap> tileBitmapMap) {
		for (Map.Entry<Tile, Bitmap> entry : tileBitmapMap.entrySet()) {
			Tile tile = entry.getKey();
			Bitmap bitmap = entry.getValue();
			mInMemoryTilesCache.add(tile.key, bitmap);
		}
		Message message = mHandler.obtainMessage();
		message.what = TileHandler.TILE_LOADED;
		mHandler.sendMessage(message);
	}
	
	@Override
	public void onTilesNotLoadedFromDb(List<Tile> tiles) {
		for (Tile tile : tiles) {
			
			Tile tileMinusOneZoomLevel = mResizedTilesCache.findClosestMinusTile(tile);
			if (tileMinusOneZoomLevel != null) {
				mResizedTilesCache.queueResize(new Tile(tile));				
			}
			
			if (mAllowRequestTilesViaInternet)
				mRemoteTileLoader.queueTileRequest(new Tile(tile));
			
		}
	}
	
	public void stopThreads() {
		mRemoteTileLoader.interruptThreads();
		mResizedTilesCache.interrupt();
		mInDbTileLoader.interrupt();
	}
	
	public void clearCache() {
		mInMemoryTilesCache.clean();
	}

	public void clearResizeCache() {
		mResizedTilesCache.clear();
	}
}
