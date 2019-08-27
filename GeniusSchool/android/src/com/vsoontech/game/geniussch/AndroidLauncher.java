package com.vsoontech.game.geniussch;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.vsoontech.game.BuildConfig;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initGsConfig();
		initialize(new GeniusSchool(), config);
	}

	private void initGsConfig() {
		GsConfig.appVersion = BuildConfig.VERSION_CODE;
		GsConfig.appVersionName = BuildConfig.VERSION_NAME;
	}
}
