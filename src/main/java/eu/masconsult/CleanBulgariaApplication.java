package eu.masconsult;

import java.util.List;

import com.google.inject.Module;

import eu.masconsult.guice.CleanBulgariaConfigurationModule;

import roboguice.application.RoboApplication;

public class CleanBulgariaApplication extends RoboApplication{
	
	@Override
	protected void addApplicationModules(List<Module> modules) {
		modules.add(new CleanBulgariaConfigurationModule());
	}

}
