package com.nythral.chronos.client.config;

public enum ChronosDisplayMode {
	TIMER("Timer"),
	BAR("Bar");

	private final String displayName;

	ChronosDisplayMode(String displayName) {
		this.displayName = displayName;
	}

	public String displayName() {
		return this.displayName;
	}

	public ChronosDisplayMode next() {
		ChronosDisplayMode[] values = values();

		return values[
			(this.ordinal() + 1) % values.length
		];
	}
}