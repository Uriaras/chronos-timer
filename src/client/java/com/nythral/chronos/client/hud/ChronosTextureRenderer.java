package com.nythral.chronos.client.hud;

import com.nythral.chronos.ChronosTimer;
import java.awt.Color;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public final class ChronosTextureRenderer {

	private static final Identifier TOP_FILL_TEXTURE =
		ChronosTimer.id(
			"textures/gui/top_fill.png"
		);

	private static final int TOP_TEXTURE_WIDTH = 20;
	private static final int TOP_TEXTURE_HEIGHT = 10;

	private ChronosTextureRenderer() {
	}

	public static void drawBar(
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

		draw(
			graphics,
			baseTexture,
			0,
			0,
			TOP_TEXTURE_WIDTH,
			TOP_TEXTURE_HEIGHT,
			opacity
		);

		drawProgressFill(
			graphics,
			progress,
			opacity,
			fillFromRight
		);

		graphics.pose().popMatrix();
	}

	private static void drawProgressFill(
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
			progressColor(
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

	public static void draw(
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
			color(
				opacity
			)
		);
	}

	public static void drawMirrored(
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
			color(
				opacity
			)
		);
	}

	public static void drawRotated(
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

		draw(
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

	public static float alpha(
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

	private static int color(
		int opacity
	) {
		return argb(
			opacity,
			0x00FFFFFF
		);
	}

	private static int argb(
		int opacity,
		int rgb
	) {
		int alpha =
			Math.round(
				alpha(
					opacity
				) * 255.0F
			);

		return (alpha << 24)
			| (rgb & 0x00FFFFFF);
	}

	private static int progressColor(
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
}