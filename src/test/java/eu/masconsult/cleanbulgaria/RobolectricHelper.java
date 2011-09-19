package eu.masconsult.cleanbulgaria;

import java.io.UnsupportedEncodingException;

import org.apache.http.ProtocolVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;

import com.xtremelabs.robolectric.Robolectric;

public class RobolectricHelper {

	BasicHttpResponse response;
	public static RobolectricHelper createPendingHttpResponse() {
		return new RobolectricHelper();
	}
	
	public RobolectricHelper withStatus(int status)  {
		response = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 0), status, "reason");
		StringEntity entity = null;
		try {
			entity = new StringEntity("content", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		response.setEntity(entity);
		return this;
	}
	
	public RobolectricHelper withHeader(String name, String value) {
		response.addHeader(name, value);
		return this;
	}
	
	public void add() {
		Robolectric.addPendingHttpResponse(response);
	}
	
	
}
