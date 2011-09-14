package eu.masconsult.cleanbulgaria;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.view.View.OnClickListener;

public class UploadActivity extends RoboActivity {

	@InjectView(R.id.wasteTypeSelectButton)
	private Button wasteTypeSelectButton;
	
	
	private AlertDialog alert;
	
	private final CharSequence[] items = {
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

		wasteTypeSelectButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alert = dialogBuilder.create();
				alert.show();
			}
		});
	}
	
	public AlertDialog getDialog() {
		return alert;
	}
	
	public AlertDialog.Builder setUpDialogBuilder() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Тип на отпадъците");
		builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				
			}
		
		});
		
		return builder;
	}
}
