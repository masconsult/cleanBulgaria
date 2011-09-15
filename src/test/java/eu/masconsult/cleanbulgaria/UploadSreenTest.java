package eu.masconsult.cleanbulgaria;



import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.widget.Button;
import android.widget.Spinner;

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
		
		dialog.clickOnItem(0);
		dialog.clickOnItem(2);
		
		boolean[] actual = dialog.getCheckedItems();
		boolean[] expected = new boolean[5];
		expected[0] = true;
		expected[2] = true;
		
		for(int i = 0; i < 5; i++) {
			if(expected[i] != actual[i])
				fail();
		}
	}
	
	@Test
	public void testWasteTypeData() {
		selectWasteType.performClick();
		ShadowAlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
		
		dialog.clickOnItem(0);
		dialog.clickOnItem(2);
		boolean[] expected = new boolean[5];
		expected[0] = true;
		expected[2] = true;
		
		boolean[] actual = uploadActivity.getWasteTypes();
		
		for(int i = 0; i < 5; i++) {
			if(expected[i] != actual[i])
				Assert.assertTrue(false);
		}
		
	}
	
}
