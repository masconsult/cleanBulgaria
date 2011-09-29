package eu.masconsult.cleanbulgaria;

import java.util.List;

import roboguice.application.RoboApplication;

import com.google.inject.Module;

import eu.masconsult.cleanbulgaria.guice.CleanBulgariaConfigurationModule;

public class CleanBulgariaApplication extends RoboApplication {

	Module module = new CleanBulgariaConfigurationModule();

	@Override
	protected void addApplicationModules(List<Module> modules) {
		modules.add(module);
	}

	public void setModule(Module module) {
		this.module = module;
	}
}
