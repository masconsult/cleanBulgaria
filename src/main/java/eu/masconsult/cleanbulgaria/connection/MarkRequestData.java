package eu.masconsult.cleanbulgaria.connection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MarkRequestData {
	public File imageFile;
	public String address;
	public String lat;
	public String lng;
	public List<Integer> wasteTypes = new ArrayList<Integer>();
	public String wasteVolume;
	public String wasteMetric;
	public String wasteInfo;
	public String submitX;
	public String submitY;
	@Override
	public String toString() {
		return "MarkRequestData [imageFile=" + imageFile + ", address="
				+ address + ", lat=" + lat + ", lng=" + lng + ", wasteTypes="
				+ wasteTypes + ", wasteVolume=" + wasteVolume
				+ ", wasteMetric=" + wasteMetric + ", wasteInfo=" + wasteInfo
				+ ", submitX=" + submitX + ", submitY=" + submitY + "]";
	}
	
}
