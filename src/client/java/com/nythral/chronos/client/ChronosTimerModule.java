package com.nythral.chronos.client;

import com.nythral.chronos.client.screen.ChronosSettingsScreen;
import com.nythral.lib.client.api.NythralModule;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class ChronosTimerModule implements NythralModule {

	@Override
	public String id() {
		return "chronos-timer";
	}

	@Override
	public Component name() {
		return Component.literal("Chronos Timer");
	}

	@Override
	public Component description() {
		return Component.literal("Status effect timer HUD");
	}

	@Override
	public Screen createSettingsScreen(Screen parent) {
		return new ChronosSettingsScreen(parent);
	}
}