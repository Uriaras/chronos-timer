package com.nythral.chronos.client.hud;

import net.minecraft.world.effect.MobEffectInstance;

public final class ChronosDurationFormatter {

	private static final int TICKS_PER_SECOND = 20;
	private static final int SECONDS_PER_MINUTE = 60;

	private ChronosDurationFormatter() {
	}

	public static String format(
		MobEffectInstance effect
	) {
		if (effect.isInfiniteDuration()) {
			return "∞";
		}

		int totalSeconds =
			remainingSeconds(effect);

		if (totalSeconds >= SECONDS_PER_MINUTE) {
			return totalSeconds
				/ SECONDS_PER_MINUTE
				+ "m";
		}

		return Integer.toString(
			totalSeconds
		);
	}

	public static int remainingSeconds(
		MobEffectInstance effect
	) {
		if (effect.isInfiniteDuration()) {
			return Integer.MAX_VALUE;
		}

		return Math.max(
			0,
			(int) Math.ceil(
				effect.getDuration()
					/ (double) TICKS_PER_SECOND
			)
		);
	}
}