package com.nythral.chronos.client.hud;

import com.nythral.chronos.client.config.ChronosConfig;
import java.util.List;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public final class ChronosHud {

	private ChronosHud() {
	}

	public static void render(
		GuiGraphics graphics,
		DeltaTracker deltaTracker
	) {
		Minecraft minecraft =
			Minecraft.getInstance();

		ChronosConfig config =
			ChronosConfig.INSTANCE;

		if (
			!config.enabled()
				|| minecraft.player == null
				|| minecraft.options.hideGui
		) {
			return;
		}

		List<ChronosEffectEntry> entries =
			ChronosEffectCollector.collect(
				minecraft.player.getActiveEffects()
			);

		if (entries.isEmpty()) {
			ChronosEffectDurationTracker.clear();
			return;
		}

		ChronosEffectDurationTracker.update(
			entries
		);

		ChronosHudRenderer.render(
			graphics,
			minecraft,
			entries,
			config
		);
	}
}