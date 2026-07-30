package com.nythral.chronos.client.hud;

import com.nythral.chronos.ChronosTimer;
import com.nythral.chronos.client.config.ChronosAttachment;
import com.nythral.chronos.client.config.ChronosConfig;
import com.nythral.chronos.client.config.ChronosDisplayMode;
import com.nythral.chronos.client.config.ChronosSide;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;

public final class ChronosEffectRenderer {

	private static final Identifier EFFECT_BACKGROUND =
		Identifier.withDefaultNamespace(
			"hud/effect_background"
		);

	private static final Identifier EFFECT_BACKGROUND_AMBIENT =
		Identifier.withDefaultNamespace(
			"hud/effect_background_ambient"
		);

	private static final Identifier TOP_TEXTURE =
		ChronosTimer.id(
			"textures/gui/top.png"
		);

	private static final Identifier TOP_AMBIENT_TEXTURE =
		ChronosTimer.id(
			"textures/gui/top_ambient.png"
		);

	private static final Identifier TIMER_TEXTURE =
		ChronosTimer.id(
			"textures/gui/timer.png"
		);

	private static final Identifier TIMER_AMBIENT_TEXTURE =
		ChronosTimer.id(
			"textures/gui/timer_ambient.png"
		);

	private static final int ICON_BOX_SIZE = 24;
	private static final int ICON_SIZE = 18;
	private static final int TOP_TEXTURE_WIDTH = 20;
	private static final int TOP_TEXTURE_HEIGHT = 10;
	private static final int TIMER_TEXTURE_WIDTH = 27;
	private static final int TIMER_TEXTURE_HEIGHT = 16;
	private static final int SIDE_BAR_ROTATED_WIDTH = 10;
	private static final int SIDE_BAR_ROTATED_HEIGHT = 20;
	private static final int TEXTURE_OVERLAP = 1;
	private static final int TOP_TEXT_OFFSET_Y = 2;
	private static final int BOTTOM_TEXT_OFFSET_Y = 0;
	private static final int SIDE_TEXT_OFFSET_Y = 2;
	private static final int TOP_TEXT_OFFSET_X = 0;
	private static final int TOP_TEXT_WIDTH = 20;
	private static final int TOP_TEXT_HEIGHT = 8;
	private static final int TIMER_CONNECTOR_WIDTH = 3;
	private static final int TIMER_TEXT_WIDTH = 24;
	private static final int TIMER_TEXT_HEIGHT = 14;
	private static final int EFFECT_LEVEL_OFFSET_X = 3;
	private static final int EFFECT_LEVEL_OFFSET_Y = 3;
	private static final int EFFECT_LEVEL_COLOR = 0xFFFFFFFF;
	private static final int WARNING_TIME_SECONDS = 10;
	private static final int NORMAL_TEXT_COLOR = 0xFFFFFFFF;
	private static final int WARNING_TEXT_COLOR = 0xFFFF0000;

	private ChronosEffectRenderer() {
	}

	public static void render(
		GuiGraphics graphics,
		Minecraft minecraft,
		ChronosEffectEntry entry,
		int iconX,
		int iconY,
		ChronosConfig config
	) {
		MobEffectInstance effect =
			entry.effect();

		boolean ambient =
			effect.isAmbient();

		renderVanillaEffect(
			graphics,
			minecraft,
			effect,
			iconX,
			iconY,
			ambient,
			config.opacity()
		);

		if (
			config.attachment()
				== ChronosAttachment.TOP
		) {
			renderTopAttachment(
				graphics,
				minecraft,
				entry,
				iconX,
				iconY,
				config,
				ambient
			);
			return;
		}

		if (
			config.attachment()
				== ChronosAttachment.BOTTOM
		) {
			renderBottomAttachment(
				graphics,
				minecraft,
				entry,
				iconX,
				iconY,
				config,
				ambient
			);
			return;
		}

		renderSideAttachment(
			graphics,
			minecraft,
			entry,
			iconX,
			iconY,
			config,
			ambient
		);
	}

