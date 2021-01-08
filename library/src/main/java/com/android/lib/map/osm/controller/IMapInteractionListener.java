package com.android.lib.map.osm.controller;

import com.android.lib.map.osm.overlay.MapMarker;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Listener for Map events like touch, single and double tap, zoom, etc...
 */
public interface IMapInteractionListener {
	boolean onMapTouchEvent(MotionEvent event);
	void onMapDraw(Canvas canvas);
	void onMapSingleTapConfirmed(MotionEvent event);
	void onMapLongClick(MotionEvent event);
	void onMapMarkerTap(MapMarker overlayItem);
	void onMapStopPanning();
	void onMapZoomChanged(int zoomLevel);
	void onMapCalloutTap(MotionEvent event);
}
