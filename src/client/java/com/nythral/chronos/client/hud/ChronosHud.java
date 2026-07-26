package com.nythral.chronos.client.hud;

import com.nythral.chronos.ChronosTimer;
import com.nythral.chronos.client.config.ChronosAnchor;
import com.nythral.chronos.client.config.ChronosAttachment;
import com.nythral.chronos.client.config.ChronosConfig;
import com.nythral.chronos.client.config.ChronosDisplayMode;
import com.nythral.chronos.client.config.ChronosLayout;
import com.nythral.chronos.client.config.ChronosSide;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;

public final class ChronosHud {

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

	private static final Identifier TOP_FILL_TEXTURE =
		ChronosTimer.id(
			"textures/gui/top_fill.png"
		);

	private static final Identifier TIMER_TEXTURE =
		ChronosTimer.id(
			"textures/gui/timer.png"
		);

	private static final Identifier TIMER_AMBIENT_TEXTURE =
		ChronosTimer.id(
			"textures/gui/timer_ambient.png"
		);

	private static final Map<String, Integer> MAX_EFFECT_DURATIONS =
		new HashMap<>();

	private static final int ICON_BOX_SIZE = 24;
	private static final int ICON_SIZE = 18;

	private static final int TOP_TEXTURE_WIDTH = 20;
	private static final int TOP_TEXTURE_HEIGHT = 10;

	private static final int TIMER_TEXTURE_WIDTH = 27;
	private static final int TIMER_TEXTURE_HEIGHT = 16;

	private static final int SIDE_BAR_TEXTURE_WIDTH = 20;
	private static final int SIDE_BAR_TEXTURE_HEIGHT = 10;

	private static final int SIDE_BAR_ROTATED_WIDTH =
		SIDE_BAR_TEXTURE_HEIGHT;

	private static final int SIDE_BAR_ROTATED_HEIGHT =
		SIDE_BAR_TEXTURE_WIDTH;

	private static final int TEXTURE_OVERLAP = 1;

	private static final int EFFECT_GAP = 1;
	private static final int HORIZONTAL_ELEMENT_GAP = 1;

	private static final int SCREEN_MARGIN_X = 1;
	private static final int SCREEN_MARGIN_Y = 1;

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

	private ChronosHud() {
	}

	public static void render(
		GuiGraphics graphics,
		DeltaTracker deltaTracker
	) {
		Minecraft minecraft =
			Minecraft.getInstance();

		ChronosConfig config =
			ChronosConfig.INSTANCE;

		if (
			!config.enabled()
				|| minecraft.player == null
				|| minecraft.options.hideGui
		) {
			return;
		}

		List<ChronosEffectEntry> entries =
			createEntries(
				minecraft.player.getActiveEffects()
			);

		if (entries.isEmpty()) {
			MAX_EFFECT_DURATIONS.clear();

			return;
		}

		updateTrackedEffectDurations(
			entries
		);

		if (
			config.layout()
				== ChronosLayout.VERTICAL
		) {
			renderVertical(
				graphics,
				minecraft,
				entries,
				config
			);

			return;
		}

		renderHorizontal(
			graphics,
			minecraft,
			entries,
			config
		);
	}

	private static void renderVertical(
		GuiGraphics graphics,
		Minecraft minecraft,
		List<ChronosEffectEntry> entries,
		ChronosConfig config
	) {
		int verticalStep =
			calculateVerticalStep(
				config
			);

		int elementHeight =
			calculateVerticalElementHeight(
				config
			);

		int totalHeight =
			elementHeight
				+ Math.max(
					0,
					entries.size() - 1
				)
				* verticalStep;

		int firstIconY =
			calculateVerticalStartY(
				graphics.guiHeight(),
				totalHeight,
				config.anchor()
			);

		if (
			config.anchor()
				== ChronosAnchor.TOP
				&& config.attachment()
					== ChronosAttachment.TOP
		) {
			firstIconY +=
				TOP_TEXTURE_HEIGHT
					- TEXTURE_OVERLAP;
		}

		int iconX =
			calculateVerticalIconX(
				graphics.guiWidth(),
				config.side()
			);

		for (
			int index = 0;
			index < entries.size();
			index++
		) {
			int iconY =
				firstIconY
					+ index * verticalStep;

			renderEntry(
				graphics,
				minecraft,
				entries.get(index),
				iconX,
				iconY,
				config
			);
		}
	}