	private static void renderVanillaEffect(
		GuiGraphics graphics,
		Minecraft minecraft,
		MobEffectInstance effect,
		int iconBoxX,
		int iconBoxY,
		boolean ambient,
		int opacity
	) {
		Identifier background =
			ambient
				? EFFECT_BACKGROUND_AMBIENT
				: EFFECT_BACKGROUND;

		graphics.blitSprite(
			RenderPipelines.GUI_TEXTURED,
			background,
			iconBoxX,
			iconBoxY,
			ICON_BOX_SIZE,
			ICON_BOX_SIZE,
			ChronosTextureRenderer.alpha(
				opacity
			)
		);

		Identifier effectSprite =
			Gui.getMobEffectSprite(
				effect.getEffect()
			);

		int effectIconX =
			iconBoxX
				+ (
					ICON_BOX_SIZE
						- ICON_SIZE
				) / 2;

		int effectIconY =
			iconBoxY
				+ (
					ICON_BOX_SIZE
						- ICON_SIZE
				) / 2;

		graphics.blitSprite(
			RenderPipelines.GUI_TEXTURED,
			effectSprite,
			effectIconX,
			effectIconY,
			ICON_SIZE,
			ICON_SIZE
		);

		renderEffectLevel(
			graphics,
			minecraft,
			effect,
			iconBoxX,
			iconBoxY
		);
	}

	private static void renderEffectLevel(
		GuiGraphics graphics,
		Minecraft minecraft,
		MobEffectInstance effect,
		int iconBoxX,
		int iconBoxY
	) {
		int level =
			effect.getAmplifier()
				+ 1;

		if (
			level <= 1
		) {
			return;
		}

		String romanLevel =
			toRomanNumeral(
				level
			);

		int textWidth =
			minecraft.font.width(
				romanLevel
			);

		int textX =
			iconBoxX
				+ ICON_BOX_SIZE
				- textWidth
				- EFFECT_LEVEL_OFFSET_X;

		int textY =
			iconBoxY
				+ EFFECT_LEVEL_OFFSET_Y;

		graphics.drawString(
			minecraft.font,
			romanLevel,
			textX,
			textY,
			EFFECT_LEVEL_COLOR,
			true
		);
	}

	private static String toRomanNumeral(
		int value
	) {
		if (
			value <= 0
		) {
			return "";
		}

		int[] values = {
			1000,
			900,
			500,
			400,
			100,
			90,
			50,
			40,
			10,
			9,
			5,
			4,
			1
		};

		String[] numerals = {
			"M",
			"CM",
			"D",
			"CD",
			"C",
			"XC",
			"L",
			"XL",
			"X",
			"IX",
			"V",
			"IV",
			"I"
		};

		StringBuilder result =
			new StringBuilder();

		int remaining =
			value;

		for (
			int index = 0;
			index < values.length;
			index++
		) {
			while (
				remaining >= values[index]
			) {
				result.append(
					numerals[index]
				);

				remaining -=
					values[index];
			}
		}

		return result.toString();
	}

	private static void renderTopAttachment(
		GuiGraphics graphics,
		Minecraft minecraft,
		ChronosEffectEntry entry,
		int iconX,
		int iconY,
		ChronosConfig config,
		boolean ambient
	) {
		Identifier texture =
			ambient
				? TOP_AMBIENT_TEXTURE
				: TOP_TEXTURE;

		int textureX =
			iconX
				+ (
					ICON_BOX_SIZE
						- TOP_TEXTURE_WIDTH
				) / 2;

		int textureY =
			iconY
				- TOP_TEXTURE_HEIGHT
				+ TEXTURE_OVERLAP;

		if (
			config.displayMode()
				== ChronosDisplayMode.BAR
		) {
			ChronosTextureRenderer.drawBar(
				graphics,
				texture,
				textureX,
				textureY,
				0.0F,
				true,
				ChronosEffectDurationTracker.progress(
					entry.effect()
				),
				config.opacity()
			);
			return;
		}

		ChronosTextureRenderer.draw(
			graphics,
			texture,
			textureX,
			textureY,
			TOP_TEXTURE_WIDTH,
			TOP_TEXTURE_HEIGHT,
			config.opacity()
		);

		renderTopBottomTimerText(
			graphics,
			minecraft,
			entry.effect(),
			entry.durationText(),
			textureX,
			textureY,
			config
		);
	}

