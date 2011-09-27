package eu.masconsult.cleanbulgaria;

import java.io.UnsupportedEncodingException;

import android.content.DialogInterface;
import android.widget.PopupWindow;
import com.xtremelabs.robolectric.shadows.*;
import junit.framework.Assert;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.xtremelabs.robolectric.RobolectricTestRunner;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


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
        String expected = loginActivity.getString(R.string.invalidCredentials);
        Assert.assertEquals(ShadowToast.getTextOfLatestToast(), expected);
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
