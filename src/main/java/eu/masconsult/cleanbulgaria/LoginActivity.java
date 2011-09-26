package eu.masconsult.cleanbulgaria;



import java.io.IOException;

import com.google.inject.Inject;

import eu.masconsult.cleanbulgaria.connection.Connection;
import eu.masconsult.cleanbulgaria.connection.ConnectionException;
import eu.masconsult.cleanbulgaria.connection.InvalidDataException;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
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
	
	@Inject
	Connection connection;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		emailTextEdit.setText("dani7@abv.bg");
		passwordTextEdit.setText("alabala");
		
		
		passwordTextEdit.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(event.getAction() == EditorInfo.IME_ACTION_DONE || event.getAction() == EditorInfo.IME_NULL) {
					loginButton.performClick();
					return true;
				}
				return false;
			}
		});
		
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
	
				String email = emailTextEdit.getText().toString();
				String password  = passwordTextEdit.getText().toString();
				if(email.equals("") || password.equals("")) {
					Toast validationToast = Toast.makeText(getApplicationContext(), "Попълнете Полетата", 2);
					validationToast.show();
					return;
				}
				try {		
					connection.login(email, password);
					startActivity(new Intent(getApplicationContext(), MainActivity.class));
				} catch (InvalidDataException e) {
					Toast validationToast = Toast.makeText(getApplicationContext(), "Невалиден Е-Мейл или парола", 2);
					validationToast.show();
					return;
				} catch (ConnectionException e) {
					Toast.makeText(getBaseContext(), "Възникна проблем при свързване", 3).show();
					return;
				}
				
			}
		});
	}
}
