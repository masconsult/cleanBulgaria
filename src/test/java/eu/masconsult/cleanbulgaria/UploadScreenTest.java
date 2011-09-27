package eu.masconsult.cleanbulgaria;



import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import roboguice.activity.RoboPreferenceActivity;
import roboguice.application.RoboApplication;
import roboguice.inject.RoboApplicationProvider;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowAlertDialog;
import com.xtremelabs.robolectric.shadows.ShadowIntent;
import com.xtremelabs.robolectric.shadows.ShadowPendingIntent;


@RunWith(RobolectricTestRunner.class)
public class UploadScreenTest {

	private UploadActivity uploadActivity;
	private Button selectWasteType;
	private Spinner metricTypeSpinner;
	
	@Before
	public void setUp() {
		uploadActivity = new UploadActivity();
		uploadActivity.onCreate(null);
		selectWasteType = (Button) uploadActivity.findViewById(R.id.wasteTypeSelectButton);
		metricTypeSpinner = (Spinner) uploadActivity.findViewById(R.id.metricTypeSpinner);
	}
	
	@Test
	public void testWasteTypeDialogAppearsOnClick() {
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
		
		boolean[] actual = uploadActivity.getWasteTypes();
		
		boolean[] expected = new boolean[5];
		expected[0] = true;
		expected[2] = true;
		
		for(int i = 0; i < 5; i++) {
			if(expected[i] != actual[i])
				fail();
		}
	}
	
	@Test
	public void testMetricTypeRightValue() {
		metricTypeSpinner.bringToFront();
		metricTypeSpinner.setSelection(1);
		String expected = "2";
		Assert.assertEquals(expected, uploadActivity.getMetric());
	}
	
	
}
