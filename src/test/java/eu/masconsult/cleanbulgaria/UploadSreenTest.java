package eu.masconsult.cleanbulgaria;



import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.AlertDialog;
import android.widget.Button;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowAlertDialog;


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
		ShadowAlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
		Assert.assertTrue(dialog.isShowing());
	}
	
	@Test 
	public void testWasteTypeDataSelection() {
		selectWasteType.performClick();
		ShadowAlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
		
		dialog.clickOnItem(9);
		dialog.clickOnItem(2);
	}
	
	
}
