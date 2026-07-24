package com.nythral.chronos.client.config;

public final class ChronosConfigData {

	public boolean enabled = true;

	public String anchor =
		ChronosAnchor.TOP.name();

	public String side =
		ChronosSide.RIGHT.name();

	public String layout =
		ChronosLayout.VERTICAL.name();

	public String displayMode =
		ChronosDisplayMode.TIMER.name();

	public String attachment =
		ChronosAttachment.SIDE.name();

	public int opacity = 100;

	public static ChronosConfigData fromConfig(
		ChronosConfig config
	) {
		ChronosConfigData data =
			new ChronosConfigData();

		data.enabled =
			config.enabled();

		data.anchor =
			config.anchor().name();

		data.side =
			config.side().name();

		data.layout =
			config.layout().name();

		data.displayMode =
			config.displayMode().name();

		data.attachment =
			config.attachment().name();

		data.opacity =
			config.opacity();

		return data;
	}

	public void applyTo(
		ChronosConfig config
	) {
		config.setEnabled(
			this.enabled
		);

		config.setAnchor(
			parseEnum(
				ChronosAnchor.class,
				this.anchor,
				ChronosAnchor.TOP
			)
		);

		config.setSide(
			parseEnum(
				ChronosSide.class,
				this.side,
				ChronosSide.RIGHT
			)
		);

		config.setLayout(
			parseEnum(
				ChronosLayout.class,
				this.layout,
				ChronosLayout.VERTICAL
			)
		);

		config.setDisplayMode(
			parseEnum(
				ChronosDisplayMode.class,
				this.displayMode,
				ChronosDisplayMode.TIMER
			)
		);

		config.setAttachment(
			parseEnum(
				ChronosAttachment.class,
				this.attachment,
				ChronosAttachment.SIDE
			)
		);

		config.setOpacity(
			this.opacity
		);
	}

	private static <T extends Enum<T>> T parseEnum(
		Class<T> enumClass,
		String value,
		T fallback
	) {
		if (value == null) {
			return fallback;
		}

		try {
			return Enum.valueOf(
				enumClass,
				value
			);
		} catch (IllegalArgumentException exception) {
			return fallback;
		}
	}
}