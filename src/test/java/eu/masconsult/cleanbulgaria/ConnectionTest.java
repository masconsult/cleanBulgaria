package eu.masconsult.cleanbulgaria;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.junit.runner.RunWith;


import com.xtremelabs.robolectric.RobolectricTestRunner;

import eu.masconsult.cleanbulgaria.connection.Connection;
import eu.masconsult.cleanbulgaria.connection.ConnectionException;
import eu.masconsult.cleanbulgaria.connection.InvalidDataException;
import eu.masconsult.cleanbulgaria.connection.MarkRequestData;

@RunWith(RobolectricTestRunner.class)
public class ConnectionTest {

	Connection connection = new Connection();

	@Test(expected = InvalidDataException.class)
	public void testInvalidLoginData() throws ParseException,
			IOException, ConnectionException, InvalidDataException {

		
		RobolectricHelper.createPendingHttpResponse().withStatus(HttpStatus.SC_OK).withHeader("Location", "login.php").add();

		connection.login("alabala", "alabala");
		fail();
	}

	@Test
	public void testConnectionLoginOK() throws InvalidDataException,
			ConnectionException, IOException {

		RobolectricHelper.createPendingHttpResponse().withStatus(HttpStatus.SC_MOVED_TEMPORARILY).withHeader("Location", "index.php").add();

		connection.login("dani7@abv.bg", "alabala");

		Assert.assertEquals(HttpStatus.SC_MOVED_TEMPORARILY, connection.getConnectionStatusCode());
	}

	@Test
	public void testConnectionMark() throws ClientProtocolException,
			IOException, InvalidDataException, ConnectionException {

		RobolectricHelper.createPendingHttpResponse().withStatus(HttpStatus.SC_MOVED_TEMPORARILY).withHeader("Location", "index.php").add();
		
		connection.login("dani7@abv.bg", "alabala");

		RobolectricHelper.createPendingHttpResponse().withStatus(HttpStatus.SC_MOVED_TEMPORARILY).withHeader("Location", "thankyou.php").add();
		
		MarkRequestData data = new MarkRequestData();
		data.address = "ул. Ангел";
		data.imageFile = new File("dfsdfsd");
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
		Assert.assertEquals(HttpStatus.SC_MOVED_TEMPORARILY, status);
	}

}
