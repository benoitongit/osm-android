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

**How to add the position indicator overlay**
```
OsmMapView.OsmMapViewBuilder mapBuilder = new OsmMapView.OsmMapViewBuilder();
mapBuilder.setPositionIndicatorDrawableId(R.drawable.blue_position_indicator);
OsmLocationOverlay locationOverlay = new OsmLocationOverlay(getApplicationContext(), mapBuilder, mapView);
mapView.addOverlay(locationOverlay);
```
