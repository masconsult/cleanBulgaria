package eu.masconsult.cleanbulgaria;

import static org.junit.Assert.*;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.junit.Before;
import org.junit.Test;

import eu.masconsult.cleanbulgaria.connection.Connection;
import eu.masconsult.cleanbulgaria.connection.ConnectionException;
import eu.masconsult.cleanbulgaria.connection.InvalidDataException;
import eu.masconsult.cleanbulgaria.connection.MarkRequestData;

public class ConnectionTest {

	Connection connection = new Connection();

	
	@Test
	public void testInvalidConnectionLogin() throws ParseException,
			IOException, ConnectionException {
		try {
			connection.login("alabala", "alabala");
			fail();
		} catch (InvalidDataException e) {
		}
	}

	@Test
	public void testConnectionLoginOK() throws InvalidDataException,
			ConnectionException, IOException {
		connection.login("dani7@abv.bg", "alabala");

		Assert.assertEquals(302, connection.getConnectionStatusCode());
	}
	
	@Test
	public void testConnectionMark() throws ClientProtocolException, IOException, InvalidDataException, ConnectionException {
		
		connection.login("dani7@abv.bg", "alabala");
		
		MarkRequestData data = new MarkRequestData();
		data.address = "ул. Ангел";
		data.imageFile = new File("/home/rangel/android_logo.gif");
		data.wasteInfo = "Много мръсно";
		data.wasteMetric = "2";
		List<Integer> types = new ArrayList<Integer>();
		types.add(2);
		data.wasteTypes = types;
		data.wasteVolume = "19";
		data.lat = "42.145362";
		data.lng = "24.746618";
		data.submitX = "106";
		data.submitY = "12";
		
		connection.mark(data);
		
		int status = connection.getConnectionStatusCode();
		Assert.assertEquals(302, status);
	}

}
