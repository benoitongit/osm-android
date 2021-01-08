package com.android.lib.map.osm.overlay;

import android.view.MotionEvent;
import android.view.View;

/**
 * A map layer
 * An overlay can do different things, like show markers, polygons and tracks.
 * Children include {@link OsmMarkerOverlay} {@link OsmPolygonOverlay} {@link OsmTrackOverlay}
 */
public abstract class OsmOverlay {

	
	public void draw(android.graphics.Canvas canvas, View view) {
		
	}
	
	public void onZoomLevelChanges(View view) {
		
	}
	
	public boolean onSingleTap(final MotionEvent event, View view) {
		return false;
	}

	public boolean onInterceptSingleTap(final MotionEvent event, View view) {
		return false;
	}
}
