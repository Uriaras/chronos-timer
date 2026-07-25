package com.nythral.chronos.client.mixin;

import com.nythral.chronos.client.config.ChronosConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {

	@Inject(
		method = "renderEffects",
		at = @At("HEAD"),
		cancellable = true
	)
	private void chronos$hideVanillaEffects(
		GuiGraphics graphics,
		DeltaTracker deltaTracker,
		CallbackInfo callbackInfo
	) {
		if (
			ChronosConfig.INSTANCE.enabled()
		) {
			callbackInfo.cancel();
		}
	}
}