package com.android.lib.map.osm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Vector;

import android.util.Log;

public class RequestTile {
	private static final String URL_OSM_A = "https://a.tile.openstreetmap.org/";
	private static final String URL_OSM_B = "https://b.tile.openstreetmap.org/";
	private static final String URL_OSM_C = "https://c.tile.openstreetmap.org/";
	
	private static final int IO_BUFFER_SIZE = 8192;
	
	private final OsmBasePool mOsmBasePool = new OsmBasePool();
	private final byte[] mBuffer;
	
	
	public RequestTile() {
		mBuffer = new byte[IO_BUFFER_SIZE];
	}
	
	public byte[] loadBitmap(Tile tile) throws InterruptedException {
		
		String tileUrl = mOsmBasePool.getNextBase() + tile.key;
		
		InputStream in;
		OutputStream out;
		ByteArrayOutputStream dataStream;

		try {
			URL urL = new URL(tileUrl);
			InputStream inStream = urL.openStream();
			in = new BufferedInputStream(inStream, IO_BUFFER_SIZE);

			dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
			copy(in, out);

			out.flush();
			out.close();
			return dataStream.toByteArray();
		} catch (IOException e) {
			Log.e("class RequestTile", "IOException: Error getting the tile from url");
			Thread.sleep(3000);
		} catch (Exception e) {
			Log.e("class RequestTile", "Exception: Error getting the tile from url");
			Thread.sleep(3000);
		}
		
		return null;
	}

	private void copy(InputStream in, OutputStream out) {
		int read;
		try {
			while ((read = in.read(mBuffer)) != -1) {
				out.write(mBuffer, 0, read);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static class OsmBasePool {
		private final Vector<String> bases = new Vector<>();
		private int iterator = 0;

		OsmBasePool() {
			bases.add(URL_OSM_A);
			bases.add(URL_OSM_B);
			bases.add(URL_OSM_C);
		}

		public String getNextBase() {
			++iterator;
			if (iterator == bases.size()) {
				iterator = 0;
			}

			return bases.elementAt(iterator);
		}

	}
	
}
