package eu.masconsult.cleanbulgaria;

import java.io.File;
import java.io.FileOutputStream;

import roboguice.activity.RoboActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

public class MainActivity extends RoboActivity {

	private static final int CAPTURE_IMAGE_REQUEST = 1;
	private static final int UPLOAD_DATA_REQUEST = 2;


	//	@InjectView(R.id.markPlaceButton)
	//	Button markPlaceButton;
	//
	//	@InjectView(R.id.takePicture)
	//	Button takePictureButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//		setContentView(R.layout.main_screen_layout);

		//		markPlaceButton.setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View v) {
		//				startActivity(new Intent(getApplicationContext(), UploadActivity.class));
		//			}
		//		});
		//
		//
		//		takePictureButton.setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View v) {
		//				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		//				intent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString()); 
		//				startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
		//			}
		//		});
		startCaptureActivity();
	}

	private void startCaptureActivity() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString()); 
		startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == CAPTURE_IMAGE_REQUEST) {
			if (resultCode == RESULT_OK) {
				Bitmap image = (Bitmap) data.getExtras().get("data");
				savePicture(image);
			} else {
				Intent loginInten = new Intent(this, LoginActivity.class);
				startActivity(loginInten);
			}
		}

		if(requestCode == UPLOAD_DATA_REQUEST) {
			startCaptureActivity();
		}
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
			showUploadScreen(imageFile);
		} catch (Exception e) {
			Toast.makeText(this, "Picture was not taken", Toast.LENGTH_LONG)
			.show();
		}
	}

	private void showUploadScreen(File image) {
		Uri imageUri = Uri.parse(image.getAbsolutePath());
		Intent imageData = new Intent(getApplicationContext(), UploadActivity.class);
		imageData.setData(imageUri);
		startActivityForResult(imageData, UPLOAD_DATA_REQUEST);
		//		startActivity(imageData);
	}
}
