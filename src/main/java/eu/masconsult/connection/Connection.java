package eu.masconsult.connection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class Connection {

	private HttpClient client;

	public Connection() {
		init();
	}

	private void init() {
		client = new DefaultHttpClient();
	}

	public void login(final String username, final String password)
			throws ConnectionException {
		List<NameValuePair> formParameters = new ArrayList<NameValuePair>();
		formParameters.add(new BasicNameValuePair("email", username));
		formParameters.add(new BasicNameValuePair("password", password));
		formParameters.add(new BasicNameValuePair("submit", "Вход"));

		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
					formParameters, "UTF-8");
			HttpPost loginRequest = new HttpPost(
					"http://ng.btv.bg/map/login.php");
			loginRequest.setEntity(entity);
			HttpResponse response = client.execute(loginRequest);
			String responseContent = EntityUtils.toString(response.getEntity());
			if (responseContent.contains("password")) {
				Log.d("CONNECTION", "Incorect login data: "
						+ username + ", " + password);
				throw new ConnectionException("Incorect login data: "
						+ username + ", " + password);
			}
		} catch (UnsupportedEncodingException e) {
			throw new ConnectionException(e);
		} catch (ClientProtocolException e) {
			throw new ConnectionException(e);
		} catch (IOException e) {
			throw new ConnectionException(e);
		}

	}

}
