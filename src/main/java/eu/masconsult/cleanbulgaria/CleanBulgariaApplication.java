package eu.masconsult.cleanbulgaria;

import java.util.List;

import com.google.inject.Module;

import eu.masconsult.cleanbulgaria.guice.CleanBulgariaConfigurationModule;

import roboguice.application.RoboApplication;

public class CleanBulgariaApplication extends RoboApplication {

	 protected void addApplicationModules(List<Module> modules) {
		 modules.add(new CleanBulgariaConfigurationModule());
	 }
}
