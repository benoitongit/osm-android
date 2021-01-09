# OSM Android

OSM Android is a free Android map library using OpenStreetMap tiles.

![alt text](https://github.com/benoitongit/osm-android/blob/main/map_screenshot.jpg?raw=true)

## Features

* Works offline, tiles are cached in a local SQLite database
* Add/remove Overlays
  * markers
  * tracks
  * polygons

## Requirements

OSM-Android works with API 21 and up.
It runs using a gradle script version 6.5.

## Getting started

After you download the project, you may import into Android Studio the library using either the source code or the .aar file provided.

For .aar:
 * Click File > New > New Module.
 * Select the option "Import .JAR/.AAR Package", then click Next.
 * Select `osm_android.aar` in the root level of osm-android.
 
For the full library, if you wish to have the source code as well:
 * Click File > New > Import Module.
 * Select the `library` folder in the root level of osm-android.
 
Then, be sure in your projects root gradle file to add the following:
```
implementation project(":[library module name]")
```

Also verify your `settings.gradle` has the following
```
include ':app', ':[library module name]'
```

For more info, see the google developer docs on [importing libraries](https://developer.android.com/studio/projects/android-library#AddDependency).

## Using OSM Android
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

  OsmMapView.OsmMapViewConfig mapConfig = new OsmMapView.OsmMapViewConfig();
  mapConfig.setIsNetworkRequestAllowed(true);
  mapView = new OsmMapView(getApplicationContext(), mapConfig, this);

  ViewGroup mapLayout = (ViewGroup) findViewById(R.id.mapLayout);
  RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
  mapLayout.addView(mapView, layoutParams);

  mapView.setCenter(37.7793, -122.4192);
  mapView.setZoom(12); 
}

@Override
protected void onDestroy() {
    mapView.clear();
    super.onDestroy();
}
```

### Map Overlays

**How to add the position indicator**
```
OsmMapView.OsmMapViewConfig mapConfig = new OsmMapView.OsmMapViewConfig();
mapConfig.setPositionIndicatorDrawableId(R.drawable.blue_position_indicator);
OsmLocationOverlay overlay = new OsmLocationOverlay(getApplicationContext(), mapConfig, mapView);
mapView.addOverlay(overlay);
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

// Add track coordinates
List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
geoPoints.add(new GeoPoint(37.792260, -122.403490));
geoPoints.add(new GeoPoint(41.888650, -87.627460));

mapTrack.setTrack(geoPoints); 
trackOverlay.addTrack(mapTrack);
mapView.addOverlay(trackOverlay);
```

**How to add a polygon**

```
OsmPolygonOverlay overlay = new OsmPolygonOverlay(mapView);
MapPolygon mapPolygon = new MapPolygon();

// Set polygon border
Paint paintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
paintStroke.setColor(Color.RED);
paintStroke.setStyle(Paint.Style.STROKE);
paintStroke.setStrokeJoin(Paint.Join.ROUND);
paintStroke.setStrokeCap(Paint.Cap.ROUND);
paintStroke.setStrokeWidth(7);

// Fill polygon with slightly transparent red color
Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
paint.setColor(Color.RED);
paint.setAlpha(50);

mapPolygon.setPaint(paint);
mapPolygon.setPaintStroke(paintStroke);

// Add polygon coordinates
List<GeoPoint> geoPoints = new ArrayList<>();
geoPoints.add(new GeoPoint(25.774, -80.19));
geoPoints.add(new GeoPoint(18.466, -66.118));
geoPoints.add(new GeoPoint(32.321, -64.757 ));
mapPolygon.setPolygon(geoPoints);

overlay.addPolygon(mapPolygon);
mapView.addOverlay(overlay);
```

## Feature Roadmap
* Show current location
* Support infinite world map scroll
* Support .svg marker
* Add ability to download a map area for offline usage
* Add zoom in/out buttons
