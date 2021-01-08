package com.hackathonopenstreetmap;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import com.android.lib.map.osm.overlay.MapTrack;
import com.android.lib.map.osm.overlay.OsmLocationOverlay;
import com.android.lib.map.osm.overlay.OsmMarkerOverlay;
import com.android.lib.map.osm.overlay.OsmTrackOverlay;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.ticofab.androidgpxparser.parser.GPXParser;
import io.ticofab.androidgpxparser.parser.domain.Gpx;
import io.ticofab.androidgpxparser.parser.domain.Track;
import io.ticofab.androidgpxparser.parser.domain.TrackPoint;
import io.ticofab.androidgpxparser.parser.domain.TrackSegment;

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
        addTrack();
        addMarkers();

        ViewGroup mapLayout = (ViewGroup) findViewById(R.id.mapLayout);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mapLayout.addView(mOsmMapView, layoutParams);

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
        addMarker(new GeoPoint(37.792260, -122.403490), markerOverlay);
        // Chicago SF office
        addMarker(new GeoPoint(41.888650, -87.627460), markerOverlay);
        // LA SF office
        addMarker(new GeoPoint(34.057330, -118.417170), markerOverlay);
        // NYC SF office
        addMarker(new GeoPoint(40.754080, -73.988820), markerOverlay);
        // Beijing SF office
        addMarker(new GeoPoint(40.009477, 116.461179), markerOverlay);
        mOsmMapView.addOverlay(markerOverlay);
    }

    private void addMarker(GeoPoint p, OsmMarkerOverlay markerOverlay) {
        MapMarker marker = new MapMarker();
        marker.setCoordinate(p);
        markerOverlay.addMarker(marker);
    }

    private void addTrack() {
        OsmTrackOverlay trackOverlay = new OsmTrackOverlay(mOsmMapView);
        MapTrack mapTrack = new MapTrack();

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setDither(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(8);

        mapTrack.setPaint(paint);
        mapTrack.setTrack(getTrackFromGpx());
        trackOverlay.addTrack(mapTrack);
        mOsmMapView.addOverlay(trackOverlay);
    }

    private List<GeoPoint> getTrackFromGpx() {
        List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
        GPXParser parser = new GPXParser();
        Gpx parsedGpx = null;
        try {
            InputStream in = getAssets().open("morning_run.gpx");
            parsedGpx = parser.parse(in);
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        if (parsedGpx != null) {
            for (Track track: parsedGpx.getTracks()) {
                for (TrackSegment trackSegment: track.getTrackSegments()) {
                    for (TrackPoint point: trackSegment.getTrackPoints()) {
                        geoPoints.add(new GeoPoint(point.getLatitude(), point.getLongitude()));
                    }
                }
            }
        }
        return geoPoints;
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