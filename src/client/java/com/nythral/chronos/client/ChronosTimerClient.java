package com.nythral.chronos.client;

import com.nythral.chronos.ChronosTimer;
import com.nythral.chronos.client.config.ChronosConfigManager;
import com.nythral.lib.client.api.NythralModuleRegistry;
import net.fabricmc.api.ClientModInitializer;

public final class ChronosTimerClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ChronosConfigManager.load();

		NythralModuleRegistry.register(
			new ChronosTimerModule()
		);

		ChronosTimer.LOGGER.info(
			"Chronos Timer client initialized."
		);
	}
}