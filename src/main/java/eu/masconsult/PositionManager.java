package eu.masconsult;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class PositionManager {
	LocationManager lm;
	boolean gps_enabled = false;
	boolean network_enabled = false;
	private Location currentPosition;
	private Context context;

	public void initPositionManager(Context context) throws PositionException {
		// I use LocationResult callback class to pass location value from
		// MyLocation to user code.
		this.context = context;
		if (lm == null)
			lm = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);

		// exceptions will be thrown if provider is not permitted.
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		// don't start listeners if no provider is enabled
		if (!gps_enabled && !network_enabled)
			throw new PositionException("no available providers");

		if (gps_enabled)
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
					locationListenerGps);
		if (network_enabled)
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
					locationListenerNetwork);
	}

	LocationListener locationListenerGps = new LocationListener() {
		public void onLocationChanged(Location location) {
			currentPosition = location;
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

	};

	LocationListener locationListenerNetwork = new LocationListener() {
		public void onLocationChanged(Location location) {
			currentPosition = location;
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	public Location getPosition() {
		if (currentPosition == null) {
			currentPosition = getLastLocation();
		}

		return currentPosition;
	}

	private Location getLastLocation() {
		Location res;
		Location net_loc = null, gps_loc = null;
		if (gps_enabled)
			gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (network_enabled)
			net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		// if there are both values use the latest one
		if (gps_loc != null && net_loc != null) {
			if (gps_loc.getTime() > net_loc.getTime()) {
				res = gps_loc;
			} else {
				res = net_loc;
			}
			return res;
		}

		if (gps_loc != null) {
			return gps_loc;
		}
		if (net_loc != null) {
			return net_loc;
		}

		return null;
	}

	public String getAddress() throws IOException {
		Geocoder geocoder = new Geocoder(context);
		List<Address> addresses = geocoder.getFromLocation(
				currentPosition.getLatitude(), currentPosition.getLongitude(),
				1);

		String address;
		if (addresses != null) {
			Address returnedAddress = addresses.get(0);
			StringBuilder strReturnedAddress = new StringBuilder();
			for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
				strReturnedAddress.append(returnedAddress.getAddressLine(i))
						.append(" ");
			}
			address = strReturnedAddress.toString();
		} else {
			address = null;
		}
		
		return address;
	}
}
