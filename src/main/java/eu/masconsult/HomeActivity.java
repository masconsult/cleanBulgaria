package eu.masconsult;

import android.app.Activity;
import android.os.Bundle;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Venelin Valkov <venelin@masconsult.eu>
 * Date: 08-09-2011
 */
public class HomeActivity extends Activity {
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    try {

      HttpClient client = new DefaultHttpClient();

      HttpPost post = loginRequest();

      client.execute(post);

      HttpPost wastePost = markRequest();
      client.execute(wastePost, new ResponseHandler<String>() {
        @Override
        public String  handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
//          Log.d("Connection", httpResponse.getStatusLine().toString());
          System.out.println(EntityUtils.toString(httpResponse.getEntity()));

//          InputStream inputStream = httpResponse.getEntity().getContent();
          return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("ERROR!");
    }
  }

  private HttpPost markRequest() throws UnsupportedEncodingException {

//    HttpClient client = new DefaultHttpClient();
//    client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

    HttpPost        post   = new HttpPost("http://ng.btv.bg/map/add_waste_info.php");
    MultipartEntity entity = new MultipartEntity( HttpMultipartMode.BROWSER_COMPATIBLE );

// For File parameters

    File uploadFile = new File("/sdcard/android_logo.gif");

    entity.addPart( "file[]", new FileBody(uploadFile, "image/gif" ));

// For usual String parameters
    entity.addPart( "address", new StringBody( "ул. Ангел", "text/plain",
                                               Charset.forName("UTF-8")));
    entity.addPart("lat", new StringBody("42.145362",
            Charset.forName("UTF-8")));
    entity.addPart("lng", new StringBody("24.746618",
            Charset.forName("UTF-8")));
    entity.addPart("wastetype[2]", new StringBody("2",
            Charset.forName("UTF-8")));
    entity.addPart("wastevolume", new StringBody("10",
            Charset.forName("UTF-8")));
    entity.addPart("waste_metric", new StringBody("2",
            Charset.forName("UTF-8")));
    entity.addPart("wasteinfo", new StringBody( "Mnogo boklyk, batka",
                                               Charset.forName("UTF-8")));
    entity.addPart("submit.x", new StringBody( "106",
                                               Charset.forName("UTF-8")));
    entity.addPart("submit.y", new StringBody("12",
            Charset.forName("UTF-8")));
    entity.addPart("go", new StringBody("go",
            Charset.forName("UTF-8")));

    post.setEntity( entity );

// Here we go!
//    String response = EntityUtils.toString( client.execute( post ).getEntity(), "UTF-8" );

//    client.getConnectionManager().shutdown();


//    HttpPost post = new HttpPost("http://ng.btv.bg/map/add_waste_info.php");
//
//    List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//    parameters.add(new BasicNameValuePair("address", "ул. Ангел"));
//    parameters.add(new BasicNameValuePair("lat", "42.145362"));
//    parameters.add(new BasicNameValuePair("lng", "24.746618"));
//    parameters.add(new BasicNameValuePair("wastetype[2]", "2"));
//    parameters.add(new BasicNameValuePair("wastevolume", "10"));
//    parameters.add(new BasicNameValuePair("waste_metric", "2"));
//    parameters.add(new BasicNameValuePair("wasteinfo", "Mnogo boklyk, batka"));
//    parameters.add(new BasicNameValuePair("submit.x", "106"));
//    parameters.add(new BasicNameValuePair("submit.y", "12"));
//    parameters.add(new BasicNameValuePair("go", "go"));
//    UrlEncodedFormEntity wasteEntity = new UrlEncodedFormEntity(parameters, "UTF-8");
//    post.setEntity(wasteEntity);
    return post;
  }

  private HttpPost loginRequest() throws UnsupportedEncodingException {
    List<NameValuePair> formParameters = new ArrayList<NameValuePair>();
    formParameters.add(new BasicNameValuePair("email", "dani7@abv.bg"));
    formParameters.add(new BasicNameValuePair("password", "alabala"));
    formParameters.add(new BasicNameValuePair("submit", "Вход"));
    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParameters, "UTF-8");
    HttpPost post = new HttpPost("http://ng.btv.bg/map/login.php");
    post.setEntity(entity);
    return post;
  }
}