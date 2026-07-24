package com.nythral.chronos;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ChronosTimer implements ModInitializer {

	public static final String MOD_ID = "chronos-timer";

	public static final Logger LOGGER =
		LoggerFactory.getLogger(MOD_ID);

	public ChronosTimer() {
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Chronos Timer initialized.");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(
			MOD_ID,
			path
		);
	}
}