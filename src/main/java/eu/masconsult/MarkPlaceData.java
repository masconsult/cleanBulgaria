package eu.masconsult;

import java.io.File;
import java.util.List;

public class MarkPlaceData {
	
	private File image;
	private List<Integer> wasteTypes;
	private String wasteInfo;
	private String address;
	private double lat;
	private double lng;
	
	public MarkPlaceData() {
		this.image = null;
		this.wasteTypes = null;
		this.wasteInfo = null;
		this.address = null;
		this.lat = 0;
		this.lng = 0;
	}
	
	public MarkPlaceData(File image, List<Integer> wasteTypes,
			String wasteInfo, String address, double lat, double lng) {
		super();
		this.image = image;
		this.wasteTypes = wasteTypes;
		this.wasteInfo = wasteInfo;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
	}



	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public List<Integer> getWasteTypes() {
		return wasteTypes;
	}

	public void setWasteTypes(List<Integer> wasteTypes) {
		this.wasteTypes = wasteTypes;
	}

	public String getWasteInfo() {
		return wasteInfo;
	}

	public void setWasteInfo(String wasteInfo) {
		this.wasteInfo = wasteInfo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		long temp;
		temp = Double.doubleToLongBits(lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lng);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((wasteInfo == null) ? 0 : wasteInfo.hashCode());
		result = prime * result
				+ ((wasteTypes == null) ? 0 : wasteTypes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MarkPlaceData other = (MarkPlaceData) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat))
			return false;
		if (Double.doubleToLongBits(lng) != Double.doubleToLongBits(other.lng))
			return false;
		if (wasteInfo == null) {
			if (other.wasteInfo != null)
				return false;
		} else if (!wasteInfo.equals(other.wasteInfo))
			return false;
		if (wasteTypes == null) {
			if (other.wasteTypes != null)
				return false;
		} else if (!wasteTypes.equals(other.wasteTypes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MarkPlaceData [image=");
		builder.append(image);
		builder.append(", wasteTypes=");
		builder.append(wasteTypes);
		builder.append(", wasteInfo=");
		builder.append(wasteInfo);
		builder.append(", address=");
		builder.append(address);
		builder.append(", lat=");
		builder.append(lat);
		builder.append(", lng=");
		builder.append(lng);
		builder.append("]");
		return builder.toString();
	}
	
}
