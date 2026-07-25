package com.nythral.chronos.client.hud;

import com.nythral.chronos.ChronosTimer;
import com.nythral.chronos.client.config.ChronosAnchor;
import com.nythral.chronos.client.config.ChronosAttachment;
import com.nythral.chronos.client.config.ChronosConfig;
import com.nythral.chronos.client.config.ChronosDisplayMode;
import com.nythral.chronos.client.config.ChronosLayout;
import com.nythral.chronos.client.config.ChronosSide;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

	private static final int SIDE_BAR_TEXTURE_WIDTH = 20;
	private static final int SIDE_BAR_TEXTURE_HEIGHT = 10;

	private static final int SIDE_BAR_ROTATED_WIDTH =
		SIDE_BAR_TEXTURE_HEIGHT;

	private static final int SIDE_BAR_ROTATED_HEIGHT =
		SIDE_BAR_TEXTURE_WIDTH;

	private static final int TEXTURE_OVERLAP = 1;

	private static final int EFFECT_GAP = 1;
	private static final int HORIZONTAL_ELEMENT_GAP = 1;

	private static final int SCREEN_MARGIN_X = 6;
	private static final int SCREEN_MARGIN_Y = 6;

	private static final int TEXT_OFFSET_Y = 0;
	private static final int SIDE_TEXT_EXTRA_OFFSET_Y = 1;

	private static final int TOP_TEXT_OFFSET_X = 0;
	private static final int TOP_TEXT_WIDTH = 20;
	private static final int TOP_TEXT_HEIGHT = 8;

	private static final int TIMER_CONNECTOR_WIDTH = 3;
	private static final int TIMER_TEXT_WIDTH = 24;
	private static final int TIMER_TEXT_HEIGHT = 14;

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
			return;
		}

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
					formatDuration(effect)
				)
			);
		}

		return entries;
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
			effect,
			iconX,
			iconY,
			ambient
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
		MobEffectInstance effect,
		int iconBoxX,
		int iconBoxY,
		boolean ambient
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
			ICON_BOX_SIZE
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
				TIMER_TEXTURE_HEIGHT
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
			entry.durationText(),
			textureX,
			textureY,
			config
		);
	}

	private static void renderSideBarAttachment(
		GuiGraphics graphics,
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
		} else {
			textureX =
				iconX
					+ ICON_BOX_SIZE
					- TEXTURE_OVERLAP;

			rotation =
				90.0F;
		}

		drawRotatedTexture(
			graphics,
			texture,
			textureX,
			textureY,
			SIDE_BAR_TEXTURE_WIDTH,
			SIDE_BAR_TEXTURE_HEIGHT,
			rotation,
			config.opacity()
		);
	}

	private static void renderTopBottomTimerText(
		GuiGraphics graphics,
		Minecraft minecraft,
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
				+ TEXT_OFFSET_Y;

		drawCenteredText(
			graphics,
			minecraft,
			text,
			textAreaX,
			textAreaY,
			TOP_TEXT_WIDTH,
			TOP_TEXT_HEIGHT
		);
	}

	private static void renderSideTimerText(
		GuiGraphics graphics,
		Minecraft minecraft,
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
				+ TEXT_OFFSET_Y
				+ SIDE_TEXT_EXTRA_OFFSET_Y;

		drawCenteredText(
			graphics,
			minecraft,
			text,
			textAreaX,
			textAreaY,
			TIMER_TEXT_WIDTH,
			TIMER_TEXT_HEIGHT
		);
	}

	private static void drawCenteredText(
		GuiGraphics graphics,
		Minecraft minecraft,
		String text,
		int areaX,
		int areaY,
		int areaWidth,
		int areaHeight
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
			0xFFFFFFFF,
			true
		);
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
		int alpha =
			Math.round(
				opacity
					/ 100.0F
					* 255.0F
			);

		int color =
			(alpha << 24)
				| 0x00FFFFFF;

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
			color
		);
	}

	private static void drawMirroredTexture(
		GuiGraphics graphics,
		Identifier texture,
		int x,
		int y,
		int width,
		int height
	) {
		graphics.blit(
			texture,
			x,
			y,
			x + width,
			y + height,
			1.0F,
			0.0F,
			0.0F,
			1.0F
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