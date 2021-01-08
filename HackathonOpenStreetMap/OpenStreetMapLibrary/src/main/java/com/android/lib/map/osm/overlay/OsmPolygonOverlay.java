package com.android.lib.map.osm.overlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Point;
import android.view.View;

import com.android.lib.map.osm.GeoPoint;
import com.android.lib.map.osm.OsmMapView;

public class OsmPolygonOverlay extends OsmOverlay {

	private final OsmMapView mMapView;
	private final Map<MapPolygon, Point> mStartPoints;
	private final List<MapPolygon> mPolygons;

	private GeoPoint mStartLocation;
	
	public OsmPolygonOverlay(OsmMapView mapView) {
		mMapView = mapView;
		mStartPoints = new HashMap<>();
		mPolygons = new ArrayList<>();
	}
	
	public void addPolygon(MapPolygon polygon) {
		mPolygons.add(polygon);
	}
	
	public List<MapPolygon> getPolygons() {
		return mPolygons;
	}
	
	public void removePolygons() {
		for (MapPolygon mapPolygon : mPolygons)
			mapPolygon.getPath().reset();
		mPolygons.clear();
		mStartPoints.clear();
	}
	
	@Override
	public void draw(Canvas canvas, View view) {
		super.draw(canvas, view);
		
		for (MapPolygon mapPolygon : mPolygons) {
			Path path = mapPolygon.getPath();
			if (path.isEmpty())
				drawPathForMapPolygon(mapPolygon);
			offsetPath(mapPolygon);
			canvas.drawPath(path, mapPolygon.getPaintStroke());
			canvas.drawPath(path, mapPolygon.getPaint());
		}
	}

	private void drawPathForMapPolygon(MapPolygon polygon) {

		Path path = polygon.getPath();
		
		if (!path.isEmpty())
			path.rewind();

		GeoPoint previousPoint = null;
		
		Point startPoint = new Point();
		startPoint.x = mMapView.getOffsetX();
		startPoint.y = mMapView.getOffsetY();
		mStartPoints.put(polygon, startPoint);
				
		for (GeoPoint g : polygon.getPolygon()) {

			if (previousPoint == null) {
				previousPoint = g;
				Point p = convertGeoPointToPixel(g);
				path.moveTo(p.x, p.y);
				
				if (mStartLocation == null)
					mStartLocation = g;

				continue;
			}

			drawLine(path, g);

			previousPoint = g;
		}

		path.close();
	}
	
	@Override
	public void onZoomLevelChanges(View view) {
		super.onZoomLevelChanges(view);

		for (MapPolygon polygon : mPolygons) {
			drawPathForMapPolygon(polygon);
		}
	}
	
	private void offsetPath(MapPolygon mapPolygon) {
		Point startPoint = mStartPoints.get(mapPolygon);
		Path path = mapPolygon.getPath();

		if (startPoint == null && path != null && !path.isEmpty()) {
			for (MapPolygon polygon : mPolygons) {
				drawPathForMapPolygon(polygon);
			}
		}
		
		if (startPoint != null && !path.isEmpty()) {
			int x = mMapView.getOffsetX() - startPoint.x;
			int y = mMapView.getOffsetY() - startPoint.y;

			path.offset(x, y);
			startPoint.x = mMapView.getOffsetX();
			startPoint.y = mMapView.getOffsetY();
		}
	}

	private void drawLine(Path path, GeoPoint gp2) {
		Point p2 = convertGeoPointToPixel(gp2);
		path.lineTo(p2.x, p2.y);
	}

	private Point convertGeoPointToPixel(GeoPoint g) {
		return mMapView.geopointToPixelProjection(g);
	}
	
}
