package eu.masconsult.cleanbulgaria;

import junit.framework.Assert;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.widget.Button;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;


@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
	
	private MainActivity mainActivity;
	
	private Button takePictureButton;
	
	@Before
	public void setUp() {
		mainActivity = new MainActivity();
		mainActivity.onCreate(null);
		takePictureButton = (Button) mainActivity.findViewById(R.id.takePicture);
	}
	
	@Test
	public void testTakePictureActivityStarted() {
		
		takePictureButton.performClick();
		
		ShadowActivity shadow = Robolectric.shadowOf(mainActivity);
		
		Intent startTakePictureIntent = shadow.getNextStartedActivity();
		
		assertThat(startTakePictureIntent.getComponent().getClassName(), equalTo(TakePhotoActivity.class.getName()));
	}

}
