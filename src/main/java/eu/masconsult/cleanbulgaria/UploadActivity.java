package eu.masconsult.cleanbulgaria;

import java.io.File;
import java.io.IOException;
import com.google.inject.Inject;

import eu.masconsult.cleanbulgaria.connection.Connection;
import eu.masconsult.cleanbulgaria.connection.InvalidDataException;
import eu.masconsult.cleanbulgaria.connection.MarkRequestData;
import eu.masconsult.cleanbulgaria.connection.PositionException;
import eu.masconsult.cleanbulgaria.connection.PositionManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.view.View.OnClickListener;

public class UploadActivity extends RoboActivity {
	
	@InjectView(R.id.wasteTypeSelectButton)
	private Button wasteTypeSelectButton;
	
	@InjectView(R.id.metricTypeSpinner)
	private Spinner metricTypeSpinner;
	
	@InjectView(R.id.quantatyText)
	private EditText quantatyText;
	
	@InjectView(R.id.uploadDataButton)
	private Button uploadDataButton;
	
	@InjectView(R.id.wasteInfoTextEdit)
	private EditText wasteInfoTextEdit;
	
	@Inject
	private PositionManager positionManager;
	
	@Inject 
	private Connection connection;
	
	private Uri imageFileUri = null;
	
	private boolean[] selectedWasteTypes = new boolean[5];
	
	private String metric;
	
	private AlertDialog selectWasteTypeDialog;
	
	private final CharSequence[] wasteTypes = {
			"Леки Битови Отпадъци", 
			"Тежки битови отпадъци", 
			"Строителни",
			"Индустриални",
			"Други"
	};

	private final class UploadDataButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if(!isDataValid()) {
				return;
			}
			String addr = null;
			Location loc = positionManager.getPosition();
			MarkRequestData markData = setUpMarkData();
			
			try {
				connection.mark(markData);
			} catch (InvalidDataException e) {
				Toast toast = Toast.makeText(getApplicationContext(), "Невалидни данни", 3);
				toast.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private final class OnMetricTypeSelectedListener implements
			AdapterView.OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			metric = Integer.toString(++position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	}

	private final class WasteTypeButtonListener implements OnClickListener {
		private final Builder dialogBuilder;

		private WasteTypeButtonListener(Builder dialogBuilder) {
			this.dialogBuilder = dialogBuilder;
		}

		@Override
		public void onClick(View v) {
			selectWasteTypeDialog = dialogBuilder.create();
			selectWasteTypeDialog.show();
		}
	}

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_screen_layout);
		
		final AlertDialog.Builder dialogBuilder = setUpDialogBuilder();
		wasteTypeSelectButton.setOnClickListener(new WasteTypeButtonListener(dialogBuilder));
		uploadDataButton.setOnClickListener(new UploadDataButtonListener());
		setUpMetricSpinner();
		try {
			positionManager.initPositionManager(getApplicationContext());
		} catch (PositionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	private void setUpMetricSpinner() {
		ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.metricTypes, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    metricTypeSpinner.setAdapter(adapter);
	    metricTypeSpinner.setOnItemSelectedListener(new OnMetricTypeSelectedListener());
	}	
	
	private AlertDialog.Builder setUpDialogBuilder() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Тип на отпадъците");
		builder.setMultiChoiceItems(wasteTypes, selectedWasteTypes, new DialogInterface.OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				selectedWasteTypes[which] = isChecked;
			}
		});
		return builder;
	}

	
	private MarkRequestData setUpMarkData() {
	
		MarkRequestData data = new MarkRequestData();
		for(int i = 0; i < selectedWasteTypes.length; i++) {
			if(selectedWasteTypes[i] == true) {
				data.wasteTypes.add(i+1);
			}
		}
		data.wasteVolume = quantatyText.getText().toString();
		data.wasteMetric = metric;
		data.wasteInfo = wasteInfoTextEdit.getText().toString();
		data.imageFile = new File(imageFileUri.toString());
		data.address = "Улица Ангел";
		data.lat = "";
		data.lng = "";
		data.submitX = "106";
		data.submitY = "12";
		
		return data;
	}
	
	private boolean isDataValid() {
		if(!isWasteTypeSelected()) {
			Toast noSelectedWasteTypes = Toast.makeText(getApplicationContext(), "Моля изберете тип на боклука", 3);
			noSelectedWasteTypes.show();
			return false;
		}
		if(metric.isEmpty()) {
			Toast noSelectedMetric = Toast.makeText(getApplicationContext(), "Моля изберете мярка", 3);
			noSelectedMetric.show();
			return false;
		}
		if(quantatyText.getText().toString().isEmpty()) {
			Toast noQuantaty = Toast.makeText(getApplicationContext(), "Моля въведете количество", 3);
			noQuantaty.show();
			return false;
		}
		if(imageFileUri == null) {
			Toast invalidFile = Toast.makeText(getApplicationContext(), "Невалидна снимка", 3);
			invalidFile.show();
			return false;
		}
		imageFileUri = (Uri)getIntent().getData();
		return true;
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(getApplicationContext(), TakePhotoActivity.class));
	}
	
	private boolean isWasteTypeSelected() {
		for(int i = 0; i < selectedWasteTypes.length; i++) {
			if(selectedWasteTypes[i] == true) {
				return true;
			}
		}
		return false;
	}

	public boolean[] getWasteTypes() {
		return selectedWasteTypes;
	}
	
	protected String getMetric() {
		return metric;
	}
}
