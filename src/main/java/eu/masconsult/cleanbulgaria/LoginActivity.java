package eu.masconsult.cleanbulgaria;

import java.io.IOException;

import com.google.inject.Inject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class LoginActivity extends RoboActivity {
	
	@InjectView(R.id.emailText)
	EditText emailTextEdit;
	
	@InjectView(R.id.passwordText)
	EditText passwordTextEdit;
	
	@InjectView(R.id.loginButton)
	Button loginButton;
	
	//Toast validationToast;
	
	@Inject
	Connection connection;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		
		
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(emailTextEdit.length() == 0  || passwordTextEdit.length() == 0) {
					Toast validationToast = Toast.makeText(getApplicationContext(), "Попълнете Полетата", 2);
					validationToast.show();
					return;
				}
				String email = emailTextEdit.getText().toString();
				String password  = passwordTextEdit.getText().toString();
				
				try {
					connection.login(email, password);
				} catch (InvalidDataException e) {
					Toast validationToast = Toast.makeText(getApplicationContext(), "Невалиден Е-Мейл или парола", 2);
					validationToast.show();
				} catch (ConnectionException e) {
					
				}
				
			}
		});
	}
}
