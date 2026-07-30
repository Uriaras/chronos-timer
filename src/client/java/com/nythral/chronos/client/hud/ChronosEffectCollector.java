package com.nythral.chronos.client.hud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;

public final class ChronosEffectCollector {

	private ChronosEffectCollector() {
	}

	public static List<ChronosEffectEntry> collect(
		Collection<MobEffectInstance> activeEffects
	) {
		List<ChronosEffectEntry> beaconEntries =
			new ArrayList<>();

		List<ChronosEffectEntry> regularEntries =
			new ArrayList<>();

		List<ChronosEffectEntry> harmfulEntries =
			new ArrayList<>();

		for (
			MobEffectInstance effect
				: activeEffects
		) {
			ChronosEffectEntry entry =
				new ChronosEffectEntry(
					effect,
					ChronosDurationFormatter.format(
						effect
					)
				);

			if (isHarmful(effect)) {
				harmfulEntries.add(entry);
				continue;
			}

			if (isBeacon(effect)) {
				beaconEntries.add(entry);
				continue;
			}

			regularEntries.add(entry);
		}

		List<ChronosEffectEntry> entries =
			new ArrayList<>(
				activeEffects.size()
			);

		entries.addAll(beaconEntries);
		entries.addAll(regularEntries);
		entries.addAll(harmfulEntries);

		return entries;
	}

	public static boolean isHarmful(
		MobEffectInstance effect
	) {
		return effect.getEffect()
			.value()
			.getCategory()
				== MobEffectCategory.HARMFUL;
	}

	private static boolean isBeacon(
		MobEffectInstance effect
	) {
		return effect.isAmbient();
	}
}