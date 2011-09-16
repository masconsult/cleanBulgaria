package eu.masconsult.cleanbulgaria.connection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.ClientContext; 
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class Connection {

	private final String loginRedirectUri = "/map/index.php";
	private final String markSuccess = "/map/thankyou.php";
	private HttpClient client;
	private HttpContext context;
	private int statusCode;
	private Charset utfSet = Charset.forName("UTF-8");
	private boolean loggedIn = false;

	public Connection() {
		client = new DefaultHttpClient();
		context = new BasicHttpContext();
		client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
	}

	public void login(final String email, final String password) throws InvalidDataException, ConnectionException {
		
		HttpResponse loginResponse;
		
		try {
			loginResponse = doLoginPost(email, password);
		} catch (Exception e) {
			throw new ConnectionException(e);
		}
		
		try {
			String pageSource = EntityUtils.toString(loginResponse.getEntity());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpUriRequest currentRequest = (HttpUriRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
		
		if(!currentRequest.getURI().toString().equals(loginRedirectUri)) {
			throw new InvalidDataException();
		}
		
		int connectionStatus = loginResponse.getStatusLine().getStatusCode();
		
		statusCode = connectionStatus;
		loggedIn = true;
	}
	
	public int getConnectionStatusCode() {
		return statusCode;
	}

	private HttpResponse doLoginPost(final String email, final String password) throws IOException {
		List<NameValuePair> loginParameters = setUpLoginParameters(email, password);
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(loginParameters, "UTF-8");
		HttpPost post = new HttpPost("http://ng.btv.bg/map/login.php");
		post.setEntity(entity);

		return client.execute(post, context);

	}

	private List<NameValuePair> setUpLoginParameters(final String email, final String password) {
		List<NameValuePair> formParameters = new ArrayList<NameValuePair>();
		formParameters.add(new BasicNameValuePair("email", email));
		formParameters.add(new BasicNameValuePair("password", password));
		formParameters.add(new BasicNameValuePair("submit", "Вход"));
		return formParameters;
	}
	
	public void mark(final MarkRequestData data) throws InvalidDataException, IOException {
		if(!loggedIn) {
			throw new ClientProtocolException();
		}
		HttpResponse response = doMarkPost(data);
		
		int connectionStatus = response.getStatusLine().getStatusCode();
		
		HttpUriRequest currentRequest = (HttpUriRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
		
		EntityUtils.toString(response.getEntity());
		if(!currentRequest.getURI().toString().equals(markSuccess)) {
			throw new InvalidDataException();
		}
		this.statusCode = connectionStatus;
	}
	
	private HttpResponse doMarkPost(final MarkRequestData data) throws ClientProtocolException, IOException {
		MultipartEntity entity = setUpMarkParameters(data);
		HttpPost markPost = new HttpPost("http://ng.btv.bg/map/add_waste_info.php");
		markPost.setEntity(entity);
		return client.execute(markPost, context);
		
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
	
	public boolean isLoggedIn() {
		return loggedIn;
	}

}