	private static void renderHorizontal(
		GuiGraphics graphics,
		Minecraft minecraft,
		List<ChronosEffectEntry> entries,
		ChronosConfig config
	) {
		int elementWidth =
			calculateHorizontalElementWidth(
				config
			);

		int step =
			elementWidth
				+ HORIZONTAL_ELEMENT_GAP;

		int iconY =
			calculateHorizontalIconY(
				graphics.guiHeight(),
				config.anchor()
			);

		if (
			config.anchor()
				== ChronosAnchor.TOP
				&& config.attachment()
					== ChronosAttachment.TOP
		) {
			iconY +=
				TOP_TEXTURE_HEIGHT
					- TEXTURE_OVERLAP;
		}

		if (
			config.side()
				== ChronosSide.RIGHT
		) {
			int firstIconX =
				graphics.guiWidth()
					- SCREEN_MARGIN_X
					- ICON_BOX_SIZE;

			for (
				int index = 0;
				index < entries.size();
				index++
			) {
				int iconX =
					firstIconX
						- index * step;

				renderEntry(
					graphics,
					minecraft,
					entries.get(index),
					iconX,
					iconY,
					config
				);
			}

			return;
		}

		int firstIconX =
			SCREEN_MARGIN_X;

		for (
			int index = 0;
			index < entries.size();
			index++
		) {
			int iconX =
				firstIconX
					+ index * step;

			renderEntry(
				graphics,
				minecraft,
				entries.get(index),
				iconX,
				iconY,
				config
			);
		}
	}

	private static List<ChronosEffectEntry> createEntries(
		Collection<MobEffectInstance> activeEffects
	) {
		List<ChronosEffectEntry> entries =
			new ArrayList<>();

		for (
			MobEffectInstance effect
				: activeEffects
		) {
			entries.add(
				new ChronosEffectEntry(
					effect,
					formatDuration(
						effect
					)
				)
			);
		}

		return entries;
	}

	private static void updateTrackedEffectDurations(
		List<ChronosEffectEntry> entries
	) {
		Set<String> activeKeys =
			new HashSet<>();

		for (
			ChronosEffectEntry entry
				: entries
		) {
			MobEffectInstance effect =
				entry.effect();

			String key =
				createEffectKey(
					effect
				);

			activeKeys.add(
				key
			);

			if (
				!effect.isInfiniteDuration()
			) {
				int remainingTicks =
					Math.max(
						1,
						effect.getDuration()
					);

				MAX_EFFECT_DURATIONS.merge(
					key,
					remainingTicks,
					Math::max
				);
			}
		}

		MAX_EFFECT_DURATIONS.keySet()
			.removeIf(
				key ->
					!activeKeys.contains(
						key
					)
			);
	}

