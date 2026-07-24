package com.nythral.chronos.client.config;

public enum ChronosAttachment {
	TOP("Top"),
	SIDE("Side"),
	BOTTOM("Bottom");

	private final String displayName;

	ChronosAttachment(String displayName) {
		this.displayName = displayName;
	}

	public String displayName() {
		return this.displayName;
	}

	public ChronosAttachment next() {
		ChronosAttachment[] values = values();

		return values[
			(this.ordinal() + 1) % values.length
		];
	}
}