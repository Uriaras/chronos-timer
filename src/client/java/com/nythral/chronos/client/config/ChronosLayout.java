package com.nythral.chronos.client.config;

public enum ChronosLayout {
	HORIZONTAL("Horizontal"),
	VERTICAL("Vertical");

	private final String displayName;

	ChronosLayout(String displayName) {
		this.displayName = displayName;
	}

	public String displayName() {
		return this.displayName;
	}

	public ChronosLayout next() {
		ChronosLayout[] values = values();

		return values[
			(this.ordinal() + 1) % values.length
		];
	}
}