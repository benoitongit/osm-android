# tile

Tile is a free Android map library using OpenStreetMap tiles.

## Features

* Works offline, tiles are saved in a local sqlite database
* Show current location
* Overlays
  * markers
  * tracks
  * polygon

## Requirements

Tile works on Android X.X+ (API level XX+) and on Java X+.

## Gettting started

### Manifest

In most cases, you will have to set the following authorizations in your AndroidManifest.xml:

```
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```
You can adjust the permissions accordingly, depending on what is used.


### Map initialization

In your Activity class, add the following:
```
private OsmMapView mapView;

@Override
public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);

  OsmMapView.OsmMapViewBuilder mapBuilder = new OsmMapView.OsmMapViewBuilder();
  mapBuilder.setIsNetworkRequestAllowed(true);
  mapView = new OsmMapView(getApplicationContext(), mapBuilder, this);

  ViewGroup mapLayout = (ViewGroup) findViewById(R.id.mapLayout);
  RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
  mapLayout.addView(mapView, layoutParams);

  mapView.setCenter(37.7793, -122.4192);
  mapView.setZoom(12); 
}
```

### Map Overlays

**How to add the position indicator**
```
OsmMapView.OsmMapViewBuilder mapBuilder = new OsmMapView.OsmMapViewBuilder();
mapBuilder.setPositionIndicatorDrawableId(R.drawable.blue_position_indicator);
OsmLocationOverlay locationOverlay = new OsmLocationOverlay(getApplicationContext(), mapBuilder, mapView);
mapView.addOverlay(locationOverlay);
```

**How to add a marker**

```
Drawable drawable = ContextCompat.getDrawable(this, R.drawable.marker);
OsmMarkerOverlay markerOverlay = new OsmMarkerOverlay(mapView, drawable);
MapMarker marker = new MapMarker();
marker.setCoordinate(new GeoPoint(37.792260, -122.403490));
markerOverlay.addMarker(marker);
mapView.addOverlay(markerOverlay);
```

**How to add a track**

```
OsmTrackOverlay trackOverlay = new OsmTrackOverlay(mapView);
MapTrack mapTrack = new MapTrack();

// Define track color, shape, width, etc...
Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
paint.setDither(true);
paint.setColor(Color.RED);
paint.setStyle(Paint.Style.STROKE);
paint.setStrokeJoin(Paint.Join.ROUND);
paint.setStrokeCap(Paint.Cap.ROUND);
paint.setStrokeWidth(8);

mapTrack.setPaint(paint);
List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
geoPoints.add(new GeoPoint(37.792260, -122.403490));
geoPoints.add(new GeoPoint(41.888650, -87.627460));
mapTrack.setTrack(geoPoints); 
trackOverlay.addTrack(mapTrack);
mapView.addOverlay(trackOverlay);
```
