package eu.masconsult.activity;


import com.google.inject.Inject;

import eu.masconsult.PositionException;
import eu.masconsult.PositionManager;
import eu.masconsult.R;
import eu.masconsult.R.id;
import eu.masconsult.R.layout;
import eu.masconsult.connection.Connection;
import eu.masconsult.connection.ConnectionException;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends RoboActivity{
	
	@InjectView(R.id.loginBtn) 
	Button loginBtn;
	@InjectView(R.id.username)
	TextView username;
	@InjectView(R.id.password)
	TextView password;
	@Inject
	Connection connection;
	@Inject
	PositionManager positionManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		init();
	}
	
	private void init() {
		try {
			positionManager.initPositionManager(getApplicationContext());
		} catch (PositionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		username.setText("dani7@abv.bg");
		password.setText("alabala");
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
					try {
						connection.login(username.getText().toString(), password.getText().toString());
						startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
					} catch (ConnectionException e) {
						Toast toast = Toast.makeText(getApplicationContext(), "Incorect login data.", Toast.LENGTH_SHORT);
						toast.show();
					}
			}
		});
	}

}
