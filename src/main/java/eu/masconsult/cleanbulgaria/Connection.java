package eu.masconsult.cleanbulgaria;

import java.io.IOException;
import java.io.UTFDataFormatException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Connection {

	private HttpClient client;
	private int statusCode;
	private static final String invalidDataError = "for=\"email\" class=\"error\"";

	public Connection() {
		client = new DefaultHttpClient();
	}

	public void login(final String email, final String password) throws InvalidDataException, ConnectionException {
		
		HttpResponse loginResponse;

		try {
			loginResponse = doLoginPost(email, password);
		} catch (Exception e) {
			throw new ConnectionException();
		}
		
		int connectionStatus = loginResponse.getStatusLine().getStatusCode();
		String pageSource = null;
		try {
			pageSource = EntityUtils.toString(loginResponse.getEntity());
		} catch (Exception e) {
			throw new ConnectionException();
		}
		
		System.out.println("++++++++++++++++++++++++++++++++++");
		System.out.println("CONN STATUS: " + connectionStatus);
		System.out.println("++++++++++++++++++++++++++++++++++");
		System.out.println(pageSource);
	
		if(pageSource.contains(invalidDataError)) {
			throw new InvalidDataException();
		}
		statusCode = connectionStatus;
	}
	
	public int getConnectionStatusCode() {
		return statusCode;
	}

	public HttpResponse doLoginPost(final String email, final String password) throws ClientProtocolException, IOException  {
		List<NameValuePair> loginParameters = setUpLoginParameters(email, password);
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(loginParameters, "UTF-8");
		HttpPost post = new HttpPost("http://ng.btv.bg/map/login.php");
		post.setEntity(entity);

		return client.execute(post);

	}

	private List<NameValuePair> setUpLoginParameters(final String email,
			final String password) {
		List<NameValuePair> formParameters = new ArrayList<NameValuePair>();
		formParameters.add(new BasicNameValuePair("email", email));
		formParameters.add(new BasicNameValuePair("password", password));
		formParameters.add(new BasicNameValuePair("submit", "Вход"));
		return formParameters;
	}

}
