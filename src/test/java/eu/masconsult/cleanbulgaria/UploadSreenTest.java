package eu.masconsult.cleanbulgaria;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.AlertDialog;
import android.widget.Button;

import com.xtremelabs.robolectric.RobolectricTestRunner;


@RunWith(RobolectricTestRunner.class)
public class UploadSreenTest {

	private UploadActivity uploadActivity;
	private Button selectWasteType;
	
	
	@Before
	public void setUp() {
		uploadActivity = new UploadActivity();
		uploadActivity.onCreate(null);
		selectWasteType = (Button) uploadActivity.findViewById(R.id.wasteTypeSelectButton);
	}
	
	@Test
	public void testWasteTypeDialogAppersOnClick() {
		selectWasteType.performClick();
		AlertDialog alert = uploadActivity.getDialog();
		Assert.assertTrue(alert.isShowing());
	}
	
}
