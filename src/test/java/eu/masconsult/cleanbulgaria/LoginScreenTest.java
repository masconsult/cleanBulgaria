package eu.masconsult.cleanbulgaria;

import java.io.UnsupportedEncodingException;
import junit.framework.Assert;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowHandler;
import com.xtremelabs.robolectric.shadows.ShadowToast;

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
	
	

}
