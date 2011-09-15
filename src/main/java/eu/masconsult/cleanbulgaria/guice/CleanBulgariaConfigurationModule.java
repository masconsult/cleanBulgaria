package eu.masconsult.cleanbulgaria.guice;

import com.google.inject.Singleton;

import eu.masconsult.cleanbulgaria.connection.Connection;
import eu.masconsult.cleanbulgaria.connection.PositionManager;
import roboguice.config.AbstractAndroidModule;

public class CleanBulgariaConfigurationModule extends AbstractAndroidModule {

	@Override
	protected void configure() {
		bind(Connection.class).in(Singleton.class);
		bind(PositionManager.class).in(Singleton.class);
	}

}
