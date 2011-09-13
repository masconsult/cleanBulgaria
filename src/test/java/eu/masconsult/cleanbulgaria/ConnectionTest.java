package eu.masconsult.cleanbulgaria;

import static org.junit.Assert.*;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.http.ParseException;
import org.junit.Before;
import org.junit.Test;

public class ConnectionTest {

	Connection connection;

	@Before
	public void setUp() {
		connection = new Connection();
	}

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

}
