package eu.masconsult.cleanbulgaria.connection;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Connection {

	private HttpClient client;
	private int statusCode;
	private Charset utfSet = Charset.forName("UTF-8");
	//	private boolean loggedIn = true;

	public Connection() {
		client = new DefaultHttpClient();
		client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
	}

	public void login(final String email, final String password) throws InvalidDataException, ConnectionException {

		HttpResponse loginResponse;
		try {
			loginResponse = doLoginPost(email, password);
		} catch (IOException e) {
			throw new ConnectionException(e);
		}

		Header redirectHeader = loginResponse.getFirstHeader("Location");

		if(redirectHeader == null) {
			throw new InvalidDataException();
		}

		if(!redirectHeader.getValue().equals("index.php")) {
			throw new InvalidDataException();
		}


		int connectionStatus = loginResponse.getStatusLine().getStatusCode();
		statusCode = connectionStatus;
		//		loggedIn = true;
	}

	public int getConnectionStatusCode() {
		return statusCode;
	}

	private HttpResponse doLoginPost(final String email, final String password) throws IOException, ClientProtocolException {
		List<NameValuePair> loginParameters = setUpLoginParameters(email, password);
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(loginParameters, "UTF-8");
		HttpPost post = new HttpPost("http://ng.btv.bg/map/login.php");
		post.setEntity(entity);

		return client.execute(post);

	}

	private List<NameValuePair> setUpLoginParameters(final String email, final String password) {
		List<NameValuePair> formParameters = new ArrayList<NameValuePair>();
		formParameters.add(new BasicNameValuePair("email", email));
		formParameters.add(new BasicNameValuePair("password", password));
		formParameters.add(new BasicNameValuePair("submit", "Вход"));
		return formParameters;
	}

	public void mark(final MarkRequestData data) throws InvalidDataException, IOException, UserNotLoggedInException {
		//		if(!loggedIn) {
		//			throw new ClientProtocolException();
		//		}
		if(!isLoggedIn()) {
			throw new UserNotLoggedInException();
		}

		HttpResponse response = doMarkPost(data);

		Header redirectHeader = response.getFirstHeader("Location");
		if(redirectHeader == null) {
			throw new InvalidDataException();
		}

		if(!redirectHeader.getValue().equals("thankyou.php")) {
			throw new InvalidDataException();
		}
		int connectionStatus = response.getStatusLine().getStatusCode();

		this.statusCode = connectionStatus;
	}

	private HttpResponse doMarkPost(final MarkRequestData data) throws ClientProtocolException, IOException {
		MultipartEntity entity = setUpMarkParameters(data);
		HttpPost markPost = new HttpPost("http://ng.btv.bg/map/add_waste_info.php");
		markPost.setEntity(entity);
		return client.execute(markPost);
	}

	private MultipartEntity setUpMarkParameters(final MarkRequestData data) throws UnsupportedEncodingException {
		MultipartEntity entity = new MultipartEntity( HttpMultipartMode.BROWSER_COMPATIBLE );
		entity.addPart( "file[]", new FileBody(data.imageFile, "image/jpg"));
		entity.addPart("address", new StringBody(data.address, "text/plain", utfSet));
		entity.addPart("lat", new StringBody(data.lat, utfSet));
		entity.addPart("lng", new StringBody(data.lng, utfSet));
		for(Integer i : data.wasteTypes) {
			entity.addPart("wastetype["+ i +"]", new StringBody(i.toString(), utfSet));
		}
		entity.addPart("wastevolume", new StringBody(data.wasteVolume, utfSet));
		entity.addPart("waste_metric", new StringBody(data.wasteMetric, utfSet));
		entity.addPart("wasteinfo", new StringBody(data.wasteInfo, "text/plain", utfSet));
		entity.addPart("submit.x", new StringBody(data.submitX, utfSet));
		entity.addPart("submit.y", new StringBody(data.submitY, utfSet));
		entity.addPart("go", new StringBody("go", utfSet));
		return entity;
	}

	public boolean isLoggedIn() throws ClientProtocolException, IOException {
		String pageSource = getPageSource("http://ng.btv.bg/map/thankyou.php");
		return loginLinkIsNotFoundOn(pageSource);
	}

	private String getPageSource(String pageUrl) throws IOException, ClientProtocolException {
		HttpGet request = new HttpGet(pageUrl);
		HttpResponse response = client.execute(request);
		return EntityUtils.toString(response.getEntity());
	}

	private boolean loginLinkIsNotFoundOn(String pageSource) {
		return !pageSource.contains("login.php");
	}
}
