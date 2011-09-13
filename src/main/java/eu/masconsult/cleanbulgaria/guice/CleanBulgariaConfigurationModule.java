package eu.masconsult.cleanbulgaria.guice;

import com.google.inject.Singleton;

import eu.masconsult.cleanbulgaria.Connection;
import roboguice.config.AbstractAndroidModule;

public class CleanBulgariaConfigurationModule extends AbstractAndroidModule {

	@Override
	protected void configure() {
		bind(Connection.class).in(Singleton.class);
	}

}
