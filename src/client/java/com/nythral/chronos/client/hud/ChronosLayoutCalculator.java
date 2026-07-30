package com.nythral.chronos.client.hud;

import com.nythral.chronos.client.config.ChronosAnchor;
import com.nythral.chronos.client.config.ChronosAttachment;
import com.nythral.chronos.client.config.ChronosConfig;
import com.nythral.chronos.client.config.ChronosDisplayMode;
import com.nythral.chronos.client.config.ChronosSide;

public final class ChronosLayoutCalculator {

	private static final int ICON_BOX_SIZE = 24;

	private static final int TOP_TEXTURE_HEIGHT = 10;

	private static final int TIMER_TEXTURE_WIDTH = 27;
	private static final int TIMER_TEXTURE_HEIGHT = 16;

	private static final int SIDE_BAR_ROTATED_WIDTH = 10;
	private static final int SIDE_BAR_ROTATED_HEIGHT = 20;

	private static final int TEXTURE_OVERLAP = 1;
	private static final int EFFECT_GAP = 1;

	private static final int SCREEN_MARGIN_X = 1;
	private static final int SCREEN_MARGIN_Y = 1;

	private ChronosLayoutCalculator() {
	}

	public static int verticalStep(
		ChronosConfig config
	) {
		return verticalElementHeight(
			config
		) + EFFECT_GAP;
	}

	public static int verticalElementHeight(
		ChronosConfig config
	) {
		if (
			config.attachment()
				== ChronosAttachment.TOP
				|| config.attachment()
					== ChronosAttachment.BOTTOM
		) {
			return ICON_BOX_SIZE
				+ TOP_TEXTURE_HEIGHT
				- TEXTURE_OVERLAP;
		}

		if (
			config.attachment()
				== ChronosAttachment.SIDE
				&& config.displayMode()
					== ChronosDisplayMode.BAR
		) {
			return Math.max(
				ICON_BOX_SIZE,
				SIDE_BAR_ROTATED_HEIGHT
			);
		}

		return Math.max(
			ICON_BOX_SIZE,
			TIMER_TEXTURE_HEIGHT
		);
	}

	public static int verticalIconX(
		int screenWidth,
		ChronosSide side
	) {
		if (
			side
				== ChronosSide.LEFT
		) {
			return SCREEN_MARGIN_X;
		}

		return screenWidth
			- SCREEN_MARGIN_X
			- ICON_BOX_SIZE;
	}

	public static int verticalStartY(
		int screenHeight,
		int totalHeight,
		ChronosAnchor anchor
	) {
		if (
			anchor
				== ChronosAnchor.TOP
		) {
			return SCREEN_MARGIN_Y;
		}

		if (
			anchor
				== ChronosAnchor.BOTTOM
		) {
			return screenHeight
				- SCREEN_MARGIN_Y
				- totalHeight;
		}

		return (
			screenHeight
				- totalHeight
		) / 2;
	}

	public static int horizontalElementWidth(
		ChronosConfig config
	) {
		if (
			config.attachment()
				!= ChronosAttachment.SIDE
		) {
			return ICON_BOX_SIZE;
		}

		if (
			config.displayMode()
				== ChronosDisplayMode.BAR
		) {
			return ICON_BOX_SIZE
				+ SIDE_BAR_ROTATED_WIDTH
				- TEXTURE_OVERLAP;
		}

		return ICON_BOX_SIZE
			+ TIMER_TEXTURE_WIDTH
			- TEXTURE_OVERLAP;
	}
}