package com.nythral.chronos.client.hud;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.world.effect.MobEffectInstance;

public final class ChronosEffectDurationTracker {

	private static final Map<String, Integer> MAX_DURATIONS =
		new HashMap<>();

	private ChronosEffectDurationTracker() {
	}

	public static void clear() {
		MAX_DURATIONS.clear();
	}

	public static void update(
		List<ChronosEffectEntry> entries
	) {
		Set<String> activeKeys =
			new HashSet<>();

		for (ChronosEffectEntry entry : entries) {
			MobEffectInstance effect =
				entry.effect();

			String key =
				createKey(effect);

			activeKeys.add(key);

			if (!effect.isInfiniteDuration()) {
				MAX_DURATIONS.merge(
					key,
					Math.max(
						1,
						effect.getDuration()
					),
					Math::max
				);
			}
		}

		MAX_DURATIONS.keySet()
			.removeIf(
				key -> !activeKeys.contains(key)
			);
	}

	public static float progress(
		MobEffectInstance effect
	) {
		if (effect.isInfiniteDuration()) {
			return 1.0F;
		}

		int remainingTicks =
			Math.max(
				0,
				effect.getDuration()
			);

		int maximumTicks =
			MAX_DURATIONS.getOrDefault(
				createKey(effect),
				Math.max(
					1,
					remainingTicks
				)
			);

		if (maximumTicks <= 0) {
			return 0.0F;
		}

		return Math.max(
			0.0F,
			Math.min(
				1.0F,
				remainingTicks
					/ (float) maximumTicks
			)
		);
	}

	private static String createKey(
		MobEffectInstance effect
	) {
		return effect.getEffect()
			+ ":"
			+ effect.getAmplifier();
	}
}