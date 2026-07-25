package com.nythral.chronos.client.hud;

import net.minecraft.world.effect.MobEffectInstance;

public record ChronosEffectEntry(
	MobEffectInstance effect,
	String durationText
) {
}