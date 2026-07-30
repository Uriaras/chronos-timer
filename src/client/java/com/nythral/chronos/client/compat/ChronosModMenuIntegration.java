package com.nythral.chronos.client.compat;

import com.nythral.chronos.client.screen.ChronosSettingsScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public final class ChronosModMenuIntegration
	implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return ChronosSettingsScreen::new;
	}
}