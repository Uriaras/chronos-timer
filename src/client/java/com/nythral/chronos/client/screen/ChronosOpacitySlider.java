package com.nythral.chronos.client.screen;

import java.util.function.IntConsumer;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public final class ChronosOpacitySlider
	extends AbstractSliderButton {

	private static final int STEP = 25;

	private final IntConsumer setter;

	public ChronosOpacitySlider(
		int x,
		int y,
		int width,
		int height,
		int initialValue,
		IntConsumer setter
	) {
		super(
			x,
			y,
			width,
			height,
			Component.empty(),
			snap(initialValue) / 100.0
		);

		this.setter = setter;

		updateMessage();
	}

	@Override
	protected void updateMessage() {
		setMessage(
			Component.literal(
				"Opacity: "
					+ currentPercentage()
					+ "%"
			)
		);
	}

	@Override
	protected void applyValue() {
		int percentage =
			currentPercentage();

		this.value =
			percentage / 100.0;

		this.setter.accept(percentage);

		updateMessage();
	}

	private int currentPercentage() {
		return snap(
			(int) Math.round(
				this.value * 100.0
			)
		);
	}

	private static int snap(int value) {
		int snapped =
			Math.round(value / (float) STEP)
				* STEP;

		return Math.max(
			0,
			Math.min(100, snapped)
		);
	}
}