# tile

Android is free map library using Open street map tiles.

## Features

* Works offline, tiles are saved in a local sqlite database
* Overlays
  * markers
  * tracks
  * polygon

## Requirements
...

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
private OsmMapView mOsmMapView;

@Override
public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);

  OsmMapView.OsmMapViewBuilder mapBuilder = new OsmMapView.OsmMapViewBuilder();
  mapBuilder.setIsNetworkRequestAllowed(true);
  mOsmMapView = new OsmMapView(getApplicationContext(), mapBuilder, this);

  ViewGroup mapLayout = (ViewGroup) findViewById(R.id.mapLayout);
  RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
  mapLayout.addView(mOsmMapView, layoutParams);

  mOsmMapView.setCenter(37.7793, -122.4192);
  mOsmMapView.setZoom(12); 
}
```

### Map Overlays

```
OsmMapView.OsmMapViewBuilder mapBuilder = new OsmMapView.OsmMapViewBuilder();
OsmLocationOverlay osmLocationOverlay = new OsmLocationOverlay(getApplicationContext(), mapBuilder, mapView);
osmMapView.addOverlay(osmLocationOverlay);
```
