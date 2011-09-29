package eu.masconsult.cleanbulgaria;

import org.mockito.Mockito;

import roboguice.config.AbstractAndroidModule;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.google.inject.Singleton;

import eu.masconsult.cleanbulgaria.connection.Connection;
import eu.masconsult.cleanbulgaria.connection.PositionManager;

public class CleanBulgariaTestModule extends AbstractAndroidModule {

	@Override
	protected void configure() {
		bind(Connection.class).in(Singleton.class);
		bind(PositionManager.class).in(Singleton.class);
		bind(GoogleAnalyticsTracker.class).toInstance(Mockito.mock(GoogleAnalyticsTracker.class));
	}

}
