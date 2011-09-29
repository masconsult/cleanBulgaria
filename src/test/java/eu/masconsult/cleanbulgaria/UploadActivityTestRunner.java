package eu.masconsult.cleanbulgaria;

import org.junit.runners.model.InitializationError;

import roboguice.application.RoboApplication;
import android.app.Application;

import com.google.inject.Injector;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

public class UploadActivityTestRunner extends RobolectricTestRunner {

	public UploadActivityTestRunner(Class<?> testClass)	throws InitializationError {
		super(testClass);
	}

	@Override
	protected Application createApplication() {
		CleanBulgariaApplication cleanBulgariaApplication =  (CleanBulgariaApplication) super.createApplication();
		cleanBulgariaApplication.setModule(new CleanBulgariaTestModule());
		return cleanBulgariaApplication;
	}

	@Override
	public void prepareTest(Object test) {
		super.prepareTest(test);
		RoboApplication cleanBulgariaApp = (RoboApplication) Robolectric.application;
		Injector injector = cleanBulgariaApp.getInjector();
		injector.injectMembers(test);
	}

}