	private static void renderEntry(
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
			calculateAlpha(
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
			drawBarTexture(
				graphics,
				texture,
				textureX,
				textureY,
				0.0F,
				true,
				calculateEffectProgress(
					entry.effect()
				),
				config.opacity()
			);

			return;
		}

		drawTexture(
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
			drawBarTexture(
				graphics,
				texture,
				textureX,
				textureY,
				180.0F,
				false,
				calculateEffectProgress(
					entry.effect()
				),
				config.opacity()
			);

			return;
		}

		drawRotatedTexture(
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

			drawMirroredTexture(
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

			drawTexture(
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

		drawBarTexture(
			graphics,
			texture,
			textureX,
			textureY,
			rotation,
			fillFromRight,
			calculateEffectProgress(
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

	private static void drawBarTexture(
		GuiGraphics graphics,
		Identifier baseTexture,
		int x,
		int y,
		float rotationDegrees,
		boolean fillFromRight,
		float progress,
		int opacity
	) {
		int rotatedWidth;
		int rotatedHeight;

		if (
			rotationDegrees == 90.0F
				|| rotationDegrees == 270.0F
		) {
			rotatedWidth =
				TOP_TEXTURE_HEIGHT;

			rotatedHeight =
				TOP_TEXTURE_WIDTH;
		} else {
			rotatedWidth =
				TOP_TEXTURE_WIDTH;

			rotatedHeight =
				TOP_TEXTURE_HEIGHT;
		}

		float centerX =
			x
				+ rotatedWidth / 2.0F;

		float centerY =
			y
				+ rotatedHeight / 2.0F;

		graphics.pose().pushMatrix();

		graphics.pose().translate(
			centerX,
			centerY
		);

		graphics.pose().rotate(
			(float) Math.toRadians(
				rotationDegrees
			)
		);

		graphics.pose().translate(
			-TOP_TEXTURE_WIDTH / 2.0F,
			-TOP_TEXTURE_HEIGHT / 2.0F
		);

		drawTexture(
			graphics,
			baseTexture,
			0,
			0,
			TOP_TEXTURE_WIDTH,
			TOP_TEXTURE_HEIGHT,
			opacity
		);

		drawProgressFillTexture(
			graphics,
			progress,
			opacity,
			fillFromRight
		);

		graphics.pose().popMatrix();
	}

	private static void drawProgressFillTexture(
		GuiGraphics graphics,
		float progress,
		int opacity,
		boolean fillFromRight
	) {
		float safeProgress =
			Math.max(
				0.0F,
				Math.min(
					1.0F,
					progress
				)
			);

		int visibleWidth =
			Math.round(
				safeProgress
					* TOP_TEXTURE_WIDTH
			);

		if (
			visibleWidth <= 0
		) {
			return;
		}

		int sourceX =
			fillFromRight
				? TOP_TEXTURE_WIDTH - visibleWidth
				: 0;

		int destinationX =
			sourceX;

		int progressColor =
			calculateProgressBarColor(
				safeProgress,
				opacity
			);

		graphics.blit(
			RenderPipelines.GUI_TEXTURED,
			TOP_FILL_TEXTURE,
			destinationX,
			0,
			(float) sourceX,
			0.0F,
			visibleWidth,
			TOP_TEXTURE_HEIGHT,
			TOP_TEXTURE_WIDTH,
			TOP_TEXTURE_HEIGHT,
			progressColor
		);
	}

	private static float calculateEffectProgress(
		MobEffectInstance effect
	) {
		if (
			effect.isInfiniteDuration()
		) {
			return 1.0F;
		}

		int remainingTicks =
			Math.max(
				0,
				effect.getDuration()
			);

		String key =
			createEffectKey(
				effect
			);

		int maximumTicks =
			MAX_EFFECT_DURATIONS.getOrDefault(
				key,
				Math.max(
					1,
					remainingTicks
				)
			);

		if (
			maximumTicks <= 0
		) {
			return 0.0F;
		}

		return Math.max(
			0.0F,
			Math.min(
				1.0F,
				remainingTicks
					/ (float) maximumTicks
			)
		);
	}

	private static String createEffectKey(
		MobEffectInstance effect
	) {
		return effect.getEffect()
			.toString()
			+ ":"
			+ effect.getAmplifier();
	}

	private static void drawTexture(
		GuiGraphics graphics,
		Identifier texture,
		int x,
		int y,
		int width,
		int height,
		int opacity
	) {
		graphics.blit(
			RenderPipelines.GUI_TEXTURED,
			texture,
			x,
			y,
			0.0F,
			0.0F,
			width,
			height,
			width,
			height,
			calculateColor(
				opacity
			)
		);
	}

	private static void drawMirroredTexture(
		GuiGraphics graphics,
		Identifier texture,
		int x,
		int y,
		int width,
		int height,
		int opacity
	) {
		graphics.blit(
			RenderPipelines.GUI_TEXTURED,
			texture,
			x,
			y,
			(float) width,
			0.0F,
			width,
			height,
			-width,
			height,
			width,
			height,
			calculateColor(
				opacity
			)
		);
	}

	private static void drawRotatedTexture(
		GuiGraphics graphics,
		Identifier texture,
		int x,
		int y,
		int textureWidth,
		int textureHeight,
		float rotationDegrees,
		int opacity
	) {
		int rotatedWidth;
		int rotatedHeight;

		if (
			rotationDegrees == 90.0F
				|| rotationDegrees == 270.0F
		) {
			rotatedWidth =
				textureHeight;

			rotatedHeight =
				textureWidth;
		} else {
			rotatedWidth =
				textureWidth;

			rotatedHeight =
				textureHeight;
		}

		float centerX =
			x
				+ rotatedWidth / 2.0F;

		float centerY =
			y
				+ rotatedHeight / 2.0F;

		graphics.pose().pushMatrix();

		graphics.pose().translate(
			centerX,
			centerY
		);

		graphics.pose().rotate(
			(float) Math.toRadians(
				rotationDegrees
			)
		);

		graphics.pose().translate(
			-textureWidth / 2.0F,
			-textureHeight / 2.0F
		);

		drawTexture(
			graphics,
			texture,
			0,
			0,
			textureWidth,
			textureHeight,
			opacity
		);

		graphics.pose().popMatrix();
	}

	private static float calculateAlpha(
		int opacity
	) {
		int safeOpacity =
			Math.max(
				0,
				Math.min(
					100,
					opacity
				)
			);

		return safeOpacity
			/ 100.0F;
	}

	private static int calculateColor(
		int opacity
	) {
		return calculateArgbColor(
			opacity,
			0x00FFFFFF
		);
	}

	private static int calculateArgbColor(
		int opacity,
		int rgb
	) {
		int alpha =
			Math.round(
				calculateAlpha(
					opacity
				) * 255.0F
			);

		return (alpha << 24)
			| (rgb & 0x00FFFFFF);
	}

	private static int calculateProgressBarColor(
		float progress,
		int opacity
	) {
		float safeProgress =
			Math.max(
				0.0F,
				Math.min(
					1.0F,
					progress
				)
			);

		int rgb =
			Color.HSBtoRGB(
				safeProgress / 3.0F,
				1.0F,
				1.0F
			) & 0x00FFFFFF;

		return 0xFF000000
			| rgb;
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
			Math.max(
				0,
				(int) Math.ceil(
					effect.getDuration()
						/ 20.0
				)
			);

		if (
			remainingSeconds
				<= WARNING_TIME_SECONDS
		) {
			return WARNING_TEXT_COLOR;
		}

		return NORMAL_TEXT_COLOR;
	}

	private static String formatDuration(
		MobEffectInstance effect
	) {
		if (
			effect.isInfiniteDuration()
		) {
			return "∞";
		}

		int totalSeconds =
			Math.max(
				0,
				(int) Math.ceil(
					effect.getDuration()
						/ 20.0
				)
			);

		if (
			totalSeconds >= 60
		) {
			return totalSeconds / 60
				+ "m";
		}

		return Integer.toString(
			totalSeconds
		);
	}

	private static int calculateVerticalStep(
		ChronosConfig config
	) {
		return calculateVerticalElementHeight(
			config
		) + EFFECT_GAP;
	}

	private static int calculateVerticalElementHeight(
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

	private static int calculateVerticalIconX(
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

	private static int calculateVerticalStartY(
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

	private static int calculateHorizontalIconY(
		int screenHeight,
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
				- ICON_BOX_SIZE;
		}

		return (
			screenHeight
				- ICON_BOX_SIZE
		) / 2;
	}

	private static int calculateHorizontalElementWidth(
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