package eu.masconsult.cleanbulgaria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.inject.Inject;

import eu.masconsult.cleanbulgaria.connection.Connection;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.view.View.OnClickListener;

public class MainActivity extends RoboActivity {
	
	@Inject
	Connection connection;
	@InjectView(R.id.markPlaceButton)
	Button markPlaceButton;
	
	@InjectView(R.id.takePicture)
	Button takePictureButton;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen_layout);
		
		markPlaceButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), UploadActivity.class));
			}
		});
		
	}

}
