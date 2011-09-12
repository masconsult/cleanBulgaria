package eu.masconsult.connection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import eu.masconsult.MarkPlaceData;

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
	
	public void markPlace(MarkPlaceData data) throws ConnectionException {
	    try {
	    	HttpPost        markPlaceRequest   = new HttpPost("http://ng.btv.bg/map/add_waste_info.php");
		    MultipartEntity entity = fillData(data);
		    markPlaceRequest.setEntity( entity );
			client.execute(markPlaceRequest);
	    } catch (UnsupportedEncodingException e) {
	    	throw new ConnectionException(e);
		} catch (ClientProtocolException e) {
			throw new ConnectionException(e);
		} catch (IOException e) {
			throw new ConnectionException(e);
		}
	}
	
	private MultipartEntity fillData(MarkPlaceData data) throws UnsupportedEncodingException {
		MultipartEntity entity = new MultipartEntity( HttpMultipartMode.BROWSER_COMPATIBLE );

		if(data.getImage() != null) {
			entity.addPart( "file[]", new FileBody(data.getImage(), "image/gif" ));
		}
		if(data.getAddress() != null) {
			entity.addPart( "address", new StringBody( data.getAddress(), "text/plain",Charset.forName("UTF-8")));
		}
		
		if(data.getLat() != 0) {
			entity.addPart("lat", new StringBody(String.valueOf(data.getLat()), Charset.forName("UTF-8")));
		}
		if(data.getLng() != 0) {
			entity.addPart("lng", new StringBody(String.valueOf(data.getLng()), Charset.forName("UTF-8")));
		}
		if(data.getWasteTypes() != null) {
			for(int wasteType: data.getWasteTypes()) {
				if(wasteType >= 1 && wasteType <= 5) {
					entity.addPart("wastetype[" + wasteType + "]", new StringBody(String.valueOf(wasteType), Charset.forName("UTF-8")));
				}
			}
		}
//	    entity.addPart("wastevolume", new StringBody("10", Charset.forName("UTF-8")));
//	    entity.addPart("waste_metric", new StringBody("2", Charset.forName("UTF-8")));
		if(data.getWasteInfo() != null) {
			entity.addPart("wasteinfo", new StringBody( data.getWasteInfo(), Charset.forName("UTF-8")));
		}
	    entity.addPart("submit.x", new StringBody( "106", Charset.forName("UTF-8")));
	    entity.addPart("submit.y", new StringBody("12", Charset.forName("UTF-8")));
	    entity.addPart("go", new StringBody("go", Charset.forName("UTF-8")));
	    
	    return entity;
	}

}
//name="address"\r\n\r\nÑÐ». ÐÐ½Ð³ÐµÐ» ÐÑÐºÑÑÐµÑÐ»Ð¸ÐµÐ² , ÐÐ»Ð¾Ð²Ð´Ð¸Ð² 
//name="lat"\r\n\r\n42.145743 
//name="lng"\r\n\r\n24.748163 
//name="wastetype[1]"\r\n\r\n1 
//name="wastevolume"\r\n\r\n1 
//name="waste_metric"\r\n\r\n1 
//name="wasteinfo"\r\n\r\nÐ°ÑÐ´Ð°Ð´Ð°Ð´Ð°Ð´Ð°Ñ 
//name="file[]"; filename="" 
//name="go"\r\n\r\ngo 
//name="submit.x"\r\n\r\n53 
//name="submit.y"\r\n\r\n15