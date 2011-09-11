package eu.masconsult;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Set;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class MainScreenActivity extends RoboActivity {

	private static final int CAPTURE_IMAGE_REQUEST = 1;

	@InjectView(R.id.markPlaceBtn)
	Button markPlaceBtn;

	private Uri imageUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen_layout);
		init();
	}

	private void init() {
		markPlaceBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
			}
		});
	}
	
	private void showUploadScreen(File image) {
		
	}
	
	private void savePicture(Bitmap image) {
		try {
			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File(sdCard.getAbsolutePath()
					+ "/cleanBulgaria");
			dir.mkdirs();
			File file = new File(dir, "img.jpeg");
			file.createNewFile();

			FileOutputStream out = new FileOutputStream(file);
			image.compress(Bitmap.CompressFormat.JPEG, 100, out);
		} catch (Exception e) {
			Toast.makeText(this, "Picture was not taken",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == CAPTURE_IMAGE_REQUEST) {
			if (resultCode == RESULT_OK) {
				Bitmap image = (Bitmap) data.getExtras().get("data");
				savePicture(image);
			}
		}
	}

}
