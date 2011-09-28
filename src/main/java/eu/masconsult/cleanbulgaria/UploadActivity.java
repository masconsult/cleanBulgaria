package eu.masconsult.cleanbulgaria;

import java.io.File;
import java.io.IOException;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.inject.Inject;

import eu.masconsult.cleanbulgaria.connection.Connection;
import eu.masconsult.cleanbulgaria.connection.InvalidDataException;
import eu.masconsult.cleanbulgaria.connection.MarkRequestData;
import eu.masconsult.cleanbulgaria.connection.PositionException;
import eu.masconsult.cleanbulgaria.connection.PositionManager;
import eu.masconsult.cleanbulgaria.connection.UserNotLoggedInException;

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
	private ProgressDialog progressDialog;


	private final class UploadDataButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (!isDataValid()) {
				return;
			}
			new MarkPlaceTask().execute();
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

		progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle(R.string.loginProcessTitle);
		progressDialog.setMessage(getString(R.string.loginProcessMessage));

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


	private boolean isDataValid() {
		if (!isWasteTypeSelected()) {
			Toast noSelectedWasteTypes = Toast.makeText(getApplicationContext(), "Моля изберете тип на боклука", Toast.LENGTH_LONG);
			noSelectedWasteTypes.show();
			return false;
		}
		if (metric.isEmpty()) {
			Toast noSelectedMetric = Toast.makeText(getApplicationContext(), "Моля изберете мярка", Toast.LENGTH_LONG);
			noSelectedMetric.show();
			return false;
		}
		if (quantatyText.getText().toString().isEmpty()) {
			Toast noQuantaty = Toast.makeText(getApplicationContext(), "Моля въведете количество", Toast.LENGTH_LONG);
			noQuantaty.show();
			return false;
		}

		imageFileUri = getIntent().getData();
		if (imageFileUri == null) {
			Toast invalidFile = Toast.makeText(getApplicationContext(), "Невалидна снимка", Toast.LENGTH_LONG);
			invalidFile.show();
			return false;
		}

		return true;
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(getApplicationContext(), MainActivity.class));
	}

	private boolean isWasteTypeSelected() {
		for (int i = 0; i < selectedWasteTypes.length; i++) {
			if (selectedWasteTypes[i] == true) {
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

	private class MarkPlaceTask extends AsyncTask<Void, Void, Integer> {

		private static final int SUCCESS = 1;
		private static final int INVALID_DATA = 2;
		private static final int UNABLE_TO_CONNECT = 3;
		private static final int USER_NOT_LOGGED_IN = 4;

		@Override
		protected void onPreExecute() {
			progressDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			Location currentLocation = positionManager.getPosition();
			MarkRequestData markData = setUpMarkData(currentLocation);
			try {
				connection.mark(markData);
				return SUCCESS;
			} catch (InvalidDataException e) {
				return INVALID_DATA;
			} catch (IOException e) {
				return UNABLE_TO_CONNECT;
			} catch (UserNotLoggedInException e) {
				return USER_NOT_LOGGED_IN;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			progressDialog.dismiss();
			switch (result) {
			case SUCCESS:
				Toast.makeText(UploadActivity.this, R.string.markRequestSuccessful, Toast.LENGTH_LONG).show();
				break;

			case INVALID_DATA:
				Toast.makeText(UploadActivity.this, R.string.invalidCredentials, Toast.LENGTH_LONG).show();
				break;

			case UNABLE_TO_CONNECT:
				Toast.makeText(UploadActivity.this, R.string.noConnection, Toast.LENGTH_LONG).show();
				break;

			case USER_NOT_LOGGED_IN:
				Intent intent = new Intent(UploadActivity.this, LoginActivity.class);
				startActivity(intent);
				break;
			}
		}

		private MarkRequestData setUpMarkData(Location location) {

			MarkRequestData data = new MarkRequestData();
			for (int i = 0; i < selectedWasteTypes.length; i++) {
				if (selectedWasteTypes[i] == true) {
					data.wasteTypes.add(i + 1);
				}
			}
			data.wasteVolume = quantatyText.getText().toString();
			data.wasteMetric = metric;
			data.wasteInfo = wasteInfoTextEdit.getText().toString();
			data.imageFile = new File(imageFileUri.toString());
			if (location == null) {
				data.lat = "42.155931";
				data.lng = "24.714543";
				data.address = "Улица Ангел";
			} else {
				data.lat = String.valueOf(location.getLatitude());
				data.lng = String.valueOf(location.getLongitude());
				try {
					data.address = positionManager.getAddress();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			data.submitX = "0";
			data.submitY = "0";

			return data;
		}
	}
}
