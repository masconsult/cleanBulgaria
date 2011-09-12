package eu.masconsult.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;


import eu.masconsult.MarkPlaceData;
import eu.masconsult.R;
import eu.masconsult.R.layout;
import eu.masconsult.connection.Connection;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class UploadScreenActivity extends RoboActivity{
	
	private static final int LIGHT_GARBAGE_TYPE = 1;
	private static final int HEAVY_GARBAGE_TYPE = 2;
	
	@InjectView(R.id.lightGarbage)
	CheckBox lightGarbageCheckBox;
	@InjectView(R.id.heavyGarbage)
	CheckBox heavyGarbageCheckBox;
	@InjectView(R.id.commentField)
	TextView commentField;
	@InjectView(R.id.markPlaceBtn)
	Button markPlaceBtn;
	
	@Inject
	LocationManager locationManager;
	@Inject
	Connection connection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_layout);
		init();
	}
	
	private void init() {
		markPlaceBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				upload();
			}
		});
	}
	
	private void upload() {
		List<Integer> wasteTypes = new ArrayList<Integer>();
		if(lightGarbageCheckBox.isChecked()) {
			wasteTypes.add(LIGHT_GARBAGE_TYPE);
		}
		if(heavyGarbageCheckBox.isChecked()) {
			wasteTypes.add(HEAVY_GARBAGE_TYPE);
		}
		
//		null, wasteTypes, commentField.getText().toString(), address, lat, lng
		MarkPlaceData uploadData = new MarkPlaceData();
		uploadData.setWasteTypes(wasteTypes);
		uploadData.setWasteInfo(commentField.getText().toString());
		fillLocationData(uploadData);
	}

	private void fillLocationData(MarkPlaceData uploadData) {
	}
	
}