	private static void renderBottomAttachment(
		GuiGraphics graphics,
		Minecraft minecraft,
		ChronosEffectEntry entry,
		int iconX,
		int iconY,
		ChronosConfig config,
		boolean ambient
	) {
		Identifier texture =
			ambient
				? TOP_AMBIENT_TEXTURE
				: TOP_TEXTURE;

		int textureX =
			iconX
				+ (
					ICON_BOX_SIZE
						- TOP_TEXTURE_WIDTH
				) / 2;

		int textureY =
			iconY
				+ ICON_BOX_SIZE
				- TEXTURE_OVERLAP;

		if (
			config.displayMode()
				== ChronosDisplayMode.BAR
		) {
			ChronosTextureRenderer.drawBar(
				graphics,
				texture,
				textureX,
				textureY,
				180.0F,
				false,
				ChronosEffectDurationTracker.progress(
					entry.effect()
				),
				config.opacity()
			);
			return;
		}

		ChronosTextureRenderer.drawRotated(
			graphics,
			texture,
			textureX,
			textureY,
			TOP_TEXTURE_WIDTH,
			TOP_TEXTURE_HEIGHT,
			180.0F,
			config.opacity()
		);

		renderTopBottomTimerText(
			graphics,
			minecraft,
			entry.effect(),
			entry.durationText(),
			textureX,
			textureY,
			config
		);
	}

	private static void renderSideAttachment(
		GuiGraphics graphics,
		Minecraft minecraft,
		ChronosEffectEntry entry,
		int iconX,
		int iconY,
		ChronosConfig config,
		boolean ambient
	) {
		if (
			config.displayMode()
				== ChronosDisplayMode.BAR
		) {
			renderSideBarAttachment(
				graphics,
				entry,
				iconX,
				iconY,
				config,
				ambient
			);
			return;
		}

		renderSideTimerAttachment(
			graphics,
			minecraft,
			entry,
			iconX,
			iconY,
			config,
			ambient
		);
	}

	private static void renderSideTimerAttachment(
		GuiGraphics graphics,
		Minecraft minecraft,
		ChronosEffectEntry entry,
		int iconX,
		int iconY,
		ChronosConfig config,
		boolean ambient
	) {
		Identifier texture =
			ambient
				? TIMER_AMBIENT_TEXTURE
				: TIMER_TEXTURE;

		int textureY =
			iconY
				+ (
					ICON_BOX_SIZE
						- TIMER_TEXTURE_HEIGHT
				) / 2;

		int textureX;

		if (
			config.side()
				== ChronosSide.RIGHT
		) {
			textureX =
				iconX
					- TIMER_TEXTURE_WIDTH
					+ TEXTURE_OVERLAP;

			ChronosTextureRenderer.drawMirrored(
				graphics,
				texture,
				textureX,
				textureY,
				TIMER_TEXTURE_WIDTH,
				TIMER_TEXTURE_HEIGHT,
				config.opacity()
			);
		} else {
			textureX =
				iconX
					+ ICON_BOX_SIZE
					- TEXTURE_OVERLAP;

			ChronosTextureRenderer.draw(
				graphics,
				texture,
				textureX,
				textureY,
				TIMER_TEXTURE_WIDTH,
				TIMER_TEXTURE_HEIGHT,
				config.opacity()
			);
		}

		renderSideTimerText(
			graphics,
			minecraft,
			entry.effect(),
			entry.durationText(),
			textureX,
			textureY,
			config
		);
	}

