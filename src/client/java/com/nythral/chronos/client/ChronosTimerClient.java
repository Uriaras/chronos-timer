package com.nythral.chronos.client;

import com.nythral.chronos.ChronosTimer;
import com.nythral.chronos.client.config.ChronosConfigManager;
import com.nythral.chronos.client.hud.ChronosHud;
import com.nythral.lib.client.api.NythralModuleRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.resources.Identifier;

public final class ChronosTimerClient implements ClientModInitializer {

	private static final Identifier CHRONOS_HUD_ID =
		Identifier.fromNamespaceAndPath(
			"chronos-timer",
			"status_effect_timer"
		);

	@Override
	public void onInitializeClient() {
		ChronosConfigManager.load();

		NythralModuleRegistry.register(
			new ChronosTimerModule()
		);

		HudElementRegistry.attachElementAfter(
			VanillaHudElements.BOSS_BAR,
			CHRONOS_HUD_ID,
			ChronosHud::render
		);

		ChronosTimer.LOGGER.info(
			"Chronos Timer client initialized."
		);
	}
}