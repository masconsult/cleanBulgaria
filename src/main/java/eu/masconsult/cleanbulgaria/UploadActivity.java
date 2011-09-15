package eu.masconsult.cleanbulgaria;




import java.io.File;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.google.inject.Inject;

import eu.masconsult.cleanbulgaria.connection.Connection;
import eu.masconsult.cleanbulgaria.connection.MarkRequestData;
import eu.masconsult.cleanbulgaria.connection.PositionException;
import eu.masconsult.cleanbulgaria.connection.PositionManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.view.View.OnClickListener;

public class UploadActivity extends RoboActivity {

	private final class UploadDataButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			
			
			MarkRequestData markData = setUpMarkData();
			
			try {
				connection.mark(markData);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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

	@InjectView(R.id.wasteTypeSelectButton)
	private Button wasteTypeSelectButton;
	
	@InjectView(R.id.metricTypeSpinner)
	Spinner metricTypeSpinner;
	
	@InjectView(R.id.quantatyText)
	EditText quantatyText;
	
	@InjectView(R.id.uploadDataButton)
	Button uploadDataButton;
	
	@InjectView(R.id.wasteInfoTextEdit)
	EditText wasteInfoTextEdit;
	
	@Inject
	PositionManager positionManager;
	
	@Inject 
	Connection connection;
	
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
		data.imageFile = new File("/sdcard/waste.jpg");
		data.address = "Пловдив, Тракия, Лаута";
		data.lat = "42.138877";
		data.lng = "24.774903";
		data.submitX = "106";
		data.submitY = "12";
		System.out.println(data);
		
		return data;
	}
	
	public boolean[] getWasteTypes() {
		return selectedWasteTypes;
	}
	
}
