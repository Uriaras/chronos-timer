package com.nythral.chronos.client.config;

public enum ChronosSide {
	LEFT("Left"),
	RIGHT("Right");

	private final String displayName;

	ChronosSide(String displayName) {
		this.displayName = displayName;
	}

	public String displayName() {
		return this.displayName;
	}

	public ChronosSide next() {
		ChronosSide[] values = values();

		return values[
			(this.ordinal() + 1) % values.length
		];
	}
}