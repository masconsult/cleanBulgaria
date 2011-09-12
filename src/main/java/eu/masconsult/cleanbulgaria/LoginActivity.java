package eu.masconsult.cleanbulgaria;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
	
	Toast validationToast;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		validationToast = Toast.makeText(getApplicationContext(), "", 2);
		
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(emailTextEdit.getText().toString().isEmpty() || passwordTextEdit.getText().toString().isEmpty()) {
					validationToast.setText("Попълнете Полетата");
					validationToast.show();
					return;
				}
				if(!emailTextEdit.getText().toString().contains("@")) {
					validationToast.setText("Невалиден Е-мейл");
					validationToast.show();
				}
			}
		});
	}
}
