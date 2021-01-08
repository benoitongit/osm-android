package com.hackathonopenstreetmap;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.android.lib.map.osm.GeoPoint;
import com.android.lib.map.osm.OsmMapView;
import com.android.lib.map.osm.controller.IMapInteractionListener;
import com.android.lib.map.osm.helpers.OsmDatabaseHelper;
import com.android.lib.map.osm.models.OsmModel;
import com.android.lib.map.osm.overlay.MapMarker;
import com.android.lib.map.osm.overlay.OsmLocationOverlay;
import com.android.lib.map.osm.overlay.OsmMarkerOverlay;

import java.io.File;

public class MapActivity extends Activity implements IMapInteractionListener,
        LocationListenerHelper.IMyLocationListener {

    private LocationListenerHelper mLocationListener;
    private OsmMapView mOsmMapView;
    private OsmLocationOverlay mOsmLocationOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        mLocationListener = new LocationListenerHelper(this);

        initOsmDatabase();

        initMap();

    }

    @Override
    protected void onResume() {
        super.onResume();

        mLocationListener.startListeningLocation(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mLocationListener.stopListeningLocation();
    }

    @Override
    protected void onDestroy() {
        mOsmMapView.clear();
        super.onDestroy();
    }

    private void initMap() {
        OsmMapView.OsmMapViewBuilder mapBuilder = new OsmMapView.OsmMapViewBuilder();

        mapBuilder.setIsNetworkRequestAllowed(true);
        mapBuilder.setPositionIndicatorDrawableId(R.drawable.blue_position_indicator);

        mOsmMapView = new OsmMapView(getApplicationContext(), mapBuilder, this);

        mOsmLocationOverlay = new OsmLocationOverlay(getApplicationContext(), mapBuilder, mOsmMapView);

        mOsmMapView.addOverlay(mOsmLocationOverlay);

        ViewGroup mapLayout = (ViewGroup) findViewById(R.id.mapLayout);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mapLayout.addView(mOsmMapView, layoutParams);

        addMarkers();

        mOsmMapView.setCenter(37.7793, -122.4192);
        mOsmMapView.setZoom(4);
    }

    private void initOsmDatabase() {

        File destFile = new File(getFilesDir(), "osm_db.sqlite");

        OsmDatabaseHelper osmDbHelper = new OsmDatabaseHelper();

        osmDbHelper.setDatabaseFile(destFile);

        boolean success = osmDbHelper.openOrCreateDatabase(this, destFile);

        if (success) {
            OsmModel.mDbHelper = osmDbHelper;
        }
    }

    private void addMarkers() {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.tubi_marker);
        OsmMarkerOverlay markerOverlay = new OsmMarkerOverlay(mOsmMapView, drawable);
        // Tubi SF office
        addMarker(new GeoPoint((int) (37.792260 * 1e6), (int) (-122.403490 * 1e6)), markerOverlay);
        // Chicago SF office
        addMarker(new GeoPoint((int) (41.888650 * 1e6), (int) (-87.627460 * 1e6)), markerOverlay);
        // LA SF office
        addMarker(new GeoPoint((int) (34.057330 * 1e6), (int) (-118.417170 * 1e6)), markerOverlay);
        // NYC SF office
        addMarker(new GeoPoint((int) (40.754080 * 1e6), (int) (-73.988820 * 1e6)), markerOverlay);
        // Beijing SF office
        addMarker(new GeoPoint((int) (40.009477 * 1e6), (int) (116.461179 * 1e6)), markerOverlay);
        mOsmMapView.addOverlay(markerOverlay);
    }

    private void addMarker(GeoPoint p, OsmMarkerOverlay markerOverlay) {
        MapMarker marker = new MapMarker();
        marker.setCoordinate(p);
        markerOverlay.addMarker(marker);
    }

    @Override
    public boolean onMapTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void onMapDraw(Canvas canvas) {

    }

    @Override
    public void onMapSingleTapConfirmed(MotionEvent event) {

    }

    @Override
    public void onMapStopPanning() {

    }

    @Override
    public void onMapZoomChanged(int zoomLevel) {

    }

    @Override
    public void onMapLongClick(MotionEvent event) {

    }

    @Override
    public void onMapMarkerTap(MapMarker overlayItem) {

    }

    @Override
    public void onMapCalloutTap(MotionEvent event) {

    }

    @Override
    public void onNewLocation(Location location) {
        if (mOsmLocationOverlay != null && location != null) {
            mOsmLocationOverlay.setLocation(location);
        }
    }

}