	private static void renderSideBarAttachment(
		GuiGraphics graphics,
		ChronosEffectEntry entry,
		int iconX,
		int iconY,
		ChronosConfig config,
		boolean ambient
	) {
		Identifier texture =
			ambient
				? TOP_AMBIENT_TEXTURE
				: TOP_TEXTURE;

		int textureY =
			iconY
				+ (
					ICON_BOX_SIZE
						- SIDE_BAR_ROTATED_HEIGHT
				) / 2;

		int textureX;
		float rotation;
		boolean fillFromRight;

		if (
			config.side()
				== ChronosSide.RIGHT
		) {
			textureX =
				iconX
					- SIDE_BAR_ROTATED_WIDTH
					+ TEXTURE_OVERLAP;

			rotation =
				270.0F;

			fillFromRight =
				false;
		} else {
			textureX =
				iconX
					+ ICON_BOX_SIZE
					- TEXTURE_OVERLAP;

			rotation =
				90.0F;

			fillFromRight =
				true;
		}

		ChronosTextureRenderer.drawBar(
			graphics,
			texture,
			textureX,
			textureY,
			rotation,
			fillFromRight,
			ChronosEffectDurationTracker.progress(
				entry.effect()
			),
			config.opacity()
		);
	}

	private static void renderTopBottomTimerText(
		GuiGraphics graphics,
		Minecraft minecraft,
		MobEffectInstance effect,
		String text,
		int textureX,
		int textureY,
		ChronosConfig config
	) {
		if (
			config.displayMode()
				!= ChronosDisplayMode.TIMER
		) {
			return;
		}

		int textAreaX =
			textureX
				+ TOP_TEXT_OFFSET_X;

		int textAreaY =
			textureY
				+ (
					config.attachment()
						== ChronosAttachment.BOTTOM
							? BOTTOM_TEXT_OFFSET_Y
							: TOP_TEXT_OFFSET_Y
				);

		drawCenteredText(
			graphics,
			minecraft,
			text,
			textAreaX,
			textAreaY,
			TOP_TEXT_WIDTH,
			TOP_TEXT_HEIGHT,
			calculateTimerTextColor(
				effect
			)
		);
	}

	private static void renderSideTimerText(
		GuiGraphics graphics,
		Minecraft minecraft,
		MobEffectInstance effect,
		String text,
		int textureX,
		int textureY,
		ChronosConfig config
	) {
		if (
			config.displayMode()
				!= ChronosDisplayMode.TIMER
		) {
			return;
		}

		int textAreaX;

		if (
			config.side()
				== ChronosSide.RIGHT
		) {
			textAreaX =
				textureX;
		} else {
			textAreaX =
				textureX
					+ TIMER_CONNECTOR_WIDTH;
		}

		int textAreaY =
			textureY
				+ SIDE_TEXT_OFFSET_Y;

		drawCenteredText(
			graphics,
			minecraft,
			text,
			textAreaX,
			textAreaY,
			TIMER_TEXT_WIDTH,
			TIMER_TEXT_HEIGHT,
			calculateTimerTextColor(
				effect
			)
		);
	}

	private static void drawCenteredText(
		GuiGraphics graphics,
		Minecraft minecraft,
		String text,
		int areaX,
		int areaY,
		int areaWidth,
		int areaHeight,
		int textColor
	) {
		int textWidth =
			minecraft.font.width(
				text
			);

		int textHeight =
			minecraft.font.lineHeight;

		int textX =
			areaX
				+ (
					areaWidth
						- textWidth
				) / 2;

		int textY =
			areaY
				+ (
					areaHeight
						- textHeight
				) / 2;

		graphics.drawString(
			minecraft.font,
			text,
			textX,
			textY,
			textColor,
			true
		);
	}

	private static int calculateTimerTextColor(
		MobEffectInstance effect
	) {
		if (
			effect.isInfiniteDuration()
		) {
			return NORMAL_TEXT_COLOR;
		}

		int remainingSeconds =
			ChronosDurationFormatter.remainingSeconds(
				effect
			);

		if (
			remainingSeconds
				<= WARNING_TIME_SECONDS
		) {
			return WARNING_TEXT_COLOR;
		}

		return NORMAL_TEXT_COLOR;
	}
}