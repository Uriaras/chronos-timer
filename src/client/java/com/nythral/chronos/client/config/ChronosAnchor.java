package com.nythral.chronos.client.config;

public enum ChronosAnchor {
	TOP("Top"),
	CENTER("Middle"),
	BOTTOM("Bottom");

	private final String displayName;

	ChronosAnchor(String displayName) {
		this.displayName = displayName;
	}

	public String displayName() {
		return this.displayName;
	}

	public ChronosAnchor next() {
		ChronosAnchor[] values = values();

		return values[
			(this.ordinal() + 1) % values.length
		];
	}
}