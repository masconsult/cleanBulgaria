package eu.masconsult.cleanbulgaria;

import java.io.UnsupportedEncodingException;
import junit.framework.Assert;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;

import com.xtremelabs.robolectric.shadows.ShadowHandler;
import com.xtremelabs.robolectric.shadows.ShadowToast;
import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


import eu.masconsult.cleanbulgaria.LoginActivity;
import eu.masconsult.cleanbulgaria.R;


@RunWith(RobolectricTestRunner.class)
public class LoginScreenTest {

	private LoginActivity loginActivity;
	private Button logInButton;
	private EditText emailText;
	private EditText passwordText;

	@Before
	public void setUp() {
		loginActivity = new LoginActivity();
		loginActivity.onCreate(null);
		logInButton = (Button) loginActivity.findViewById(R.id.loginButton);
		emailText = (EditText) loginActivity.findViewById(R.id.emailText);
		passwordText = (EditText) loginActivity.findViewById(R.id.passwordText);
		
	}

	@Test
	public void testEmptyData() {
		emailText.setText("");
		passwordText.setText("");
		ShadowHandler.idleMainLooper(); 
		logInButton.performClick();
		Assert.assertEquals(ShadowToast.getTextOfLatestToast(), "Попълнете Полетата"); 
	}
	
	@Test
	public void testInvalidData() throws UnsupportedEncodingException {
		RobolectricHelper.createPendingHttpResponse().withStatus(HttpStatus.SC_OK).withHeader("Location", "login.php").add();
		
		ShadowHandler.idleMainLooper(); 
		emailText.setText("invalid@invalid.com");
		passwordText.setText("password");
		
		logInButton.performClick();
		
	
		
		Assert.assertEquals(ShadowToast.getTextOfLatestToast(), "Невалиден Е-Мейл или парола"); 
	}
	
	@Test
	public void testLoginSuccess() {
		RobolectricHelper.createPendingHttpResponse().withStatus(HttpStatus.SC_OK).withHeader("Location", "index.php").add();
		emailText.setText("dani7@abv.bg");
		passwordText.setText("alabala");
		
		logInButton.performClick();
		
		ShadowActivity shadowOfLoginActivity = shadowOf(loginActivity);
		
		Intent startedIntent = shadowOfLoginActivity.getNextStartedActivity();
		
		assertThat(startedIntent.getComponent().getClassName(), equalTo(MainActivity.class.getName()));
	}

}
