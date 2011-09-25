package eu.masconsult.cleanbulgaria;


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

public class TakePhotoActivity extends RoboActivity {

	private static final int CAPTURE_IMAGE_REQUEST = 1;

	private Uri imageUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString()); 
		startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
	}

	private void showUploadScreen(File image) {
		Intent uploadData = new Intent(getApplicationContext(), UploadActivity.class);
		uploadData.setData(imageUri);
		startActivity(uploadData);
	}

	private void savePicture(Bitmap image) {
		try {
			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File(sdCard.getAbsolutePath() + "/cleanBulgaria");
			dir.mkdirs();
			File imageFile = new File(dir, "img.jpeg");
			imageFile.createNewFile();

			FileOutputStream out = new FileOutputStream(imageFile);
			image.compress(Bitmap.CompressFormat.JPEG, 100, out);
			imageUri = Uri.parse(imageFile.getAbsolutePath());
			showUploadScreen(imageFile);
		} catch (Exception e) {
			Toast.makeText(this, "Picture was not taken", Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
